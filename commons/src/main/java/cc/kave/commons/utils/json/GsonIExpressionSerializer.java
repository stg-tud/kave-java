package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import cc.kave.commons.model.names.LambdaName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonIExpressionSerializer implements JsonSerializer<IExpression>, JsonDeserializer<IExpression> {

	@Override
	public JsonElement serialize(IExpression src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		String discriminator = src.getClass().getSimpleName();
		jsonObject.addProperty("$type", JsonUtils.getTypePath(src));
		switch (discriminator) {
		case "CompletionExpression":
			CompletionExpression c = (CompletionExpression) src;
			jsonObject.addProperty("Token", c.getToken());
			return jsonObject;
		case "ComposedExpression":
			ComposedExpression cE = (ComposedExpression) src;
			jsonObject.add("References", JsonUtils.parseListToJson(cE.getReferences()));
			return jsonObject;
		case "IfElseExpression":
			IfElseExpression i = (IfElseExpression) src;
			jsonObject.add("Condition", JsonUtils.parseObject(i.getCondition(), i.getCondition().getClass()));
			jsonObject.add("ThenExpression",
					JsonUtils.parseObject(i.getThenExpression(), i.getThenExpression().getClass()));
			jsonObject.add("ElseExpression",
					JsonUtils.parseObject(i.getElseExpression(), i.getElseExpression().getClass()));
			return jsonObject;
		case "InvocationExpression":
			InvocationExpression iE = (InvocationExpression) src;
			jsonObject.add("Reference", JsonUtils.parseObject(iE.getReference(), iE.getReference().getClass()));
			jsonObject.addProperty("MethodName", JsonUtils.parseName(iE.getMethodName()));
			jsonObject.add("Parameters", JsonUtils.parseListToJson(iE.getParameters()));
			return jsonObject;
		case "LambdaExpression":
			LambdaExpression lE = (LambdaExpression) src;
			jsonObject.addProperty("Name", JsonUtils.parseName(lE.getName()));
			jsonObject.add("Body", JsonUtils.parseListToJson(lE.getBody()));
			return jsonObject;
		case "ReferenceExpression":
			ReferenceExpression rE = (ReferenceExpression) src;
			jsonObject.add("Reference", JsonUtils.parseObject(rE.getReference(), rE.getReference().getClass()));
			return jsonObject;
		case "LoopHeaderBlockExpression":
			LoopHeaderBlockExpression l = (LoopHeaderBlockExpression) src;
			jsonObject.add("Body", JsonUtils.parseListToJson(l.getBody()));
			return jsonObject;
		default:
			return jsonObject;
		}
	}

	@Override
	public IExpression deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		String discriminator = jsonObject
				.get("$type")
				.getAsString()
				.substring(jsonObject.get("$type").getAsString().lastIndexOf('.') + 1,
						jsonObject.get("$type").getAsString().length() - 1);
		switch (discriminator) {
		case "CompletionExpression":
			CompletionExpression compExp = new CompletionExpression();
			compExp.setToken(jsonObject.get("Token").getAsString());
			if (jsonObject.get("TypeReference") != null)
				compExp.setTypeReference(JsonUtils
						.parseJson(jsonObject.get("TypeReference").toString(), TypeName.class));
			if (jsonObject.get("ObjectReference") != null)
				compExp.setObjectReference(JsonUtils.parseJson(jsonObject.get("ObjectReference").toString(),
						IVariableReference.class));
			return compExp;
		case "ComposedExpression":
			ComposedExpression composedExp = new ComposedExpression();
			composedExp.setReferences(JsonUtils.parseJsonToList(IVariableReference.class,
					jsonObject.getAsJsonArray("References")));
			return composedExp;
		case "IfElseExpression":
			IfElseExpression ifElseExp = new IfElseExpression();
			ifElseExp
					.setCondition(JsonUtils.parseJson(jsonObject.get("Condition").toString(), ISimpleExpression.class));
			ifElseExp.setCondition(JsonUtils.parseJson(jsonObject.get("ThenExpression").toString(),
					ISimpleExpression.class));
			ifElseExp.setCondition(JsonUtils.parseJson(jsonObject.get("ElseExpression").toString(),
					ISimpleExpression.class));
			return ifElseExp;
		case "InvocationExpression":
			InvocationExpression invoExp = new InvocationExpression();
			invoExp.setMethodName(JsonUtils.parseJson(jsonObject.get("MethodName").toString(), MethodName.class));
			invoExp.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(), IVariableReference.class));
			invoExp.setParameters(JsonUtils.parseJsonToList(ISimpleExpression.class,
					jsonObject.getAsJsonArray("Parameters")));
			return invoExp;
		case "LambdaExpression":
			LambdaExpression lambdaExp = new LambdaExpression();
			lambdaExp.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
			lambdaExp.setName(JsonUtils.parseJson(jsonObject.get("Name").toString(), LambdaName.class));
			return lambdaExp;
		case "ConstantValueExpression":
			ConstantValueExpression constValExp = new ConstantValueExpression();
			if (jsonObject.get("Value") != null)
				constValExp.setValue(jsonObject.get("Value").getAsString());
			return constValExp;
		case "NullExpression":
			NullExpression nullExp = new NullExpression();
			return nullExp;
		case "ReferenceExpression":
			ReferenceExpression refExp = new ReferenceExpression();
			refExp.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(), IReference.class));
			return refExp;
		case "UnknownExpression":
			return new UnknownExpression();
		case "LoopHeaderBlockExpression":
			LoopHeaderBlockExpression loopExp = new LoopHeaderBlockExpression();
			loopExp.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
			return loopExp;
		default:
			return null;
		}
	}
}
