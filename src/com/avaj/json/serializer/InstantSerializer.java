package com.avaj.json.serializer;

import java.lang.reflect.Type;
import java.time.Instant;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class InstantSerializer implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

	@Override
	public JsonElement serialize(Instant instant, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(instant.toEpochMilli());
	}

	@Override
	public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		long milli = Long.valueOf(json.getAsJsonPrimitive().getAsString());
		return Instant.ofEpochMilli(milli);
	}

}
