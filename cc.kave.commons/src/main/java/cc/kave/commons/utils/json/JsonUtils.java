/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.utils.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.events.ActivityEvent;
import cc.kave.commons.model.events.CommandEvent;
import cc.kave.commons.model.events.ErrorEvent;
import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.InfoEvent;
import cc.kave.commons.model.events.NavigationEvent;
import cc.kave.commons.model.events.SystemEvent;
import cc.kave.commons.model.events.Trigger;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.IProposalSelection;
import cc.kave.commons.model.events.completionevents.Proposal;
import cc.kave.commons.model.events.completionevents.ProposalSelection;
import cc.kave.commons.model.events.completionevents.TerminationState;
import cc.kave.commons.model.events.testrunevents.TestRunEvent;
import cc.kave.commons.model.events.userprofiles.UserProfileEvent;
import cc.kave.commons.model.events.versioncontrolevents.VersionControlEvent;
import cc.kave.commons.model.events.visualstudio.BuildEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerEvent;
import cc.kave.commons.model.events.visualstudio.DocumentEvent;
import cc.kave.commons.model.events.visualstudio.EditEvent;
import cc.kave.commons.model.events.visualstudio.FindEvent;
import cc.kave.commons.model.events.visualstudio.IDEStateEvent;
import cc.kave.commons.model.events.visualstudio.InstallEvent;
import cc.kave.commons.model.events.visualstudio.SolutionEvent;
import cc.kave.commons.model.events.visualstudio.UpdateEvent;
import cc.kave.commons.model.events.visualstudio.WindowEvent;
import cc.kave.commons.model.names.IAliasName;
import cc.kave.commons.model.names.IAssemblyName;
import cc.kave.commons.model.names.IDelegateTypeName;
import cc.kave.commons.model.names.IEventName;
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.ILambdaName;
import cc.kave.commons.model.names.ILocalVariableName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IName;
import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.AliasName;
import cc.kave.commons.model.names.csharp.ArrayTypeName;
import cc.kave.commons.model.names.csharp.AssemblyName;
import cc.kave.commons.model.names.csharp.DelegateTypeName;
import cc.kave.commons.model.names.csharp.EnumTypeName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.InterfaceTypeName;
import cc.kave.commons.model.names.csharp.LambdaName;
import cc.kave.commons.model.names.csharp.LocalVariableName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.Name;
import cc.kave.commons.model.names.csharp.NamespaceName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.names.csharp.StructTypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.names.csharp.TypeParameterName;
import cc.kave.commons.model.names.csharp.UnknownTypeName;
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
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
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
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.commons.utils.json.legacy.GsonUtil;
import cc.recommenders.assertions.Asserts;
import cc.recommenders.assertions.Throws;

public abstract class JsonUtils {

	private static Gson gson;

	static {
		GsonBuilder gb = new GsonBuilder();

		GsonUtil.addTypeAdapters(gb);

		// name interface types
		registerName(gb, IAliasName.class);
		registerName(gb, IAssemblyName.class);
		registerName(gb, IEventName.class);
		registerName(gb, IFieldName.class);
		registerName(gb, ILambdaName.class);
		registerName(gb, ILocalVariableName.class);
		registerName(gb, IMethodName.class);
		registerName(gb, IName.class);
		registerName(gb, INamespaceName.class);
		registerName(gb, IParameterName.class);
		registerName(gb, IPropertyName.class);
		registerName(gb, ITypeName.class);
		registerName(gb, IDelegateTypeName.class);
		// C# name types
		registerName(gb, AliasName.class);
		registerName(gb, AssemblyName.class);
		registerName(gb, EventName.class);
		registerName(gb, FieldName.class);
		registerName(gb, LambdaName.class);
		registerName(gb, LocalVariableName.class);
		registerName(gb, MethodName.class);
		registerName(gb, Name.class);
		registerName(gb, NamespaceName.class);
		registerName(gb, ParameterName.class);
		registerName(gb, PropertyName.class);
		registerName(gb, TypeName.class);
		registerName(gb, DelegateTypeName.class);
		registerName(gb, UnknownTypeName.class);
		registerName(gb, TypeParameterName.class);
		registerName(gb, StructTypeName.class);
		registerName(gb, InterfaceTypeName.class);
		registerName(gb, EnumTypeName.class);
		registerName(gb, ArrayTypeName.class);
		// resharper names
		registerName(gb, LiveTemplateName.class);

		// events
		registerHierarchy(gb, IIDEEvent.class,
				// general
				ActivityEvent.class, CommandEvent.class, ErrorEvent.class, InfoEvent.class, NavigationEvent.class,
				SystemEvent.class,
				// visual studio
				BuildEvent.class, DebuggerEvent.class, DocumentEvent.class, EditEvent.class, FindEvent.class,
				IDEStateEvent.class, InstallEvent.class, SolutionEvent.class, UpdateEvent.class, WindowEvent.class,
				// complex events
				CompletionEvent.class, TestRunEvent.class, UserProfileEvent.class, VersionControlEvent.class);

		// SST Model
		registerHierarchy(gb, ISST.class, SST.class);
		// Declarations
		registerHierarchy(gb, IMemberDeclaration.class, IDelegateDeclaration.class, IEventDeclaration.class,
				IFieldDeclaration.class, IMethodDeclaration.class, IPropertyDeclaration.class, IReference.class);
		registerHierarchy(gb, IFieldDeclaration.class, FieldDeclaration.class);
		registerHierarchy(gb, IDelegateDeclaration.class, DelegateDeclaration.class);
		registerHierarchy(gb, IEventDeclaration.class, EventDeclaration.class);
		registerHierarchy(gb, IPropertyDeclaration.class, PropertyDeclaration.class);
		registerHierarchy(gb, IMethodDeclaration.class, MethodDeclaration.class);
		registerHierarchy(gb, IVariableDeclaration.class, VariableDeclaration.class);
		// References
		registerHierarchy(gb, IReference.class, UnknownReference.class, EventReference.class, FieldReference.class,
				IndexAccessReference.class, PropertyReference.class, MethodReference.class, VariableReference.class);
		registerHierarchy(gb, IAssignableReference.class, EventReference.class, FieldReference.class,
				IndexAccessReference.class, PropertyReference.class, UnknownReference.class, VariableReference.class);
		registerHierarchy(gb, IVariableReference.class, VariableReference.class);
		registerHierarchy(gb, IIndexAccessExpression.class, IndexAccessExpression.class);

		// Expressions
		registerHierarchy(gb, IAssignableExpression.class,
				// assignable
				BinaryExpression.class, CastExpression.class, CompletionExpression.class, ComposedExpression.class,
				IfElseExpression.class, IndexAccessExpression.class, InvocationExpression.class, LambdaExpression.class,
				TypeCheckExpression.class, UnaryExpression.class,
				// simple
				ConstantValueExpression.class, NullExpression.class, ReferenceExpression.class,
				UnknownExpression.class);

		registerHierarchy(gb, ISimpleExpression.class, ConstantValueExpression.class, NullExpression.class,
				ReferenceExpression.class, UnknownExpression.class);

		registerHierarchy(gb, ILoopHeaderExpression.class,
				// loop header
				LoopHeaderBlockExpression.class,
				// simple
				ConstantValueExpression.class, NullExpression.class, ReferenceExpression.class,
				UnknownExpression.class);

		// Statements
		registerHierarchy(gb, IStatement.class, Assignment.class, BreakStatement.class, ContinueStatement.class,
				DoLoop.class, ExpressionStatement.class, ForEachLoop.class, ForLoop.class, GotoStatement.class,
				IfElseBlock.class, LabelledStatement.class, LockBlock.class, ReturnStatement.class, SwitchBlock.class,
				ThrowStatement.class, TryBlock.class, UncheckedBlock.class, UnknownStatement.class, UnsafeBlock.class,
				UsingBlock.class, EventSubscriptionStatement.class, VariableDeclaration.class, WhileLoop.class);

		registerHierarchy(gb, ICatchBlock.class, CatchBlock.class);
		registerHierarchy(gb, ICaseBlock.class, CaseBlock.class);

		// Context
		registerHierarchy(gb, Context.class, Context.class);
		registerHierarchy(gb, ITypeShape.class, TypeShape.class);
		registerHierarchy(gb, IMethodHierarchy.class, MethodHierarchy.class);
		registerHierarchy(gb, ITypeHierarchy.class, TypeHierarchy.class);

		// completion event
		registerHierarchy(gb, ICompletionEvent.class, CompletionEvent.class);
		registerHierarchy(gb, CompletionEvent.class, CompletionEvent.class);
		registerHierarchy(gb, IProposal.class, Proposal.class);
		registerHierarchy(gb, IProposalSelection.class, ProposalSelection.class);

		// enums
		gb.registerTypeAdapter(Trigger.class, EnumDeSerializer.create(Trigger.values()));
		gb.registerTypeAdapter(EventKind.class, EnumDeSerializer.create(EventKind.values()));
		gb.registerTypeAdapter(TerminationState.class, EnumDeSerializer.create(TerminationState.values()));
		gb.registerTypeAdapter(CatchBlockKind.class, EnumDeSerializer.create(CatchBlockKind.values()));
		gb.registerTypeAdapter(EventSubscriptionOperation.class,
				EnumDeSerializer.create(EventSubscriptionOperation.values()));
		gb.registerTypeAdapter(CastOperator.class, EnumDeSerializer.create(CastOperator.values()));
		gb.registerTypeAdapter(BinaryOperator.class, EnumDeSerializer.create(BinaryOperator.values()));
		gb.registerTypeAdapter(UnaryOperator.class, EnumDeSerializer.create(UnaryOperator.values()));

		gb.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
		gb.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);

		gson = gb.create();
	}

	@SafeVarargs
	private static <T> void registerHierarchy(GsonBuilder gsonBuilder, Class<T> type, Class<? extends T>... subtypes) {
		Asserts.assertTrue(subtypes.length > 0);

		RuntimeTypeAdapterFactory<T> factory = RuntimeTypeAdapterFactory.of(type, "$type");
		for (int i = 0; i < subtypes.length; i++) {
			factory = factory.registerSubtype(subtypes[i]);
		}

		gsonBuilder.registerTypeAdapterFactory(factory);
	}

	private static <T> void registerName(GsonBuilder gsonBuilder, Class<T> type) {
		gsonBuilder.registerTypeAdapter(type, new GsonNameDeserializer());
	}

	public static <T> T fromJson(String json, Type targetType) {
		json = TypeUtil.toJavaTypeNames(json);
		return gson.fromJson(json, targetType);
	}

	public static <T> String toJson(Object obj, Type targetType) {
		String json = gson.toJsonTree(obj, targetType).toString();
		return TypeUtil.toCSharpTypeNames(json);
	}

	public static <T> String toJson(Object obj) {
		String json = gson.toJson(obj);
		return TypeUtil.toCSharpTypeNames(json);
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