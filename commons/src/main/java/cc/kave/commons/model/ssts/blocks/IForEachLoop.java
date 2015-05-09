package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IForEachLoop extends IStatement {

	@Nonnull
	IVariableDeclaration getDeclaration();

	@Nonnull
	IVariableReference getLoopedReference();

	@Nonnull
	List<IStatement> getBody();

}
