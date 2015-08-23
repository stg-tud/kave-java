package cc.kave.commons.utils.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cc.kave.commons.model.events.Trigger;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.IProposalSelection;
import cc.kave.commons.model.events.completionevents.Proposal;
import cc.kave.commons.model.events.completionevents.ProposalSelection;
import cc.kave.commons.model.events.completionevents.TerminationState;
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
import cc.kave.commons.model.names.resharper.LiveTemplateName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
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
import cc.recommenders.assertions.Asserts;
import cc.recommenders.assertions.Throws;

public abstract class JsonUtils {

	private static Gson gson;

	static {
		GsonBuilder gb = new GsonBuilder();

		// name interface types
		gb.registerTypeAdapter(AliasName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(BundleName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(EventName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(FieldName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(LambdaName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(LocalVariableName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(MethodName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(Name.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(NamespaceName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(ParameterName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(PropertyName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(TypeName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(DelegateTypeName.class, new GsonNameDeserializer());
		// C# name types
		gb.registerTypeAdapter(CsAliasName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsAssemblyName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsEventName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsFieldName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsLambdaName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsLocalVariableName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsMethodName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsNamespaceName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsParameterName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsPropertyName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsTypeName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsDelegateTypeName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsUnknownTypeName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsTypeParameterName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsStructTypeName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsInterfaceTypeName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsEnumTypeName.class, new GsonNameDeserializer());
		gb.registerTypeAdapter(CsArrayTypeName.class, new GsonNameDeserializer());
		// resharper names
		gb.registerTypeAdapter(LiveTemplateName.class, new GsonNameDeserializer());

		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IAssignableExpression.class, "$type")
				.registerSubtype(CompletionExpression.class).registerSubtype(ComposedExpression.class)
				.registerSubtype(IfElseExpression.class).registerSubtype(InvocationExpression.class)
				.registerSubtype(LambdaExpression.class).registerSubtype(UnknownExpression.class)
				.registerSubtype(ConstantValueExpression.class).registerSubtype(NullExpression.class)
				.registerSubtype(ReferenceExpression.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(ISST.class, "$type").registerSubtype(SST.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IMemberDeclaration.class, "$type")
				.registerSubtype(IMemberDeclaration.class));
		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IFieldDeclaration.class, "$type").registerSubtype(FieldDeclaration.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IDelegateDeclaration.class, "$type")
				.registerSubtype(DelegateDeclaration.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IPropertyDeclaration.class, "$type")
				.registerSubtype(PropertyDeclaration.class));
		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IEventDeclaration.class, "$type").registerSubtype(EventDeclaration.class));
		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IReference.class, "$type").registerSubtype(IReference.class)
						.registerSubtype(UnknownReference.class).registerSubtype(EventReference.class)
						.registerSubtype(FieldReference.class).registerSubtype(PropertyReference.class)
						.registerSubtype(MethodReference.class).registerSubtype(VariableReference.class));
		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IAssignableReference.class, "$type").registerSubtype(EventReference.class)
						.registerSubtype(FieldReference.class).registerSubtype(PropertyReference.class)
						.registerSubtype(UnknownReference.class).registerSubtype(VariableReference.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IVariableReference.class, "$type")
				.registerSubtype(VariableReference.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(ISimpleExpression.class, "$type")
				.registerSubtype(UnknownExpression.class).registerSubtype(ReferenceExpression.class)
				.registerSubtype(NullExpression.class).registerSubtype(ConstantValueExpression.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IMethodDeclaration.class, "$type")
				.registerSubtype(MethodDeclaration.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IVariableDeclaration.class, "$type")
				.registerSubtype(VariableDeclaration.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(ILoopHeaderExpression.class, "$type")
				.registerSubtype(LoopHeaderBlockExpression.class).registerSubtype(UnknownExpression.class)
				.registerSubtype(NullExpression.class).registerSubtype(ConstantValueExpression.class)
				.registerSubtype(ReferenceExpression.class));
		gb.registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(IStatement.class, "$type")
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
		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(ICatchBlock.class, "$type").registerSubtype(CatchBlock.class));
		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(ICaseBlock.class, "$type").registerSubtype(CaseBlock.class));

		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(Context.class, "$type").registerSubtype(Context.class));
		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(ITypeShape.class, "$type").registerSubtype(TypeShape.class));
		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(IMethodHierarchy.class, "$type").registerSubtype(MethodHierarchy.class));
		gb.registerTypeAdapterFactory(
				RuntimeTypeAdapterFactory.of(ITypeHierarchy.class, "$type").registerSubtype(TypeHierarchy.class));

		// completion event
		register(gb, ICompletionEvent.class, CompletionEvent.class);
		register(gb, IProposal.class, Proposal.class);
		register(gb, IProposalSelection.class, ProposalSelection.class);

		// enums
		gb.registerTypeAdapter(Trigger.class, EnumDeSerializer.create(Trigger.values()));
		gb.registerTypeAdapter(TerminationState.class, EnumDeSerializer.create(TerminationState.values()));
		gb.registerTypeAdapter(CatchBlockKind.class, EnumDeSerializer.create(CatchBlockKind.values()));

		gb.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
		gb.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
		gson = gb.create();
	}

	@SafeVarargs
	private static <T> void register(GsonBuilder gsonBuilder, Class<T> type, Class<? extends T>... subtypes) {
		Asserts.assertTrue(subtypes.length > 0);

		RuntimeTypeAdapterFactory<T> factory = RuntimeTypeAdapterFactory.of(type, "$type");
		for (int i = 0; i < subtypes.length; i++) {
			factory = factory.registerSubtype(subtypes[i]);
		}

		gsonBuilder.registerTypeAdapterFactory(factory);
	}

	public static <T> T fromJson(String json, Type targetType) {
		json = toJavaTypeNames(json);
		return gson.fromJson(json, targetType);
	}

	public static <T> String toJson(Object obj, Type targetType) {
		String json = gson.toJsonTree(obj, targetType).toString();
		return toCSharpTypeNames(json);
	}

	public static <T> String toJson(Object obj) {
		String json = gson.toJson(obj);
		return toCSharpTypeNames(json);
	}

	private static String toCSharpTypeNames(String json) {
		StringBuffer resultString = new StringBuffer();
		Pattern regex = Pattern.compile("cc\\.kave\\.commons\\.model\\.ssts\\.impl\\.([.a-zA-Z0-9_]+)");
		Matcher regexMatcher = regex.matcher(json);
		while (regexMatcher.find()) {
			String replacement = "[SST:" + toUpperCaseNamespace(regexMatcher.group(1)) + "]";
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		regex = Pattern.compile("cc\\.kave\\.commons\\.model\\.([.a-zA-Z0-9_]+)");
		regexMatcher = regex.matcher(resultString.toString());
		resultString = new StringBuffer();
		while (regexMatcher.find()) {
			String replacement = "KaVE.Commons.Model." + toUpperCaseNamespace(regexMatcher.group(1)) + ", KaVE.Commons";
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		return resultString.toString();
	}

	private static String toJavaTypeNames(String json) {
		StringBuffer resultString = new StringBuffer();
		Pattern regex = Pattern.compile("\\[SST:([.a-zA-Z0-9_]+)\\]");
		Matcher regexMatcher = regex.matcher(json);
		while (regexMatcher.find()) {
			String replacement = "cc.kave.commons.model.ssts.impl." + toLowerCaseNamespace(regexMatcher.group(1));
			regexMatcher.appendReplacement(resultString, replacement);
		}

		// cc.kave.commons.model.events.completionevents.CompletionEvent

		regexMatcher.appendTail(resultString);
		regex = Pattern.compile("KaVE\\.Commons\\.Model\\.([.a-zA-Z0-9_]+), KaVE.Commons");
		regexMatcher = regex.matcher(resultString.toString());
		resultString = new StringBuffer();
		while (regexMatcher.find()) {
			String replacement = "cc.kave.commons.model." + toLowerCaseNamespace(regexMatcher.group(1));
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		return resultString.toString();
	}

	private static String toLowerCaseNamespace(String string) {
		if (string.contains(".")) {
			return string.substring(0, string.lastIndexOf(".")).toLowerCase()
					+ string.substring(string.lastIndexOf("."));
		}
		return string;
	}

	private static String toUpperCaseNamespace(String string) {
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

	public static <T> T fromJson(File file, Type classOfT) {
		try {
			String json = FileUtils.readFileToString(file);
			return fromJson(json, classOfT);
		} catch (IOException e) {
			throw Throws.throwUnhandledException(e);
		}
	}

	public static <T> T fromJson(InputStream in, Type classOfT) {
		try {
			String json = IOUtils.toString(in, Charset.defaultCharset().toString());
			return fromJson(json, classOfT);
		} catch (IOException e) {
			throw Throws.throwUnhandledException(e);
		}
	}

	public static <T> void toJson(T obj, File file) {
		try {
			String json = toJson(obj);
			FileUtils.writeStringToFile(file, json);
		} catch (IOException e) {
			throw Throws.throwUnhandledException(e);
		}
	}
}