package cc.kave.commons.model.groum.impl;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.ISubGroum;

public class SubGroumTest {

	@Test
	public void extendsWithSingleNodeSingleEdge() {
		IGroum parent = Fixture.createConnectedGroumOfSize(4);		
		ISubGroum subgroum = new SubGroum(parent);	
		List<INode> parentnodes = new LinkedList<>();		
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
