package cc.kave.commons.model.ssts.visitor;

public interface ISSTNode {
	public <TContext, TReturn> TReturn Accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context);
}
