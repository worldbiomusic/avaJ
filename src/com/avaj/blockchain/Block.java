package com.avaj.blockchain;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;

import com.avaj.crypto.CryptoUtils;
import com.avaj.utils.Settings;
import com.avaj.utils.Utils;
import com.google.gson.Gson;

/**
 * 
 *
 */
public class Block {
	transient public static Block TOP;
//	transient public static Block GENESIS;

	private String hash;
	private Instant timeStamp;
	private long nonce;
	private BigInteger difficulty;
	private long reward;
	private long length;
	private Account creator;

	private AccountManager accountManager;
	private TransactionManager transactionManager;
	private Block previousBlock;

	public Block(Block previousBlock, Account creator) {
		this.previousBlock = previousBlock;
		this.timeStamp = Instant.now();
		this.nonce = Long.MIN_VALUE;
		this.length = isGenesis() ? 0 : this.previousBlock.getLength() + 1;
		this.creator = creator;

		retargetDifficulty();
		reduceReward();

		this.accountManager = new AccountManager(this,
				isGenesis() ? new ArrayList<>() : this.previousBlock.getAccountManager().getAccounts());
		this.transactionManager = new TransactionManager(this);

		// [IMPORTANT] must be called at last after the other data setup
		this.hash = doHash();
	}

	public boolean isGenesis() {
		return this.previousBlock == null;
	}

	public String getHash() {
		return hash;
	}

	public Block getPreviousBlock() {
		return previousBlock;
	}

	public Instant getTimeStamp() {
		return timeStamp;
	}

	public long getNonce() {
		return this.nonce;
	}

	public BigInteger getDifficulty() {
		return this.difficulty;
	}

	public long getReward() {
		return this.reward;
	}

	public AccountManager getAccountManager() {
		return this.accountManager;
	}

	public TransactionManager getTransactionManager() {
		return this.transactionManager;
	}

	public String doHash() {
		String allData = "";

		// previous block
		allData += (isGenesis()) ? "0" : this.previousBlock.getHash();

		// time stamp
		allData += this.timeStamp.toEpochMilli();

		// nonce
		allData += Long.toString(this.nonce);

		// difficulty
		allData += String.valueOf(this.difficulty);

		// reward
		allData += Long.toString(this.reward);

		// length
		allData += String.valueOf(this.length);

		// accounts
		allData += this.accountManager.getHashTree().getRoot().getHash();

		// transactions
		allData += this.transactionManager.getHashTree().getRoot().getHash();

		return CryptoUtils.hashToHex(allData.getBytes());
	}

	/**
	 * Check all chained blocks hash again
	 * 
	 * @return True if chain is valid
	 */
	public boolean isValid() {
		boolean isValid = true;

		Block block = this;
		while (block != null && isValid) {
			// check data
			isValid &= block.isHashValid();
			// check hash difficulty
			isValid &= block.isDifficultyValid();

			// move to previous block
			block = block.getPreviousBlock();
		}

		return isValid;
	}

	public boolean isHashValid() {
		return getHash().equals(doHash());
	}

	public boolean isDifficultyValid() {
		BigInteger hash = new BigInteger(this.hash, 16);

		// hash is smaller than or equals with difficulty
		return hash.compareTo(this.difficulty) < 1;
	}

	/**
	 * Mine block<br>
	 * Run with other thread
	 */
	public void mine() {
		// build
		build();

		System.out.println("Start mining... ");

		LocalTime startTime = LocalTime.now();

		// never stop
		while (true) {
			// init values
			this.timeStamp = Instant.now();

			// do hash
			this.hash = doHash();

			// check difficulty
			if (isDifficultyValid()) {
				if (Settings.DEBUG) {
					System.out.println("hash: " + this.hash);
					System.out.println("diff: " + Settings.GENESIS_DIFFICULTY.toString(16));
				}

				System.out.println("Block mined!");
				LocalTime finishTime = LocalTime.now();
				String duration = Duration.between(startTime, finishTime).toMillis() + "";
				System.out.println("Duration:  " + duration + " ms");
				break;
			}

			// increase nonce
			this.nonce = (this.nonce + 1) % Long.MAX_VALUE;
		}

		// update TOP
		TOP = this;
	}

	public long getLength() {
		return this.length;
	}

	public Account getCreator() {
		return this.creator;
	}

	/**
	 * Genesis block = 0 index
	 * 
	 * @param index Index of block
	 * @return Block
	 */
	public Block indexOf(long index) {
		Block block = this;
		for (int i = 0; i < getLength() - index; i++) {
			block = block.getPreviousBlock();
		}
		return block;
	}

	public void retargetDifficulty() {
		if (previousBlock == null) {
			this.difficulty = Settings.GENESIS_DIFFICULTY;
			return;
		}

		// check difficulty retargeting period
		if (getLength() % Settings.DIFFICULTY_RETARGETING_INTERVAL == 0) {
			double avgMiningSec = 0;
			Block block = this;
			for (int i = 0; i < Settings.DIFFICULTY_RETARGETING_INTERVAL && block != null; i++) {
				long secDiff = block.getTimeStamp().getEpochSecond()
						- block.getPreviousBlock().getTimeStamp().getEpochSecond();
				avgMiningSec += secDiff;

				// move to previous block
				block = block.getPreviousBlock();
			}

			// avg
			avgMiningSec /= Settings.DIFFICULTY_RETARGETING_INTERVAL;

			// rate
			int rate = (int) ((avgMiningSec / Settings.DIFFICULTY_RETARGETING_INTERVAL) * 100);

			// retarget difficulty
			this.difficulty = this.difficulty.multiply(new BigInteger(String.valueOf(rate)));
		} else {
			this.difficulty = this.previousBlock.getDifficulty();
		}
	}

	public void reduceReward() {
		if (previousBlock == null) {
			this.reward = Settings.GENESIS_REWARD;
			return;
		}

		// check reward halving period
		if (getLength() % Settings.REWARD_HALVING_INTERVAL == 0) {
			this.reward -= this.reward * Settings.REWARD_HALVING_RATE;
		} else {
			this.reward = this.previousBlock.getReward();
		}
	}

	public void build() {
		this.transactionManager.processTransactions();
	}

	public enum Scope {
		ONE, ALL;
	}

	synchronized public String data(Scope scope) {
		Gson gson = Utils.gson();

		Block preBlock = this.previousBlock;
		if (scope == Scope.ONE) {
			this.previousBlock = null;
		}

		String gsonData = gson.toJson(this);

		this.previousBlock = preBlock;

		return gsonData;

//		String previousBlockStr = (scope == Scope.ONE) ? "" : ", \npreviousBlock=" + previousBlock;
//		return "Block [\nhash=" + hash + ", \ntimeStamp=" + timeStamp + ", \nnonce=" + nonce + ", \ndifficulty="
//				+ difficulty + ", \nreward=" + reward + ", \nlength=" + length + ", \ncreator=" + creator
//				+ ", \naccounts=" + this.accountManager.getAccounts() + ", \ntransactions="
//				+ this.transactionManager.getTransactions() + previousBlockStr + "]";
	}

}
