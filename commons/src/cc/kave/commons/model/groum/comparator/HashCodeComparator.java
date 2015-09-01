package cc.kave.commons.model.groum.comparator;

import java.util.Comparator;

public class HashCodeComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		return Integer.compare(o1.hashCode(), o2.hashCode());
	}

}
