package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsUnknownTypeName;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ThrowStatement implements IThrowStatement {

	private TypeName exception;

	// TODO: Check refactoring
	public ThrowStatement() {
		this.exception = CsUnknownTypeName.UNKNOWN_NAME;
	}

	@Override
	public TypeName getException() {
		return this.exception;
	}

	public void setException(TypeName exception) {
		this.exception = exception;
	}

	@Override
	public int hashCode() {
		return 18 + this.exception.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ThrowStatement))
			return false;
		ThrowStatement other = (ThrowStatement) obj;
		if (exception == null) {
			if (other.exception != null)
				return false;
		} else if (!exception.equals(other.exception))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
