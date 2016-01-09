package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class InvocationExpression implements IInvocationExpression {

	private IVariableReference reference;
	private IMethodName methodName;
	private List<ISimpleExpression> parameters;

	public InvocationExpression() {
		this.reference = new VariableReference();
		this.methodName = MethodName.UNKNOWN_NAME;
		this.parameters = new ArrayList<>();
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
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
	public List<ISimpleExpression> getParameters() {
		return this.parameters;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setMethodName(IMethodName methodName) {
		this.methodName = methodName;
	}

	public void setParameters(List<ISimpleExpression> parameters) {
		this.parameters = parameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
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
		InvocationExpression other = (InvocationExpression) obj;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
