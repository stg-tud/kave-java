package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import cc.kave.commons.model.names.IName;
import cc.kave.commons.model.names.csharp.AliasName;
import cc.kave.commons.model.names.csharp.AssemblyName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.LambdaName;
import cc.kave.commons.model.names.csharp.LocalVariableName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.Name;
import cc.kave.commons.model.names.csharp.NamespaceName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.names.resharper.LiveTemplateName;

public class GsonNameDeserializer implements JsonDeserializer<IName>, JsonSerializer<IName> {

	@Override
	public IName deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String[] nameInfo = json.getAsString().split(":", 2);
		String discriminator = nameInfo[0];
		final String identifier = nameInfo[1];
		switch (discriminator) {
		case "CSharp.AliasName":
			return AliasName.newAliasName(identifier);
		case "CSharp.AssemblyName":
			return AssemblyName.newAssemblyName(identifier);
		case "CSharp.EventName":
			return EventName.newEventName(identifier);
		case "CSharp.FieldName":
			return FieldName.newFieldName(identifier);
		case "CSharp.LambdaName":
			return LambdaName.newLambdaName(identifier);
		case "CSharp.LocalVariableName":
			return LocalVariableName.newLocalVariableName(identifier);
		case "CSharp.MethodName":
			return MethodName.newMethodName(identifier);
		case "CSharp.Name":
			return Name.newName(identifier);
		case "CSharp.NamespaceName":
			return NamespaceName.newNamespaceName(identifier);
		case "CSharp.ParameterName":
			return ParameterName.newParameterName(identifier);
		case "CSharp.PropertyName":
			return PropertyName.newPropertyName(identifier);
		case "CSharp.TypeName":
			return TypeName.newTypeName(identifier);
		/* resharper name */
		case "ReSharper.LiveTemplateName":
			return LiveTemplateName.newLiveTemplateName(identifier);
		default:
			if ((discriminator.startsWith("CSharp.") && discriminator.endsWith("TypeName"))
					|| discriminator.equals("CSharp.TypeParameterName")) {
				return TypeName.newTypeName(identifier);
			}
			throw new JsonParseException("Not a valid serialized name: '" + json + "'");
		}
	}

	@Override
	public JsonElement serialize(IName src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive((src.getClass() != LiveTemplateName.class ? "CSharp." : "ReSharper.")
				+ src.getClass().getSimpleName().replaceFirst("Cs", "") + ":" + src.getIdentifier());
	}

}
