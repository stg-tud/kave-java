package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EnumDeSerializer<T extends Enum<T>> implements JsonSerializer<T>, JsonDeserializer<T> {

	private T[] values;

	private EnumDeSerializer(T[] values) {
		this.values = values;
	}

	public static <U extends Enum<U>> EnumDeSerializer<U> create(U[] values) {
		return new EnumDeSerializer<>(values);
	}

	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		int index = -1;
		for (int i = 0; i < values.length; i++) {
			if (values[i] == src) {
				index = i;
			}
		}

		return new JsonPrimitive(index);
	}

	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		int index = json.getAsInt();
		if (index >= 0 && index < values.length) {
			return values[index];
		}
		return values[0];
	}
}