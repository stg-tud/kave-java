package cc.kave.commons.model.ssts.statements;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IThrowStatement extends IStatement {

	@NonNull
	TypeName getException();

    @Nonnull
	IVariableReference getReference();

	boolean isReThrow();
}
