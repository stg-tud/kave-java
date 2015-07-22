package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonNameDeserializer implements JsonDeserializer<Name>, JsonSerializer<Name> {

	@Override
	public Name deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String[] nameInfo = json.getAsString().split(":", 2);
		String discriminator = nameInfo[0];
		final String identifier = nameInfo[1];
		switch (discriminator) {
		case "CSharp.AliasName":
			return CsAliasName.newAliasName(identifier);
		case "CSharp.AssemblyName":
			return CsAssemblyName.newAssemblyName(identifier);
		case "CSharp.EventName":
			return CsEventName.newEventName(identifier);
		case "CSharp.FieldName":
			return CsFieldName.newFieldName(identifier);
		case "CSharp.LambdaName":
			return CsLambdaName.newLambdaName(identifier);
		case "CSharp.LocalVariableName":
			return CsLocalVariableName.newLocalVariableName(identifier);
		case "CSharp.MethodName":
			return CsMethodName.newMethodName(identifier);
		case "CSharp.Name":
			return CsName.newName(identifier);
		case "CSharp.NamespaceName":
			return CsNamespaceName.newNamespaceName(identifier);
		case "CSharp.ParameterName":
			return CsParameterName.newParameterName(identifier);
		case "CSharp.PropertyName":
			return CsPropertyName.newPropertyName(identifier);
		case "CSharp.TypeName":
			return CsTypeName.newTypeName(identifier);
		default:
			if (discriminator.startsWith("CSharp.") && discriminator.endsWith("TypeName")) {
				return CsTypeName.newTypeName(identifier);
			}
			throw new JsonParseException("Not a valid serialized name: '" + json + "'");
		}
	}

	@Override
	public JsonElement serialize(Name src, Type typeOfSrc, JsonSerializationContext context) {
		String superType;
		if (src.getClass().getGenericInterfaces().length != 0) {
			superType = src.getClass().getGenericInterfaces()[0].getTypeName();
			superType = superType.substring(superType.lastIndexOf('.'));
		} else
			superType = "." + src.getClass().getSimpleName().substring(2);

		return new JsonPrimitive("CSharp" + superType + ":" + src.getIdentifier());
	}

}
