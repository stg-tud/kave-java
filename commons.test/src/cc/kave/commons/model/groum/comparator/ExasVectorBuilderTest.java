package cc.kave.commons.model.groum.comparator;

import org.junit.Test;

import static org.junit.Assert.*;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.Node;
import static cc.kave.commons.model.groum.GroumTestUtils.*;
import static cc.kave.commons.model.groum.GroumBuilder.*;

public class ExasVectorBuilderTest {

	@Test
	public void buildsVector() {
		Node[] nodes = createNodes("A", "B", "C");
		Groum groum = buildGroum(nodes).withEdge(nodes[0], nodes[1]).withEdge(nodes[1], nodes[2]).build();
		
		ExasVector vector = new ExasVectorBuilder().build(groum);
		
		assertFalse(vector.getNPaths().isEmpty());
		assertFalse(vector.getPQNodes().isEmpty());
	}
}
