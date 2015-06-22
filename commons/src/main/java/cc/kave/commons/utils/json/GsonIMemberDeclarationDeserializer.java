package cc.kave.commons.utils.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;

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
		case "FieldDeclaration":
			FieldDeclaration field = new FieldDeclaration();
			field.setName(JsonUtils.parseJson(jsonObject.get("Name").toString(), FieldName.class));
			return field;
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
		case "UnknownReference":
			return new UnknownReference();
		default:
			return null;
		}
	}
}
