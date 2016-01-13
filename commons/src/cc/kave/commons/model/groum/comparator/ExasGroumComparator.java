package cc.kave.commons.model.groum.comparator;

import java.util.Comparator;

import cc.kave.commons.model.groum.IGroum;

public class ExasGroumComparator implements Comparator<IGroum> {

	private ExasVectorBuilder exasVectorBuilder = new ExasVectorBuilder();

	@Override
	public int compare(IGroum o1, IGroum o2) {
		return exasVectorBuilder.build(o1).compareTo(exasVectorBuilder.build(o2));
	}

}
