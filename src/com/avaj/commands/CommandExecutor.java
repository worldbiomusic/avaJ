package com.avaj.commands;

import java.io.File;

import com.avaj.blockchain.Account;
import com.avaj.blockchain.AccountHelper;
import com.avaj.blockchain.Block;
import com.avaj.blockchain.Block.Scope;
import com.avaj.utils.Utils;

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
	 * block account <public-key> [<block index>]
	 */
	private void block(String[] args) {
		String menu = args[0];

		switch (menu) {
		case "status":
			block_status(args);
			break;
		case "account":
			block_account(args);
			break;
		}
	}

	private void block_status(String[] args) {
		long index = (args.length == 2) ? Long.parseLong(args[1]) : Block.TOP.getLength();

		Block topBlock = Block.TOP;
		Block block = topBlock.indexOf(index);

		// print
		System.out.println("Index: " + index);
		System.out.println(block.data(Scope.ONE));
	}

	private void block_account(String[] args) {
		String publicKey = args[1];

		long index = (args.length == 3) ? Long.parseLong(args[2]) : Block.TOP.getLength();

		Block topBlock = Block.TOP;
		Block block = topBlock.indexOf(index);

		// check exist publicKey
		if (!block.getAccountManager().containsAccount(publicKey)) {
			System.out.println(publicKey + " doesn't exist");
			return;
		}

		Account account = block.getAccountManager().getAccount(publicKey);
		System.out.println(publicKey + " account in " + index + " block");
		System.out.println(account.toString());
	}

	private void exit(String[] args) {
		System.out.println("[avaJ Blockchain] Bye");
		System.exit(0);
	}
}
