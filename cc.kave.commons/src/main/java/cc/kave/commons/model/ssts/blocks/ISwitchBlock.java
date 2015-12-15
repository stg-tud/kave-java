package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface ISwitchBlock extends IStatement {

	@NonNull
	IVariableReference getReference();

	@NonNull
	List<ICaseBlock> getSections();

	@NonNull
	List<IStatement> getDefaultSection();

}
