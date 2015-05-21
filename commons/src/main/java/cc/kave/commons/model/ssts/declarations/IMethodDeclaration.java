package cc.kave.commons.model.ssts.declarations;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IStatement;

public interface IMethodDeclaration extends IMemberDeclaration {

	@Nonnull
	MethodName getName();

	boolean isEntryPoint();

	@Nonnull
	List<IStatement> getBody();

}
