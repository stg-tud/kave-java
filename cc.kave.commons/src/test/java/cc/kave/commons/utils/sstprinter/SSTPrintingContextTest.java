package cc.kave.commons.utils.sstprinter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;
import cc.kave.commons.utils.sstprinter.SSTPrintingVisitor;

public class SSTPrintingContextTest {

	private static void AssertTypeFormat(String expected, String typeIdentifier) {
		SSTPrintingContext sut = new SSTPrintingContext();
		Assert.assertEquals(expected, sut.type(CsTypeName.newTypeName(typeIdentifier)).toString());
	}

	@Test
	public void testTypeNameFormat() {
		AssertTypeFormat("T", "T,P");
	}

	@Test
	public void testTypeNameFormat_Generics() {
		AssertTypeFormat("EventHandler<EventArgsType>", "EventHandler`1[[T -> EventArgsType,P]],P");
	}

	@Test
	public void testTypeNameFormat_UnknownGenericType() {
		// these TypeNames are equivalent
		AssertTypeFormat("C<?>", "C`1[[T]],P");
		AssertTypeFormat("C<?>", "C`1[[T -> ?]],P");
		AssertTypeFormat("C<?>", "C`1[[T -> T]],P");
	}

	@Test
	public void testTypeNameFormat_UnknownToUnknownGenericType() {
		AssertTypeFormat("Task<?>", "Task`1[[TResult -> T]], mscorlib, 4.0.0.0");
	}

	@Test
	public void testTypeNameFormat_MultipleGenerics() {
		AssertTypeFormat("A<B, C>", "A`2[[T1 -> B,P],[T2 -> C,P]],P");
	}

	@Test
	public void testTypeNameFormat_NestedGenerics() {
		AssertTypeFormat("A<B<C>>", "A`1[[T -> B`1[[T -> C,P]],P]],P");
	}

	@Test
	public void testStatementBlock_NotEmpty_WithBrackets() {
		List<IStatement> stmts = new ArrayList<>();
		stmts.add(new ContinueStatement());
		stmts.add(new BreakStatement());
		SSTPrintingVisitor visitor = new SSTPrintingVisitor();
		SSTPrintingContext sut = new SSTPrintingContext();

		String expected = String.join("\n", "", "{", "    continue;", "    break;", "}");

		sut.statementBlock(stmts, visitor, true);
		Assert.assertEquals(expected, sut.toString());
	}

	@Test
	public void testStatementBlock_Empty_WithBrackets() {
		List<IStatement> stmts = new ArrayList<>();
		SSTPrintingVisitor visitor = new SSTPrintingVisitor();
		SSTPrintingContext sut = new SSTPrintingContext();

		sut.statementBlock(stmts, visitor, true);
		Assert.assertEquals(" { }", sut.toString());
	}

	@Test
	public void testStatementBlock_NotEmpty_WithoutBrackets() {
		List<IStatement> stmts = new ArrayList<>();
		stmts.add(new ContinueStatement());
		stmts.add(new BreakStatement());
		SSTPrintingVisitor visitor = new SSTPrintingVisitor();
		SSTPrintingContext sut = new SSTPrintingContext();

		String expected = String.join("\n", "", "    continue;", "    break;");

		sut.statementBlock(stmts, visitor, false);
		Assert.assertEquals(expected, sut.toString());
	}

	@Test
	public void testStatementBlock_Empty_WithoutBrackets() {
		List<IStatement> stmts = new ArrayList<>();
		SSTPrintingVisitor visitor = new SSTPrintingVisitor();
		SSTPrintingContext sut = new SSTPrintingContext();

		sut.statementBlock(stmts, visitor, false);
		Assert.assertEquals("", sut.toString());
	}

	@Test
	public void testParameterList_NoParameters() {
		List<ParameterName> parameters = new ArrayList<>();
		SSTPrintingContext sut = new SSTPrintingContext();
		sut.parameterList(parameters);
		Assert.assertEquals("()", sut.toString());
	}

	@Test
	public void testParameterList_OneParameter() {
		List<ParameterName> parameters = new ArrayList<>();
		parameters.add(CsParameterName.newParameterName("[A,P] p1"));
		SSTPrintingContext sut = new SSTPrintingContext();
		sut.parameterList(parameters);
		Assert.assertEquals("(A p1)", sut.toString());
	}

	@Test
	public void testParameterList_MultipleParameters() {
		List<ParameterName> parameters = new ArrayList<>();
		parameters.add(CsParameterName.newParameterName("[A,P] p1"));
		parameters.add(CsParameterName.newParameterName("[B,P] p2"));
		SSTPrintingContext sut = new SSTPrintingContext();
		sut.parameterList(parameters);
		Assert.assertEquals("(A p1, B p2)", sut.toString());
	}
}
