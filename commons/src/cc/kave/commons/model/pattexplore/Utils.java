package cc.kave.commons.model.pattexplore;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.SubGroum;

public class Utils {

	public static List<SubGroum> breakdown(Groum groum) {
		List<SubGroum> subgroums = new LinkedList<>();
		for (INode node : groum.getAllNodes()) {
			SubGroum subgroum = new SubGroum(groum);
			subgroum.addVertex(node);
			subgroums.add(subgroum);
		}
		return subgroums;
	}
}
