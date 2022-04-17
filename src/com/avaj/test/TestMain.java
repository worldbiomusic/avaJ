package com.avaj.test;

import java.io.File;
import java.util.List;

import com.avaj.test.blockchain.Block;
import com.avaj.test.crypto.CryptoUtils;
import com.avaj.test.crypto.FileUtils;
import com.avaj.test.crypto.KeyWallet;
import com.avaj.test.hashtree.HashTree;
import com.avaj.test.hashtree.HashTreeNode;

public class TestMain {
	public static void main(String[] args) {
		TestMain t = new TestMain();
		t.blockchain();
	}

	void blockchain() {
		try {
			Block genesisBlock = new Block(null, "Genesis block");
			genesisBlock.mine();

			Block secondBlock = new Block(genesisBlock, "Second block");
			secondBlock.mine();

			Block thirdBlock = new Block(secondBlock, "Third block");
			thirdBlock.mine();

			System.out.println("Validation: " + thirdBlock.isValid());

//			String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(thirdBlock);
//			System.out.println(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void crypto() {
		try {
			byte[] publicKey = FileUtils.load(new File("public-key.avaj"));
			byte[] privateKey = FileUtils.load(new File("private-key.avaj"));
			KeyWallet keyWallet = new KeyWallet(publicKey, privateKey);
			System.out.println("public key");
			for (byte b : keyWallet.getPublicKey().getEncoded()) {
				System.out.print(b);
			}
			System.out.println();

//			CryptographyTool cryptoTool = new CryptographyTool();
//			String msg = "This is avaJ";
//			byte[] msgBytes = msg.getBytes();
//			System.out.println("raw msg: " + new String(msgBytes));
//			FileUtils.save(new File("msg.txt"), msgBytes);

			byte[] msgFileData = FileUtils.load(new File("msg.txt"));
			byte[] encryptedData = CryptoUtils.encrypt(msgFileData, keyWallet.getPublicKey());
			System.out.println("Encrypted msg: " + new String(encryptedData));

			encryptedData = CryptoUtils.encrypt(msgFileData, keyWallet.getPublicKey());
			System.out.println("Encrypted msg2: " + new String(encryptedData));

			FileUtils.save(new File("encrypted-msg.txt"), encryptedData);

			byte[] decryptedData = CryptoUtils.decrypt(encryptedData, keyWallet.getPrivateKey());
			System.out.println("Decrypted msg: " + new String(decryptedData));

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	void hashtree() {
		try {
			String msg1 = "world";
			String msg2 = "bio";
			String msg3 = "music";

			byte[] msg1Hash = CryptoUtils.hash(msg1.getBytes());
//			CryptographyUtils.printBytes(msg1Hash);
			System.out.println(CryptoUtils.byteToHex(msg1Hash));

			byte[] msg2Hash = CryptoUtils.hash(msg2.getBytes());
//			CryptographyUtils.printBytes(msg2Hash);
			System.out.println(CryptoUtils.byteToHex(msg2Hash));

			byte[] msg3Hash = CryptoUtils.hash(msg3.getBytes());
//			CryptographyUtils.printBytes(msg2Hash);
			System.out.println(CryptoUtils.byteToHex(msg3Hash));
//
//			HashTreeNode node1 = new HashTreeNode(msg1Hash);
//			HashTreeNode node2 = new HashTreeNode(msg2Hash);
//			HashTreeNode node12 = new HashTreeNode(node1, node2);
//			
//			System.out.println(CryptographyUtils.byteToHex(node12.getDigest()));

			HashTree hashTree = new HashTree(List.of(msg1Hash, msg2Hash, msg3Hash));
			HashTreeNode root = hashTree.build().getRoot();
			System.out.println(CryptoUtils.byteToHex(root.getDigest()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
