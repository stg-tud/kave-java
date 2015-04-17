package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsAliasName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsName;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class GsonNameDeserializer implements JsonDeserializer<Name> {

	@Override
	public Name deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String[] nameInfo = json.getAsString().split(":");
		String discriminator = nameInfo[0];
		final String identifier = nameInfo[1];
		switch (discriminator) {
		case "CSharp.AliasName":
			return CsAliasName.newAliasName(identifier);
		case "CSharp.AssemblyName":
			return CsAssemblyName.newAssemblyName(identifier);
		case "CSharp.Name":
			return CsName.newName(identifier);
		default:
			throw new IllegalArgumentException("Not a valid serialized name: '" + json + "'");
		}
	}

}
