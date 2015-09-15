package cc.kave.commons.model.groum;

import java.util.Set;
import static cc.kave.commons.model.groum.GroumBuilder.*;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.hasItem;
import static cc.kave.commons.model.groum.GroumTestUtils.*;

public class SubGroumTest {

	@Test(expected = IllegalArgumentException.class)
	public void doesntAcceptForeignNode() {
		Groum groum = createGroum("A");

		createSubGroum(groum, new TestNode("B"));
	}

	@Test
	public void inheritsEdges() {
		Node[] nodes = createNodes("A", "B");
		Groum parent = buildGroum(nodes).withEdge(nodes[0], nodes[1]).build();

		IGroum uut = createSubGroum(parent, nodes[0], nodes[1]);

		Set<Node> successors = uut.getSuccessors(nodes[0]);
		assertThat(successors, hasItem(nodes[1]));
	}
}
