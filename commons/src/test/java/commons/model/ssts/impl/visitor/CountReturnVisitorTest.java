package commons.model.ssts.impl.visitor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.CountReturnsVisitor;

public class CountReturnVisitorTest extends InliningBaseTest {

	@Test
	public void testReturnStatementCount() {
		IMethodDeclaration method = declareEntryPoint("m1", //
				declareVar("a"), //
				forLoop("i", loopHeader(expr(constant("true"))), //
						simpleIf(Lists.newArrayList(returnStatement(constant("5"), false)), constant("true"),
								returnStatement(constant("5"), false))),
				simpleIf(Lists.newArrayList(returnStatement(constant("6"), false)), constant("true"),
						returnStatement(constant("7"), true)));
		assertThat(4, equalTo(method.accept(new CountReturnsVisitor(), null)));
	}
}
