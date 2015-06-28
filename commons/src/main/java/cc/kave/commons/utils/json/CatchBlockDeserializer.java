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

public class CatchBlockDeserializer implements JsonDeserializer<ICatchBlock> {

	@Override
	public ICatchBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		CatchBlock catchBlock = new CatchBlock();
		for (JsonElement j : jsonObject.getAsJsonArray("Body"))
			catchBlock.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
		catchBlock.setParameter(JsonUtils.parseJson(jsonObject.get("Parameter").toString(), ParameterName.class));
		if (jsonObject.get("IsGeneral") != null)
			catchBlock.setGeneral(jsonObject.get("IsGeneral").getAsBoolean());
		if (jsonObject.get("IsUnnamed") != null)
			catchBlock.setUnnamed(jsonObject.get("IsUnnamed").getAsBoolean());
		return catchBlock;
	}
}
