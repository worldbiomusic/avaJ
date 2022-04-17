package com.avaj.test.crypto.hashtree;

import java.util.ArrayList;
import java.util.List;

public class HashTree {
	private HashTreeNode root;
	
	/**
	 * Not raw bytes, hashed value bytes (digest)
	 */
	private List<byte[]> hashBytes;

	public HashTree(List<byte[]> hashBytes) {
		this.hashBytes = hashBytes;
	}

	public HashTreeNode getRoot() {
		return this.root;
	}

	public HashTree build() {
		if (this.hashBytes.isEmpty()) {
			return this;
		}

		// convert byte data to HashTreeNode
		List<HashTreeNode> nodes = new ArrayList<>();
		for (byte[] bytes : this.hashBytes) {
			nodes.add(new HashTreeNode(bytes));
		}

		// combine all nodes until only one node remains
		while (nodes.size() != 1) {
			List<HashTreeNode> nextNodes = new ArrayList<>();

			for (int i = 0; i < nodes.size(); i += 2) {
				HashTreeNode leftChild = nodes.get(i);
				// if not exist, use leftChild again
				HashTreeNode rightChild = ((i + 1) < nodes.size()) ? nodes.get(i + 1) : nodes.get(i);
				nextNodes.add(combine(leftChild, rightChild));
			}

			nodes = nextNodes;
		}

		// set root node
		this.root = nodes.get(0);
		
		return this;
	}

	private HashTreeNode combine(HashTreeNode node1, HashTreeNode node2) {
		return new HashTreeNode(node1, node2);
	}

	public void addData(byte[] data) {
		this.hashBytes.add(data);
	}

	public void removeData(byte[] data) {
		this.hashBytes.remove(data);
	}

	public boolean containsData(byte[] data) {
		return this.hashBytes.contains(data);
	}

}
