package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;

public interface ICatchBlock {

	@Nonnull
	IVariableDeclaration getException();

	@Nonnull
	List<IStatement> getBody();

}
