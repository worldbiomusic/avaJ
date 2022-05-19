package com.avaj.commands;

import java.io.File;

import com.avaj.blockchain.AccountHelper;
import com.avaj.blockchain.Block;
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
		case "block":
			block(args);
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

	/*
	 * block status [index]
	 * block account <public-key>
	 */
	private void block(String[] args) {
		String menu = args[0];

		switch (menu) {
		case "status":

			break;
		case "account":

			break;
		}
	}

	private void block_status(String[] args) {
		int index = (args.length == 2) ? Integer.parseInt(args[1]) : Block.TOP.getLength() ;
		
		Block topBlock = Block.TOP;
		topBlock.indexOf(index);
		
		
	}

	private void block_account(String[] args) {

	}

	private void exit(String[] args) {
		System.out.println("[avaJ Blockchain] Bye");
		System.exit(0);
	}
}
