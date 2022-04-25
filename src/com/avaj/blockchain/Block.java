package com.avaj.blockchain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import com.avaj.crypto.CryptoUtils;
import com.avaj.hashtree.HashTree;
import com.avaj.utils.Settings;

/**
 * 
 *
 */
public class Block {
	private String hash;
	private Block previousBlock;
	private LocalDateTime timeStamp; // long unixTimestamp = Instant.now().getEpochSecond();
	private long nonce;
	private int difficulty;
	private long reward;

	private Set<Account> accounts;
	private Set<Transaction> transactions;

	public Block(Block previousBlock) {
		this.previousBlock = previousBlock;
		this.timeStamp = LocalDateTime.now();
		this.nonce = Long.MIN_VALUE;
		retargetDifficulty();
		reduceReward();

		this.accounts = new HashSet<>();
		this.transactions = new HashSet<>();

		// [IMPORTANT] must be called at last after the other data setup
		this.hash = doHash();
	}

	public String getHash() {
		return hash;
	}

	public Block getPreviousBlock() {
		return previousBlock;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public long getNonce() {
		return this.nonce;
	}

	public int getDifficulty() {
		return this.difficulty;
	}

	public long getReward() {
		return this.reward;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public HashTree getAccountHashTree() {
		HashTree tree = new HashTree();
		this.accounts.forEach(a -> tree.addData(a.doHash()));
		return tree.build();
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public HashTree getTransactionHashTree() {
		HashTree tree = new HashTree();
		this.transactions.forEach(t -> tree.addData(t.doHash()));
		return tree.build();
	}

	public String doHash() {
		String allData = "";

		// previous block
		allData += (this.previousBlock == null) ? "0" : this.previousBlock.getHash();

		// time stamp
		allData += this.timeStamp.toString();

		// nonce
		allData += Long.toString(this.nonce);

		// difficulty
		allData += Integer.toString(this.difficulty);

		// reward
		allData += Long.toString(this.reward);

		// accounts
		allData += getAccountHashTree().getRoot().getHash();

		// transactions
		allData += getTransactionHashTree().getRoot().getHash();

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
			isValid &= block.getHash().equals(block.doHash());
			// check hash difficulty
			isValid &= CryptoUtils.hexToBinary(block.getHash()).startsWith("0".repeat(block.getDifficulty()));
			// move to previous block
			block = block.getPreviousBlock();
		}

		return isValid;
	}

	/**
	 * Mine block<br>
	 * Run with other thread
	 */
	public void mine() {
		System.out.print("Start mining... ");
		String target = "0".repeat(this.difficulty);

		LocalTime startTime = LocalTime.now();

		// never stop
		while (true) {
			while (this.nonce <= Long.MAX_VALUE) {
				if (CryptoUtils.hexToBinary(doHash()).startsWith(target)) {
					this.hash = doHash();

					System.out.println("Block mined!");
					LocalTime finishTime = LocalTime.now();
					String duration = Duration.between(startTime, finishTime).toMillis() + "";
					System.out.println("Duration:  " + duration + " ms");

					System.out.println(toString() + "\n");
					return;
				}
				nonce++;
			}

			// update time
			this.timeStamp = LocalDateTime.now();
		}
	}

	public int getSize() {
		int count = 0;
		Block block = this;
		while (block != null) {
			block = block.getPreviousBlock();
			count++;
		}
		return count;
	}

	/**
	 * Genesis block = 1 index
	 * 
	 * @param index Index of block
	 * @return Block
	 */
	public Block indexOf(long index) {
		Block block = this;
		for (int i = 0; i < getSize() - index; i++) {
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
		if (getSize() % Settings.DIFFICULTY_RETARGETING_INTERVAL == 0) {
			double avgMiningSec = 0;
			Block block = this;
			for (int i = 0; i < Settings.DIFFICULTY_RETARGETING_INTERVAL && block != null; i++) {
				long secDiff = ChronoUnit.SECONDS.between(block.getTimeStamp(),
						block.getPreviousBlock().getTimeStamp());
				avgMiningSec += secDiff;

				// move to previous block
				block = block.getPreviousBlock();
			}

			// avg
			avgMiningSec /= Settings.DIFFICULTY_RETARGETING_INTERVAL;

			// rate
			int rate = (int) ((avgMiningSec / Settings.DIFFICULTY_RETARGETING_INTERVAL) * 100);

			// retarget difficulty

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
		if (getSize() % Settings.REWARD_HALVING_INTERVAL == 0) {
			this.reward -= this.reward * Settings.REWARD_HALVING_RATE;
		} else {
			this.reward = this.previousBlock.getReward();
		}
	}

	@Override
	public String toString() {
		return "Block [hash=" + hash + ", previousBlock=" + previousBlock + ", timeStamp=" + timeStamp + ", nonce="
				+ nonce + ", difficulty=" + difficulty + "]";
	}

}
