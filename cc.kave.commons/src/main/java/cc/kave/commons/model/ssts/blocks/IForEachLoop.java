package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IForEachLoop extends IStatement {

	@NonNull
	IVariableDeclaration getDeclaration();

	@NonNull
	IVariableReference getLoopedReference();

	@NonNull
	List<IStatement> getBody();

}
