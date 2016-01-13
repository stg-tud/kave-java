package cc.kave.commons.model.ssts.references;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.MethodName;

public interface IMethodReference extends IMemberReference {

	@NonNull
	MethodName getMethodName();

}
