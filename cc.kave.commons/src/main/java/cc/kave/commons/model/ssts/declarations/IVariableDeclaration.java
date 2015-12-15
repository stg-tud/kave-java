package cc.kave.commons.model.ssts.declarations;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IVariableDeclaration extends IStatement {

	@NonNull
	IVariableReference getReference();

	@NonNull
	TypeName getType();

	boolean isMissing();

}
