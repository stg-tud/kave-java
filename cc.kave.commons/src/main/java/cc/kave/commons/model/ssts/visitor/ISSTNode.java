package cc.kave.commons.model.ssts.visitor;

public interface ISSTNode {
	
	Iterable<ISSTNode> getChildren();
	
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context);
}
