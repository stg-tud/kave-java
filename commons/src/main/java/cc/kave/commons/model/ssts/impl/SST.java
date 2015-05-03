package cc.kave.commons.model.ssts.impl;

import java.util.Set;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class SST implements ISST {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public TypeName getEnclosingType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IFieldDeclaration> getFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IPropertyDeclaration> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IMethodDeclaration> getMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IEventDeclaration> getEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IDelegateDeclaration> getDelegates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IMethodDeclaration> getEntryPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IMethodDeclaration> getNonEntryPoints() {
		// TODO Auto-generated method stub
		return null;
	}

}
