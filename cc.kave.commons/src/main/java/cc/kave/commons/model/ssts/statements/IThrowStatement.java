package cc.kave.commons.model.ssts.statements;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IThrowStatement extends IStatement {
	@Nonnull
	IVariableReference getReference();

	boolean isReThrow();
}