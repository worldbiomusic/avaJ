package com.avaj.hashtree;

import com.avaj.crypto.CryptoUtils;

public class HashTreeNode {
	private String hash;
	private HashTreeNode leftChild;
	private HashTreeNode rightChild;

	public HashTreeNode(String hash) {
		this.hash = hash;
	}

	public HashTreeNode(HashTreeNode leftChild, HashTreeNode rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;

		// set hash using left and right child's hash
		String combinedHash = leftChild.getHash() + rightChild.getHash();

		this.hash = CryptoUtils.hashToHex(combinedHash.getBytes());
	}

	public String getHash() {
		return this.hash;
	}

	public HashTreeNode getLeftChild() {
		return this.leftChild;
	}

	public HashTreeNode getRightChild() {
		return this.rightChild;
	}
}
