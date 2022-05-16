package com.avaj.commands;

import java.io.File;

import com.avaj.blockchain.AccountHelper;
import com.avaj.utils.Settings;

/**
 * e.g. key(command) generate(args0) {@code<account>}(args1)
 *
 */
public class CommandExecutor {
	public void onCommand(String command, String[] args) {
		switch (command) {
		case "account":
			account(args);
			break;
		case "exit":
			exit(args);
			break;
		default:
			System.out.println("Wrong command.");
		}
	}

	private void account(String[] args) {
		if (args[0].equalsIgnoreCase("generate")) {
			String accountName = args[1];
			File accountFile = AccountHelper.createAccountFile(accountName);

			// msg
			if (accountFile == null) {
				System.out.println(accountName + " is NOT generated");
			} else {
				System.out.println(accountFile.getAbsolutePath() + " generated");
			}
		}
	}

	private void exit(String[] args) {
		System.out.println("[avaJ Blockchain] Bye");
		System.exit(0);
	}
}
