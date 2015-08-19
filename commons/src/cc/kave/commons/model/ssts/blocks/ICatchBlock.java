package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.ssts.IStatement;

public interface ICatchBlock {

	@Nonnull
	ParameterName getParameter();

	@Nonnull
	List<IStatement> getBody();

	boolean isGeneral();

	boolean isUnnamed();

}
