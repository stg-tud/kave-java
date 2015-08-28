package cc.kave.commons.model.ssts.references;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IReference;

public interface IMemberReference extends IReference {

	@NonNull
	IVariableReference getReference();

}
