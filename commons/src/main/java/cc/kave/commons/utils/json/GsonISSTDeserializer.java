package cc.kave.commons.utils.json;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
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
		SST sst = new SST();
		sst.setEnclosingType(JsonUtils.parseJson(jsonObject.get("EnclosingType").toString(), TypeName.class));

		Set<IFieldDeclaration> fields = new HashSet<IFieldDeclaration>();
		for (JsonElement j : jsonObject.getAsJsonArray("Fields")) {
			fields.add(JsonUtils.parseJson(j.toString(), IFieldDeclaration.class));
		}
		Set<IPropertyDeclaration> properties = new HashSet<IPropertyDeclaration>();
		for (JsonElement j : jsonObject.getAsJsonArray("Properties")) {
			properties.add(JsonUtils.parseJson(j.toString(), IPropertyDeclaration.class));
		}
		sst.setFields(fields);
		sst.setProperties(properties);
		return sst;
	}
}
