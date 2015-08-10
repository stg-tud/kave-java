package cc.kave.commons.model.groum;

public interface ISubGroum extends IGroum {
	public IGroum getParent();

	public ISubGroum extensibleWith(ISubGroum groum);
}
