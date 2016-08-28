package cc.kave.commons.utils;

import static cc.kave.commons.utils.TypeErasure.ErasureStrategy.FULL;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;

public class CompleteTypeErasureTest {
	@Test
	public void noGenerics() {
		String inp = "T,P";
		String out = "T,P";
		assertRepl(inp, out);
	}

	@Test
	public void generic_unbound() {
		String inp = "T`1[[G]],P";
		String out = "T,P";
		assertRepl(inp, out);
	}

	@Test
	public void generic_bound() {
		String inp = "T`1[[G -> T2,P]],P";
		String out = "T,P";
		assertRepl(inp, out);
	}

	@Test
	public void generic_multiple_unbound() {
		String inp = "T`2[[G],[G2]],P";
		String out = "T,P";
		assertRepl(inp, out);
	}

	@Test
	public void generic_multiple_bound() {
		String inp = "T`2[[G -> T2,P],[G2 -> T3,P]],P";
		String out = "T,P";
		assertRepl(inp, out);
	}

	@Test
	public void array_bound() {
		String inp = "T`1[][[G -> T2,P]],P";
		String out = "T[],P";
		assertRepl(inp, out);
	}

	@Test
	public void nestedGenercis() {
		String inp = "T`1[[G1 -> T`1[[G2-> T2]],P]],P";
		String out = "T,P";
		assertRepl(inp, out);
	}

	@Test
	public void strangeNestingSyntax_unbound() {
		String inp = "T`1+U`1[[G1],[G2]],P";
		String out = "T+U,P";
		assertRepl(inp, out);
	}

	@Test
	public void strangeNestingSyntax_bound() {
		String inp = "T`1+U`1[[G1 -> T2,P],[G2 -> T3,P]],P";
		String out = "T+U,P";
		assertRepl(inp, out);
	}

	@Test
	public void method_happyPjgfath() {
		String inp = "[T,P] [T,P].M`2[[G1],[G2 -> T,P]]()";
		String out = "[T,P] [T,P].M()";
		assertRepl(inp, out);
	}

	@Test
	public void repeatingTheSameReplacement() {
		String inp = "[T`1[[G->T,P]],P] [T`1[[G->T,P]],P].M()";
		String out = "[T,P] [T,P].M()";
		assertRepl(inp, out);
	}
	
	@Test
	public void type_happyPath() {
		ITypeName inp = Names.newType("T`1[[G->T2,P]],P");
		ITypeName exp = Names.newType("T,P");
		ITypeName act = TypeErasure.of(inp, FULL);
		assertEquals(exp, act);
	}

	@Test
	public void method_happyPath() {
		IMethodName inp = Names.newMethod("[T,P] [T,P].M`1[[G1 -> T,P]]()");
		IMethodName exp = Names.newMethod("[T,P] [T,P].M()");
		IMethodName act = TypeErasure.of(inp, FULL);
		assertEquals(exp, act);
	}

	private void assertRepl(String in, String expected) {
		String actual = TypeErasure.of(in, FULL);
		assertEquals(expected, actual);
	}
}
