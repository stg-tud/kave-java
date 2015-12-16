package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.IMethodName;

public interface IMethodReference extends IMemberReference {

	@Nonnull
	IMethodName getMethodName();

}
