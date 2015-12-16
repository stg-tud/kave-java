package cc.kave.commons.model.ssts.declarations;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IVariableDeclaration extends IStatement {

	@Nonnull
	IVariableReference getReference();

	@Nonnull
	ITypeName getType();

	boolean isMissing();

}
