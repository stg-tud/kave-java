package gsonexample.serializer;

import gsonexample.model.Intbucket;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class IntbucketSerializer implements JsonSerializer<Intbucket> {

	@Override
	public JsonElement serialize(Intbucket src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("$type", new JsonPrimitive(typeOfSrc.getTypeName()));
		return jsonObject;
	}
}
