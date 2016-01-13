package cc.kave.commons.model.ssts.impl.declarations;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class FieldDeclaration implements IFieldDeclaration {

	private FieldName name;

	public FieldDeclaration() {
		this.name = CsFieldName.UNKNOWN_NAME;
	}

	@Override
	public FieldName getName() {
		return this.name;
	}

	public void setName(FieldName name) {
		this.name = name;
	}

	private boolean equals(FieldDeclaration other) {
		return this.name.equals(other.getName());
	}

	public boolean equals(Object obj) {
		return obj instanceof FieldDeclaration ? this.equals((FieldDeclaration) obj) : false;
	}

	public int hashCode() {
		return 21 + this.name.hashCode();
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
