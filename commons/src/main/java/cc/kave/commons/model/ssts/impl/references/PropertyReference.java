package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class PropertyReference implements IPropertyReference {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public IVariableReference getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyName getPropertyName() {
		// TODO Auto-generated method stub
		return null;
	}

}
