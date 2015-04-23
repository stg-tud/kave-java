package cc.kave.commons.model.ssts.blocks;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IForEachLoop extends IStatement {

	@Nonnull
	IVariableDeclaration getDeclaration();

	@Nonnull
	IVariableReference LoopedReference();

	@Nonnull
	Set<IStatement> getBody();

}
