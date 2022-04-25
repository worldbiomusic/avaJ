package com.avaj.blockchain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import com.avaj.crypto.CryptoUtils;
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
	

	public Block(Block previousBlock) {
		this.previousBlock = previousBlock;
		this.timeStamp = LocalDateTime.now();
		this.nonce = Long.MIN_VALUE;
		retargetDifficulty();
		reduceReward();

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

	public String doHash() {
		String previousBlockHash = (this.previousBlock == null) ? "0" : this.previousBlock.getHash();
		String allData = previousBlockHash + this.timeStamp.toString() + Long.toString(this.nonce)
				+ Integer.toString(this.difficulty) + Long.toString(this.reward);
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
		while (block != null) {
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
		if (getSize() % Settings.DIFFICULTY_RETARGETING_PERIOD == 0) {
			double avgMiningSec = 0;
			Block block = this;
			for (int i = 0; i < Settings.DIFFICULTY_RETARGETING_PERIOD && block != null; i++) {
				long secDiff = ChronoUnit.SECONDS.between(block.getTimeStamp(),
						block.getPreviousBlock().getTimeStamp());
				avgMiningSec += secDiff;

				// move to previous block
				block = block.getPreviousBlock();
			}

			// avg
			avgMiningSec /= Settings.DIFFICULTY_RETARGETING_PERIOD;

			// rate
			int rate = (int) ((avgMiningSec / Settings.DIFFICULTY_RETARGETING_PERIOD) * 100);

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
		if (getSize() % Settings.REWARD_HALVING_PERIOD == 0) {
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
