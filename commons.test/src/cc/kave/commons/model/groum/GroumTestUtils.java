package cc.kave.commons.model.groum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.pattexplore.PattExplorer;
import static cc.kave.commons.model.groum.GroumBuilder.*;

public class GroumTestUtils {

	public static Groum createGroum(String... nodeIds) {
		Node[] nodes = createNodes(nodeIds);
		GroumBuilder builder = buildGroum(nodes);
		for (int i = 0; i < nodes.length - 1; i++) {
			builder.withEdge(nodes[i], nodes[i+1]);
		}
		return builder.build();
	}

	public static Node[] createNodes(String... nodeIds) {
		Node[] nodes = new Node[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			nodes[i] = createNode(nodeIds[i]);
		}
		return nodes;
	}

	public static TestNode createNode(String id) {
		return new TestNode(id);
	}

	public static Set<IGroum> findPatternsWithMinFrequency(int threshold,
			Groum... groums) {
		PattExplorer uut = new PattExplorer(threshold);
		return uut.explore(Arrays.asList(groums));
	}

	public static SubGroum createSubGroum(Groum parent, Node... nodes) {
		return new SubGroum(parent, new HashSet<>(Arrays.asList(nodes)));
	}
}
