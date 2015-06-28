package cc.kave.commons.utils.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

public class GsonIExpressionDeserializer implements JsonDeserializer<IExpression> {

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
			List<IVariableReference> references = new ArrayList<>();
			for (JsonElement j : jsonObject.getAsJsonArray("References")) {
				references.add(JsonUtils.parseJson(j.toString(), IVariableReference.class));
			}
			composedExp.setReferences(references);
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
			List<ISimpleExpression> parameters = new ArrayList<>();
			for (JsonElement j : jsonObject.getAsJsonArray("Parameters")) {
				parameters.add(JsonUtils.parseJson(j.toString(), ISimpleExpression.class));
			}
			invoExp.setParameters(parameters);
			return invoExp;
		case "LambdaExpression":
			LambdaExpression lambdaExp = new LambdaExpression();
			List<IStatement> body = new ArrayList<>();
			for (JsonElement j : jsonObject.getAsJsonArray("Body")) {
				body.add(JsonUtils.parseJson(j.toString(), IStatement.class));
			}
			lambdaExp.setBody(body);
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
			List<IStatement> b = new ArrayList<>();
			for (JsonElement j : jsonObject.getAsJsonArray("Body")) {
				b.add(JsonUtils.parseJson(j.toString(), IStatement.class));
			}
			loopExp.setBody(b);
			return loopExp;
		default:
			return null;
		}
	}
}
