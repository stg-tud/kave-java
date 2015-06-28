package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ForEachLoop implements IForEachLoop {

	private IVariableDeclaration declaration;
	private IVariableReference loopedReference;
	private List<IStatement> body;

	public ForEachLoop() {
		this.declaration = new VariableDeclaration();
		this.loopedReference = new VariableReference();
		this.body = new ArrayList<>();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((declaration == null) ? 0 : declaration.hashCode());
		result = prime * result + ((loopedReference == null) ? 0 : loopedReference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ForEachLoop))
			return false;
		ForEachLoop other = (ForEachLoop) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (declaration == null) {
			if (other.declaration != null)
				return false;
		} else if (!declaration.equals(other.declaration))
			return false;
		if (loopedReference == null) {
			if (other.loopedReference != null)
				return false;
		} else if (!loopedReference.equals(other.loopedReference))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
