package cc.kave.commons.model.ssts;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public interface ISST extends ISSTNode {

	@Nonnull
	TypeName getEnclosingType();

	@Nonnull
	Set<IFieldDeclaration> getFields();

	@Nonnull
	Set<IPropertyDeclaration> getProperties();

	@Nonnull
	Set<IMethodDeclaration> getMethods();

	@Nonnull
	Set<IEventDeclaration> getEvents();

	@Nonnull
	Set<IDelegateDeclaration> getDelegates();

	@Nonnull
	Set<IMethodDeclaration> getEntryPoints();

	@Nonnull
	Set<IMethodDeclaration> getNonEntryPoints();

	@Nonnull
	String getPartialClassIdentifier();

	boolean isPartialClass();
}
