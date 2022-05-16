package com.avaj.json.serializer;

import java.lang.reflect.Type;

import com.avaj.crypto.CryptoUtils;
import com.avaj.crypto.KeyWallet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class KeyWalletSerializer implements JsonSerializer<KeyWallet>, JsonDeserializer<KeyWallet> {

	@Override
	public JsonElement serialize(KeyWallet keyWallet, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(CryptoUtils.bytesToHex(keyWallet.getPublicKey().getEncoded()));
	}

	@Override
	public KeyWallet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		String publicKeyHex = json.getAsJsonPrimitive().getAsString();
		return new KeyWallet(publicKeyHex, null);
	}

}