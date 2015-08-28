package cc.kave.commons.model.ssts.declarations;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IStatement;

public interface IMethodDeclaration extends IMemberDeclaration {

	@NonNull
	MethodName getName();

	boolean isEntryPoint();

	@NonNull
	List<IStatement> getBody();

}
