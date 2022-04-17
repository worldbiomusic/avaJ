package com.avaj.test.crypto.hashtree;

import java.util.List;

import com.avaj.test.crypto.CryptoUtils;

public class HashTreeNode {
	private byte[] digest;
	private HashTreeNode leftChild;
	private HashTreeNode rightChild;

	public HashTreeNode(byte[] digest) {
		this.digest = digest;
	}

	public HashTreeNode(HashTreeNode leftChild, HashTreeNode rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;

		// set digest using left and right child's digest
		byte[] combinedBytes = CryptoUtils
				.combineBytes(List.of(leftChild.getDigest(), rightChild.getDigest()));

		this.digest = CryptoUtils.hash(combinedBytes);
	}

	public byte[] getDigest() {
		return this.digest;
	}

	public HashTreeNode getLeftChild() {
		return this.leftChild;
	}

	public HashTreeNode getRightChild() {
		return this.rightChild;
	}
}
