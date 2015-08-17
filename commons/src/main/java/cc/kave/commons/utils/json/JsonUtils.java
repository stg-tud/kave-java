package cc.kave.commons.utils.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cc.kave.commons.model.events.completionevents.Context;
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
import cc.kave.commons.model.names.csharp.CsArrayTypeName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
import cc.kave.commons.model.names.csharp.CsEnumTypeName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsInterfaceTypeName;
import cc.kave.commons.model.names.csharp.CsLambdaName;
import cc.kave.commons.model.names.csharp.CsLocalVariableName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.names.csharp.CsStructTypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.names.csharp.CsTypeParameterName;
import cc.kave.commons.model.names.csharp.CsUnknownTypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
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
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public abstract class JsonUtils {
	public static <T> T parseJson(String json, Type targetType) {
		json = addDetails(json);
		Gson gson = createGson();
		return gson.fromJson(json, targetType);
	}

	public static <T> String parseObject(Object obj, Type targetType) {
		Gson gson = createGson();
		String json = gson.toJsonTree(obj, targetType).toString();
		return removeDetails(json);
	}

	public static <T> T deserialize(File file, Type targetType) {
		StringBuilder builder = new StringBuilder();
		try {
			FileInputStream stream = new FileInputStream(file);
			int ch;

			while ((ch = stream.read()) != -1) {
				builder.append((char) ch);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parseJson(builder.toString(), targetType);
	}

	public static <T> void serialize(T object, File file) {
		try {
			Writer stream = new FileWriter(file);
			String output = parseObject(object, object.getClass());
			stream.write(output);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String removeDetails(String json) {
		StringBuffer resultString = new StringBuffer();
		Pattern regex = Pattern.compile("cc\\.kave\\.commons\\.model\\.ssts\\.impl\\.([.a-zA-Z0-9_]+)");
		Matcher regexMatcher = regex.matcher(json);
		while (regexMatcher.find()) {
			String replacement = "[SST:" + upperCase(regexMatcher.group(1)) + "]";
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		regex = Pattern.compile("cc\\.kave\\.commons\\.model\\.([.a-zA-Z0-9_]+)");
		regexMatcher = regex.matcher(resultString.toString());
		resultString = new StringBuffer();
		while (regexMatcher.find()) {
			String replacement = "KaVE.Commons.Model." + upperCase(regexMatcher.group(1)) + ", KaVE.Commons";
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		return resultString.toString();
	}

	private static String addDetails(String json) {
		StringBuffer resultString = new StringBuffer();
		Pattern regex = Pattern.compile("\\[SST:([.a-zA-Z0-9_]+)\\]");
		Matcher regexMatcher = regex.matcher(json);
		while (regexMatcher.find()) {
			String replacement = "cc.kave.commons.model.ssts.impl." + lowerCase(regexMatcher.group(1));
			regexMatcher.appendReplacement(resultString, replacement);
		}

		regexMatcher.appendTail(resultString);
		regex = Pattern.compile("KaVE\\.Commons\\.Model\\.([.a-zA-Z0-9_]+), KaVE.Commons");
		regexMatcher = regex.matcher(resultString.toString());
		resultString = new StringBuffer();
		while (regexMatcher.find()) {
			String replacement = "cc.kave.commons.model." + lowerCase(regexMatcher.group(1));
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		return resultString.toString();
	}

	private static String lowerCase(String string) {
		if (string.contains(".")) {
			return string.substring(0, string.lastIndexOf(".")).toLowerCase()
					+ string.substring(string.lastIndexOf("."));
		}
		return string;
	}

	private static String upperCase(String string) {
		String[] path = string.split("[.]");
		String type = "";
		for (int i = 0; i < path.length; i++) {
			if (i != path.length - 1) {
				if (path[i].equals("loopheader"))
					path[i] = "loopHeader";
				else if (path[i].equals("completionevents"))
					path[i] = "completionEvents";
				else if (path[i].equals("typeshapes"))
					path[i] = "typeShapes";
				type += path[i].substring(0, 1).toUpperCase() + path[i].substring(1) + ".";
			} else
				type += path[i];
		}
		return type;
	}

	public static Gson createGson() {
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
		gsonBuilder.registerTypeAdapter(CsUnknownTypeName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsTypeParameterName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsStructTypeName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsInterfaceTypeName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsEnumTypeName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsArrayTypeName.class, new GsonNameDeserializer());

		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IAssignableExpression.class, "$type")
				.registerSubtype(CompletionExpression.class).registerSubtype(ComposedExpression.class)
				.registerSubtype(IfElseExpression.class).registerSubtype(InvocationExpression.class)
				.registerSubtype(LambdaExpression.class).registerSubtype(UnknownExpression.class)
				.registerSubtype(ConstantValueExpression.class).registerSubtype(NullExpression.class)
				.registerSubtype(ReferenceExpression.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(ISST.class, "$type").registerSubtype(SST.class));
		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IMemberDeclaration.class, "$type")
				.registerSubtype(IMemberDeclaration.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IFieldDeclaration.class, "$type").registerSubtype(FieldDeclaration.class));
		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IDelegateDeclaration.class, "$type")
				.registerSubtype(DelegateDeclaration.class));
		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IPropertyDeclaration.class, "$type")
				.registerSubtype(PropertyDeclaration.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IEventDeclaration.class, "$type").registerSubtype(EventDeclaration.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IReference.class, "$type").registerSubtype(IReference.class)
						.registerSubtype(UnknownReference.class).registerSubtype(EventReference.class)
						.registerSubtype(FieldReference.class).registerSubtype(PropertyReference.class)
						.registerSubtype(MethodReference.class).registerSubtype(VariableReference.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IAssignableReference.class, "$type").registerSubtype(EventReference.class)
						.registerSubtype(FieldReference.class).registerSubtype(PropertyReference.class)
						.registerSubtype(UnknownReference.class).registerSubtype(VariableReference.class));
		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IVariableReference.class, "$type")
				.registerSubtype(VariableReference.class));
		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(ISimpleExpression.class, "$type")
				.registerSubtype(UnknownExpression.class).registerSubtype(ReferenceExpression.class)
				.registerSubtype(NullExpression.class).registerSubtype(ConstantValueExpression.class));
		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IMethodDeclaration.class, "$type")
				.registerSubtype(MethodDeclaration.class));
		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IVariableDeclaration.class, "$type")
				.registerSubtype(VariableDeclaration.class));
		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(ILoopHeaderExpression.class, "$type")
				.registerSubtype(LoopHeaderBlockExpression.class).registerSubtype(UnknownExpression.class)
				.registerSubtype(NullExpression.class).registerSubtype(ConstantValueExpression.class)
				.registerSubtype(ReferenceExpression.class));
		gsonBuilder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IStatement.class, "$type")
				.registerSubtype(Assignment.class).registerSubtype(BreakStatement.class)
				.registerSubtype(ContinueStatement.class).registerSubtype(DoLoop.class)
				.registerSubtype(ExpressionStatement.class).registerSubtype(ForEachLoop.class)
				.registerSubtype(ForLoop.class).registerSubtype(GotoStatement.class).registerSubtype(IfElseBlock.class)
				.registerSubtype(LabelledStatement.class).registerSubtype(LockBlock.class)
				.registerSubtype(ReturnStatement.class).registerSubtype(SwitchBlock.class)
				.registerSubtype(ThrowStatement.class).registerSubtype(TryBlock.class)
				.registerSubtype(UncheckedBlock.class).registerSubtype(UnknownStatement.class)
				.registerSubtype(UnsafeBlock.class).registerSubtype(UsingBlock.class)
				.registerSubtype(VariableDeclaration.class).registerSubtype(WhileLoop.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(ICatchBlock.class, "$type").registerSubtype(CatchBlock.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(ICaseBlock.class, "$type").registerSubtype(CaseBlock.class));

		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(Context.class, "$type").registerSubtype(Context.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(ITypeShape.class, "$type").registerSubtype(TypeShape.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IMethodHierarchy.class, "$type").registerSubtype(MethodHierarchy.class));
		gsonBuilder.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(ITypeHierarchy.class, "$type").registerSubtype(TypeHierarchy.class));

		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
		gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
		return gsonBuilder.create();
	}

}
