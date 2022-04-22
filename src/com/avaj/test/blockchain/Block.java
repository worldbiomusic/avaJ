package com.avaj.test.blockchain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import com.avaj.test.crypto.CryptoUtils;
import com.avaj.test.utils.Settings;

public class Block {
	private String hash;
	private Block previousBlock;
	private String data;
	private LocalDateTime timeStamp; // long unixTimestamp = Instant.now().getEpochSecond();
	private long nonce;
	private int difficulty;

	public Block(Block previousBlock, String data) {
		this.previousBlock = previousBlock;
		this.data = data;
		this.timeStamp = LocalDateTime.now();
		this.nonce = Long.MIN_VALUE;
		this.difficulty = (previousBlock == null) ? Settings.GENESIS_DIFFICULTY : previousBlock.getDifficulty();

		// [IMPORTANT] must be called at last after the other data setup
		this.hash = doHash();
	}

	public String getHash() {
		return hash;
	}

	public Block getPreviousBlock() {
		return previousBlock;
	}

	public String getData() {
		return data;
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

	public String doHash() {
		String previousBlockHash = (this.previousBlock == null) ? "0" : this.previousBlock.getHash();
		String allData = previousBlockHash + this.data + this.timeStamp.toString() + Long.toString(this.nonce)
				+ Integer.toString(this.difficulty);
		return CryptoUtils.hashToHex(allData);
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
			isValid &= block.getHash().startsWith("0".repeat(block.getDifficulty()));
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
				if (doHash().startsWith(target)) {
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

	@Override
	public String toString() {
		return "Block [hash=" + hash + ", previousBlock=" + previousBlock + ", data=" + data + ", timeStamp="
				+ timeStamp + ", nonce=" + nonce + ", difficulty=" + difficulty + "]";
	}

}
