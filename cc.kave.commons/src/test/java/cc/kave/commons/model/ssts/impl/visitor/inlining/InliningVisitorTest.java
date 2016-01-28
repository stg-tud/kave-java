package cc.kave.commons.model.ssts.impl.visitor.inlining;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningContext;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningContext;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningIStatementVisitor;
import cc.kave.commons.utils.sstprinter.SSTPrintingUtils;

public class InliningVisitorTest extends InliningBaseTest {

	@Test
	public void testRemoveMethod() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2"), //
				declareEntryPoint("m1", //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST(declareEntryPoint("m1"));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testNameInlining() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						declareVar("b")),
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						declareVar("$0_a"), //
						declareVar("$1_b")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testBasicInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						declareVar("b"), //
						assign(ref("a"), refExpr("b"))),
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						declareVar("$0_a"), //
						declareVar("$1_b"), //
						assign(ref("$0_a"), refExpr("$1_b")))); //
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testBasicInline1() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						returnStatement(constant("5"), false)), //
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						assign(ref("b"), invocationExpr("m2", ref("b"))), //
						declareVar("d")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						assign(ref("b"), constant("5")), //
						declareVar("d")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testBasicInline2() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("c"), //
						assign(ref("c"), constant("1")), //
						declareVar("d"), //
						assign(ref("d"), constant("3")), //
						assign(ref("d"), compose(ref("c"), ref("d"))), //
						returnStatement(refExpr("d"), false)),
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						assign(ref("b"), invocationExpr("m2", ref("b")))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						declareVar("c"), //
						assign(ref("c"), constant("1")), //
						declareVar("d"), //
						assign(ref("d"), constant("3")), //
						assign(ref("d"), compose(ref("c"), ref("d"))), //
						assign(ref("b"), refExpr("d"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testSecondEntryPoint() {
		ISST sst = buildSST(
				declareEntryPoint("m2", //
						declareVar("c"), //
						assign(ref("c"), constant("1")), //
						declareVar("d"), //
						assign(ref("d"), constant("1"))),
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						assign(ref("b"), constant("1")), //
						invocationStatement("m2")));
		assertSSTs(sst, sst);
	}

	@Test
	public void testForLoopInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"))),
				declareEntryPoint("m1", //
						declareVar("b"), //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST(declareEntryPoint("m1", //
				declareVar("b"), //
				declareVar("a"), //
				forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testInlineIntoForLoop() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						declareVar("i")), //
				declareEntryPoint("m1", //
						declareVar("a"), //
						forLoop("i", loopHeader(expr(constant("true"))), //
								invocationStatement("m2"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("$0_a"), //
								declareVar("$1_i"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testInlineAfterLoop() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", // ,
						declareVar("i")), //
				declareEntryPoint("m1", //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("a")),
						invocationStatement("m2")));
		ISST inlinedSST = buildSST(//
				declareEntryPoint("m1", //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("a")), //
						declareVar("$0_i")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testInlineAfterLoopAndInLoop() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", // ,
						declareVar("i")), //
				declareEntryPoint("m1", //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("a"), invocationStatement("m2")),
						invocationStatement("m2")));
		ISST inlinedSST = buildSST(//
				declareEntryPoint("m1", //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("a"), declareVar("$0_i")), //
						declareVar("$1_i")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testInlineBeforeInAfterLoop() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", // ,
						declareVar("i")), //
				declareEntryPoint("m1", //
						invocationStatement("m2"), //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("a"), invocationStatement("m2")),
						invocationStatement("m2")));
		ISST inlinedSST = buildSST(//
				declareEntryPoint("m1", //
						declareVar("$0_i"), //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("a"), declareVar("$1_i")), //
						declareVar("$2_i")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testWhileLoopInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("b"), //
						whileLoop(loopHeader(expr(constant("true")), declareVar("c")))), //
				declareEntryPoint("m1", //
						declareVar("b"),
						whileLoop(loopHeader(expr(constant("true")), declareVar("c"), invocationStatement("m2")))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("b"), //
						whileLoop(loopHeader(expr(constant("true")), //
								declareVar("c"), //
								declareVar("$0_b"), //
								whileLoop(loopHeader(expr(constant("true")), declareVar("$1_c")))))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testIfInlinie() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"),
						simpleIf(Lists.newArrayList(declareVar("b")), constant("true"), declareVar("c"))),
				declareEntryPoint("m1", //
						declareVar("a"), //
						simpleIf(Lists.newArrayList(declareVar("b")), constant("true"), invocationStatement("m2"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						simpleIf(Lists.newArrayList(declareVar("b")), constant("true"), //
								declareVar("$0_a"), //
								simpleIf(Lists.newArrayList(declareVar("$1_b")), constant("true"), declareVar("c")))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testDoLoopInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						doLoop(loopHeader(expr(constant("true"))), //
								declareVar("b"))), //
				declareEntryPoint("m1", //
						declareVar("a"),
						doLoop(loopHeader(expr(constant("true"))), //
								declareVar("c"), //
								invocationStatement("m2"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"),
						doLoop(loopHeader(expr(constant("true"))), //
								declareVar("c"), //
								declareVar("$0_a"), //
								doLoop(loopHeader(expr(constant("true"))), //
										declareVar("b")))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testForEachLoopInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						forEachLoop("i", "a", //
								assign(ref("i"), constant("1")))), //
				declareEntryPoint("m1", //
						declareVar("i"), //
						forEachLoop("j", "a", //
								assign(ref("j"), refExpr("i")), //
								invocationStatement("m2"))));
		ISST inlinedSST = buildSST(declareEntryPoint("m1", //
				declareVar("i"),
				forEachLoop("j", "a", //
						assign(ref("j"), refExpr("i")), //
						declareVar("$0_a"), //
						forEachLoop("$1_i", "$0_a", //
								assign(ref("$1_i"), constant("1"))))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testSwitchBlockInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						switchBlock("a", //
								caseBlock("1", declareVar("b")))), //
				declareEntryPoint("m1", //
						declareVar("a"), //
						switchBlock("a", //
								caseBlock("1", declareVar("b")), //
								caseBlock("2", invocationStatement("m2")))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						switchBlock("a", //
								caseBlock("1", declareVar("b")), //
								caseBlock("2", declareVar("$0_a"), //
										switchBlock("$0_a", //
												caseBlock("1", declareVar("$1_b")))))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testTryBlockInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						tryBlock(declareVar("b"), declareVar("c"), catchBlock(declareVar("d")))),
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						tryBlock(declareVar("c"), declareVar("d"), catchBlock(invocationStatement("m2")))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						tryBlock(declareVar("c"), declareVar("d"), //
								catchBlock(declareVar("$0_a"), //
										tryBlock(declareVar("$1_b"), declareVar("$2_c"),
												catchBlock(declareVar("$3_d")))))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testUncheckedBlockInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						uncheckedBlock(declareVar("b"))), //
				declareEntryPoint("m1", //
						declareVar("a"), //
						uncheckedBlock(declareVar("b"), //
								invocationStatement("m2"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						uncheckedBlock(declareVar("b"), //
								declareVar("$0_a"), //
								uncheckedBlock(declareVar("$1_b")))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testUsingBlockInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						usingBlock(ref("a"), assign(ref("a"), refExpr("b")))),
				declareEntryPoint("m1", //
						declareVar("a"), //
						usingBlock(ref("a"), invocationStatement("m2"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						usingBlock(ref("a"), //
								declareVar("$0_a"), //
								usingBlock(ref("$0_a"), assign(ref("$0_a"), refExpr("b"))))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testLockBlockInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						lockBlock("a", assign(ref("a"), refExpr("b")))), //
				declareEntryPoint("m1", //
						declareVar("c"), //
						lockBlock("b", invocationStatement("m2"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("c"), //
						lockBlock("b", //
								declareVar("a"), //
								lockBlock("a", assign(ref("a"), refExpr("$0_b"))))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testReferenceInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						assign(ref("a"), refExpr(refProperty("b"))), //
						assign(ref("a"), refExpr(refEvent("c"))), //
						assign(ref("a"), refExpr(refField("d"))), //
						assign(ref("a"), refExpr(unknownRef()))),
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						declareVar("c"), //
						declareVar("d"), //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						declareVar("c"), //
						declareVar("d"), //
						declareVar("$0_a"), //
						assign(ref("$0_a"), refExpr(refProperty("b"))), //
						assign(ref("$0_a"), refExpr(refEvent("c"))), //
						assign(ref("$0_a"), refExpr(refField("d"))), //
						assign(ref("$0_a"), refExpr(unknownRef()))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testStatementInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						unsafeBlock(), //
						forLoop("i", loopHeader(expr(constant("true"))), //
								continueStatement(), //
								breakStatement(), //
								unknownStatement()),
						label("a", declareVar("b")), //
						gotoStatement("a"), //
						throwStatement()), //
				declareEntryPoint("m1", //
						declareVar("b"), //
						forLoop("i", loopHeader(expr(constant("true"))), //
								invocationStatement("m2")), //
						declareVar("d")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("b"), //
						forLoop("i", loopHeader(expr(constant("true"))), //
								unsafeBlock(), //
								forLoop("$0_i", loopHeader(expr(constant("true"))), //
										continueStatement(), //
										breakStatement(), //
										unknownStatement()),
								label("a", declareVar("$1_b")), //
								gotoStatement("a"), //
								throwStatement()), //
						declareVar("d")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testExpressionInline() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						expr(ifElseExpr(nullExpr(), unknownExpression(), constant("true"))), //
						expr(lambdaExpr(declareVar("a"))), //
						assign(ref("a"), completionExpr("b"))), //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						expr(ifElseExpr(nullExpr(), unknownExpression(), constant("true"))), //
						expr(lambdaExpr(declareVar("$0_a"))), //
						assign(ref("$0_a"), completionExpr("$1_b"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testSettingUpGuardVariables() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						simpleIf(Lists.newArrayList(returnStatement(constant("5"), false)), constant("true"),
								returnStatement(constant("6"), false))), //
				declareEntryPoint("m1", //
						assign(ref("b"), invocationExpr("m2", ref("b")))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar(InliningContext.RESULT_NAME + "[Integer] [?].m2()", INTEGER), //
						declareVar(InliningContext.RESULT_FLAG + "[Integer] [?].m2()", BOOLEAN), //
						assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"), constant("true")), //
						simpleIf(
								Lists.newArrayList(
										assign(ref(InliningContext.RESULT_NAME + "[Integer] [?].m2()"), constant("5")),
										assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"),
												constant("false"))),
								constant("true"),
								assign(ref(InliningContext.RESULT_NAME + "[Integer] [?].m2()"), constant("6")),
								assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"), constant("false"))),
						assign(ref("b"), refExpr(InliningContext.RESULT_NAME + "[Integer] [?].m2()"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testCreateGuardStatement() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						simpleIf(Lists.newArrayList(), constant("true"), //
								simpleIf(Lists.newArrayList(), constant("false"), //
										returnStatement(constant("6"), false)), //
								declareVar("b"), //
								declareVar("c"), //
								returnStatement(constant("7"), false))),
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), invocationExpr("m2", ref("a")))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar(InliningContext.RESULT_NAME + "[Integer] [?].m2()", INTEGER), //
						declareVar(InliningContext.RESULT_FLAG + "[Integer] [?].m2()", BOOLEAN), //
						assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"), constant("true")), //
						declareVar("$0_a"), //
						simpleIf(Lists.newArrayList(), constant("true"), //
								simpleIf(Lists.newArrayList(), constant("false"), //
										assign(ref(InliningContext.RESULT_NAME + "[Integer] [?].m2()"), constant("6")),
										assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"),
												constant("false"))), //
								simpleIf(Lists.newArrayList(),
										refExpr(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"), //
										declareVar("b"), //
										declareVar("c"), //
										assign(ref(InliningContext.RESULT_NAME + "[Integer] [?].m2()"), constant("7")),
										assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"),
												constant("false")))),
						assign(ref("a"), refExpr(InliningContext.RESULT_NAME + "[Integer] [?].m2()"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testCreateGuardsAfterBlock() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						simpleIf(Lists.newArrayList(), constant("true"), //
								simpleIf(Lists.newArrayList(), constant("false"), //
										returnStatement(constant("6"), false)), //
								declareVar("b"), //
								declareVar("c"), //
								returnStatement(constant("7"), false)),
						declareVar("d"), //
						declareVar("e"), //
						returnStatement(constant("8"), false)),
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), invocationExpr("m2", ref("a")))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar(InliningContext.RESULT_NAME + "[Integer] [?].m2()", INTEGER), //
						declareVar(InliningContext.RESULT_FLAG + "[Integer] [?].m2()", BOOLEAN), //
						assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"), constant("true")), //
						declareVar("$0_a"), //
						simpleIf(Lists.newArrayList(), constant("true"), //
								simpleIf(Lists.newArrayList(), constant("false"), //
										assign(ref(InliningContext.RESULT_NAME + "[Integer] [?].m2()"), constant("6")),
										assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"),
												constant("false"))), //
								simpleIf(Lists.newArrayList(),
										refExpr(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"), //
										declareVar("b"), //
										declareVar("c"), //
										assign(ref(InliningContext.RESULT_NAME + "[Integer] [?].m2()"), constant("7")),
										assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"),
												constant("false")))),
						simpleIf(Lists.newArrayList(), refExpr(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"), //
								declareVar("d"), //
								declareVar("e"), //
								assign(ref(InliningContext.RESULT_NAME + "[Integer] [?].m2()"), constant("8")),
								assign(ref(InliningContext.RESULT_FLAG + "[Integer] [?].m2()"), constant("false"))),
						assign(ref("a"), refExpr(InliningContext.RESULT_NAME + "[Integer] [?].m2()"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testPairRecursiveExample() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", invocationStatement("m2")), //
				declareNonEntryPoint("m2", invocationStatement("m3")), //
				declareNonEntryPoint("m3", invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", invocationStatement("m2")), //
				declareNonEntryPoint("m2", invocationStatement("m3")), //
				declareNonEntryPoint("m3", invocationStatement("m2")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testSimpleRecursiveExample() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", invocationStatement("m2")), //
				declareNonEntryPoint("m2", invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", invocationStatement("m2")), //
				declareNonEntryPoint("m2", invocationStatement("m2")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testRecursiveCycle() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", invocationStatement("m2")), //
				declareNonEntryPoint("m2", invocationStatement("m3")), //
				declareNonEntryPoint("m3", invocationStatement("m4")), //
				declareNonEntryPoint("m4", invocationStatement("m5")),
				declareNonEntryPoint("m5", invocationStatement("m6")),
				declareNonEntryPoint("m6", invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", invocationStatement("m2")), //
				declareNonEntryPoint("m2", invocationStatement("m3")), //
				declareNonEntryPoint("m3", invocationStatement("m4")), //
				declareNonEntryPoint("m4", invocationStatement("m5")),
				declareNonEntryPoint("m5", invocationStatement("m6")),
				declareNonEntryPoint("m6", invocationStatement("m2")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testMultipleRecursiveCalls() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", invocationStatement("m2")), //
				declareNonEntryPoint("m2", invocationStatement("m3"), //
						invocationStatement("m4")), //
				declareNonEntryPoint("m3", invocationStatement("m2")),
				declareNonEntryPoint("m4", invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", invocationStatement("m2")), //
				declareNonEntryPoint("m2", invocationStatement("m3"), //
						invocationStatement("m4")), //
				declareNonEntryPoint("m3", invocationStatement("m2")),
				declareNonEntryPoint("m4", invocationStatement("m2")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testRefMethodKeyWord() {
		IMethodName name = MethodName.newMethodName("[?] [?].m2(ref [Integer] b)");
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						invocationStatement(name, refExpr("a"))),
				declareMethod(name, false, //
						assign(ref("b"), constant("1"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), constant("1"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testOutMethodKeyWord() {
		IMethodName name = MethodName.newMethodName("[?] [?].m2(ref [Integer] b)");
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						invocationStatement(name, refExpr("a"))),
				declareMethod(name, false, //
						assign(ref("b"), constant("1"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), constant("1"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testParamsMethodKeyWord() {
		IMethodName name = MethodName.newMethodName("[?] [?].m2(params [Integer] b)");
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						invocationStatement(name, refExpr("a"), refExpr("b")), //
						declareVar("d")),
				declareMethod(name, false));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						declareVar("$0_b", INTEGER), //
						assign(ref("$0_b"), new UnknownExpression()), //
						declareVar("d")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testMethodsWithSameName() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						invocationStatement("m2")),
				declareNonEntryPoint("m2"), //
				declareNonEntryPoint("m2", //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						invocationStatement("m2")),
				declareNonEntryPoint("m2"), //
				declareNonEntryPoint("m2", //
						invocationStatement("m2")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testMethodsWithSameName2() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						invocationStatement("m2")),
				declareNonEntryPoint("m2", //
						invocationStatement("m2")), //
				declareNonEntryPoint("m2", declareVar("a")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						invocationStatement("m2")),
				declareNonEntryPoint("m2", //
						invocationStatement("m2")),
				declareNonEntryPoint("m2", declareVar("a")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testReturnStatementInEntryPoint() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						invocationStatement("m2"), //
						returnStatement(constant("true"), false)), //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						declareVar("b")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b"), //
						returnStatement(constant("true"), false)));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testTwoMethodsWithInlining() {
		ISST sst = buildSST(//
				declareEntryPoint("m1", //
						declareVar("a"), //
						invocationStatement("m3")), //
				declareEntryPoint("m2", //
						declareVar("b"), //
						invocationStatement("m3")), //
				declareNonEntryPoint("m3", //
						declareVar("a"), //
						declareVar("b")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("$0_a"), //
						declareVar("b")), //
				declareEntryPoint("m2", //
						declareVar("b"), //
						declareVar("a"), //
						declareVar("$0_b")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testPersistenceOfNotInlinedMethod() {
		ISST sst = buildSST(//
				declareEntryPoint("m1", //
						declareVar("a"), //
						invocationStatement("m2")),
				declareNonEntryPoint("m2", //
						declareVar("b")),
				declareNonEntryPoint("m3", //
						declareVar("c")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("b")),
				declareNonEntryPoint("m3", //
						declareVar("c")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testPreChangedNameInvocationStatement() {
		IMethodName name = MethodName.newMethodName("[?] [?].m2([?] b)");
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						invocationStatement(name, refExpr("a")), //
						declareVar("b")), //
				declareMethod(name, false, //
						assign(ref("b"), constant("1"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testDeclareAndAssignParameters() {
		IMethodName name = MethodName.newMethodName("[?] [?].m2([?] b)");
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						invocationStatement(name, constant("true")), //
						declareVar("b")), //
				declareMethod(name, false, //
						assign(ref("b"), constant("1"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar("$0_b"), //
						assign(ref("$0_b"), constant("true")), //
						assign(ref("$0_b"), constant("1")), //
						declareVar("b")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testInlineInEntryAndNonEntryPoint() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						invocationStatement("m2")),
				declareNonEntryPoint("m3", //
						invocationStatement("m2")),
				declareNonEntryPoint("m2", //
						declareVar("a")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a")),
				declareNonEntryPoint("m3", //
						invocationStatement("m2")),
				declareNonEntryPoint("m2", //
						declareVar("a")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testCompletionExprInlining() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						invocationStatement("m2")), //
				declareNonEntryPoint("m2", //
						expr(completionExpr("a"))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						expr(completionExpr("$0_a"))));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testNullCompletionExprInlining() {
		ISST sst = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						invocationStatement("m2")), //
				declareNonEntryPoint("m2", //
						expr(new CompletionExpression())));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						expr(new CompletionExpression())));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testMultipleNonEntryPoints() {
		ISST sst = buildSST( //
				declareEntryPoint("ep1", //
						invocationStatement("p1")), //
				declareNonEntryPoint("p1", //
						invocationStatement("p2")), //
				declareNonEntryPoint("p2", //
						declareVar("a")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("ep1", //
						declareVar("a")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testMultipleNonEntryPoints2() {
		ISST sst = buildSST( //
				declareEntryPoint("ep1", //
						invocationStatement("p1")), //
				declareNonEntryPoint("p1", //
						invocationStatement("p2")), //
				declareNonEntryPoint("p2", //
						declareVar("a")), //
				declareEntryPoint("ep2", //
						invocationStatement("p2")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("ep1", //
						declareVar("a")), //
				declareEntryPoint("ep2", //
						declareVar("a")));
		assertSSTs(sst, inlinedSST);
	}

	@Test
	public void testMultipleReturns() {
		IMethodName name = MethodName.newMethodName("[Void] [?].p1()");
		ISST sst = buildSST( //
				declareEntryPoint("ep1", //
						declareVar("a"), //
						invocationStatement(name)), //
				declareMethod(name, false, //
						simpleIf(new ArrayList<IStatement>(), new UnknownExpression(),
								returnStatement(new UnknownExpression(), true)), //
						declareVar("a")));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("ep1", //
						declareVar("a"), //
						declareVar(InliningContext.RESULT_FLAG + "[Void] [?].p1()", BOOLEAN), //
						assign(ref(InliningContext.RESULT_FLAG + "[Void] [?].p1()"), constant("true")), //
						simpleIf(new ArrayList<IStatement>(), new UnknownExpression(),
								assign(ref(InliningContext.RESULT_FLAG + "[Void] [?].p1()"), constant("false"))), //
						simpleIf(new ArrayList<IStatement>(), refExpr(InliningContext.RESULT_FLAG + "[Void] [?].p1()"),
								declareVar("$0_a"))));
		assertSSTs(sst, inlinedSST);
	}
	
	@Test
	public void testUnaryBinaryExpressionInline(){
		ISST sst = buildSST(//
				declareEntryPoint("ep1", //
						invocationStatement("ep2")),
				declareNonEntryPoint("ep2", //
						declareVar("a"),
						assign(ref("a"), new BinaryExpression()),
						assign(ref("a"), new UnaryExpression())));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("ep1", //
						declareVar("a"), //
						assign(ref("a"), new BinaryExpression()),
						assign(ref("a"), new UnaryExpression())));
		assertSSTs(sst, inlinedSST);
	}
	
	@Test
	public void testCastExpressionInline(){
		CastExpression expr = new CastExpression();
		expr.setReference(ref("b"));
		ISST sst = buildSST(//
				declareEntryPoint("ep1", //
						invocationStatement("ep2")),
				declareNonEntryPoint("ep2", //
						declareVar("a"),
						assign(ref("a"), expr)));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("ep1", //
						declareVar("a"), //
						assign(ref("a"), expr)));
		assertSSTs(sst, inlinedSST);
	}
	
	@Test
	public void testTypeCheckExpressionInline(){
		TypeCheckExpression expr = new TypeCheckExpression();
		expr.setReference(ref("b"));
		ISST sst = buildSST(//
				declareEntryPoint("ep1", //
						invocationStatement("ep2")),
				declareNonEntryPoint("ep2", //
						declareVar("a"),
						assign(ref("a"), expr)));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("ep1", //
						declareVar("a"), //
						assign(ref("a"), expr)));
		assertSSTs(sst, inlinedSST);
	}
	
	@Test
	public void testIndexAccessExpressionInline(){
		IndexAccessExpression expr = new IndexAccessExpression();
		expr.setReference(ref("b"));
		ISST sst = buildSST(//
				declareEntryPoint("ep1", //
						invocationStatement("ep2")),
				declareNonEntryPoint("ep2", //
						declareVar("a"),
						assign(ref("a"), expr)));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("ep1", //
						declareVar("a"), //
						assign(ref("a"), expr)));
		assertSSTs(sst, inlinedSST);
	}

	public static void assertSSTs(ISST sst, ISST inlinedSST) {
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		//System.out.println(SSTPrintingUtils.printSST(context.getSST()));
		System.out.println(context.getSST());
		System.out.println(inlinedSST);
		assertThat(context.getSST(), equalTo(inlinedSST));
	}
}
