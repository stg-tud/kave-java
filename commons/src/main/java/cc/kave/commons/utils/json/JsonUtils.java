package cc.kave.commons.utils.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

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
import cc.kave.commons.model.ssts.impl.expressions.loopHeader.LoopHeaderBlockExpression;
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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public abstract class JsonUtils {
	public static <T> T parseJson(String json, Class<T> targetType) {
		Gson gson = createGson();
		return gson.fromJson(json, targetType);
	}

	public static String toString(Object instance) {
		Gson gson = createJavaOutputGson();
		return gson.toJson(instance);
	}

	/*
	 * Customizes Gson to fit .NET Json output format
	 */
	private static Gson createJavaOutputGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		List<RuntimeTypeAdapterFactory> factories = new LinkedList<RuntimeTypeAdapterFactory>();

		// name interface types
		gsonBuilder.registerTypeAdapter(AliasName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(BundleName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(EventName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(FieldName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(LambdaName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(LocalVariableName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(MethodName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(Name.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(NamespaceName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(ParameterName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(PropertyName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(TypeName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(DelegateTypeName.class, new GsonNameAdapter());
		// C# name types
		gsonBuilder.registerTypeAdapter(CsAliasName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsAssemblyName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsEventName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsFieldName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsLambdaName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsLocalVariableName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsMethodName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsNamespaceName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsParameterName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsPropertyName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsTypeName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsDelegateTypeName.class, new GsonNameAdapter());

		// Dummy Adapters for simple Class printing
		factories.add(factoryFor(SST.class, SST.class));

		factories.add(factoryFor(CaseBlock.class, CaseBlock.class));
		factories.add(factoryFor(CatchBlock.class, CatchBlock.class));
		factories.add(factoryFor(DoLoop.class, DoLoop.class));
		factories.add(factoryFor(ForEachLoop.class, ForEachLoop.class));
		factories.add(factoryFor(ForLoop.class, ForLoop.class));
		factories.add(factoryFor(IfElseBlock.class, IfElseBlock.class));
		factories.add(factoryFor(LockBlock.class, LockBlock.class));
		factories.add(factoryFor(SwitchBlock.class, SwitchBlock.class));
		factories.add(factoryFor(TryBlock.class, TryBlock.class));
		factories.add(factoryFor(UncheckedBlock.class, UncheckedBlock.class));
		factories.add(factoryFor(UnsafeBlock.class, UnsafeBlock.class));
		factories.add(factoryFor(UsingBlock.class, UsingBlock.class));
		factories.add(factoryFor(WhileLoop.class, WhileLoop.class));

		factories.add(factoryFor(DelegateDeclaration.class, DelegateDeclaration.class));
		factories.add(factoryFor(EventDeclaration.class, EventDeclaration.class));
		factories.add(factoryFor(FieldDeclaration.class, FieldDeclaration.class));
		factories.add(factoryFor(MethodDeclaration.class, MethodDeclaration.class));
		factories.add(factoryFor(PropertyDeclaration.class, PropertyDeclaration.class));

		factories.add(factoryFor(CompletionExpression.class, CompletionExpression.class));
		factories.add(factoryFor(ComposedExpression.class, ComposedExpression.class));
		factories.add(factoryFor(IfElseExpression.class, IfElseExpression.class));
		factories.add(factoryFor(InvocationExpression.class, InvocationExpression.class));
		factories.add(factoryFor(LambdaExpression.class, LambdaExpression.class));

		factories.add(factoryFor(LoopHeaderBlockExpression.class, LoopHeaderBlockExpression.class));

		factories.add(factoryFor(ConstantValueExpression.class, ConstantValueExpression.class));
		factories.add(factoryFor(NullExpression.class, NullExpression.class));
		factories.add(factoryFor(ReferenceExpression.class, ReferenceExpression.class));
		factories.add(factoryFor(UnknownExpression.class, UnknownExpression.class));

		factories.add(factoryFor(EventReference.class, EventReference.class));
		factories.add(factoryFor(FieldReference.class, FieldReference.class));
		factories.add(factoryFor(MethodReference.class, MethodReference.class));
		factories.add(factoryFor(PropertyReference.class, PropertyReference.class));
		factories.add(factoryFor(UnknownReference.class, UnknownReference.class));
		factories.add(factoryFor(VariableReference.class, VariableReference.class));

		factories.add(factoryFor(Assignment.class, Assignment.class));
		factories.add(factoryFor(BreakStatement.class, BreakStatement.class));
		factories.add(factoryFor(ContinueStatement.class, ContinueStatement.class));
		factories.add(factoryFor(ExpressionStatement.class, ExpressionStatement.class));
		factories.add(factoryFor(GotoStatement.class, GotoStatement.class));
		factories.add(factoryFor(LabelledStatement.class, LabelledStatement.class));
		factories.add(factoryFor(ReturnStatement.class, ReturnStatement.class));
		factories.add(factoryFor(ThrowStatement.class, ThrowStatement.class));
		factories.add(factoryFor(UnknownStatement.class, UnknownStatement.class));
		factories.add(factoryFor(VariableDeclaration.class, VariableDeclaration.class));

		// Adapters for 1st lvl interfaces
		factories.add(factoryFor(ISST.class, SST.class));

		factories.add(factoryFor(ICaseBlock.class, CaseBlock.class));
		factories.add(factoryFor(ICatchBlock.class, CatchBlock.class));
		factories.add(factoryFor(IDoLoop.class, DoLoop.class));
		factories.add(factoryFor(IForEachLoop.class, ForEachLoop.class));
		factories.add(factoryFor(IForLoop.class, ForLoop.class));
		factories.add(factoryFor(IIfElseBlock.class, IfElseBlock.class));
		factories.add(factoryFor(ILockBlock.class, LockBlock.class));
		factories.add(factoryFor(ISwitchBlock.class, SwitchBlock.class));
		factories.add(factoryFor(ITryBlock.class, TryBlock.class));
		factories.add(factoryFor(IUncheckedBlock.class, UncheckedBlock.class));
		factories.add(factoryFor(IUnsafeBlock.class, UnsafeBlock.class));
		factories.add(factoryFor(IUsingBlock.class, UsingBlock.class));
		factories.add(factoryFor(IWhileLoop.class, WhileLoop.class));

		factories.add(factoryFor(IDelegateDeclaration.class, DelegateDeclaration.class));
		factories.add(factoryFor(IEventDeclaration.class, EventDeclaration.class));
		factories.add(factoryFor(IFieldDeclaration.class, FieldDeclaration.class));
		factories.add(factoryFor(IMethodDeclaration.class, MethodDeclaration.class));
		factories.add(factoryFor(IPropertyDeclaration.class, PropertyDeclaration.class));

		factories.add(factoryFor(ICompletionExpression.class, CompletionExpression.class));
		factories.add(factoryFor(IComposedExpression.class, ComposedExpression.class));
		factories.add(factoryFor(IIfElseExpression.class, IfElseExpression.class));
		factories.add(factoryFor(IInvocationExpression.class, InvocationExpression.class));
		factories.add(factoryFor(ILambdaExpression.class, LambdaExpression.class));

		factories.add(factoryFor(ILoopHeaderBlockExpression.class, LoopHeaderBlockExpression.class));

		factories.add(factoryFor(IConstantValueExpression.class, ConstantValueExpression.class));
		factories.add(factoryFor(INullExpression.class, NullExpression.class));
		factories.add(factoryFor(IReferenceExpression.class, ReferenceExpression.class));
		factories.add(factoryFor(IUnknownExpression.class, UnknownExpression.class));

		factories.add(factoryFor(IEventReference.class, EventReference.class));
		factories.add(factoryFor(IFieldReference.class, FieldReference.class));
		factories.add(factoryFor(IMethodReference.class, MethodReference.class));
		factories.add(factoryFor(IPropertyReference.class, PropertyReference.class));
		factories.add(factoryFor(IUnknownReference.class, UnknownReference.class));
		factories.add(factoryFor(IVariableReference.class, VariableReference.class));

		factories.add(factoryFor(IAssignment.class, Assignment.class));
		factories.add(factoryFor(IBreakStatement.class, BreakStatement.class));
		factories.add(factoryFor(IContinueStatement.class, ContinueStatement.class));
		factories.add(factoryFor(IExpressionStatement.class, ExpressionStatement.class));
		factories.add(factoryFor(IGotoStatement.class, GotoStatement.class));
		factories.add(factoryFor(ILabelledStatement.class, LabelledStatement.class));
		factories.add(factoryFor(IReturnStatement.class, ReturnStatement.class));
		factories.add(factoryFor(IThrowStatement.class, ThrowStatement.class));
		factories.add(factoryFor(IUnknownStatement.class, UnknownStatement.class));
		factories.add(factoryFor(IVariableDeclaration.class, VariableDeclaration.class));

		// Recursive interface adapters
		factories.add(factoryFor(IStatement.class, Assignment.class, BreakStatement.class, ContinueStatement.class,
				DoLoop.class, ExpressionStatement.class, ForEachLoop.class, ForLoop.class, GotoStatement.class,
				IfElseBlock.class, LabelledStatement.class, LockBlock.class, ReturnStatement.class, SwitchBlock.class,
				ThrowStatement.class, TryBlock.class, UncheckedBlock.class, UnknownStatement.class, UnsafeBlock.class,
				UsingBlock.class, VariableDeclaration.class, WhileLoop.class));
		factories.add(factoryFor(ISimpleExpression.class, ConstantValueExpression.class, NullExpression.class,
				ReferenceExpression.class, UnknownExpression.class));
		factories.add(factoryFor(IMemberDeclaration.class, DelegateDeclaration.class, EventDeclaration.class,
				FieldDeclaration.class, MethodDeclaration.class, PropertyDeclaration.class, EventReference.class,
				FieldReference.class, PropertyReference.class, UnknownReference.class, VariableReference.class,
				MethodReference.class));
		factories.add(factoryFor(IReference.class, EventReference.class, FieldReference.class, PropertyReference.class,
				UnknownReference.class, VariableReference.class, MethodReference.class));
		factories.add(factoryFor(IAssignableReference.class, EventReference.class, FieldReference.class,
				PropertyReference.class, UnknownReference.class, VariableReference.class));
		factories.add(factoryFor(IMemberReference.class, EventReference.class, FieldReference.class,
				PropertyReference.class, MethodReference.class));
		factories.add(factoryFor(IExpression.class, CompletionExpression.class, ComposedExpression.class,
				IfElseExpression.class, InvocationExpression.class, LambdaExpression.class,
				ConstantValueExpression.class, NullExpression.class, ReferenceExpression.class,
				UnknownExpression.class, LoopHeaderBlockExpression.class));
		factories
				.add(factoryFor(IAssignableExpression.class, CompletionExpression.class, ComposedExpression.class,
						IfElseExpression.class, InvocationExpression.class, LambdaExpression.class,
						ConstantValueExpression.class, NullExpression.class, ReferenceExpression.class,
						UnknownExpression.class));
		factories
				.add(factoryFor(ILoopHeaderExpression.class, LoopHeaderBlockExpression.class,
						ConstantValueExpression.class, NullExpression.class, ReferenceExpression.class,
						UnknownExpression.class));

		for (RuntimeTypeAdapterFactory factory : factories)
			gsonBuilder.registerTypeAdapterFactory(factory);

		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
		Gson gson = gsonBuilder.create();
		return gson;
	}

	public static <T> void toFile(File file, T object) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(toString(object));
		writer.flush();
		writer.close();
	}

	public static <T> T fromString(String str, Class<T> clazz) {
		Gson gson = createGson();
		T bag = gson.fromJson(str, clazz);
		return bag;
	}

	@SafeVarargs
	private static <T> RuntimeTypeAdapterFactory<T> factoryFor(Class<T> baseclass, Class<? extends T>... subclasses) {
		RuntimeTypeAdapterFactory<T> factory = RuntimeTypeAdapterFactory.of(baseclass, "$type");
		for (Class<? extends T> subclass : subclasses) {
			String typename = subclass.getName();
			factory.registerSubtype(subclass, "[" + abbreviateNamespace(typename) + "]");
		}
		return factory;
	}

	public static String abbreviateNamespace(String typename) {
		return WordUtils.capitalize(typename.replaceAll("cc\\.kave\\.commons\\.model\\.ssts\\.impl\\.", "SST:"), '.',
				':');
	}

	public static String removeNamespace(String typename) {
		String[] split = typename.split("\\.");
		String simpleTypename = split[split.length - 1];
		return "SST:" + WordUtils.capitalize(simpleTypename);
	}

	private static Gson createGson() {
		// GsonBuilder gsonBuilder = new GsonBuilder();

		/*
		 * 
		 */
		GsonBuilder gsonBuilder = new GsonBuilder();
		List<RuntimeTypeAdapterFactory> factories = new LinkedList<RuntimeTypeAdapterFactory>();

		// name interface types
		gsonBuilder.registerTypeAdapter(AliasName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(BundleName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(EventName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(FieldName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(LambdaName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(LocalVariableName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(MethodName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(Name.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(NamespaceName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(ParameterName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(PropertyName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(TypeName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(DelegateTypeName.class, new GsonNameAdapter());
		// C# name types
		gsonBuilder.registerTypeAdapter(CsAliasName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsAssemblyName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsEventName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsFieldName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsLambdaName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsLocalVariableName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsMethodName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsNamespaceName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsParameterName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsPropertyName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsTypeName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsDelegateTypeName.class, new GsonNameAdapter());

		// Dummy Adapters for simple Class printing
		factories.add(factoryFor(SST.class, SST.class));

		factories.add(factoryFor(CaseBlock.class, CaseBlock.class));
		factories.add(factoryFor(CatchBlock.class, CatchBlock.class));
		factories.add(factoryFor(DoLoop.class, DoLoop.class));
		factories.add(factoryFor(ForEachLoop.class, ForEachLoop.class));
		factories.add(factoryFor(ForLoop.class, ForLoop.class));
		factories.add(factoryFor(IfElseBlock.class, IfElseBlock.class));
		factories.add(factoryFor(LockBlock.class, LockBlock.class));
		factories.add(factoryFor(SwitchBlock.class, SwitchBlock.class));
		factories.add(factoryFor(TryBlock.class, TryBlock.class));
		factories.add(factoryFor(UncheckedBlock.class, UncheckedBlock.class));
		factories.add(factoryFor(UnsafeBlock.class, UnsafeBlock.class));
		factories.add(factoryFor(UsingBlock.class, UsingBlock.class));
		factories.add(factoryFor(WhileLoop.class, WhileLoop.class));

		factories.add(factoryFor(DelegateDeclaration.class, DelegateDeclaration.class));
		factories.add(factoryFor(EventDeclaration.class, EventDeclaration.class));
		factories.add(factoryFor(FieldDeclaration.class, FieldDeclaration.class));
		factories.add(factoryFor(MethodDeclaration.class, MethodDeclaration.class));
		factories.add(factoryFor(PropertyDeclaration.class, PropertyDeclaration.class));

		factories.add(factoryFor(CompletionExpression.class, CompletionExpression.class));
		factories.add(factoryFor(ComposedExpression.class, ComposedExpression.class));
		factories.add(factoryFor(IfElseExpression.class, IfElseExpression.class));
		factories.add(factoryFor(InvocationExpression.class, InvocationExpression.class));
		factories.add(factoryFor(LambdaExpression.class, LambdaExpression.class));

		factories.add(factoryFor(LoopHeaderBlockExpression.class, LoopHeaderBlockExpression.class));

		factories.add(factoryFor(ConstantValueExpression.class, ConstantValueExpression.class));
		factories.add(factoryFor(NullExpression.class, NullExpression.class));
		factories.add(factoryFor(ReferenceExpression.class, ReferenceExpression.class));
		factories.add(factoryFor(UnknownExpression.class, UnknownExpression.class));

		factories.add(factoryFor(EventReference.class, EventReference.class));
		factories.add(factoryFor(FieldReference.class, FieldReference.class));
		factories.add(factoryFor(MethodReference.class, MethodReference.class));
		factories.add(factoryFor(PropertyReference.class, PropertyReference.class));
		factories.add(factoryFor(UnknownReference.class, UnknownReference.class));
		factories.add(factoryFor(VariableReference.class, VariableReference.class));

		factories.add(factoryFor(Assignment.class, Assignment.class));
		factories.add(factoryFor(BreakStatement.class, BreakStatement.class));
		factories.add(factoryFor(ContinueStatement.class, ContinueStatement.class));
		factories.add(factoryFor(ExpressionStatement.class, ExpressionStatement.class));
		factories.add(factoryFor(GotoStatement.class, GotoStatement.class));
		factories.add(factoryFor(LabelledStatement.class, LabelledStatement.class));
		factories.add(factoryFor(ReturnStatement.class, ReturnStatement.class));
		factories.add(factoryFor(ThrowStatement.class, ThrowStatement.class));
		factories.add(factoryFor(UnknownStatement.class, UnknownStatement.class));
		factories.add(factoryFor(VariableDeclaration.class, VariableDeclaration.class));

		// Adapters for 1st lvl interfaces
		factories.add(factoryFor(ISST.class, SST.class));

		factories.add(factoryFor(ICaseBlock.class, CaseBlock.class));
		factories.add(factoryFor(ICatchBlock.class, CatchBlock.class));
		factories.add(factoryFor(IDoLoop.class, DoLoop.class));
		factories.add(factoryFor(IForEachLoop.class, ForEachLoop.class));
		factories.add(factoryFor(IForLoop.class, ForLoop.class));
		factories.add(factoryFor(IIfElseBlock.class, IfElseBlock.class));
		factories.add(factoryFor(ILockBlock.class, LockBlock.class));
		factories.add(factoryFor(ISwitchBlock.class, SwitchBlock.class));
		factories.add(factoryFor(ITryBlock.class, TryBlock.class));
		factories.add(factoryFor(IUncheckedBlock.class, UncheckedBlock.class));
		factories.add(factoryFor(IUnsafeBlock.class, UnsafeBlock.class));
		factories.add(factoryFor(IUsingBlock.class, UsingBlock.class));
		factories.add(factoryFor(IWhileLoop.class, WhileLoop.class));

		factories.add(factoryFor(IDelegateDeclaration.class, DelegateDeclaration.class));
		factories.add(factoryFor(IEventDeclaration.class, EventDeclaration.class));
		factories.add(factoryFor(IFieldDeclaration.class, FieldDeclaration.class));
		factories.add(factoryFor(IMethodDeclaration.class, MethodDeclaration.class));
		factories.add(factoryFor(IPropertyDeclaration.class, PropertyDeclaration.class));

		factories.add(factoryFor(ICompletionExpression.class, CompletionExpression.class));
		factories.add(factoryFor(IComposedExpression.class, ComposedExpression.class));
		factories.add(factoryFor(IIfElseExpression.class, IfElseExpression.class));
		factories.add(factoryFor(IInvocationExpression.class, InvocationExpression.class));
		factories.add(factoryFor(ILambdaExpression.class, LambdaExpression.class));

		factories.add(factoryFor(ILoopHeaderBlockExpression.class, LoopHeaderBlockExpression.class));

		factories.add(factoryFor(IConstantValueExpression.class, ConstantValueExpression.class));
		factories.add(factoryFor(INullExpression.class, NullExpression.class));
		factories.add(factoryFor(IReferenceExpression.class, ReferenceExpression.class));
		factories.add(factoryFor(IUnknownExpression.class, UnknownExpression.class));

		factories.add(factoryFor(IEventReference.class, EventReference.class));
		factories.add(factoryFor(IFieldReference.class, FieldReference.class));
		factories.add(factoryFor(IMethodReference.class, MethodReference.class));
		factories.add(factoryFor(IPropertyReference.class, PropertyReference.class));
		factories.add(factoryFor(IUnknownReference.class, UnknownReference.class));
		factories.add(factoryFor(IVariableReference.class, VariableReference.class));

		factories.add(factoryFor(IAssignment.class, Assignment.class));
		factories.add(factoryFor(IBreakStatement.class, BreakStatement.class));
		factories.add(factoryFor(IContinueStatement.class, ContinueStatement.class));
		factories.add(factoryFor(IExpressionStatement.class, ExpressionStatement.class));
		factories.add(factoryFor(IGotoStatement.class, GotoStatement.class));
		factories.add(factoryFor(ILabelledStatement.class, LabelledStatement.class));
		factories.add(factoryFor(IReturnStatement.class, ReturnStatement.class));
		factories.add(factoryFor(IThrowStatement.class, ThrowStatement.class));
		factories.add(factoryFor(IUnknownStatement.class, UnknownStatement.class));
		factories.add(factoryFor(IVariableDeclaration.class, VariableDeclaration.class));

		// Recursive interface adapters
		factories.add(factoryFor(IStatement.class, Assignment.class, BreakStatement.class, ContinueStatement.class,
				DoLoop.class, ExpressionStatement.class, ForEachLoop.class, ForLoop.class, GotoStatement.class,
				IfElseBlock.class, LabelledStatement.class, LockBlock.class, ReturnStatement.class, SwitchBlock.class,
				ThrowStatement.class, TryBlock.class, UncheckedBlock.class, UnknownStatement.class, UnsafeBlock.class,
				UsingBlock.class, VariableDeclaration.class, WhileLoop.class));
		factories.add(factoryFor(ISimpleExpression.class, ConstantValueExpression.class, NullExpression.class,
				ReferenceExpression.class, UnknownExpression.class));
		factories.add(factoryFor(IMemberDeclaration.class, DelegateDeclaration.class, EventDeclaration.class,
				FieldDeclaration.class, MethodDeclaration.class, PropertyDeclaration.class, EventReference.class,
				FieldReference.class, PropertyReference.class, UnknownReference.class, VariableReference.class,
				MethodReference.class));
		factories.add(factoryFor(IReference.class, EventReference.class, FieldReference.class, PropertyReference.class,
				UnknownReference.class, VariableReference.class, MethodReference.class));
		factories.add(factoryFor(IAssignableReference.class, EventReference.class, FieldReference.class,
				PropertyReference.class, UnknownReference.class, VariableReference.class));
		factories.add(factoryFor(IMemberReference.class, EventReference.class, FieldReference.class,
				PropertyReference.class, MethodReference.class));
		factories.add(factoryFor(IExpression.class, CompletionExpression.class, ComposedExpression.class,
				IfElseExpression.class, InvocationExpression.class, LambdaExpression.class,
				ConstantValueExpression.class, NullExpression.class, ReferenceExpression.class,
				UnknownExpression.class, LoopHeaderBlockExpression.class));
		factories
				.add(factoryFor(IAssignableExpression.class, CompletionExpression.class, ComposedExpression.class,
						IfElseExpression.class, InvocationExpression.class, LambdaExpression.class,
						ConstantValueExpression.class, NullExpression.class, ReferenceExpression.class,
						UnknownExpression.class));
		factories
				.add(factoryFor(ILoopHeaderExpression.class, LoopHeaderBlockExpression.class,
						ConstantValueExpression.class, NullExpression.class, ReferenceExpression.class,
						UnknownExpression.class));

		for (RuntimeTypeAdapterFactory factory : factories)
			gsonBuilder.registerTypeAdapterFactory(factory);

		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);

		/*
		 * 
		 */

		// name interface types
		gsonBuilder.registerTypeAdapter(AliasName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(BundleName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(EventName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(FieldName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(LambdaName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(LocalVariableName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(MethodName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(Name.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(NamespaceName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(ParameterName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(PropertyName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(TypeName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(DelegateTypeName.class, new GsonNameAdapter());
		// C# name types
		gsonBuilder.registerTypeAdapter(CsAliasName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsAssemblyName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsEventName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsFieldName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsLambdaName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsLocalVariableName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsMethodName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsNamespaceName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsParameterName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsPropertyName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsTypeName.class, new GsonNameAdapter());
		gsonBuilder.registerTypeAdapter(CsDelegateTypeName.class, new GsonNameAdapter());

		// IMemberDeclaration interfaces
		gsonBuilder.registerTypeAdapter(IMemberDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IDelegateDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IEventDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IFieldDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IMethodDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IPropertyDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IAssignableReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IMemberReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IEventReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IFieldReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IPropertyReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IUnknownReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IVariableReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(IMethodReference.class, new GsonIMemberDeclarationDeserializer());
		// IMemberDeclaration implementation
		gsonBuilder.registerTypeAdapter(DelegateDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(EventDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(FieldDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(MethodDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(PropertyDeclaration.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(EventReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(FieldReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(PropertyReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(UnknownReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(VariableReference.class, new GsonIMemberDeclarationDeserializer());
		gsonBuilder.registerTypeAdapter(MethodReference.class, new GsonIMemberDeclarationDeserializer());

		// IExpression interfaces
		gsonBuilder.registerTypeAdapter(IExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(IAssignableExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(ILoopHeaderExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(ICompletionExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(IComposedExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(IIfElseExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(IInvocationExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(ILambdaExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(ISimpleExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(ILoopHeaderBlockExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(IConstantValueExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(INullExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(IReferenceExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(IUnknownExpression.class, new GsonIExpressionDeserializer());
		// IExpression implementations
		gsonBuilder.registerTypeAdapter(CompletionExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(ComposedExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(IfElseExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(InvocationExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(LambdaExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(ConstantValueExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(NullExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(ReferenceExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(UnknownExpression.class, new GsonIExpressionDeserializer());
		gsonBuilder.registerTypeAdapter(LoopHeaderBlockExpression.class, new GsonIExpressionDeserializer());

		// IStatement interfaces
		gsonBuilder.registerTypeAdapter(IStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IAssignment.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IBreakStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IContinueStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IDoLoop.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IExpressionStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IForEachLoop.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IForLoop.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IGotoStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IIfElseBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ILabelledStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ILockBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IReturnStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ISwitchBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IThrowStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ITryBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IUncheckedBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IUnknownStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IUnsafeBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IUsingBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IVariableDeclaration.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IWhileLoop.class, new GsonIStatementDeserializer());
		// IStatement implementation
		gsonBuilder.registerTypeAdapter(Assignment.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(BreakStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ContinueStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(DoLoop.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ExpressionStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ForEachLoop.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ForLoop.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(GotoStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(IfElseBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(LabelledStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(LockBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ReturnStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(SwitchBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(ThrowStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(TryBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(UncheckedBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(UnknownStatement.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(UnsafeBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(UsingBlock.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(VariableDeclaration.class, new GsonIStatementDeserializer());
		gsonBuilder.registerTypeAdapter(WhileLoop.class, new GsonIStatementDeserializer());
		// Case and CatchBlock
		gsonBuilder.registerTypeAdapter(ICatchBlock.class, new GsonCatchBlockDeserializer());
		gsonBuilder.registerTypeAdapter(CatchBlock.class, new GsonCatchBlockDeserializer());
		gsonBuilder.registerTypeAdapter(ICaseBlock.class, new GsonCaseBlockDeserializer());
		gsonBuilder.registerTypeAdapter(CaseBlock.class, new GsonCaseBlockDeserializer());

		// SST
		gsonBuilder.registerTypeAdapter(ISST.class, new GsonISSTDeserializer());
		gsonBuilder.registerTypeAdapter(SST.class, new GsonISSTDeserializer());
		Gson gson = gsonBuilder.create();
		return gson;
	}
}
