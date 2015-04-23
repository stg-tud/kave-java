package cc.kave.commons.model.ssts.declarations;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IStatement;

public interface IPropertyDeclaration extends IMemberDeclaration {

	@Nonnull
	PropertyName getName();

	@Nonnull
	Set<IStatement> getGet();

	@Nonnull
	Set<IStatement> getSet();
}
