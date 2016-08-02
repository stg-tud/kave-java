package exec.recommender_reimplementation.java_printer;

import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.typeshapes.ITypeShape;
import exec.recommender_reimplementation.java_printer.javaPrinterTestSuite.JavaPrintingVisitorBaseTest;

public class PhantomClassVisitorBaseTest extends JavaPrintingVisitorBaseTest{
	
	PhantomClassVisitor sut;

	@Override
	protected void assertPrintWithCustomContext(ISSTNode sst, ITypeShape typeShape, String... expectedLines) {
		assertPrintWithCustomContext(sst, typeShape, String.join("\n", expectedLines));
	}

	@Override
	protected void assertPrintWithCustomContext(ISSTNode sst, JavaPrintingContext context, String... expectedLines) {
		assertPrintWithCustomContext(sst, context, String.join("\n", expectedLines));
	}
}
