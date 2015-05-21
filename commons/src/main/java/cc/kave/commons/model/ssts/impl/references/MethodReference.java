package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class MethodReference implements IMethodReference {

	private IVariableReference reference;
	private MethodName methodName;

	public MethodReference() {
		this.reference = new VariableReference();
		this.methodName = CsMethodName.UNKNOWN_NAME;
	}

	@Override
	public IVariableReference getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodName getMethodName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
