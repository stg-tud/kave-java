package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.references.IAssignableReference;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class GsonIStatementDeserializer implements JsonDeserializer<IStatement> {

	@Override
	public IStatement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		String discriminator = jsonObject
				.get("$type")
				.getAsString()
				.substring(jsonObject.get("$type").getAsString().lastIndexOf('.') + 1,
						jsonObject.get("$type").getAsString().length() - 1);
		switch (discriminator) {
		case "ReturnStatement":
			ReturnStatement statement = new ReturnStatement();
			statement.setIsVoid(jsonObject.get("IsVoid").getAsBoolean());
			statement.setExpression(JsonUtils.parseJson(jsonObject.get("Expression").toString(),
					ISimpleExpression.class));
			return statement;
		case "Assignment":
			Assignment assignment = new Assignment();
			assignment.setExpression(JsonUtils.parseJson(jsonObject.get("Expression").toString(),
					IAssignableExpression.class));
			assignment.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(),
					IAssignableReference.class));
			return assignment;
		default:
			return null;
		}
	}
}
