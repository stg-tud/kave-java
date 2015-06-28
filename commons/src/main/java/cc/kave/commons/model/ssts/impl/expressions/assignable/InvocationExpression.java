package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class InvocationExpression implements IInvocationExpression {

	private IVariableReference reference;
	private MethodName methodName;
	private List<ISimpleExpression> parameters;

	public InvocationExpression() {
		this.reference = new VariableReference();
		this.methodName = CsMethodName.UNKNOWN_NAME;
		this.parameters = new ArrayList<>();
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public MethodName getMethodName() {
		return this.methodName;
	}

	@Override
	public List<ISimpleExpression> getParameters() {
		return this.parameters;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setMethodName(MethodName methodName) {
		this.methodName = methodName;
	}

	public void setParameters(List<ISimpleExpression> parameters) {
		this.parameters = parameters;
	}

	@Override
	public int hashCode() {
		int hashCode = 11 + this.reference.hashCode();
		hashCode = (hashCode * 397) ^ this.methodName.hashCode();
		hashCode = (hashCode * 397) ^ this.parameters.hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InvocationExpression))
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
