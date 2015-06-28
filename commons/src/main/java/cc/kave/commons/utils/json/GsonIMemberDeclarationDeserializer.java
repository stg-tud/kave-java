package cc.kave.commons.utils.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class GsonIMemberDeclarationDeserializer implements JsonDeserializer<IMemberDeclaration> {

	@Override
	public IMemberDeclaration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		String discriminator = jsonObject
				.get("$type")
				.getAsString()
				.substring(jsonObject.get("$type").getAsString().lastIndexOf('.') + 1,
						jsonObject.get("$type").getAsString().length() - 1);
		switch (discriminator) {
		case "DelegateDeclaration":
			DelegateDeclaration delDecl = new DelegateDeclaration();
			delDecl.setName(JsonUtils.parseJson(jsonObject.get("Name").toString(), DelegateTypeName.class));
			return delDecl;
		case "EventDeclaration":
			EventDeclaration evDecl = new EventDeclaration();
			evDecl.setName(JsonUtils.parseJson(jsonObject.get("Name").toString(), EventName.class));
			return evDecl;
		case "FieldDeclaration":
			FieldDeclaration field = new FieldDeclaration();
			field.setName(JsonUtils.parseJson(jsonObject.get("Name").toString(), FieldName.class));
			return field;
		case "MethodDeclaration":
			MethodDeclaration meDecl = new MethodDeclaration();
			meDecl.setName(JsonUtils.parseJson(jsonObject.get("Name").toString(), MethodName.class));
			meDecl.setEntryPoint(jsonObject.get("IsEntryPoint").getAsBoolean());
			List<IStatement> body = new ArrayList<>();
			for (int i = 0; i < jsonObject.getAsJsonArray("Body").size(); i++) {
				JsonElement j = jsonObject.getAsJsonArray("Body").get(i);
				body.add(JsonUtils.parseJson(j.toString(), IStatement.class));
			}
			meDecl.setBody(body);
			return meDecl;
		case "PropertyDeclaration":
			PropertyDeclaration propertyDeclaration = new PropertyDeclaration();
			propertyDeclaration.setName(JsonUtils.parseJson(jsonObject.get("Name").toString(), PropertyName.class));
			List<IStatement> get = new ArrayList<IStatement>();
			for (JsonElement j : jsonObject.getAsJsonArray("Get")) {
				get.add(JsonUtils.parseJson(j.toString(), IStatement.class));
			}
			List<IStatement> set = new ArrayList<IStatement>();
			for (JsonElement j : jsonObject.getAsJsonArray("Set")) {
				set.add(JsonUtils.parseJson(j.toString(), IStatement.class));
			}
			propertyDeclaration.setGet(get);
			propertyDeclaration.setSet(set);
			return propertyDeclaration;
		case "EventReference":
			EventReference evRef = new EventReference();
			evRef.setEventName(JsonUtils.parseJson(jsonObject.get("EventName").toString(), EventName.class));
			evRef.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(), IVariableReference.class));
			return evRef;
		case "FieldReference":
			FieldReference fieldRef = new FieldReference();
			fieldRef.setFieldName(JsonUtils.parseJson(jsonObject.get("FieldName").toString(), FieldName.class));
			fieldRef.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(), IVariableReference.class));
			return fieldRef;
		case "PropertyReference":
			PropertyReference propRef = new PropertyReference();
			propRef.setPropertyName(JsonUtils.parseJson(jsonObject.get("PropertyName").toString(), PropertyName.class));
			propRef.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(), IVariableReference.class));
			return propRef;
		case "UnknownReference":
			return new UnknownReference();
		case "VariableReference":
			VariableReference varRef = new VariableReference();
			varRef.setIdentifier(jsonObject.get("Identifier").getAsString());
			return varRef;
		case "MethodReference":
			MethodReference methodRef = new MethodReference();
			methodRef.setMethodName(JsonUtils.parseJson(jsonObject.get("MethodName").toString(), MethodName.class));
			methodRef.setReference(JsonUtils
					.parseJson(jsonObject.get("Reference").toString(), IVariableReference.class));
			return methodRef;
		default:
			return null;
		}
	}
}
