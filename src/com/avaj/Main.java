package com.avaj;

import java.util.Scanner;

import com.avaj.commands.CommandExecutor;
import com.avaj.utils.Settings;

public class Main {
	public static void main(String[] args) {
		Main main = new Main();
		main.setup();

		main.run();
	}

	private void setup() {
		// make empty directories
		Settings.DATA_DIR.mkdirs();
		Settings.ACCOUNTS_DIR.mkdirs();
	}

	private void run() {
		System.out.println("[avaJ Blockchain]");

		Scanner sc = new Scanner(System.in);

		CommandExecutor cmdExecutor = new CommandExecutor();
		while (true) {
			try {
				System.out.print("> ");
				String input = sc.nextLine();

				String[] cmds = input.split(" ");
				String command = cmds[0];

				String[] arguments = new String[cmds.length - 1];
				for (int i = 1; i < cmds.length; i++) {
					arguments[i - 1] = cmds[i];
				}

				// execute command
				cmdExecutor.onCommand(command, arguments);
			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("Wrong command.");
				continue;
			}
		}
	}
}
