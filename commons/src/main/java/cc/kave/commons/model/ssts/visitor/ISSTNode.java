package cc.kave.commons.model.ssts.visitor;

public interface ISSTNode {
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context);
}
