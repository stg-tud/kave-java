package cc.kave.commons.model.pattexplore;

import java.util.List;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;

public interface IPattExplorer {
	public List<ISubGroum> explorePatterns(List<IGroum> D);

}
