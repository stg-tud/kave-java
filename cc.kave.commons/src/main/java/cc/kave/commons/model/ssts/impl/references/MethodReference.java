package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class MethodReference implements IMethodReference {

	private IVariableReference reference;
	private IMethodName methodName;

	public MethodReference() {
		this.reference = new VariableReference();
		this.methodName = MethodName.UNKNOWN_NAME;
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public IMethodName getMethodName() {
		return this.methodName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodReference other = (MethodReference) obj;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

	public void setMethodName(IMethodName name) {
		this.methodName = name;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
