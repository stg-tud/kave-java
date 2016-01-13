package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;

public interface IUncheckedBlock extends IStatement {

	@NonNull
	List<IStatement> getBody();

}
