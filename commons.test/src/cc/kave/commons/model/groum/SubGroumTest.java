package cc.kave.commons.model.groum;

import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.hasItem;
import static cc.kave.commons.model.groum.GroumTestUtils.*;
public class SubGroumTest {

	@Test(expected=IllegalArgumentException.class)
	public void doesntAcceptForeignNode() {
		Groum groum = createGroum("A");
		
		SubGroum uut = new SubGroum(groum);
		uut.addNode(createNodes("B")[0]);
	}
	
	@Test
	public void inheritsEdges() {
		Node[] nodes = createNodes("A", "B");
		Groum parent = createGroum(nodes);
		parent.addEdge(nodes[0], nodes[1]);
		
		SubGroum uut = new SubGroum(parent);
		uut.addNode(nodes[0]);
		uut.addNode(nodes[1]);
		
		Set<Node> successors = uut.getSuccessors(nodes[0]);
		assertThat(successors, hasItem(nodes[1]));
	}
}
