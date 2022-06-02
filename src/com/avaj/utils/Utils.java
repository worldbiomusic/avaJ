package com.avaj.utils;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.avaj.crypto.FileUtils;
import com.avaj.crypto.KeyWallet;
import com.avaj.json.serializer.InstantSerializer;
import com.avaj.json.serializer.KeyWalletSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {
	public static Gson gson() {
		GsonBuilder b = new GsonBuilder();

		// type adapter
		b.registerTypeAdapter(Instant.class, new InstantSerializer());
		b.registerTypeAdapter(KeyWallet.class, new KeyWalletSerializer());

		// create
		b.setPrettyPrinting();
		return b.create();
	}

//	public static Gson gsonOneBlock() {
//		return gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
//	}

	public static KeyWallet loadKeyWallet(File publicKeyFile, File privateKeyFile) {
		try {
			return new KeyWallet(FileUtils.load(publicKeyFile), FileUtils.load(privateKeyFile));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void debug(String msg) {
		if (Settings.DEBUG) {
			Logger.getLogger("avaj").log(Level.WARNING, msg);
		}
	}

}
