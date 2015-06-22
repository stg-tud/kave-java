package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class GsonIExpressionDeserializer implements JsonDeserializer<IExpression> {

	@Override
	public IExpression deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		String discriminator = jsonObject
				.get("$type")
				.getAsString()
				.substring(jsonObject.get("$type").getAsString().lastIndexOf('.') + 1,
						jsonObject.get("$type").getAsString().length() - 1);
		switch (discriminator) {
		case "UnknownExpression":
			return new UnknownExpression();
		default:
			return null;
		}
	}

}
