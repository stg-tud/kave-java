package cc.kave.commons.model.groum.comparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.Node;
import cc.kave.commons.model.groum.SubGroum;

public class SubGroumComparator implements Comparator<SubGroum> {

	public int compareOld(SubGroum o1, SubGroum o2) {
		if (o2 == null)
			return 1;
		else if (o1.equals(o2))
			return 0;
		else {
			int nodes = Integer.compare(o1.getNodeCount(), o2.getNodeCount());
			if (nodes != 0)
				return nodes;
			int edges = Integer.compare(o1.getEdgeCount(), o2.getEdgeCount());
			if (edges != 0)
				return edges;
			return o1.toString().compareTo(o2.toString());
		}
	}

	@Override
	public int compare(SubGroum o1, SubGroum o2) {

		if (o2 == null)
			return 1;

		int nodes = Integer.compare(o1.getNodeCount(), o2.getNodeCount());
		if (nodes != 0)
			return nodes;

		int edges = Integer.compare(o1.getEdgeCount(), o2.getEdgeCount());
		if (edges != 0)
			return edges;

		List<Node> myNodes = new LinkedList<>();
		myNodes.addAll(o1.getAllNodes());
		List<Node> otherNodes = new LinkedList<>();
		otherNodes.addAll(o2.getAllNodes());

		Collections.sort(myNodes);
		Collections.sort(otherNodes);

		for (int i = 0; i < myNodes.size(); i++) {
			if (!(myNodes.get(i).equals(otherNodes.get(i)))) {
				return myNodes.get(i).compareTo(otherNodes.get(i));
			}

		}

		for (int i = 0; i < myNodes.size(); i++) {
			List<Node> mysuccessors = new LinkedList<>();
			List<Node> othersuccessors = new LinkedList<>();
			mysuccessors.addAll(o1.getSuccessors(myNodes.get(i)));
			othersuccessors.addAll(o2.getSuccessors(otherNodes.get(i)));

			if (mysuccessors.size() != othersuccessors.size())
				return Integer.compare(mysuccessors.size(), othersuccessors.size());

			Collections.sort(mysuccessors);
			Collections.sort(othersuccessors);

			for (int x = 0; x < mysuccessors.size(); x++) {
				if (!(mysuccessors.get(x).equals(othersuccessors.get(x))))
					return mysuccessors.get(x).compareTo(othersuccessors.get(x));
			}
		}
		return 0;
	}

}
