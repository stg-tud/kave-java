package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class MethodReference implements IMethodReference {

	@Override
	public IVariableReference getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public MethodName getMethodName() {
		// TODO Auto-generated method stub
		return null;
	}

}
