package cc.kave.commons.model.ssts.statements;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IStatement;

public interface IThrowStatement extends IStatement {

	@Nonnull
	TypeName getException();

}
