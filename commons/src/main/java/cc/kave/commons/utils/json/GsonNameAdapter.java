package cc.kave.commons.utils.json;

import java.lang.reflect.Type;
import java.util.Map;

import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsAliasName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsLambdaName;
import cc.kave.commons.model.names.csharp.CsLocalVariableName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.names.csharp.CsUnknownTypeName;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonNameAdapter implements JsonDeserializer<Name>, JsonSerializer<Name> {

	public static interface NameFactory {
		public Name create(String identifier);
	}

	@SuppressWarnings("serial")
	private static Map<String, NameFactory> DiscriminatorToFactory = new java.util.HashMap<String, GsonNameAdapter.NameFactory>() {
		{
			put("CSharp.AliasName", CsAliasName::newAliasName);
			put("CSharp.AssemblyName", CsAssemblyName::newAssemblyName);
			put("CSharp.EventName", CsEventName::newEventName);
			put("CSharp.FieldName", CsFieldName::newFieldName);
			put("CSharp.LambdaName", CsLambdaName::newLambdaName);
			put("CSharp.LocalVariableName", CsLocalVariableName::newLocalVariableName);
			put("CSharp.MethodName", CsMethodName::newMethodName);
			put("CSharp.Name", CsName::newName);
			put("CSharp.NamespaceName", CsNamespaceName::newNamespaceName);
			put("CSharp.ParameterName", CsParameterName::newParameterName);
			put("CSharp.PropertyName", CsPropertyName::newPropertyName);
			put("CSharp.TypeName", CsTypeName::newTypeName);
			put("CSharp.UnknownTypeName", CsUnknownTypeName::newTypeName);
		}
	};

	@Override
	public Name deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String[] nameInfo = json.getAsString().split(":", 2);
		String discriminator = nameInfo[0];
		final String identifier = nameInfo[1];

		NameFactory factory = null;
		if (DiscriminatorToFactory.containsKey(discriminator)) {
			factory = DiscriminatorToFactory.get(discriminator);
		} else if (discriminator.startsWith("CSharp.") && discriminator.endsWith("TypeName")) {
			factory = CsTypeName::newTypeName;
		} else {
			throw new JsonParseException("Invalid name serialization: '" + json + "'");
		}
		return factory.create(identifier);
	}

	@Override
	public JsonElement serialize(Name src, Type typeOfSrc, JsonSerializationContext context) {
		String discriminator = src.getClass().getSimpleName().replace("Cs", "");
		return context.serialize(String.format("CSharp.%s:%s", discriminator, src.getIdentifier()));
	}
}
