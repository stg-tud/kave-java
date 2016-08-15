package exec.recommender_reimplementation.java_transformation;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.expr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.unaryExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

public class ExpressionTransformationTest extends JavaTransformationBaseTest {
	@Test
	public void indexAccessExpressionWithArray() {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setIndices(Lists.newArrayList(constant("1"), constant("2")));
		indexAccessExpression.setReference(varRef("x"));

		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("String[]")), expr(indexAccessExpression)),
				declareVar("x", type("String[]")), expr(indexAccessExpression));
	}

	@Test
	public void testIndexAccessExpressionList() {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setIndices(Lists.newArrayList(constant("1")));
		indexAccessExpression.setReference(varRef("x"));

		ITypeName listType = type("List");
		assertAroundMethodDeclaration(Lists.newArrayList(declareVar("x", listType), expr(indexAccessExpression)),
				declareVar("x", listType), expr(invocation("x", method(null, listType, "get"), constant("1"))));
	}

	@Test
	public void testIndexAccessExpressionInAssignment() {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setIndices(Lists.newArrayList(constant("1")));
		indexAccessExpression.setReference(varRef("x"));

		ITypeName listType = type("List");
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", listType), assign(varRef("y"), indexAccessExpression)),
				declareVar("x", listType),
				assign(varRef("y"), invocation("x", method(null, listType, "get"), constant("1"))));
	}

	@Test
	public void transformsDelegateMethods() {
		InvocationExpression delegateInvoke = new InvocationExpression();
		delegateInvoke.setMethodName(MethodName.newMethodName(
				"[System.Void, mscorlib, 4.0.0.0] [d:[System.Void, mscorlib, 4.0.0.0] [P1.DelegateTest+Del, P1].([System.String, mscorlib, 4.0.0.0] message)].Invoke([System.String, mscorlib, 4.0.0.0] message)"));

		InvocationExpression expected = new InvocationExpression();
		expected.setMethodName(MethodName.newMethodName(
				"[System.Void, mscorlib, 4.0.0.0] [Del].Delegate$Invoke([System.String, mscorlib, 4.0.0.0] message)"));

		assertNode(delegateInvoke, expected);
	}

	@Test
	public void constantValueExpression_Null() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("s:System.Boolean")),
						assign(varRef("x"), new ConstantValueExpression())),
				declareVar("x", type("s:System.Boolean")), assign(varRef("x"), constant("false")));
	}

	@Test
	public void composedExpressionInAssignment() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("s:System.Boolean")),
						assign(variableReference("x"), new ComposedExpression())),
				declareVar("x", type("s:System.Boolean")), assign(varRef("x"), constant("false")));
	}

	@Test
	public void composedExpressionInExprStatement() {
		assertAroundMethodDeclaration(Lists.newArrayList(declareVar("x", type("T1")), expr(new ComposedExpression())),
				declareVar("x", type("T1")), expr(constant("null")));
	}

	@Test
	public void unknownExpressionNestedInBinaryExpression() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("s:System.Boolean")),
						assign(variableReference("x"),
								binExpr(BinaryOperator.Plus, new UnknownExpression(), new UnknownExpression()))),
				declareVar("x", type("s:System.Boolean")),
				assign(variableReference("x"), binExpr(BinaryOperator.Plus, constant("false"), constant("false"))));
	}

	@Test
	public void unknownExpressionNestedInUnaryExpression() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("s:System.Boolean")),
						assign(variableReference("x"), unaryExpr(UnaryOperator.Plus, new UnknownExpression()))),
				declareVar("x", type("s:System.Boolean")),
				assign(variableReference("x"), unaryExpr(UnaryOperator.Plus, constant("false"))));
	}

	@Test
	public void testEmptyConditionInIfElseBlock() {
		IfElseBlock node = new IfElseBlock();
		node.getThen().add(new ContinueStatement());
		node.getElse().add(new BreakStatement());

		IfElseBlock expected = new IfElseBlock();
		expected.setCondition(constant("false"));
		expected.getThen().add(new ContinueStatement());
		expected.getElse().add(new BreakStatement());

		assertNode(node, expected);
	}

	@Test
	public void testEmptyLabelInCaseBlock() {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock.getSections().add(new CaseBlock());

		SwitchBlock expected = new SwitchBlock();
		CaseBlock expectedCaseBlock = new CaseBlock();
		expectedCaseBlock.setLabel(constant("0"));
		expected.getSections().add(expectedCaseBlock);

		assertNode(switchBlock, expected);
	}
}
