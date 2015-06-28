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

public class CaseBlockDeserializer implements JsonDeserializer<ICaseBlock> {

	@Override
	public ICaseBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		CaseBlock caseBlock = new CaseBlock();
		for (JsonElement j : jsonObject.getAsJsonArray("Body"))
			caseBlock.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
		caseBlock.setLabel(JsonUtils.parseJson(jsonObject.get("Label").toString(), ISimpleExpression.class));
		return caseBlock;
	}
}
