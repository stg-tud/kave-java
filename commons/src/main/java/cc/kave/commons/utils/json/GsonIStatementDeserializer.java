package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

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

		case "Assignment":
			Assignment assignment = new Assignment();
			assignment.setExpression(JsonUtils.parseJson(jsonObject.get("Expression").toString(),
					IAssignableExpression.class));
			assignment.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(),
					IAssignableReference.class));
			return assignment;
		case "BreakStatement":
			BreakStatement breakStatement = new BreakStatement();
			return breakStatement;
		case "ContinueStatement":
			ContinueStatement continueStatement = new ContinueStatement();
			return continueStatement;
		case "DoLoop":
			DoLoop doLoop = new DoLoop();
			doLoop.setCondition(JsonUtils
					.parseJson(jsonObject.get("Condition").toString(), ILoopHeaderExpression.class));
			for (JsonElement j : jsonObject.getAsJsonArray("Body")) {
				doLoop.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			}
			return doLoop;
		case "ExpressionStatement":
			ExpressionStatement expressionStatement = new ExpressionStatement();
			expressionStatement.setExpression(JsonUtils.parseJson(jsonObject.get("Expression").toString(),
					IAssignableExpression.class));
			return expressionStatement;
		case "ForEachLoop":
			ForEachLoop foreachLoop = new ForEachLoop();
			for (JsonElement j : jsonObject.getAsJsonArray("Body")) {
				foreachLoop.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			}
			foreachLoop.setDeclaration(JsonUtils.parseJson(jsonObject.get("Declaration").toString(),
					IVariableDeclaration.class));
			foreachLoop.setLoopedReference(JsonUtils.parseJson(jsonObject.get("LoopedReference").toString(),
					IVariableReference.class));
			return foreachLoop;
		case "ForLoop":
			ForLoop forLoop = new ForLoop();
			for (JsonElement j : jsonObject.getAsJsonArray("Init"))
				forLoop.getInit().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			for (JsonElement j : jsonObject.getAsJsonArray("Body"))
				forLoop.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			for (JsonElement j : jsonObject.getAsJsonArray("Step"))
				forLoop.getStep().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			forLoop.setCondition(JsonUtils.parseJson(jsonObject.get("Condition").toString(),
					ILoopHeaderExpression.class));
			return forLoop;
		case "GotoStatement":
			GotoStatement gotoStatement = new GotoStatement();
			gotoStatement.setLabel(jsonObject.get("Label").getAsString());
			return gotoStatement;
		case "IfElseBlock":
			IfElseBlock ifElseBlock = new IfElseBlock();
			for (JsonElement j : jsonObject.getAsJsonArray("Then"))
				ifElseBlock.getThen().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			for (JsonElement j : jsonObject.getAsJsonArray("Then"))
				ifElseBlock.getElse().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			ifElseBlock.setCondition(JsonUtils.parseJson(jsonObject.get("Condition").toString(),
					ISimpleExpression.class));
			return ifElseBlock;
		case "LabelledStatement":
			LabelledStatement labelledStatement = new LabelledStatement();
			labelledStatement.setLabel(jsonObject.get("Label").getAsString());
			labelledStatement.setStatement(JsonUtils
					.parseJson(jsonObject.get("Statement").toString(), IStatement.class));
			return labelledStatement;
		case "LockBlock":
			LockBlock lockBlock = new LockBlock();
			for (JsonElement j : jsonObject.getAsJsonArray("Body"))
				lockBlock.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			lockBlock.setReference(JsonUtils
					.parseJson(jsonObject.get("Reference").toString(), IVariableReference.class));
			return lockBlock;
		case "ReturnStatement":
			ReturnStatement statement = new ReturnStatement();
			statement.setIsVoid(jsonObject.get("IsVoid").getAsBoolean());
			statement.setExpression(JsonUtils.parseJson(jsonObject.get("Expression").toString(),
					ISimpleExpression.class));
			return statement;
		case "SwitchBlock":
			SwitchBlock switchBlock = new SwitchBlock();
			for (JsonElement j : jsonObject.getAsJsonArray("Sections"))
				switchBlock.getSections().add(JsonUtils.parseJson(j.toString(), ICaseBlock.class));
			for (JsonElement j : jsonObject.getAsJsonArray("DefaultSection"))
				switchBlock.getDefaultSection().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			switchBlock.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(),
					IVariableReference.class));
			return switchBlock;
		case "ThrowStatement":
			ThrowStatement throwStatement = new ThrowStatement();
			throwStatement.setException(JsonUtils.parseJson(jsonObject.get("Exception").toString(), TypeName.class));
			return throwStatement;
		case "TryBlock":
			TryBlock tryBlock = new TryBlock();
			for (JsonElement j : jsonObject.getAsJsonArray("Body"))
				tryBlock.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			for (JsonElement j : jsonObject.getAsJsonArray("CatchBlocks"))
				tryBlock.getCatchBlocks().add(JsonUtils.parseJson(j.toString(), ICatchBlock.class));
			for (JsonElement j : jsonObject.getAsJsonArray("Finally"))
				tryBlock.getFinally().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			return tryBlock;
		case "UncheckedBlock":
			UncheckedBlock uncheckedBlock = new UncheckedBlock();
			for (JsonElement j : jsonObject.getAsJsonArray("Body"))
				uncheckedBlock.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			return uncheckedBlock;
		case "UnknownStatement":
			UnknownStatement unknownStatement = new UnknownStatement();
			return unknownStatement;
		case "UnsafeBlock":
			UnsafeBlock unsafeBlock = new UnsafeBlock();
			return unsafeBlock;
		case "UsingBlock":
			UsingBlock usingBlock = new UsingBlock();
			for (JsonElement j : jsonObject.getAsJsonArray("Body"))
				usingBlock.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			usingBlock.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(),
					IVariableReference.class));
			return usingBlock;
		case "VariableDeclaration":
			VariableDeclaration variableDeclaration = new VariableDeclaration();
			variableDeclaration.setType(JsonUtils.parseJson(jsonObject.get("Type").toString(), TypeName.class));
			variableDeclaration.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(),
					IVariableReference.class));
			return variableDeclaration;
		case "WhileLoop":
			WhileLoop whileLoop = new WhileLoop();
			for (JsonElement j : jsonObject.getAsJsonArray("Body"))
				whileLoop.getBody().add(JsonUtils.parseJson(j.toString(), IStatement.class));
			whileLoop.setCondition(JsonUtils.parseJson(jsonObject.get("Condition").toString(),
					ILoopHeaderExpression.class));
			return whileLoop;
		default:
			return null;
		}
	}
}
