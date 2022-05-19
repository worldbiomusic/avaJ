package com.avaj;

import java.io.File;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import com.avaj.blockchain.Account;
import com.avaj.blockchain.Block;
import com.avaj.blockchain.Transaction;
import com.avaj.crypto.CryptoUtils;
import com.avaj.crypto.FileUtils;
import com.avaj.crypto.KeyWallet;
import com.avaj.utils.Settings;
import com.avaj.utils.Utils;
import com.google.gson.Gson;

public class avaJTestMain {
	public static void main(String[] args) {
//		Security.addProvider(new BouncyCastleProvider());

		BigInteger difficulty = new BigInteger("4");
//		BigInteger hash = new BigInteger("");
//		System.out.println(difficulty.compareTo(hash));
		
		System.out.println(difficulty);
		
		difficulty.multiply(new BigInteger("2"));
		System.out.println(difficulty);

		try {
			avaJTestMain t = new avaJTestMain();
//			t.blockchain();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Account getGenesisAvajAccount() throws InvalidKeySpecException, NoSuchAlgorithmException {
		KeyWallet keyWallet = new KeyWallet(Settings.avaJ_PUBLIC_KEY, Settings.avaJ_PRIVATE_KEY);
		Account avaJ = new Account(keyWallet);

		avaJ.plusValue(Settings.TOTAL_SUPPLY);

		return avaJ;
	}

	void blockchain() {
		try {
			Gson gson = Utils.gson();

			// my account
			KeyWallet myKey = Utils.loadKeyWallet(new File("account" + File.separator + "public-key"),
					new File("account" + File.separator + "private-key"));
			Account miner = new Account(myKey);

			// avaJ
			Account avaJ = getGenesisAvajAccount();

			// genesis block
			Block genesisBlock = new Block(null);
			genesisBlock.getAccountManager().addAccount(avaJ);
			genesisBlock.getAccountManager().addAccount(miner);

			// create reward transaction
			Transaction tx = new Transaction(avaJ, miner, genesisBlock.getReward(), 0);
			genesisBlock.getTransactionManager().addTransaction(tx);

			genesisBlock.mine();
			System.out.println("Validation1: " + genesisBlock.isValid());
			System.out.println(gson.toJson(genesisBlock));

			System.out.println("avaJ: " + avaJ.getValue());
			System.out.println("miner: " + miner.getValue());

			// second
			Block secondBlock = new Block(genesisBlock);

//			avaJ = secondBlock.getAccountManager().getAccount(avaJ.getHexPublicKey());
//			miner = secondBlock.getAccountManager().getAccount(miner.getHexPublicKey());
			secondBlock.getTransactionManager()
					.addTransaction(new Transaction(avaJ, miner, secondBlock.getReward(), 0));

			secondBlock.mine();
			System.out.println("Validation2: " + secondBlock.isValid());
			System.out.println(gson.toJson(secondBlock));

			System.out.println("avaJ: " + avaJ.getValue());
			System.out.println("miner: " + miner.getValue());

			// third
			Block thirdBlock = new Block(secondBlock);

//			avaJ = thirdBlock.getAccountManager().getAccount(avaJ.getHexPublicKey());
//			miner = thirdBlock.getAccountManager().getAccount(miner.getHexPublicKey());
			thirdBlock.getTransactionManager().addTransaction(new Transaction(avaJ, miner, thirdBlock.getReward(), 0));

			thirdBlock.mine();

			System.out.println("Validation3: " + thirdBlock.isValid());
			System.out.println(gson.toJson(thirdBlock));

//			Block deserializedBlock = gson.fromJson(jsonString, Block.class);
//			System.out.println(gson.toJson(deserializedBlock));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void crypto() {
		try {
			KeyWallet keyWallet = new KeyWallet();
			FileUtils.save(new File("avaJ-public-key"), keyWallet.getPublicKey().getEncoded());
			FileUtils.save(new File("avaJ-private-key"), keyWallet.getPrivateKey().getEncoded());
////			
			System.out.println(CryptoUtils.bytesToHex(keyWallet.getPublicKey().getEncoded()));
			System.out.println(CryptoUtils.bytesToHex(keyWallet.getPrivateKey().getEncoded()));
			System.out.println();

//			byte[] publicKeyBytes = FileUtils.load(new File("avaJ-public-key"));
//			byte[] privateKeyBytes = FileUtils.load(new File("avaJ-private-key"));
//			byte[] publicKeyBytes = CryptoUtils.hexToBytes(
//					"30819f300d06092a864886f70d010101050003818d00308189028181008fc6716531a1a32afaf714fdba91152d423a2b14041abc8487036b7fb7adfb13b9123a386372eda5e047c019d4aa5b24fee5859e6afd442e6aae2d884d085864a79d8338bb3cc5da15acafe594c4deeb779decac4ff97a96ee94c835b1a6179baf907aeb7939f3ba07984e76d1b8beeed54f8f8783d2d901b248e19d71972ad10203010001");
//			byte[] privateKeyBytes = CryptoUtils.hexToBytes(
//					"30820274020100300d06092a864886f70d01010105000482025e3082025a020100028181008fc6716531a1a32afaf714fdba91152d423a2b14041abc8487036b7fb7adfb13b9123a386372eda5e047c019d4aa5b24fee5859e6afd442e6aae2d884d085864a79d8338bb3cc5da15acafe594c4deeb779decac4ff97a96ee94c835b1a6179baf907aeb7939f3ba07984e76d1b8beeed54f8f8783d2d901b248e19d71972ad10203010001027f2b567b2d3048b3666bea63e96cd34eb98067b2e9d5a8398063dcceb530d36b5b5bedf7f4075b729c62d3e893e0b1791d2db8570e94316e313a4c466d4f75b4842db317dd57e1a836ca09565a7fbd874002ec4a30765efc3a6d7f795b0683708f765523738b8e3c5302e4414ac3c3f603a98430eaf618e7fd6cae9e9c81587b024100df0dcb67f0b4713ce15f6a3e69ddfe2861c8674f189ae0ffeedddb7fecfbded3172fb47fe52e93a9374018e03c67e15362f832b7ec36c4197e5873424e7e5d87024100a502edc9d059e26589b9c6d8eb8edfb027819594396189c76425d56bade085f263a736d37860254d5e47d816cb4f90606dd0f01ad609ff9b5b2da88b612a8ae7024072b7bb8787aaa61ab3a6c913b48b31ee3eec3d05d717c0e4a04b865ab2dd8f6a6da0616aedca18b38cf0f05a5376f1d25b325f936c6f7647ce4d5b6a1cc8acfd0241008efaf9b63c7e16092b8cb93b6ee90ef461570c98321c4d396392d0da0c0117af8f1fee06d130664222e697dc307111c62c81b5fb5ae9b0fd19c5775cc6660993024001670e20dbf5e3bd5ae2a5686fdb166f0d1d857bc8ba0a4fe51a59d3b1ecda1e2f1d327ee156c35f6cb19fa0161931d163f54489a9c5589a8b46ecae2d37c76c");
//			keyWallet = new KeyWallet(publicKeyBytes, privateKeyBytes);

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

//			HashTree hashTree = new HashTree(List.of(msg1Hash, msg2Hash, msg3Hash));
//			HashTreeNode root = hashTree.build().getRoot();
//			System.out.println(CryptoUtils.bytesToHex(root.getHash()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
