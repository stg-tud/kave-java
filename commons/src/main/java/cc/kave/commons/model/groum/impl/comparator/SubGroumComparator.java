package cc.kave.commons.model.groum.impl.comparator;

import java.util.Comparator;

import cc.kave.commons.model.groum.ISubGroum;

public class SubGroumComparator implements Comparator<ISubGroum> {

	@Override
	public int compare(ISubGroum o1, ISubGroum o2) {
		if (o2 == null)
			return 1;
		else if (o1.equals(o2))
			return 0;
		else
			return o1.toString().compareTo(o2.toString());
	}

}
