package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

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
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonIMemberDeclarationSerializer implements JsonSerializer<IMemberDeclaration>,
		JsonDeserializer<IMemberDeclaration> {

	@Override
	public JsonElement serialize(IMemberDeclaration src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		String discriminator = src.getClass().getSimpleName();
		jsonObject.addProperty("$type", JsonUtils.getTypePath(src));
		switch (discriminator) {
		case "FieldDeclaration":
			FieldDeclaration f = (FieldDeclaration) src;
			jsonObject.addProperty("Name", JsonUtils.parseName(f.getName()));
			return jsonObject;
		case "DelegateDeclaration":
			DelegateDeclaration d = (DelegateDeclaration) src;
			jsonObject.addProperty("Name", JsonUtils.parseName(d.getName()));
			return jsonObject;
		case "EventDeclaration":
			EventDeclaration e = (EventDeclaration) src;
			jsonObject.addProperty("Name", JsonUtils.parseName(e.getName()));
			return jsonObject;
		case "MethodDeclaration":
			MethodDeclaration m = (MethodDeclaration) src;
			jsonObject.addProperty("Name", JsonUtils.parseName(m.getName()));
			jsonObject.addProperty("IsEntryPoint", m.isEntryPoint());
			jsonObject.add("Body", JsonUtils.parseListToJson(m.getBody()));
			return jsonObject;
		case "PropertyDeclaration":
			PropertyDeclaration p = (PropertyDeclaration) src;
			jsonObject.addProperty("Name", JsonUtils.parseName(p.getName()));
			jsonObject.add("Get", JsonUtils.parseListToJson(p.getGet()));
			jsonObject.add("Set", JsonUtils.parseListToJson(p.getSet()));
			return jsonObject;
		case "EventReference":
			EventReference er = (EventReference) src;
			jsonObject.add("Reference", JsonUtils.parseObject(er.getReference(), er.getReference().getClass()));
			jsonObject.addProperty("EventName", JsonUtils.parseName(er.getEventName()));
			return jsonObject;
		case "FieldReference":
			FieldReference fr = (FieldReference) src;
			jsonObject.add("Reference", JsonUtils.parseObject(fr.getReference(), fr.getReference().getClass()));
			jsonObject.addProperty("FieldName", JsonUtils.parseName(fr.getFieldName()));
			return jsonObject;
		case "PropertyReference":
			PropertyReference pr = (PropertyReference) src;
			jsonObject.add("Reference", JsonUtils.parseObject(pr.getReference(), pr.getReference().getClass()));
			jsonObject.addProperty("PropertyName", JsonUtils.parseName(pr.getPropertyName()));
			return jsonObject;
		case "VariableReference":
			VariableReference vr = (VariableReference) src;
			jsonObject.addProperty("Identifier", vr.getIdentifier());
			return jsonObject;
		case "MethodReference":
			MethodReference mr = (MethodReference) src;
			jsonObject.add("Reference", JsonUtils.parseObject(mr.getReference(), mr.getReference().getClass()));
			jsonObject.addProperty("MethodName", JsonUtils.parseName(mr.getMethodName()));
			return jsonObject;
		default:
			return jsonObject;
		}
	}

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
			meDecl.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
			return meDecl;
		case "PropertyDeclaration":
			PropertyDeclaration propertyDeclaration = new PropertyDeclaration();
			propertyDeclaration.setName(JsonUtils.parseJson(jsonObject.get("Name").toString(), PropertyName.class));
			propertyDeclaration.setGet(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Get")));
			propertyDeclaration.setSet(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Set")));
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
