package commons.model.ssts.impl.visitor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.visitor.SSTBuilder;

public class InliningVisitorTest extends InliningBaseTest {

	@Test
	public void testRemoveMethod() {
		ISST sst = buildSST(declareMethod("m2", false), declareMethod("m1", true, invocationStatement("m2")));
		ISST inlinedSST = buildSST(declareMethod("m1", true));
		assertThat(SSTBuilder.inlineSST(sst), equalTo(inlinedSST));
	}

	@Test
	public void testNameInlining() {
		ISST sst = buildSST( //
				declareMethod("m2", false, //
						declareVar("a"), //
						declareVar("b")),
				declareMethod("m1", true, //
						declareVar("a"), //
						declareVar("b"), //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareMethod("m1", true, //
						declareVar("a"), //
						declareVar("b"), //
						declareVar("$1_a"), //
						declareVar("$0_b")));
		ISST newSST = SSTBuilder.inlineSST(sst);
		assertThat(newSST, equalTo(inlinedSST));
	}

	@Test
	public void testBasicInline() {
		ISST sst = buildSST( //
				declareMethod("m2", false, //
						declareVar("a"), //
						declareVar("b"), //
						assign(ref("a"), refExpr("b"))),
				declareMethod("m1", true, //
						declareVar("a"), //
						declareVar("b"), //
						invocationStatement("m2")));
		ISST defaultSST = buildSST( //
				declareMethod("m2", false, //
						declareVar("a"), //
						declareVar("b"), //
						assign(ref("a"), refExpr("b"))),
				declareMethod("m1", true, //
						declareVar("a"), //
						declareVar("b"), //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST( //
				declareMethod("m1", true, //
						declareVar("a"), //
						declareVar("b"), //
						declareVar("$1_a"), //
						declareVar("$0_b"), //
						assign(ref("$1_a"), refExpr("$0_b")))); //
		assertThat(SSTBuilder.inlineSST(sst), equalTo(inlinedSST));
		assertThat(sst, equalTo(defaultSST));
	}

	@Test
	public void testBasicInline1() {
		ISST sst = buildSST( //
				declareMethod("m2", false, //
						returnStatement(constant("5"), false)), //
				declareMethod("m1", true, //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						assign(ref("b"), invocationExpr("m2", ref("b")))));
		ISST inlinedSST = buildSST( //
				declareMethod("m1", true, //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						assign(ref("b"), constant("5"))));
		assertThat(SSTBuilder.inlineSST(sst), equalTo(inlinedSST));
	}

	@Test
	public void testBasicInline2() {
		ISST sst = buildSST( //
				declareMethod("m2", false, //
						declareVar("c"), //
						assign(ref("c"), constant("1")), //
						declareVar("d"), //
						assign(ref("d"), constant("3")), //
						assign(ref("d"), compose(ref("c"), ref("d"))), //
						returnStatement(refExpr("d"), false)),
				declareMethod("m1", true, //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						assign(ref("b"), invocationExpr("m2", ref("b")))));
		ISST inlinedSST = buildSST( //
				declareMethod("m1", true, //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						declareVar("c"), //
						assign(ref("c"), constant("1")), //
						declareVar("d"), //
						assign(ref("d"), constant("3")), //
						assign(ref("d"), compose(ref("c"), ref("d"))), //
						assign(ref("b"), refExpr("d"))));
		ISST newSST = SSTBuilder.inlineSST(sst);
		assertThat(newSST, equalTo(inlinedSST));
	}

	@Test
	public void testSecondEntryPoint() {
		ISST sst = buildSST(
				declareMethod("m2", true, //
						declareVar("c"), //
						assign(ref("c"), constant("1")), //
						declareVar("d"), //
						assign(ref("d"), constant("1"))),
				declareMethod("m1", true, //
						declareVar("a"), //
						assign(ref("a"), constant("1")), //
						declareVar("b"), //
						assign(ref("b"), constant("1")), //
						invocationStatement("m2")));
		assertThat(SSTBuilder.inlineSST(sst), equalTo(sst));
	}

	@Test
	public void testForLoopInline() {
		ISST sst = buildSST( //
				declareMethod("m2", false, //
						declareVar("a"), //
						forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"))),
				declareMethod("m1", true, //
						declareVar("b"), //
						invocationStatement("m2")));
		ISST inlinedSST = buildSST(declareMethod("m1", true, //
				declareVar("b"), //
				declareVar("a"), //
				forLoop("i", loopHeader(expr(constant("true"))), declareVar("d"))));
		ISST newSST = SSTBuilder.inlineSST(sst);
		assertThat(newSST, equalTo(inlinedSST));
	}

	@Test
	public void testInlineIntoForLoop() {
		ISST sst = buildSST( //
				declareMethod("m2", false, //
						declareVar("a"), //
						declareVar("i")), //
				declareMethod("m1", true, //
						declareVar("a"), //
						forLoop("i", loopHeader(expr(constant("true"))), //
								invocationStatement("m2"))));
		ISST inlinedSST = buildSST( //
				declareMethod("m1", true, //
						declareVar("a"), //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("$0_a"), //
								declareVar("$1_i"))));
		ISST newSST = SSTBuilder.inlineSST(sst);
		assertThat(newSST, equalTo(inlinedSST));
	}

	@Test
	public void testInlineAfterLoop() {
		ISST sst = buildSST( //
				declareMethod("m2", false, // ,
						declareVar("i")), //
				declareMethod("m1", true, //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("a")),
						invocationStatement("m2")));
		ISST inlinedSST = buildSST(//
				declareMethod("m1", true, //
						forLoop("i", loopHeader(expr(constant("true"))), //
								declareVar("a")), //
						declareVar("i")));
		ISST newSST = SSTBuilder.inlineSST(sst);
		assertThat(newSST, equalTo(inlinedSST));
	}

}
