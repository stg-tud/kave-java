package cc.kave.commons.model.groum.impl.comparator;

import java.util.Comparator;

import cc.kave.commons.model.groum.ISubGroum;

public class SubGroumIdentComparator implements Comparator<ISubGroum> {

	@Override
	public int compare(ISubGroum o1, ISubGroum o2) {
		return Integer.compare(o1.hashCode(), o2.hashCode());
	}

}
