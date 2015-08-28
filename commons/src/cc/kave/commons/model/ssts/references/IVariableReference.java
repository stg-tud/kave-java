package cc.kave.commons.model.ssts.references;

import org.eclipse.jdt.annotation.NonNull;

public interface IVariableReference extends IAssignableReference {

	@NonNull
	String getIdentifier();

	boolean isMissing();
}
