package com.avaj.crypto;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.avaj.utils.Settings;

public class KeyWallet implements Cloneable {
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
	public KeyWallet(byte[] publicKey, byte[] privateKey) {
		try {
			this.publicKey = KeyFactory.getInstance(Settings.CRYPTO_ALGORITHM)
					.generatePublic(new X509EncodedKeySpec(publicKey));
			if (privateKey != null) {
				this.privateKey = KeyFactory.getInstance(Settings.CRYPTO_ALGORITHM)
						.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public KeyWallet(String publicKeyHex, String privateKeyHex) {
		this(CryptoUtils.hexToBytes(publicKeyHex),
				(privateKeyHex == null) ? null : CryptoUtils.hexToBytes(privateKeyHex));
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

	public String doHash() {
		// only public key exists on the network
		return CryptoUtils.hashToHex(this.publicKey.getEncoded());
	}

	@Override
	public Object clone() {
		String publicKeyHex = CryptoUtils.bytesToHex(this.publicKey.getEncoded());
		String privateKeyHex = CryptoUtils.bytesToHex(this.privateKey.getEncoded());
		return new KeyWallet(publicKeyHex, privateKeyHex);
	}

	@Override
	public String toString() {
		return "KeyWallet [\npublicKey=" + CryptoUtils.bytesToHex(this.publicKey.getEncoded()) + "]";
	}

}
