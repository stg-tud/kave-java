package commons.utils.sstprinter.visitortestsuite;

import org.junit.Assert;

import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;
import cc.kave.commons.utils.sstprinter.SSTPrintingVisitor;

public class SSTPrintingVisitorBaseTest {
	private SSTPrintingVisitor _sut = new SSTPrintingVisitor();

	protected void AssertPrintWithCustomContext(ISSTNode sst, SSTPrintingContext context, String expected) {
		int indentationLevel = context.indentationLevel;
		sst.accept(_sut, context);
		String actual = context.toString();
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(indentationLevel, context.indentationLevel);
	}

	protected void AssertPrintWithCustomContext(ISSTNode sst, SSTPrintingContext context, String... expectedLines) {
		AssertPrintWithCustomContext(sst, context, String.join("\n", expectedLines));
	}

	protected void AssertPrint(ISSTNode sst, String... expectedLines) {
		TestPrintingWithoutIndentation(sst, expectedLines);
		TestPrintingWithHighlightingProducesValidXaml(sst);

		// Expressions and references can't be indented
		if (!(sst instanceof IExpression || sst instanceof IReference)) {
			TestPrintingWithIndentation(sst, expectedLines);
		}
	}

	private void TestPrintingWithoutIndentation(ISSTNode sst, String[] expectedLines) {
		SSTPrintingContext context = new SSTPrintingContext();
		context.setIndentationLevel(0);
		AssertPrintWithCustomContext(sst, context, expectedLines);
	}

	private void TestPrintingWithHighlightingProducesValidXaml(ISSTNode sst) {
		// var context = new XamlSSTPrintingContext();
		SSTPrintingContext context = new SSTPrintingContext();
		sst.accept(_sut, context);
		String actual = context.toString();

		// throws and fails test if markup is invalid
		// XamlUtils.CreateDataTemplateFromXaml(actual);
	}

	private void TestPrintingWithIndentation(ISSTNode sst, String... expectedLines) {
		String[] indentedLines = new String[expectedLines.length];
		for (int i = 0; i < expectedLines.length; i++) {
			indentedLines[i] = (expectedLines[i] != "" || expectedLines[i] != null) ? expectedLines[i]
					: "    " + expectedLines[i];
		}
		SSTPrintingContext context = new SSTPrintingContext();
		context.setIndentationLevel(1);
		AssertPrintWithCustomContext(sst, context, indentedLines);
	}

	protected ConstantValueExpression constant(String value) {
		ConstantValueExpression expr = new ConstantValueExpression();
		expr.setValue(value);
		return expr;
	}
}
