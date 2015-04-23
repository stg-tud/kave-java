package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.MethodName;

public interface IMethodReference extends IMemberReference {

	@Nonnull
	MethodName getMethodName();

}
