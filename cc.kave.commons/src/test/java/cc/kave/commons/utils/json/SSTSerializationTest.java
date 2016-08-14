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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.SST;
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

public class SSTSerializationTest {

	@Test
	public void serializeSSTToJson() {
		String actual = JsonUtils.toJson(getExample(), ISST.class);
		String expected = getExampleJson_Current();
		assertThat(actual, equalTo(expected));
	}

	@Test
	public void deserializeJsonToSST() {
		ISST a = getExample();
		ISST b = JsonUtils.fromJson(getExampleJson_Current(), ISST.class);
		assertThat(a, equalTo(b));
	}

	private static ISST getExample() {
		SST sut = new SST();

		FieldDeclaration fieldDeclaration = new FieldDeclaration();
		fieldDeclaration.setName(Names.newField("[T4,P] [T5,P].F"));

		PropertyDeclaration propertyDeclaration = new PropertyDeclaration();
		propertyDeclaration.setName(Names.newProperty("[T10,P] [T11,P].P()"));
		propertyDeclaration.setGet(Lists.newArrayList(new ReturnStatement()));
		propertyDeclaration.setSet(Lists.newArrayList(new Assignment()));

		EventDeclaration eventDeclaration = new EventDeclaration();
		eventDeclaration.setName(Names.newEvent("[T2,P] [T3,P].E"));

		DelegateDeclaration delegateDeclaration = new DelegateDeclaration();
		delegateDeclaration.setName(Names.newType("d:[R,P] [T2,P].()").asDelegateTypeName());

		MethodDeclaration methodDeclaration = new MethodDeclaration();
		methodDeclaration.setName(Names.newMethod("[T6,P] [T7,P].M1()"));
		methodDeclaration.setEntryPoint(false);
		methodDeclaration.setBody(createBody(true));
		MethodDeclaration methodDeclaration2 = new MethodDeclaration();
		methodDeclaration2.setName(Names.newMethod("[T8,P] [T9,P].M2()"));
		methodDeclaration2.setEntryPoint(true);

		sut.setEnclosingType(Names.newType("T,P"));
		sut.setProperties(Sets.newHashSet(propertyDeclaration));
		sut.setFields(Sets.newHashSet(fieldDeclaration));
		sut.setDelegates(Sets.newHashSet(delegateDeclaration));
		sut.setEvents(Sets.newHashSet(eventDeclaration));
		sut.setMethods(Sets.newHashSet(methodDeclaration, methodDeclaration2));

		return sut;
	}

	private static List<IStatement> createBody(boolean complex) {
		return Lists.newArrayList(new DoLoop(), //
				new ForEachLoop(), //
				new ForLoop(), //
				new IfElseBlock(), //
				new LockBlock(), //
				new SwitchBlock(), //
				createComplexTryBlock(), //
				new UncheckedBlock(), //
				new UnsafeBlock(), //
				new UsingBlock(), //
				new WhileLoop(), //
				//
				new Assignment(), //
				new BreakStatement(), //
				new ContinueStatement(), //
				new ExpressionStatement(), //
				new GotoStatement(), //
				new LabelledStatement(), //
				new ReturnStatement(), //
				new ThrowStatement(), //
				new UnknownStatement(),
				//
				nested(new BinaryExpression()), //
				nested(new CastExpression()), //
				nested(new CompletionExpression()), //
				nested(new ComposedExpression()), //
				nested(new IfElseExpression()), //
				nested(new IndexAccessExpression()), //
				nested(new InvocationExpression()), //
				nested(new LambdaExpression()), //
				nested(new TypeCheckExpression()), //
				nested(new UnaryExpression()), //
				nested(new LoopHeaderBlockExpression()), //
				nested(new ConstantValueExpression()), //
				nested(new NullExpression()), //
				nested(new ReferenceExpression()), //
				nested(new UnknownExpression()), //
				//
				nested(new EventReference()), nested(new FieldReference()), nested(new MethodReference()),
				nested(new PropertyReference()), nested(new UnknownReference()), nested(new VariableReference()));

	}

	private static TryBlock createComplexTryBlock() {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(Lists.newArrayList(new ContinueStatement()));
		tryBlock.setFinally(Lists.newArrayList(new BreakStatement()));
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setParameter(Names.newParameter("[T,P] p"));
		catchBlock.setBody(Lists.newArrayList(new ContinueStatement()));
		catchBlock.setKind(CatchBlockKind.Unnamed);
		tryBlock.setCatchBlocks(Lists.newArrayList(catchBlock));
		return tryBlock;
	}

	private static IStatement nested(ILoopHeaderBlockExpression expr) {
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setCondition(expr);
		return whileLoop;
	}

	private static IStatement nested(IAssignableExpression expr) {
		Assignment assignment = new Assignment();
		assignment.setExpression(expr);
		return assignment;
	}

	private static IStatement nested(IReference reference) {
		Assignment assignment = new Assignment();
		ReferenceExpression referenceExpression = new ReferenceExpression();
		referenceExpression.setReference(reference);
		assignment.setExpression(referenceExpression);
		return assignment;
	}

	private static String getExampleJson_Current() {
		return "{\"$type\":\"[SST:SST]\",\"EnclosingType\":\"0T:T,P\",\"PartialClassIdentifier\":\"\",\"Fields\":[{\"$type\":\"[SST:Declarations.FieldDeclaration]\",\"Name\":\"0F:[T4,P] [T5,P].F\"}],\"Properties\":[{\"$type\":\"[SST:Declarations.PropertyDeclaration]\",\"Name\":\"0P:[T10,P] [T11,P].P()\",\"Get\":[{\"$type\":\"[SST:Statements.ReturnStatement]\",\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"IsVoid\":false}],\"Set\":[{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}}]}],\"Methods\":[{\"$type\":\"[SST:Declarations.MethodDeclaration]\",\"Name\":\"0M:[T6,P] [T7,P].M1()\",\"IsEntryPoint\":false,\"Body\":[{\"$type\":\"[SST:Blocks.DoLoop]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.ForEachLoop]\",\"Declaration\":{\"$type\":\"[SST:Statements.VariableDeclaration]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Type\":\"0T:?\"},\"LoopedReference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.ForLoop]\",\"Init\":[],\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Step\":[],\"Body\":[]},{\"$type\":\"[SST:Blocks.IfElseBlock]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Then\":[],\"Else\":[]},{\"$type\":\"[SST:Blocks.LockBlock]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.SwitchBlock]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Sections\":[],\"DefaultSection\":[]},{\"$type\":\"[SST:Blocks.TryBlock]\",\"Body\":[{\"$type\":\"[SST:Statements.ContinueStatement]\"}],\"CatchBlocks\":[{\"$type\":\"[SST:Blocks.CatchBlock]\",\"Kind\":1,\"Parameter\":\"0Param:[T,P] p\",\"Body\":[{\"$type\":\"[SST:Statements.ContinueStatement]\"}]}],\"Finally\":[{\"$type\":\"[SST:Statements.BreakStatement]\"}]},{\"$type\":\"[SST:Blocks.UncheckedBlock]\",\"Body\":[]},{\"$type\":\"[SST:Blocks.UnsafeBlock]\"},{\"$type\":\"[SST:Blocks.UsingBlock]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.WhileLoop]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Body\":[]},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}},{\"$type\":\"[SST:Statements.BreakStatement]\"},{\"$type\":\"[SST:Statements.ContinueStatement]\"},{\"$type\":\"[SST:Statements.ExpressionStatement]\",\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}},{\"$type\":\"[SST:Statements.GotoStatement]\",\"Label\":\"\"},{\"$type\":\"[SST:Statements.LabelledStatement]\",\"Label\":\"\",\"Statement\":{\"$type\":\"[SST:Statements.UnknownStatement]\"}},{\"$type\":\"[SST:Statements.ReturnStatement]\",\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"IsVoid\":false},{\"$type\":\"[SST:Statements.ThrowStatement]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"}},{\"$type\":\"[SST:Statements.UnknownStatement]\"},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.BinaryExpression]\",\"LeftOperand\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Operator\":0,\"RightOperand\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.CastExpression]\",\"TargetType\":\"0T:?\",\"Operator\":0,\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.CompletionExpression]\",\"Token\":\"\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.ComposedExpression]\",\"References\":[]}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.IfElseExpression]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"ThenExpression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"ElseExpression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.IndexAccessExpression]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Indices\":[]}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.InvocationExpression]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"MethodName\":\"0M:[?] [?].???()\",\"Parameters\":[]}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.LambdaExpression]\",\"Name\":\"0L:???\",\"Body\":[]}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.TypeCheckExpression]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Type\":\"0T:?\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.UnaryExpression]\",\"Operator\":0,\"Operand\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}}},{\"$type\":\"[SST:Blocks.WhileLoop]\",\"Condition\":{\"$type\":\"[SST:Expressions.LoopHeader.LoopHeaderBlockExpression]\",\"Body\":[]},\"Body\":[]},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ConstantValueExpression]\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.NullExpression]\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.EventReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"EventName\":\"0E:[?] [?].???\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.FieldReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"FieldName\":\"0F:[?] [?].???\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.MethodReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"MethodName\":\"0M:[?] [?].???()\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.PropertyReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"PropertyName\":\"0P:[?] [?].???\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"}}}]},{\"$type\":\"[SST:Declarations.MethodDeclaration]\",\"Name\":\"0M:[T8,P] [T9,P].M2()\",\"IsEntryPoint\":true,\"Body\":[]}],\"Events\":[{\"$type\":\"[SST:Declarations.EventDeclaration]\",\"Name\":\"0E:[T2,P] [T3,P].E\"}],\"Delegates\":[{\"$type\":\"[SST:Declarations.DelegateDeclaration]\",\"Name\":\"0T:d:[R,P] [T2,P].()\"}]}";
	}
}