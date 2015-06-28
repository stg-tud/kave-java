package cc.kave.commons.utils.json;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.impl.SST;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class GsonISSTDeserializer implements JsonDeserializer<ISST> {

	@Override
	public ISST deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		Set<IFieldDeclaration> fields = new HashSet<IFieldDeclaration>();
		for (JsonElement j : jsonObject.getAsJsonArray("Fields")) {
			fields.add(JsonUtils.parseJson(j.toString(), IFieldDeclaration.class));
		}
		Set<IPropertyDeclaration> properties = new HashSet<IPropertyDeclaration>();
		for (JsonElement j : jsonObject.getAsJsonArray("Properties")) {
			properties.add(JsonUtils.parseJson(j.toString(), IPropertyDeclaration.class));
		}
		Set<IMethodDeclaration> methods = new HashSet<IMethodDeclaration>();
		for (int i = 0; i < jsonObject.getAsJsonArray("Methods").size(); i++) {
			JsonElement j = jsonObject.getAsJsonArray("Methods").get(i);
			methods.add(JsonUtils.parseJson(j.toString(), IMethodDeclaration.class));
		}
		Set<IEventDeclaration> events = new HashSet<IEventDeclaration>();
		for (JsonElement j : jsonObject.getAsJsonArray("Events")) {
			events.add(JsonUtils.parseJson(j.toString(), IEventDeclaration.class));
		}
		Set<IDelegateDeclaration> delegates = new HashSet<IDelegateDeclaration>();
		for (JsonElement j : jsonObject.getAsJsonArray("Delegates")) {
			delegates.add(JsonUtils.parseJson(j.toString(), IDelegateDeclaration.class));
		}
		SST sst = new SST();
		sst.setEnclosingType(JsonUtils.parseJson(jsonObject.get("EnclosingType").toString(), TypeName.class));
		sst.setFields(fields);
		sst.setProperties(properties);
		sst.setDelegates(delegates);
		sst.setEvents(events);
		sst.setMethods(methods);
		return sst;
	}
}
