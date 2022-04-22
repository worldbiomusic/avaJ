package com.avaj.test.blockchain;

import java.time.LocalDateTime;

import com.avaj.test.crypto.CryptoUtils;

public class Transaction {
	private String hash;
	private Account sender, receiver;
	private long value, fee;
	private byte[] signature;
	LocalDateTime timeStamp;

	public Transaction(Account sender, Account receiver, long value, long fee) {
		this.sender = sender;
		this.receiver = receiver;
		this.value = value;
		this.fee = fee;
		this.timeStamp = LocalDateTime.now();

		// [IMPORTANT] must be called at last after the other data setup
		this.hash = doHash();
		
		// sign
		sign();
	}

	public String doHash() {
		String allData = sender.getHexPublicKey() + receiver.getHexPublicKey() + Long.valueOf(this.value)
				+ Long.valueOf(this.fee) + this.timeStamp.toString();

		return CryptoUtils.bytesToHex(allData.getBytes());
	}

	/**
	 * Sign tranaction with hash
	 */
	public void sign() {
		this.signature = CryptoUtils.sign(this.hash.getBytes(), this.sender.getKeyWallet().getPrivateKey());
	}

	public boolean verify() {
		return CryptoUtils.verify(this.hash.getBytes(), this.sender.getKeyWallet().getPublicKey(), this.signature);
	}
}
