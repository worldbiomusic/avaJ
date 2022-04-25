package com.avaj;

import java.io.File;
import java.security.Security;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.avaj.blockchain.Block;
import com.avaj.crypto.CryptoUtils;
import com.avaj.crypto.FileUtils;
import com.avaj.crypto.KeyWallet;
import com.avaj.hashtree.HashTree;
import com.avaj.hashtree.HashTreeNode;

public class Main {
	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());

		Main t = new Main();
//		t.crypto();

//		long total = 0;
//		long reward = 1000;
//		for (int i = 50; i > 0; i--) {
//			System.out.println(i + ": " + reward);
////			if(total + reward * 525600 > 5000000000L) {
////				break;
////			}
//			total += reward * 525600;
//			reward = (long) (reward * 9 / 10);
//			
//		}
//		System.out.println("reward: " + reward);
//		System.out.println(total);

	}

	void blockchain() {
		try {
			Block genesisBlock = new Block(null);
			genesisBlock.mine();

			Block secondBlock = new Block(genesisBlock);
			secondBlock.mine();

			Block thirdBlock = new Block(secondBlock);
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
			KeyWallet keyWallet = new KeyWallet();
			FileUtils.save(new File("private-key"), keyWallet.getPublicKey().getEncoded());
			FileUtils.save(new File("public-key"), keyWallet.getPrivateKey().getEncoded());

//			byte[] publicKey = FileUtils.load(new File("public-key.avaj"));
//			byte[] privateKey = FileUtils.load(new File("private-key.avaj"));
//			KeyWallet keyWallet = new KeyWallet(publicKey, privateKey);
//			System.out.println("public key");
//			for (byte b : keyWallet.getPublicKey().getEncoded()) {
//				System.out.print(b);
//			}
//			System.out.println();

			String msg = "This is avaJ";
			byte[] msgBytes = msg.getBytes();
			System.out.println("raw msg: " + new String(msgBytes));
			FileUtils.save(new File("msg.txt"), msgBytes);

			byte[] msgFileData = FileUtils.load(new File("msg.txt"));
			byte[] encryptedData = CryptoUtils.encrypt(msgFileData, keyWallet.getPublicKey());
			System.out.println("Encrypted msg: " + new String(encryptedData));

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
			System.out.println(CryptoUtils.bytesToHex(msg1Hash));

			byte[] msg2Hash = CryptoUtils.hash(msg2.getBytes());
//			CryptographyUtils.printBytes(msg2Hash);
			System.out.println(CryptoUtils.bytesToHex(msg2Hash));

			byte[] msg3Hash = CryptoUtils.hash(msg3.getBytes());
//			CryptographyUtils.printBytes(msg2Hash);
			System.out.println(CryptoUtils.bytesToHex(msg3Hash));
//
//			HashTreeNode node1 = new HashTreeNode(msg1Hash);
//			HashTreeNode node2 = new HashTreeNode(msg2Hash);
//			HashTreeNode node12 = new HashTreeNode(node1, node2);
//			
//			System.out.println(CryptographyUtils.byteToHex(node12.getDigest()));

			HashTree hashTree = new HashTree(List.of(msg1Hash, msg2Hash, msg3Hash));
			HashTreeNode root = hashTree.build().getRoot();
			System.out.println(CryptoUtils.bytesToHex(root.getDigest()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
