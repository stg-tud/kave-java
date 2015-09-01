package cc.kave.commons.model.groum;

import java.util.Arrays;
import java.util.List;

import cc.kave.commons.model.pattexplore.PattExplorer;

public class GroumTestUtils {

	static Groum createGroum(INode... nodes) {
		Groum groum = new Groum();
		for (INode node : nodes) {
			groum.addNode(node);
		}
		return groum;
	}

	static List<SubGroum> findPatternsWithMinFrequency(int threshold, Groum... groums) {
		PattExplorer uut = new PattExplorer(threshold);
		return uut.explorePatterns(Arrays.asList(groums));
	}

}
