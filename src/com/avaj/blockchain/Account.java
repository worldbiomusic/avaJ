package com.avaj.blockchain;

import com.avaj.crypto.CryptoUtils;
import com.avaj.crypto.KeyWallet;

public class Account implements Cloneable {
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
		if (canValueChange(amount)) {
			this.value += amount;
			return true;
		}
		return false;
	}

	private boolean canPlusValue(long amount) {
		return !(this.value + amount > Long.MAX_VALUE);
	}

	public boolean minusValue(long amount) {
		if (canValueChange(-amount)) {
			this.value -= amount;
			return true;
		}
		return false;
	}

	private boolean canMinusValue(long amount) {
		return !(this.value - amount < 0);
	}

	public boolean canValueChange(long offset) {
		return offset > 0 ? canPlusValue(offset) : canMinusValue(-offset);
	}

	public boolean sendValue(Account receiver, long amount) {
		if (canValueChange(-amount) && receiver.canValueChange(amount)) {
			minusValue(amount);
			receiver.plusValue(amount);
			return true;
		}
		return false;
	}

	public long getValue() {
		return this.value;
	}

	public String doHash() {
		String allData = this.keyWallet.doHash() + Long.toString(this.value);
		return CryptoUtils.hashToHex(allData.getBytes());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof String) {
			// hex public key string
			return getHexPublicKey().equals((String) obj);
		} else if (obj instanceof Account) {
			return getHexPublicKey().equals(((Account) obj).getHexPublicKey());
		}
		return false;
	}

	@Override
	public Object clone() {
		try {
			Account copiedAccount = (Account) super.clone();
			copiedAccount.keyWallet = (KeyWallet) this.keyWallet.clone();
			return copiedAccount;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "Account [\nkeyWallet=" + this.keyWallet.toString() + "\nvalue=" + this.value + "]";
	}
}
