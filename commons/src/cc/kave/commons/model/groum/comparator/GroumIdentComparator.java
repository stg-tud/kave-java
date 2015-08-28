package cc.kave.commons.model.groum.comparator;

import java.util.Comparator;

import cc.kave.commons.model.groum.Groum;

public class GroumIdentComparator implements Comparator<Groum> {

	@Override
	public int compare(Groum o1, Groum o2) {
		return Integer.compare(o1.hashCode(), o2.hashCode());
	}

}
