package commons.model.ssts.impl.visitor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningIStatementVisitor;
import cc.kave.commons.utils.visitor.InliningContext;
import cc.kave.commons.utils.visitor.InliningUtil;

public class InliningVisitorTest extends InliningBaseTest {

	@Test
	public void testRemoveMethod() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2"), //
				declareEntryPoint("m1", //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST(declareEntryPoint("m1"));
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		ISST defaultSST = buildSST( //
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
		assertThat(sst, equalTo(defaultSST));
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
						assign(ref("b"), invocationExpr("m2", ref("b")))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						assign(ref("b"), constant("5"))));
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(sst));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
								invocationStatement("m2"))));
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
								throwStatement())));
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
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
						declareVar(InliningUtil.RESULT_NAME + "[?] [?].m2()"), //
						declareVar(InliningUtil.RESULT_FLAG + "[?] [?].m2()"), //
						assign(ref(InliningUtil.RESULT_FLAG + "[?] [?].m2()"), constant("false")), //
						simpleIf(
								Lists.newArrayList(
										assign(ref(InliningUtil.RESULT_NAME + "[?] [?].m2()"), constant("5")),
										assign(ref(InliningUtil.RESULT_FLAG + "[?] [?].m2()"), constant("true"))),
								constant("true"), assign(ref(InliningUtil.RESULT_NAME + "[?] [?].m2()"), constant("6")),
								assign(ref(InliningUtil.RESULT_FLAG + "[?] [?].m2()"), constant("true"))),
						assign(ref("b"), refExpr(InliningUtil.RESULT_NAME + "[?] [?].m2()"))));
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
	}

	@Test
	public void testCreateGuardStatement() {
		ISST sst = buildSST( //
				declareNonEntryPoint("m2", //
						declareVar("a"), //
						simpleIf(Lists.newArrayList(), constant("true"), //
								simpleIf(Lists.newArrayList(), constant("false"), //
										returnStatement(constant("6"), true)), //
								declareVar("b"), //
								declareVar("c"), //
								returnStatement(constant("7"), true))),
				declareEntryPoint("m1", //
						declareVar("a"), //
						assign(ref("a"), invocationExpr("m2", ref("a")))));
		ISST inlinedSST = buildSST( //
				declareEntryPoint("m1", //
						declareVar("a"), //
						declareVar(InliningUtil.RESULT_NAME + "[?] [?].m2()"), //
						declareVar(InliningUtil.RESULT_FLAG + "[?] [?].m2()"), //
						assign(ref(InliningUtil.RESULT_FLAG + "[?] [?].m2()"), constant("false")), //
						declareVar("$0_a"), //
						simpleIf(Lists.newArrayList(), constant("true"), //
								simpleIf(Lists.newArrayList(), constant("false"), //
										assign(ref(InliningUtil.RESULT_NAME + "[?] [?].m2()"), constant("6")),
										assign(ref(InliningUtil.RESULT_FLAG + "[?] [?].m2()"), constant("true"))), //
								simpleIf(Lists.newArrayList(), refExpr(InliningUtil.RESULT_FLAG + "[?] [?].m2()"), //
										declareVar("b"), //
										declareVar("c"), //
										assign(ref(InliningUtil.RESULT_NAME + "[?] [?].m2()"), constant("7")),
										assign(ref(InliningUtil.RESULT_FLAG + "[?] [?].m2()"), constant("true")))),
						assign(ref("a"), refExpr(InliningUtil.RESULT_NAME + "[?] [?].m2()"))));
		InliningContext context = new InliningContext();
		sst.accept(new InliningIStatementVisitor(), context);
		assertThat(context.getSST(), equalTo(inlinedSST));
	}

}
