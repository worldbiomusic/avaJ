package com.avaj.test.crypto;

import java.nio.ByteBuffer;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Cipher;

public class CryptoUtils {

	public static byte[] encrypt(byte[] data, Key key) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
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
			cipher = Cipher.getInstance("RSA");
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

	public static String byteToHex(byte[] bytes) {
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
}
