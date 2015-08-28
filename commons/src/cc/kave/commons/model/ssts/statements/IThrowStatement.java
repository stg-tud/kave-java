package cc.kave.commons.model.ssts.statements;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IStatement;

public interface IThrowStatement extends IStatement {

	@NonNull
	TypeName getException();

}
