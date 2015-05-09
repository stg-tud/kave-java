package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.VariableDeclaration;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ForEachLoop implements IForEachLoop {

	private IVariableDeclaration declaration;
	private IVariableReference loopedReference;
	private List<IStatement> body;

	public ForEachLoop() {
		this.declaration = new VariableDeclaration();
		this.loopedReference = new VariableReference();
		this.body = new ArrayList<IStatement>();
	}

	@Override
	public IVariableDeclaration getDeclaration() {
		return this.declaration;
	}

	@Override
	public IVariableReference getLoopedReference() {
		return this.loopedReference;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setDeclaration(IVariableDeclaration declaration) {
		this.declaration = declaration;
	}

	public void setLoopedReference(IVariableReference loopedReference) {
		this.loopedReference = loopedReference;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	private boolean equals(ForEachLoop other) {
		return this.body.equals(other.getBody()) && this.declaration.equals(other.getDeclaration())
				&& this.loopedReference.equals(other.getLoopedReference());
	}

	public boolean equals(Object obj) {
		return obj instanceof ForEachLoop ? this.equals((ForEachLoop) obj) : false;
	}

	public int hashCode() {
		int hashCode = 33 + this.body.hashCode();
		hashCode = (hashCode * 397) ^ this.declaration.hashCode();
		hashCode = (hashCode * 397) ^ this.loopedReference.hashCode();
		return hashCode;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
