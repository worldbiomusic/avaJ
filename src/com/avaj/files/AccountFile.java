package com.avaj.files;

import com.avaj.crypto.KeyWallet;

public class AccountFile {
	private String name;
	private KeyWallet keyWallet;

	public AccountFile(String name, KeyWallet keyWallet) {
		this.name = name;
		this.keyWallet = keyWallet;
	}

	public String getName() {
		return name;
	}

	public KeyWallet getKeyWallet() {
		return keyWallet;
	}

}
