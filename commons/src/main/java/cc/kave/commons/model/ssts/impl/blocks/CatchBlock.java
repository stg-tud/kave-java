package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.VariableDeclaration;

public class CatchBlock implements ICatchBlock {

	private IVariableDeclaration exception;
	private List<IStatement> body;

	public CatchBlock() {
		this.exception = new VariableDeclaration();
		this.body = new ArrayList<IStatement>();
	}

	@Override
	public IVariableDeclaration getException() {
		return this.exception;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setException(IVariableDeclaration exception) {
		this.exception = exception;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	private boolean equals(CatchBlock other) {
		return this.body.equals(other.getBody()) && this.exception.equals(other.getException());
	}

	public boolean equals(Object obj) {
		return obj instanceof CatchBlock ? this.equals((CatchBlock) obj) : false;
	}

	public int hashCode() {
		return 31 + (this.body.hashCode() * 397) ^ this.exception.hashCode();
	}

}
