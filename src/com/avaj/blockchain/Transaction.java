package com.avaj.blockchain;

import java.time.Instant;

import com.avaj.crypto.CryptoUtils;

public class Transaction {
	private String hash;
	private Account sender, receiver;
	private long value, fee;
	private String signature;
	private Instant timeStamp;

	public Transaction(Account sender, Account receiver, long value, long fee) {
		this.sender = sender;
		this.receiver = receiver;
		this.value = value;
		this.fee = fee;
		this.timeStamp = Instant.now();

		// [IMPORTANT] must be called at last after the other data setup
		this.hash = doHash();

		// sign transaction
		sign();
	}

	public String getHash() {
		return hash;
	}

	public Account getSender() {
		return sender;
	}

	public Account getReceiver() {
		return receiver;
	}

	public long getValue() {
		return value;
	}

	public long getFee() {
		return fee;
	}

	public String getSignature() {
		return signature;
	}

	public Instant getTimeStamp() {
		return timeStamp;
	}

	public String doHash() {
		String allData = sender.getHexPublicKey() + receiver.getHexPublicKey() + Long.valueOf(this.value)
				+ Long.valueOf(this.fee) + this.timeStamp.toString();

		return CryptoUtils.hashToHex(allData.getBytes());
	}

	public boolean isValid() {
		boolean isValid = true;
		isValid &= sender.canValueChange(-(this.value + this.fee)) && receiver.canValueChange(this.value);
		return isValid;
	}

	/**
	 * Sign tranaction with hash
	 */
	public void sign() {
		byte[] signBytes = CryptoUtils.sign(this.hash.getBytes(), this.sender.getKeyWallet().getPrivateKey());
		this.signature = CryptoUtils.bytesToHex(signBytes);
	}

	public boolean verify() {
		byte[] signBytes = CryptoUtils.hexToBytes(this.signature);
		return CryptoUtils.verify(this.hash.getBytes(), this.sender.getKeyWallet().getPublicKey(), signBytes);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (other instanceof Transaction) {
			return this.hash.equals(((Transaction) other).getHash());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Transaction [\nhash=" + hash + ", \nsender=" + sender + ", \nreceiver=" + receiver + ", \nvalue="
				+ value + ", \nfee=" + fee + ", \nsignature=" + signature + ", \ntimeStamp=" + timeStamp + "]";
	}

}
