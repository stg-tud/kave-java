package cc.kave.commons.model.groum.comparator;

import java.util.Comparator;

import cc.kave.commons.model.groum.SubGroum;

public class SubGroumIdentComparator implements Comparator<SubGroum> {

	@Override
	public int compare(SubGroum o1, SubGroum o2) {
		return Integer.compare(o1.hashCode(), o2.hashCode());
	}

}
