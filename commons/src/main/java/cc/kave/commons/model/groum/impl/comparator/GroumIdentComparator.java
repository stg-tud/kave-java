package cc.kave.commons.model.groum.impl.comparator;

import java.util.Comparator;

import cc.kave.commons.model.groum.IGroum;

public class GroumIdentComparator implements Comparator<IGroum> {

	@Override
	public int compare(IGroum o1, IGroum o2) {
		return Integer.compare(o1.hashCode(), o2.hashCode());
	}

}
