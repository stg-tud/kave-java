package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsName;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GsonNameDeserializer implements JsonDeserializer<Name> {

	@Override
	public Name deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		final String identifier = json.getAsString().split(":")[1];
        return CsName.newName(identifier);
	}

}
