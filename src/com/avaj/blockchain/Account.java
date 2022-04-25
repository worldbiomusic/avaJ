package com.avaj.blockchain;

import com.avaj.crypto.CryptoUtils;
import com.avaj.crypto.KeyWallet;

public class Account {
	private KeyWallet keyWallet;
	private long value;

	public Account(KeyWallet keyWallet) {
		this.keyWallet = keyWallet;
		this.value = 0;
	}

	public KeyWallet getKeyWallet() {
		return this.keyWallet;
	}

	public String getHexPublicKey() {
		return CryptoUtils.bytesToHex(this.keyWallet.getPublicKey().getEncoded());
	}

	public String getHexPrivateKey() {
		return CryptoUtils.bytesToHex(this.keyWallet.getPrivateKey().getEncoded());
	}

	public boolean plusValue(long amount) {
		if (this.value + amount > Long.MAX_VALUE) {
			return false;
		}

		this.value += amount;
		return true;
	}

	public boolean minusValue(int amount) {
		if (this.value - amount < 0) {
			return false;
		}

		this.value -= amount;
		return true;
	}

	public long getValue() {
		return this.value;
	}

	public String doHash() {
		String allData = this.keyWallet.doHash() + Long.toString(this.value);
		return CryptoUtils.hashToHex(allData.getBytes());
	}
}
