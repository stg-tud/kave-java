package cc.kave.commons.utils.clone;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.csharp.DelegateTypeName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.LambdaName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
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
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.utils.SSTCloneUtil;

public class SSTCloneUtilTest extends SSTCloneUtilBaseTest {

	@Test
	public void variableReference() {
		IVariableReference original = someVarRef();
		ISSTNode clone = SSTCloneUtil.clone(original, ISSTNode.class);
		assertClone(original, clone);
	}

	@Test
	public void eventReference() {
		EventReference original = new EventReference();
		original.setEventName(EventName.newEventName("event"));
		original.setReference(someVarRef());
		EventReference clone = SSTCloneUtil.clone(original, EventReference.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void fieldReference() {
		FieldReference original = new FieldReference();
		original.setFieldName(FieldName.newFieldName("field"));
		original.setReference(someVarRef());
		FieldReference clone = SSTCloneUtil.clone(original, FieldReference.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void indexAccessReference() {
		IndexAccessReference original = new IndexAccessReference();
		ISSTNode clone = SSTCloneUtil.clone(original, ISSTNode.class);
		assertClone(original, clone);
	}

	@Test
	public void propertyReference() {
		PropertyReference original = new PropertyReference();
		original.setPropertyName(PropertyName.newPropertyName("property"));
		original.setReference(someVarRef());
		PropertyReference clone = SSTCloneUtil.clone(original, PropertyReference.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void unknownReference() {
		UnknownReference original = new UnknownReference();
		ISSTNode clone = SSTCloneUtil.clone(original, ISSTNode.class);
		assertClone(original, clone);
	}

	@Test
	public void methodReference() {
		MethodReference original = new MethodReference();
		original.setMethodName(MethodName.newMethodName("method"));
		original.setReference(someVarRef());
		MethodReference clone = SSTCloneUtil.clone(original, MethodReference.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void binaryExpression() {
		BinaryExpression original = new BinaryExpression();
		original.setLeftOperand(constant());
		original.setRightOperand(constant());
		original.setOperator(BinaryOperator.And);
		BinaryExpression clone = SSTCloneUtil.clone(original, BinaryExpression.class);
		assertClone(original, clone);
		assertClone(original.getLeftOperand(), clone.getLeftOperand());
		assertClone(original.getRightOperand(), clone.getRightOperand());
	}

	@Test
	public void castExpression() {
		CastExpression original = new CastExpression();
		original.setOperator(CastOperator.Cast);
		original.setReference(someVarRef());
		original.setTargetType(someType());
		CastExpression clone = SSTCloneUtil.clone(original, CastExpression.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void completionExpression() {
		CompletionExpression original = new CompletionExpression();
		original.setObjectReference(someVarRef());
		original.setToken("a");
		original.setTypeReference(someType());
		CompletionExpression clone = SSTCloneUtil.clone(original, CompletionExpression.class);
		assertClone(original, clone);
		assertClone(original.getVariableReference(), clone.getVariableReference());
	}

	@Test
	public void composedExpression() {
		ComposedExpression original = new ComposedExpression();
		original.setReferences(Lists.newArrayList(someVarRef()));
		ComposedExpression clone = SSTCloneUtil.clone(original, ComposedExpression.class);
		assertClone(original, clone);
		assertClone(original.getReferences(), clone.getReferences());
	}

	@Test
	public void ifElseExpression() {
		IfElseExpression original = new IfElseExpression();
		original.setCondition(constant());
		original.setElseExpression(constant());
		original.setThenExpression(constant());
		IfElseExpression clone = SSTCloneUtil.clone(original, IfElseExpression.class);
		assertClone(original, clone);
		assertClone(original.getCondition(), clone.getCondition());
		assertClone(original.getElseExpression(), clone.getElseExpression());
		assertClone(original.getThenExpression(), clone.getThenExpression());
	}

	@Test
	public void indexAccessExpression() {
		IndexAccessExpression original = new IndexAccessExpression();
		original.setIndices(Lists.newArrayList(constant()));
		original.setReference(someVarRef());
		IndexAccessExpression clone = SSTCloneUtil.clone(original, IndexAccessExpression.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
		assertClone(original.getIndices(), clone.getIndices());
	}

	@Test
	public void invocationExpression() {
		InvocationExpression original = new InvocationExpression();
		original.setMethodName(MethodName.newMethodName("m1"));
		original.setParameters(Lists.newArrayList(constant()));
		original.setReference(someVarRef());
		InvocationExpression clone = SSTCloneUtil.clone(original, InvocationExpression.class);
		assertClone(original, clone);
		assertClone(original.getParameters(), clone.getParameters());
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void lambdaExpression() {
		LambdaExpression original = new LambdaExpression();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setName(LambdaName.newLambdaName("l"));
		LambdaExpression clone = SSTCloneUtil.clone(original, LambdaExpression.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
	}

	@Test
	public void typeCheckExpression() {
		TypeCheckExpression original = new TypeCheckExpression();
		original.setReference(someVarRef());
		original.setType(someType());
		TypeCheckExpression clone = SSTCloneUtil.clone(original, TypeCheckExpression.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void unaryExpression() {
		UnaryExpression original = new UnaryExpression();
		original.setOperand(constant());
		original.setOperator(UnaryOperator.Complement);
		UnaryExpression clone = SSTCloneUtil.clone(original, UnaryExpression.class);
		assertClone(original, clone);
		assertClone(original.getOperand(), clone.getOperand());
	}

	@Test
	public void constantValueExpression() {
		ConstantValueExpression original = constant();
		ConstantValueExpression clone = SSTCloneUtil.clone(original, ConstantValueExpression.class);
		assertClone(original, clone);
	}

	@Test
	public void nullExpression() {
		NullExpression original = new NullExpression();
		NullExpression clone = SSTCloneUtil.clone(original, NullExpression.class);
		assertClone(original, clone);
	}

	@Test
	public void referenceExpression() {
		ReferenceExpression original = new ReferenceExpression();
		original.setReference(someVarRef());
		ReferenceExpression clone = SSTCloneUtil.clone(original, ReferenceExpression.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void unknownExpression() {
		UnknownExpression original = new UnknownExpression();
		UnknownExpression clone = SSTCloneUtil.clone(original, UnknownExpression.class);
		assertClone(original, clone);
	}

	@Test
	public void loopHeaderBlockExpression() {
		LoopHeaderBlockExpression original = new LoopHeaderBlockExpression();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		LoopHeaderBlockExpression clone = SSTCloneUtil.clone(original, LoopHeaderBlockExpression.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
	}

	@Test
	public void assignment() {
		Assignment original = new Assignment();
		original.setExpression(someExpr());
		original.setReference(someVarRef());
		Assignment clone = SSTCloneUtil.clone(original, Assignment.class);
		assertClone(original, clone);
		assertClone(original.getExpression(), clone.getExpression());
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void breakStatement() {
		BreakStatement original = new BreakStatement();
		ISSTNode clone = SSTCloneUtil.clone(original, ISSTNode.class);
		assertClone(original, clone);
	}

	@Test
	public void continueStatement() {
		ContinueStatement original = new ContinueStatement();
		ISSTNode clone = SSTCloneUtil.clone(original, ISSTNode.class);
		assertClone(original, clone);
	}

	@Test
	public void doLoop() {
		DoLoop original = new DoLoop();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setCondition(constant());
		DoLoop clone = SSTCloneUtil.clone(original, DoLoop.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
		assertClone(original.getCondition(), clone.getCondition());
	}

	@Test
	public void eventSubscriptionStatement() {
		EventSubscriptionStatement original = new EventSubscriptionStatement();
		original.setExpression(someExpr());
		original.setOperation(EventSubscriptionOperation.Add);
		original.setReference(someVarRef());
		EventSubscriptionStatement clone = SSTCloneUtil.clone(original, EventSubscriptionStatement.class);
		assertClone(original, clone);
		assertClone(original.getExpression(), clone.getExpression());
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void expressionStatement() {
		ExpressionStatement original = new ExpressionStatement();
		original.setExpression(someExpr());
		ExpressionStatement clone = SSTCloneUtil.clone(original, ExpressionStatement.class);
		assertClone(original, clone);
		assertClone(original.getExpression(), clone.getExpression());
	}

	@Test
	public void forEachLoop() {
		ForEachLoop original = new ForEachLoop();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setDeclaration(someVarDec());
		original.setLoopedReference(someVarRef());
		ForEachLoop clone = SSTCloneUtil.clone(original, ForEachLoop.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
		assertClone(original.getDeclaration(), clone.getDeclaration());
		assertClone(original.getLoopedReference(), clone.getLoopedReference());
	}

	@Test
	public void gotoStatement() {
		GotoStatement original = new GotoStatement();
		original.setLabel("1");
		GotoStatement clone = SSTCloneUtil.clone(original, GotoStatement.class);
		assertClone(original, clone);
	}

	@Test
	public void ifElseBlock() {
		IfElseBlock original = new IfElseBlock();
		original.setCondition(constant());
		original.setElse(Lists.newArrayList(new ContinueStatement()));
		original.setThen(Lists.newArrayList(new ContinueStatement()));
		IfElseBlock clone = SSTCloneUtil.clone(original, IfElseBlock.class);
		assertClone(original, clone);
		assertClone(original.getCondition(), clone.getCondition());
		assertClone(original.getElse(), clone.getElse());
		assertClone(original.getThen(), clone.getThen());
	}

	@Test
	public void labelledStatement() {
		LabelledStatement original = new LabelledStatement();
		original.setLabel("l");
		original.setStatement(new ContinueStatement());
		LabelledStatement clone = SSTCloneUtil.clone(original, LabelledStatement.class);
		assertClone(original, clone);
		assertClone(original.getStatement(), clone.getStatement());
	}

	@Test
	public void lockBlock() {
		LockBlock original = new LockBlock();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setReference(someVarRef());
		LockBlock clone = SSTCloneUtil.clone(original, LockBlock.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void returnStatement() {
		ReturnStatement original = new ReturnStatement();
		original.setExpression(constant());
		original.setIsVoid(true);
		ReturnStatement clone = SSTCloneUtil.clone(original, ReturnStatement.class);
		assertClone(original, clone);
		assertClone(original.getExpression(), clone.getExpression());
	}

	@Test
	public void switchBlock() {
		SwitchBlock original = new SwitchBlock();
		original.setDefaultSection(Lists.newArrayList(new ContinueStatement()));
		original.setReference(someVarRef());
		CaseBlock caseBlock = new CaseBlock();
		caseBlock.getBody().add(new ContinueStatement());
		caseBlock.setLabel(constant());
		original.setSections(Lists.newArrayList(caseBlock));
		SwitchBlock clone = SSTCloneUtil.clone(original, SwitchBlock.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
		assertClone(original.getDefaultSection(), clone.getDefaultSection());
		for (int i = 0; i < clone.getSections().size(); i++) {
			assertClone(original.getSections().get(i), clone.getSections().get(i));
			assertClone(original.getSections().get(i).getBody(), clone.getSections().get(i).getBody());
			assertClone(original.getSections().get(i).getLabel(), clone.getSections().get(i).getLabel());
		}
	}

	@Test
	public void throwStatement() {
		ThrowStatement original = new ThrowStatement();
		original.setReference(someVarRef());
		ThrowStatement clone = SSTCloneUtil.clone(original, ThrowStatement.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void tryBlock() {
		TryBlock original = new TryBlock();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setFinally(Lists.newArrayList(new ContinueStatement()));
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setBody(Lists.newArrayList(new ContinueStatement()));
		catchBlock.setKind(CatchBlockKind.General);
		catchBlock.setParameter(ParameterName.newParameterName("p"));
		original.setCatchBlocks(Lists.newArrayList(catchBlock));
		TryBlock clone = SSTCloneUtil.clone(original, TryBlock.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
		assertClone(original.getFinally(), clone.getFinally());
		for (int i = 0; i < clone.getCatchBlocks().size(); i++) {
			assertClone(original.getCatchBlocks().get(i), clone.getCatchBlocks().get(i));
			assertClone(original.getCatchBlocks().get(i).getBody(), clone.getCatchBlocks().get(i).getBody());
		}
	}

	@Test
	public void uncheckedBlock() {
		UncheckedBlock original = new UncheckedBlock();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		UncheckedBlock clone = SSTCloneUtil.clone(original, UncheckedBlock.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
	}

	@Test
	public void unknownStatement() {
		UnknownStatement original = new UnknownStatement();
		UnknownStatement clone = SSTCloneUtil.clone(original, UnknownStatement.class);
		assertClone(original, clone);
	}

	@Test
	public void unsafeBlock() {
		UnsafeBlock original = new UnsafeBlock();
		UnsafeBlock clone = SSTCloneUtil.clone(original, UnsafeBlock.class);
		assertClone(original, clone);
	}

	@Test
	public void usingBlock() {
		UsingBlock original = new UsingBlock();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		UsingBlock clone = SSTCloneUtil.clone(original, UsingBlock.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
	}

	@Test
	public void variableDeclaration() {
		VariableDeclaration original = new VariableDeclaration();
		original.setReference(someVarRef());
		original.setType(TypeName.newTypeName("t"));
		VariableDeclaration clone = SSTCloneUtil.clone(original, VariableDeclaration.class);
		assertClone(original, clone);
		assertClone(original.getReference(), clone.getReference());
	}

	@Test
	public void whileLoop() {
		WhileLoop original = new WhileLoop();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setCondition(constant());
		WhileLoop clone = SSTCloneUtil.clone(original, WhileLoop.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
		assertClone(original.getCondition(), clone.getCondition());
	}

	@Test
	public void simpleSST() {
		ISST original = new SST();
		ISSTNode clone = SSTCloneUtil.clone(original, ISSTNode.class);
		assertClone(original, clone);
	}

	@Test
	public void simpleSSTwithHeader() {
		ISST original = defaultSST();
		ISSTNode clone = SSTCloneUtil.clone(original, ISSTNode.class);
		assertClone(original, clone);
	}

	@Test
	public void simpleSSTwithDeclarations() {
		ISST original = defaultSST();
		original.getDelegates().add(new DelegateDeclaration());
		original.getMethods().add(new MethodDeclaration());
		original.getEvents().add(new EventDeclaration());
		original.getFields().add(new FieldDeclaration());
		original.getProperties().add(new PropertyDeclaration());
		ISSTNode clone = SSTCloneUtil.clone(original, ISSTNode.class);
		assertClone(original, clone);
	}

	@Test
	public void methodDeclaration() {
		MethodDeclaration original = new MethodDeclaration();
		original.setBody(Lists.newArrayList(new ContinueStatement()));
		original.setEntryPoint(true);
		original.setName(MethodName.newMethodName("m"));
		MethodDeclaration clone = SSTCloneUtil.clone(original, MethodDeclaration.class);
		assertClone(original, clone);
		assertClone(original.getBody(), clone.getBody());
	}

	@Test
	public void delegateDeclaration() {
		DelegateDeclaration original = new DelegateDeclaration();
		original.setName(DelegateTypeName.newDelegateTypeName("d:"));
		DelegateDeclaration clone = SSTCloneUtil.clone(original, DelegateDeclaration.class);
		assertClone(original, clone);
	}

	@Test
	public void eventDeclaration() {
		EventDeclaration original = new EventDeclaration();
		original.setName(EventName.newEventName("E"));
		EventDeclaration clone = SSTCloneUtil.clone(original, EventDeclaration.class);
		assertClone(original, clone);
	}

	@Test
	public void fieldDeclaration() {
		FieldDeclaration original = new FieldDeclaration();
		original.setName(FieldName.newFieldName("F"));
		FieldDeclaration clone = SSTCloneUtil.clone(original, FieldDeclaration.class);
		assertClone(original, clone);
	}

	@Test
	public void propertyDeclaration() {
		PropertyDeclaration original = new PropertyDeclaration();
		original.setGet(Lists.newArrayList(new ContinueStatement()));
		original.setSet(Lists.newArrayList(new ContinueStatement()));
		original.setName(PropertyName.newPropertyName("P"));
		PropertyDeclaration clone = SSTCloneUtil.clone(original, PropertyDeclaration.class);
		assertClone(original, clone);
		assertClone(original.getGet(), clone.getGet());
		assertClone(original.getSet(), clone.getSet());
	}

}
