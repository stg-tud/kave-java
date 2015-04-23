package cc.kave.commons.utils.json;

import cc.kave.commons.model.names.AliasName;
import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.LambdaName;
import cc.kave.commons.model.names.LocalVariableName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsAliasName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JsonUtils {
	public static <T> T parseJson(String json, Class<T> targetType) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		// name interface types
		gsonBuilder.registerTypeAdapter(AliasName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(BundleName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(EventName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(FieldName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(LambdaName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(LocalVariableName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(MethodName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(Name.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(NamespaceName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(ParameterName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(PropertyName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(TypeName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(DelegateTypeName.class, new GsonNameDeserializer());
		// C# name types
		gsonBuilder.registerTypeAdapter(CsAliasName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsAssemblyName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsEventName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsFieldName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsLambdaName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsLocalVariableName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsMethodName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsNamespaceName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsParameterName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsPropertyName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsTypeName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsDelegateTypeName.class, new GsonNameDeserializer());
		Gson gson = gsonBuilder.create();

		return gson.fromJson(json, targetType);
	}
}
