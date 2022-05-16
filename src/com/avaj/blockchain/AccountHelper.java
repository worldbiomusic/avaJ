package com.avaj.blockchain;

import java.io.File;
import java.io.IOException;

import com.avaj.crypto.FileUtils;
import com.avaj.crypto.KeyWallet;
import com.avaj.files.AccountFile;
import com.avaj.utils.Settings;
import com.avaj.utils.Utils;
import com.google.gson.Gson;

public class AccountHelper {
	public static File createAccountFile(String accountName) {
		KeyWallet keyWallet = new KeyWallet();
		AccountFile file = new AccountFile(accountName, keyWallet);

		Gson gson = Utils.gson();
		String data = gson.toJson(file);

		try {
			File accountFile = new File(Settings.ACCOUNTS_DIR, accountName + ".json");
			FileUtils.save(accountFile, data.getBytes());
			return accountFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static AccountFile loadAccountFile(String accountName) {
		Gson gson = Utils.gson();

		try {
			String data = new String(FileUtils.load(new File(Settings.ACCOUNTS_DIR, accountName + ".json")));
			AccountFile file = gson.fromJson(data, AccountFile.class);
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
