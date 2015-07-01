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
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonIStatementSerializer implements JsonSerializer<IStatement>, JsonDeserializer<IStatement> {

	@Override
	public JsonElement serialize(IStatement src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		String discriminator = src.getClass().getSimpleName();
		jsonObject.addProperty("$type", JsonUtils.getTypePath(src));
		switch (discriminator) {
		case "Assignment":
			Assignment a = (Assignment) src;
			jsonObject.add("Reference", JsonUtils.parseObject(a.getReference(), a.getReference().getClass()));
			jsonObject.add("Expression", JsonUtils.parseObject(a.getExpression(), a.getExpression().getClass()));
			return jsonObject;
		case "DoLoop":
			DoLoop dL = (DoLoop) src;
			jsonObject.add("Condition", JsonUtils.parseObject(dL.getCondition(), dL.getCondition().getClass()));
			jsonObject.add("Body", JsonUtils.parseListToJson(dL.getBody()));
			return jsonObject;
		case "ExpressionStatement":
			ExpressionStatement eS = (ExpressionStatement) src;
			jsonObject.add("Expression", JsonUtils.parseObject(eS.getExpression(), eS.getExpression().getClass()));
			return jsonObject;
		case "ForEachLoop":
			ForEachLoop fE = (ForEachLoop) src;
			jsonObject.add("Declaration", JsonUtils.parseObject(fE.getDeclaration(), fE.getDeclaration().getClass()));
			jsonObject.add("LoopedReference",
					JsonUtils.parseObject(fE.getLoopedReference(), fE.getLoopedReference().getClass()));
			jsonObject.add("Body", JsonUtils.parseListToJson(fE.getBody()));
			return jsonObject;
		case "ForLoop":
			ForLoop fL = (ForLoop) src;
			jsonObject.add("Init", JsonUtils.parseListToJson(fL.getInit()));
			jsonObject.add("Condition", JsonUtils.parseObject(fL.getCondition(), fL.getCondition().getClass()));
			jsonObject.add("Step", JsonUtils.parseListToJson(fL.getStep()));
			jsonObject.add("Body", JsonUtils.parseListToJson(fL.getBody()));
			return jsonObject;
		case "GotoStatement":
			GotoStatement gS = (GotoStatement) src;
			jsonObject.addProperty("Label", gS.getLabel());
			return jsonObject;
		case "IfElseBlock":
			IfElseBlock iEB = (IfElseBlock) src;
			jsonObject.add("Condition", JsonUtils.parseObject(iEB.getCondition(), iEB.getCondition().getClass()));
			jsonObject.add("Then", JsonUtils.parseListToJson(iEB.getThen()));
			jsonObject.add("Else", JsonUtils.parseListToJson(iEB.getElse()));
			return jsonObject;
		case "LabelledStatement":
			LabelledStatement lS = (LabelledStatement) src;
			jsonObject.addProperty("Label", lS.getLabel());
			jsonObject.add("Statement", JsonUtils.parseObject(lS.getStatement(), lS.getStatement().getClass()));
			return jsonObject;
		case "LockBlock":
			LockBlock lB = (LockBlock) src;
			jsonObject.add("Reference", JsonUtils.parseObject(lB.getReference(), lB.getReference().getClass()));
			jsonObject.add("Body", JsonUtils.parseListToJson(lB.getBody()));
			return jsonObject;
		case "ReturnStatement":
			ReturnStatement rS = (ReturnStatement) src;
			jsonObject.add("Expression", JsonUtils.parseObject(rS.getExpression(), rS.getExpression().getClass()));
			jsonObject.addProperty("IsVoid", rS.isVoid());
			return jsonObject;
		case "SwitchBlock":
			SwitchBlock sB = (SwitchBlock) src;
			jsonObject.add("Reference", JsonUtils.parseObject(sB.getReference(), sB.getReference().getClass()));
			jsonObject.add("Sections", JsonUtils.parseListToJson(sB.getSections()));
			jsonObject.add("DefaultSection", JsonUtils.parseListToJson(sB.getDefaultSection()));
			return jsonObject;
		case "ThrowStatement":
			ThrowStatement tS = (ThrowStatement) src;
			jsonObject.addProperty("Exception", JsonUtils.parseName(tS.getException()));
			return jsonObject;
		case "TryBlock":
			TryBlock tB = (TryBlock) src;
			jsonObject.add("Body", JsonUtils.parseListToJson(tB.getBody()));
			jsonObject.add("CatchBlocks", JsonUtils.parseListToJson(tB.getCatchBlocks()));
			jsonObject.add("Finally", JsonUtils.parseListToJson(tB.getFinally()));
			return jsonObject;
		case "UncheckedBlock":
			UncheckedBlock unB = (UncheckedBlock) src;
			jsonObject.add("Body", JsonUtils.parseListToJson(unB.getBody()));
			return jsonObject;
		case "UsingBlock":
			UsingBlock uB = (UsingBlock) src;
			jsonObject.add("Reference", JsonUtils.parseObject(uB.getReference(), uB.getReference().getClass()));
			jsonObject.add("Body", JsonUtils.parseListToJson(uB.getBody()));
			return jsonObject;
		case "VariableDeclaration":
			VariableDeclaration vD = (VariableDeclaration) src;
			jsonObject.add("Reference", JsonUtils.parseObject(vD.getReference(), vD.getReference().getClass()));
			jsonObject.addProperty("Type", JsonUtils.parseName(vD.getType()));
			return jsonObject;
		case "WhileLoop":
			WhileLoop wL = (WhileLoop) src;
			jsonObject.add("Condition", JsonUtils.parseObject(wL.getCondition(), wL.getCondition().getClass()));
			jsonObject.add("Body", JsonUtils.parseListToJson(wL.getBody()));
			return jsonObject;
		default:
			return jsonObject;
		}
	}

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
			doLoop.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
			return doLoop;
		case "ExpressionStatement":
			ExpressionStatement expressionStatement = new ExpressionStatement();
			expressionStatement.setExpression(JsonUtils.parseJson(jsonObject.get("Expression").toString(),
					IAssignableExpression.class));
			return expressionStatement;
		case "ForEachLoop":
			ForEachLoop foreachLoop = new ForEachLoop();
			foreachLoop.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
			foreachLoop.setDeclaration(JsonUtils.parseJson(jsonObject.get("Declaration").toString(),
					IVariableDeclaration.class));
			foreachLoop.setLoopedReference(JsonUtils.parseJson(jsonObject.get("LoopedReference").toString(),
					IVariableReference.class));
			return foreachLoop;
		case "ForLoop":
			ForLoop forLoop = new ForLoop();
			forLoop.setInit(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Init")));
			forLoop.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
			forLoop.setStep(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Step")));
			forLoop.setCondition(JsonUtils.parseJson(jsonObject.get("Condition").toString(),
					ILoopHeaderExpression.class));
			return forLoop;
		case "GotoStatement":
			GotoStatement gotoStatement = new GotoStatement();
			gotoStatement.setLabel(jsonObject.get("Label").getAsString());
			return gotoStatement;
		case "IfElseBlock":
			IfElseBlock ifElseBlock = new IfElseBlock();
			ifElseBlock.setThen(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Then")));
			ifElseBlock.setElse(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Else")));
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
			lockBlock.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
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
			switchBlock.setSections(JsonUtils.parseJsonToList(ICaseBlock.class, jsonObject.getAsJsonArray("Sections")));
			switchBlock.setDefaultSection(JsonUtils.parseJsonToList(IStatement.class,
					jsonObject.getAsJsonArray("DefaultSection")));
			switchBlock.setReference(JsonUtils.parseJson(jsonObject.get("Reference").toString(),
					IVariableReference.class));
			return switchBlock;
		case "ThrowStatement":
			ThrowStatement throwStatement = new ThrowStatement();
			throwStatement.setException(JsonUtils.parseJson(jsonObject.get("Exception").toString(), TypeName.class));
			return throwStatement;
		case "TryBlock":
			TryBlock tryBlock = new TryBlock();
			tryBlock.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
			tryBlock.setCatchBlocks(JsonUtils.parseJsonToList(ICatchBlock.class,
					jsonObject.getAsJsonArray("CatchBlocks")));
			tryBlock.setFinally(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Finally")));
			return tryBlock;
		case "UncheckedBlock":
			UncheckedBlock uncheckedBlock = new UncheckedBlock();
			uncheckedBlock.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
			return uncheckedBlock;
		case "UnknownStatement":
			UnknownStatement unknownStatement = new UnknownStatement();
			return unknownStatement;
		case "UnsafeBlock":
			UnsafeBlock unsafeBlock = new UnsafeBlock();
			return unsafeBlock;
		case "UsingBlock":
			UsingBlock usingBlock = new UsingBlock();
			usingBlock.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
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
			whileLoop.setBody(JsonUtils.parseJsonToList(IStatement.class, jsonObject.getAsJsonArray("Body")));
			whileLoop.setCondition(JsonUtils.parseJson(jsonObject.get("Condition").toString(),
					ILoopHeaderExpression.class));
			return whileLoop;
		default:
			return null;
		}
	}
}
