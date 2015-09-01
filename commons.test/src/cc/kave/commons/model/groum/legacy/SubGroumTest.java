package cc.kave.commons.model.groum.legacy;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.Node;
import cc.kave.commons.model.groum.SubGroum;

public class SubGroumTest {

	@Test
	public void extendsWithSingleNodeSingleEdge() {
		Groum parent = Fixture.createConnectedGroumOfSize(4);		
		SubGroum subgroum = new SubGroum(parent);	
		List<Node> parentnodes = new LinkedList<>();		
	}
	
	@Test
	public void extendsWithSingleNodeMultipleEdges() {
		
	}
	
	@Test
	public void extendsWithTwoNodesEachOneEdge() {
		
	}
	
	@Test
	public void extendsWithTwoNodesEachMultipleEdges() {
		
	}

}
