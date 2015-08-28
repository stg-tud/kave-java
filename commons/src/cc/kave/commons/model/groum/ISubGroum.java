package cc.kave.commons.model.groum;

import java.util.List;


public interface ISubGroum extends IGroum {
	public IGroum getParent();

	public List<ISubGroum> extensibleWith(ISubGroum groum);
	
}
