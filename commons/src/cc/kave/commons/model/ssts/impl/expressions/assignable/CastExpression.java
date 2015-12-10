package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class CastExpression implements ICastExpression {

	private TypeName targetType;
	private CastOperator operator;
	private IVariableReference reference;

	public CastExpression() {
		this.reference = new VariableReference();
		this.targetType = CsTypeName.UNKNOWN_NAME;
		this.operator = CastOperator.Unknown;
	}

	@Override
	public TypeName getTargetType() {
		return this.targetType;
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setTargetType(TypeName targetType) {
		this.targetType = targetType;
	}

	@Override
	public CastOperator getOperator() {
		return this.operator;
	}

	public void setOperator(CastOperator operator) {
		this.operator = operator;
	}

	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((targetType == null) ? 0 : targetType.hashCode());
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
		CastExpression other = (CastExpression) obj;
		if (operator != other.operator)
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (targetType == null) {
			if (other.targetType != null)
				return false;
		} else if (!targetType.equals(other.targetType))
			return false;
		return true;
	}

}
