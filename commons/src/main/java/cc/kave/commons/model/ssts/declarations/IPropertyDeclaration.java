package cc.kave.commons.model.ssts.declarations;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IStatement;

public interface IPropertyDeclaration extends IMemberDeclaration {

	@NonNull
	PropertyName getName();

	@NonNull
	List<IStatement> getGet();

	@NonNull
	List<IStatement> getSet();
}
