package cc.kave.commons.model.groum;

import java.util.List;

public interface ISubgraphStrategy {
	List<ISubGroum> getIsomorphSubgraphs(IGroum groum, IGroum subgroum);
}
