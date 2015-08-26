package cc.kave.commons.model.groum;

import java.util.List;

public interface ISubGroum extends IGroum {
	public IGroum getParent();

	public ISubGroum extensibleWith(ISubGroum groum);
	
	public List<ISubGroum> extensibleWithMultiple(ISubGroum groum);
}
