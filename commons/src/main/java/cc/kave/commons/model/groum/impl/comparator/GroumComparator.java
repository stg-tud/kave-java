package cc.kave.commons.model.groum.impl.comparator;

import java.util.Comparator;

import cc.kave.commons.model.groum.IGroum;

public class GroumComparator implements Comparator<IGroum> {

	@Override
	public int compare(IGroum o1, IGroum o2) {
		if (o2 == null)
			return 1;
		else if (o1.equals(o2))
			return 0;
		else
			return o1.toString().compareTo(o2.toString());
	}

}
