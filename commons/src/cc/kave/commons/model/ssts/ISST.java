package cc.kave.commons.model.ssts;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public interface ISST extends ISSTNode {

	@NonNull
	TypeName getEnclosingType();

	@NonNull
	Set<IFieldDeclaration> getFields();

	@NonNull
	Set<IPropertyDeclaration> getProperties();

	@NonNull
	Set<IMethodDeclaration> getMethods();

	@NonNull
	Set<IEventDeclaration> getEvents();

	@NonNull
	Set<IDelegateDeclaration> getDelegates();

	@NonNull
	Set<IMethodDeclaration> getEntryPoints();

	@NonNull
	Set<IMethodDeclaration> getNonEntryPoints();

}
