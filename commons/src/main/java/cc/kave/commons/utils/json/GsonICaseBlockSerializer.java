package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonICaseBlockSerializer implements JsonSerializer<ICaseBlock>, JsonDeserializer<ICaseBlock> {

	@Override
	public JsonElement serialize(ICaseBlock src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("$type", JsonUtils.getTypePath(src));
		jsonObject.add("Label", JsonUtils.parseObject(src.getLabel(), src.getLabel().getClass()));
		jsonObject.add("Body", JsonUtils.parseListToJson(src.getBody()));
		return jsonObject;
	}

	@Override
	public ICaseBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		CaseBlock caseBlock = new CaseBlock();
		caseBlock.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
		caseBlock.setLabel(JsonUtils.parseJson(jsonObject.get("Label").toString(), ISimpleExpression.class));
		return caseBlock;
	}
}
