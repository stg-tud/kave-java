package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.ssts.IStatement;

public interface ICatchBlock {

	CatchBlockKind getKind();

	@Nonnull
	IParameterName getParameter();

	@Nonnull
	List<IStatement> getBody();

}
