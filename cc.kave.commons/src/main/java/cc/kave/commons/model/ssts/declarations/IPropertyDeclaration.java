package cc.kave.commons.model.ssts.declarations;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IStatement;

public interface IPropertyDeclaration extends IMemberDeclaration {

	@Nonnull
	PropertyName getName();

	@Nonnull
	List<IStatement> getGet();

	@Nonnull
	List<IStatement> getSet();
}
