package cc.kave.commons.model.pattexplore;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.SubGroum;

public class Utils {

	public static List<ISubGroum> breakdown(IGroum groum) {
		List<ISubGroum> subgroums = new LinkedList<>();
		for (INode node : groum.getAllNodes()) {
			ISubGroum subgroum = new SubGroum(groum);
			subgroum.addVertex(node);
			subgroums.add(subgroum);
		}
		return subgroums;
	}

	public static List<ISubGroum> removeEqualPatterns(ISubGroum reference, List<ISubGroum> list) {
		List<ISubGroum> removables = new LinkedList<>();
		for (ISubGroum groum : list) {
			if (groum.equals(reference))
				removables.add(groum);
		}
		list.removeAll(removables);
		return list;

	}

	public static int getFrequency(ISubGroum subgroum, List<ISubGroum> list) {
		int frequency = 0;
		for (ISubGroum aGroum : list) {
			if (aGroum.equals(subgroum))
				frequency++;
		}
		return frequency;
	}

	public static List<List<ISubGroum>> makeIsomorphGroups(List<ISubGroum> groumlist) {
		List<List<ISubGroum>> isomorphGroups = new LinkedList<>();
		Collections.sort(groumlist);
		List<ISubGroum> isomorphGroup = null;

		for (ISubGroum subgroum : groumlist) {
			if ((isomorphGroups.size() == 0) || (!(isomorphGroup.get(0).equals(subgroum)))) {
				isomorphGroup = new LinkedList<>();
				isomorphGroups.add(isomorphGroup);
			}
			isomorphGroup.add(subgroum);
		}

		// for (ISubGroum subgroum : groumlist) {
		// if (isomorphGroup == null) {
		// isomorphGroup = new LinkedList<>();
		// isomorphGroup.add(subgroum);
		// } else {
		// if (isomorphGroup.get(0).equals(subgroum)) {
		// isomorphGroup.add(subgroum);
		// } else {
		// isomorphGroups.add(isomorphGroup);
		// isomorphGroup = new LinkedList<>();
		// isomorphGroup.add(subgroum);
		// }
		// }
		// }

		return isomorphGroups;
	}
}
