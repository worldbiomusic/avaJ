package com.avaj.blockchain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.avaj.hashtree.HashTree;

public class TransactionManager {
	transient private Block block;
	private List<Transaction> transactions;

	public TransactionManager(Block block) {
		this.block = block;
		this.transactions = new ArrayList<>();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public boolean addTransaction(Transaction tx) {
		if (containsTransaction(tx)) {
			return false;
		}
		if (!tx.isValid()) {
			return false;
		}
		transactions.add(tx);
		return true;
	}

	public boolean removeTransaction(Transaction tx) {
		return this.transactions.remove(tx);
	}

	public boolean containsTransaction(Transaction tx) {
		return this.transactions.contains(tx);
	}

	public HashTree getHashTree() {
		HashTree tree = new HashTree();
		this.transactions.forEach(t -> tree.addData(t.doHash()));
		return tree.build();
	}

	public boolean isTransactionsValid() {
		return this.transactions.stream().filter(tx -> !tx.isValid()).toList().isEmpty();
	}

	public void processTransactions() {
		AccountManager accountManager = this.block.getAccountManager();
		Account avaJ = block.getAccountManager().getAvaJ();
		Account miner = block.getMiner();

		Set<Transaction> validTransactions = new HashSet<>();

		// process transactions
		Iterator<Transaction> txIterator = this.transactions.iterator();
		while (txIterator.hasNext()) {
			Transaction tx = txIterator.next();
			Account sender = tx.getSender();
			Account receiver = tx.getReceiver();

			if (!accountManager.containsAccount(sender)) {
				continue;
			}
			// load account
			sender = accountManager.getAccount(sender.getHexPublicKey());

			// register receiver to account list if not exist in
			if (!accountManager.containsAccount(receiver)) {
				if (receiver.getValue() != 0) {
					continue;
				}
				// register new receiver account
				accountManager.addAccount(receiver);
			} else {
				// load account
				receiver = accountManager.getAccount(receiver.getHexPublicKey());
			}

			// send value to receiver from sender
			if (!sender.sendValue(receiver, tx.getValue())) {
				continue;
			}

			long fee = tx.getFee();
			// give fee to miner
			if (miner.canValueChange(fee)) {
				miner.plusValue(fee);
			}
			// give fee to avaJ account
			else if (avaJ.canValueChange(fee)) {
				avaJ.plusValue(fee);
			}

			validTransactions.add(tx);
		}

		// remove invalid transcations
		this.transactions.stream().filter(allTx -> !validTransactions.contains(allTx)).forEach(transactions::remove);
	}

	public Account getMiner() {
		Account avaJ = block.getAccountManager().getAvaJ();
		for (Transaction tx : this.transactions) {
			if (tx.getSender().equals(avaJ)) {
				return tx.getReceiver();
			}
		}
		return null;
	}

	public long getTransactionTotalFee() {
		long fee = 0;
		for (Transaction tx : this.transactions) {
			fee += tx.getFee();
		}
		return fee;
	}
}
