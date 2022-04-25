package com.avaj.hashtree;

import java.util.ArrayList;
import java.util.List;

public class HashTree {
	private HashTreeNode root;

	/**
	 * Not raw data, hashed value
	 */
	private List<String> hashData;

	public HashTree() {
		this.hashData = new ArrayList<>();
	}

	public HashTree(List<String> hashData) {
		this.hashData = hashData;
	}

	public HashTreeNode getRoot() {
		return this.root;
	}

	public HashTree build() {
		if (this.hashData.isEmpty()) {
			return this;
		}

		// convert to HashTreeNode
		List<HashTreeNode> nodes = new ArrayList<>();
		for (String hash : this.hashData) {
			nodes.add(new HashTreeNode(hash));
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

	public void addData(String data) {
		this.hashData.add(data);
	}

	public void removeData(String data) {
		this.hashData.remove(data);
	}

	public boolean containsData(String data) {
		return this.hashData.contains(data);
	}

}
