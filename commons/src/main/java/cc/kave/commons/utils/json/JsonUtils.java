package cc.kave.commons.utils.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cc.kave.commons.model.names.AliasName;
import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.LambdaName;
import cc.kave.commons.model.names.LocalVariableName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsAliasName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsLambdaName;
import cc.kave.commons.model.names.csharp.CsLocalVariableName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
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
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
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
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
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
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public abstract class JsonUtils {
	public static <T> T parseJson(String json, Class<T> targetType) {
		Gson gson = createGson(true);
		return gson.fromJson(json, targetType);
	}

	public static <T> JsonElement parseObject(Object obj, Class<T> targetType) {
		Gson gson = createGson(false);
		return gson.toJsonTree(obj, targetType);
	}

	public static String parseName(Name name) {
		String superType;
		if (name.getClass().getGenericInterfaces().length != 0) {
			superType = name.getClass().getGenericInterfaces()[0].getTypeName();
			superType = superType.substring(superType.lastIndexOf('.'));
		} else
			superType = "." + name.getClass().getSimpleName().substring(2);

		return "CSharp" + superType + ":" + name.getIdentifier();
	}

	public static String getTypePath(Object src) {
		String[] path = src.getClass().getName().split("[.]");
		String type = "[SST:";
		for (int i = 6; i < path.length; i++) {
			if (i != path.length - 1) {
				if (path[i].equals("loopheader"))
					path[i] = "loopHeader";
				type += path[i].substring(0, 1).toUpperCase() + path[i].substring(1) + ".";
			} else
				type += path[i] + "]";
		}
		return type;
	}

	public static <T> JsonArray parseListToJson(Collection<T> list) {
		JsonArray jsonArray = new JsonArray();
		for (T t : list)
			jsonArray.add(JsonUtils.parseObject(t, t.getClass()));
		return jsonArray;
	}

	public static <T> List<T> parseJsonToList(Class<T> type, JsonArray jsonArray) {
		List<T> list = new ArrayList<>();
		for (JsonElement j : jsonArray) {
			list.add(parseJson(j.toString(), type));
		}
		return list;
	}

	private static Gson createGson(boolean fromJson) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		if (fromJson) {
			// name interface types
			gsonBuilder.registerTypeAdapter(AliasName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(BundleName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(EventName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(FieldName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(LambdaName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(LocalVariableName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(MethodName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(Name.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(NamespaceName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(ParameterName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(PropertyName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(TypeName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(DelegateTypeName.class, new GsonNameDeserializer());
			// C# name types
			gsonBuilder.registerTypeAdapter(CsAliasName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsAssemblyName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsEventName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsFieldName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsLambdaName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsLocalVariableName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsMethodName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsNamespaceName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsParameterName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsPropertyName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsTypeName.class, new GsonNameDeserializer());
			gsonBuilder.registerTypeAdapter(CsDelegateTypeName.class, new GsonNameDeserializer());
		}
		// IMemberDeclaration interfaces
		gsonBuilder.registerTypeAdapter(IMemberDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IDelegateDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IEventDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IFieldDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IMethodDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IPropertyDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IAssignableReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IMemberReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IEventReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IFieldReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IPropertyReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IUnknownReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IVariableReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(IMethodReference.class, new GsonIMemberDeclarationSerializer());
		// IMemberDeclaration implementation
		gsonBuilder.registerTypeAdapter(DelegateDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(EventDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(FieldDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(MethodDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(PropertyDeclaration.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(EventReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(FieldReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(PropertyReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(UnknownReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(VariableReference.class, new GsonIMemberDeclarationSerializer());
		gsonBuilder.registerTypeAdapter(MethodReference.class, new GsonIMemberDeclarationSerializer());

		// IExpression interfaces
		gsonBuilder.registerTypeAdapter(IExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(IAssignableExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(ILoopHeaderExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(ICompletionExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(IComposedExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(IIfElseExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(IInvocationExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(ILambdaExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(ISimpleExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(ILoopHeaderBlockExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(IConstantValueExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(INullExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(IReferenceExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(IUnknownExpression.class, new GsonIExpressionSerializer());
		// IExpression implementations
		gsonBuilder.registerTypeAdapter(CompletionExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(ComposedExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(IfElseExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(InvocationExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(LambdaExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(ConstantValueExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(NullExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(ReferenceExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(UnknownExpression.class, new GsonIExpressionSerializer());
		gsonBuilder.registerTypeAdapter(LoopHeaderBlockExpression.class, new GsonIExpressionSerializer());

		// IStatement interfaces
		gsonBuilder.registerTypeAdapter(IStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IAssignment.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IBreakStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IContinueStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IDoLoop.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IExpressionStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IForEachLoop.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IForLoop.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IGotoStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IIfElseBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ILabelledStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ILockBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IReturnStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ISwitchBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IThrowStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ITryBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IUncheckedBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IUnknownStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IUnsafeBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IUsingBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IVariableDeclaration.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IWhileLoop.class, new GsonIStatementSerializer());
		// IStatement implementation
		gsonBuilder.registerTypeAdapter(Assignment.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(BreakStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ContinueStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(DoLoop.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ExpressionStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ForEachLoop.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ForLoop.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(GotoStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(IfElseBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(LabelledStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(LockBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ReturnStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(SwitchBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(ThrowStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(TryBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(UncheckedBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(UnknownStatement.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(UnsafeBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(UsingBlock.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(VariableDeclaration.class, new GsonIStatementSerializer());
		gsonBuilder.registerTypeAdapter(WhileLoop.class, new GsonIStatementSerializer());
		// Case and CatchBlock
		gsonBuilder.registerTypeAdapter(ICatchBlock.class, new GsonICatchBlockSerializer());
		gsonBuilder.registerTypeAdapter(CatchBlock.class, new GsonICatchBlockSerializer());
		gsonBuilder.registerTypeAdapter(ICaseBlock.class, new GsonICaseBlockSerializer());
		gsonBuilder.registerTypeAdapter(CaseBlock.class, new GsonICaseBlockSerializer());

		// SST
		gsonBuilder.registerTypeAdapter(ISST.class, new GsonISSTSerializer());
		gsonBuilder.registerTypeAdapter(SST.class, new GsonISSTSerializer());

		return gsonBuilder.create();
	}

}
