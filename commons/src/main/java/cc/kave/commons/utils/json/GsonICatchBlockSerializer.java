package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonICatchBlockSerializer implements JsonSerializer<ICatchBlock>, JsonDeserializer<ICatchBlock> {

	@Override
	public JsonElement serialize(ICatchBlock src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("$type", JsonUtils.getTypePath(src));
		jsonObject.addProperty("Parameter", JsonUtils.parseName(src.getParameter()));
		jsonObject.add("Body", JsonUtils.parseListToJson(src.getBody()));
		jsonObject.addProperty("IsGeneral", src.isGeneral());
		jsonObject.addProperty("IsUnnamed", src.isUnnamed());
		return jsonObject;
	}

	@Override
	public ICatchBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
		catchBlock.setParameter(JsonUtils.parseJson(jsonObject.get("Parameter").toString(), ParameterName.class));
		if (jsonObject.get("IsGeneral") != null)
			catchBlock.setGeneral(jsonObject.get("IsGeneral").getAsBoolean());
		if (jsonObject.get("IsUnnamed") != null)
			catchBlock.setUnnamed(jsonObject.get("IsUnnamed").getAsBoolean());
		return catchBlock;
	}
}
