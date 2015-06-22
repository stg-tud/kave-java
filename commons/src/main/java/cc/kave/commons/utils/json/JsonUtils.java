package cc.kave.commons.utils.json;

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
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IReturnStatement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JsonUtils {
	public static <T> T parseJson(String json, Class<T> targetType) {
		GsonBuilder gsonBuilder = new GsonBuilder();
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

		// IMemberDeclaration
		gsonBuilder.registerTypeAdapter(IMemberDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IFieldDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IPropertyDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IAssignableReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(FieldDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(PropertyDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(UnknownReference.class, new GsonIMemberDeclarationDeserializer());

		// IExpression
		gsonBuilder.registerTypeAdapter(IExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(IAssignableExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(ISimpleExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(UnknownExpression.class, new GsonIExpressionDeserializer());
		// IStatement
		gsonBuilder.registerTypeAdapter(IStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IAssignment.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IReturnStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(Assignment.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ReturnStatement.class, new GsonIStatementDeserializer());

		gsonBuilder.registerTypeAdapter(ISST.class, new GsonISSTDeserializer());
		gsonBuilder.registerTypeAdapter(SST.class, new GsonISSTDeserializer());
		Gson gson = gsonBuilder.create();

		return gson.fromJson(json, targetType);
	}
}
