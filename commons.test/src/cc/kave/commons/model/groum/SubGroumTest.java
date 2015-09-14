package cc.kave.commons.model.groum;

import org.junit.Test;

import static cc.kave.commons.model.groum.GroumTestUtils.*;
public class SubGroumTest {

	@Test(expected=IllegalArgumentException.class)
	public void doesntAcceptForeignNode() {
		Groum groum = createGroum("A");
		
		SubGroum uut = new SubGroum(groum);
		uut.addNode(createNodes("B")[0]);
	}
}
