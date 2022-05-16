package com.avaj.blockchain;

import java.util.ArrayList;
import java.util.List;

import com.avaj.hashtree.HashTree;
import com.avaj.utils.Settings;

public class AccountManager {
	transient private Block block;
	private List<Account> accounts;

	public AccountManager(Block block, List<Account> previousAccounts) {
		this.block = block;
		this.accounts = new ArrayList<>();

		// copy previous accounts data
		previousAccounts.forEach(account -> accounts.add((Account) account.clone()));
	}

	public List<Account> getAccounts() {
		return this.accounts;
	}

	public boolean addAccount(Account account) {
		if (containsAccount(account)) {
			return false;
		}
		this.accounts.add(account);
		return true;
	}

	public boolean removeAccount(Account account) {
		return this.accounts.remove(account);
	}

	public Account getAccount(String hexPublicKey) {
		for (Account a : this.accounts) {
			if (a.equals(hexPublicKey)) {
				return a;
			}
		}
		return null;
	}
	
	public boolean containsAccount(Account account) {
		return this.accounts.contains(account);
	}

	public Account getAvaJ() {
		for (Account account : this.getAccounts()) {
			if (account.getHexPublicKey().equals(Settings.avaJ_PUBLIC_KEY)) {
				return account;
			}
		}
		return null;
	}

	public HashTree getHashTree() {
		HashTree tree = new HashTree();
		getAccounts().forEach(a -> tree.addData(a.doHash()));
		return tree.build();
	}
}
