package cc.kave.commons.model.groum;

import java.util.Arrays;
import java.util.List;

import cc.kave.commons.model.pattexplore.PattExplorer;

public class GroumTestUtils {

	public static Groum createGroum(Node... nodes) {
		Groum groum = new Groum();
		for (Node node : nodes) {
			groum.addNode(node);
		}
		return groum;
	}
	
	public static Groum createGroum(String... nodeIds) {
		return createGroum(createNodes(nodeIds));
	}
	
	public static Node[] createNodes(String... nodeIds) {
		Node[] nodes = new Node[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			nodes[i] = new TestNode(nodeIds[i]);
		}
		return nodes;
	}

	public static List<SubGroum> findPatternsWithMinFrequency(int threshold, Groum... groums) {
		PattExplorer uut = new PattExplorer(threshold);
		return uut.explorePatterns(Arrays.asList(groums));
	}

}
