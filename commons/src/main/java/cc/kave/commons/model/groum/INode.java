package cc.kave.commons.model.groum;

import java.util.Set;

public interface INode extends Comparable<INode> {

	// public boolean equals(INode anotherNode);

	public Set<String> getDataDependencies();

	public void addDependency(String dep);

	public boolean hasDataDependencyTo(INode anotherNode);

}
