package com.avaj.test.crypto;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.avaj.test.utils.Settings;

public class KeyWallet {
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public KeyWallet() {
		try {
			// use ECDSA
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(Settings.CRYPTO_ALGORITHM);

			// secure random
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			keyGenerator.initialize(1024, random);

			KeyPair keyPair = keyGenerator.generateKeyPair();
			this.publicKey = keyPair.getPublic();
			this.privateKey = keyPair.getPrivate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor for exist key pair
	 */
	public KeyWallet(byte[] publicKey, byte[] privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
		this.publicKey = KeyFactory.getInstance(Settings.CRYPTO_ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKey));
		this.privateKey = KeyFactory.getInstance(Settings.CRYPTO_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateKey));
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public void savePublicKey(File file) throws IOException {
		FileUtils.save(file, this.getPublicKey().getEncoded());
	}

	public void savePrivateKey(File file) throws IOException {
		FileUtils.save(file, this.getPrivateKey().getEncoded());
	}
}
