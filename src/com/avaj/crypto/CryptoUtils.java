package com.avaj.crypto;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.List;

import javax.crypto.Cipher;

import com.avaj.utils.Settings;

public class CryptoUtils {

	public static byte[] encrypt(byte[] data, Key key) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(Settings.CRYPTO_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] decrypt(byte[] data, Key key) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(Settings.CRYPTO_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] hash(byte[] data) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String hashToHex(byte[] bytes) {
		return bytesToHex(hash(bytes));
	}

	public static String hexToBinary(String hex) {
		int length = hex.length() * 4;
		String format = "%" + length + "s";
		return String.format(format, new BigInteger(hex, 16).toString(2)).replace(" ", "0");
	}

	public static String bytesToHex(byte[] bytes) {
		String hex = "";
		for (byte b : bytes) {
			hex += String.format("%02x", b);
		}
		return hex;
	}

	public static void printBytes(byte[] bytes) {
		for (byte b : bytes) {
			System.out.print(b);
		}
		System.out.println();
	}

	public static byte[] combineBytes(List<byte[]> byteList) {

		int byteLength = 0;
		for (byte[] list : byteList) {
			byteLength += list.length;
		}

		byte[] combinedBytes = new byte[byteLength];
		ByteBuffer buffer = ByteBuffer.wrap(combinedBytes);
		byteList.forEach(buffer::put);

		return buffer.array();
	}

	public static byte[] sign(byte[] data, PrivateKey key) {
		byte[] sign = new byte[0];
		try {
			Signature signature = Signature.getInstance(Settings.CRYPTO_ALGORITHM, "BC");
			signature.initSign(key);
			signature.update(data);
			sign = signature.sign();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	public static boolean verify(byte[] data, PublicKey publicKey, byte[] sign) {
		boolean verified = false;
		try {
			Signature signature = Signature.getInstance(Settings.CRYPTO_ALGORITHM, "BC");
			signature.initVerify(publicKey);
			signature.update(data);
			verified = signature.verify(sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verified;
	}
}
