package cc.kave.commons.model.ssts.impl.declarations;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class FieldDeclaration implements IFieldDeclaration {

	private IFieldName name;

	public FieldDeclaration() {
		this.name = FieldName.UNKNOWN_NAME;
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return Lists.newArrayList();
	}

	@Override
	public IFieldName getName() {
		return this.name;
	}

	public void setName(IFieldName name) {
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
