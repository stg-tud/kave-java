/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.pointsto.LanguageOptions;
import cc.kave.commons.pointsto.SSTBuilder;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.Callpath;
import cc.kave.commons.pointsto.analysis.PointerAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.QueryContextKey;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.pointsto.dummies.DummyCallsite;
import cc.kave.commons.pointsto.dummies.DummyDefinitionSite;
import cc.kave.commons.pointsto.dummies.DummyUsage;
import cc.recommenders.usages.DefinitionSiteKind;

public class UsageExtractionVisitorContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(UsageExtractionVisitorContext.class);

	private DefinitionSitePriorityComparator definitionSiteComparator = new DefinitionSitePriorityComparator();

	private PointerAnalysis pointerAnalysis;
	private DescentStrategy descentStrategy;
	private TypeCollector typeCollector;
	private DeclarationMapper declarationMapper;

	private ITypeName enclosingClass;

	private Map<AbstractLocation, DummyUsage> locationUsages = new HashMap<>();

	private Map<AbstractLocation, DummyDefinitionSite> implicitDefinitions = new HashMap<>();

	private IStatement currentStatement;
	private Callpath currentCallpath;

	public UsageExtractionVisitorContext(PointsToContext context, DescentStrategy descentStrategy) {
		this.pointerAnalysis = context.getPointerAnalysis();
		this.descentStrategy = descentStrategy;
		this.enclosingClass = context.getTypeShape().getTypeHierarchy().getElement();
		this.typeCollector = new TypeCollector(context);
		this.declarationMapper = new DeclarationMapper(context);

		this.currentStatement = null;
		this.currentCallpath = null;
		createImplicitDefinitions(context);
	}

	public List<DummyUsage> getUsages() {
		return new ArrayList<>(locationUsages.values());
	}

	public void setEntryPoint(IMethodName method) {
		currentCallpath = new Callpath(method);
		currentStatement = null;

		// reset usages
		locationUsages.clear();
	}

	public void setCurrentStatement(IStatement stmt) {
		this.currentStatement = stmt;
	}

	public void enterNonEntryPoint(IMethodName method) {
		currentCallpath.enterMethod(method);
	}

	public void leaveNonEntryPoint() {
		currentCallpath.leaveMethod();
	}

	private Set<AbstractLocation> queryPointerAnalysis(IReference reference, ITypeName type) {
		QueryContextKey query = new QueryContextKey(reference, currentStatement, type, currentCallpath);
		return pointerAnalysis.query(query);
	}

	private void createImplicitDefinitions(Context context) {
		LanguageOptions languageOptions = LanguageOptions.getInstance();

		// this
		DummyDefinitionSite thisDefinition = DummyDefinitionSite.byThis();
		IReference thisReference = SSTBuilder.variableReference(languageOptions.getThisName());
		for (AbstractLocation location : queryPointerAnalysis(thisReference, enclosingClass)) {
			implicitDefinitions.put(location, thisDefinition);
		}

		// super
		DummyDefinitionSite superDefinition = DummyDefinitionSite.byThis();
		IReference superReference = SSTBuilder.variableReference(languageOptions.getSuperName());
		ITypeName superType = languageOptions.getSuperType(context.getTypeShape().getTypeHierarchy());
		for (AbstractLocation location : queryPointerAnalysis(superReference, superType)) {
			implicitDefinitions.put(location, superDefinition);
		}

		for (IFieldDeclaration fieldDecl : context.getSST().getFields()) {
			IFieldName field = fieldDecl.getName();
			DummyDefinitionSite fieldDefinition = DummyDefinitionSite.byField(field);
			IReference fieldReference = SSTBuilder.fieldReference(field);
			for (AbstractLocation location : queryPointerAnalysis(fieldReference, field.getValueType())) {
				// TODO we might overwrite definitions here if two fields share one location
				implicitDefinitions.put(location, fieldDefinition);
			}
		}

		// treat properties as fields if they have no custom get code
		for (IPropertyDeclaration propertyDecl : context.getSST().getProperties()) {
			if (!propertyDecl.getGet().isEmpty()) {
				continue;
			}

			IPropertyName property = propertyDecl.getName();
			DummyDefinitionSite propertyDefinition = DummyDefinitionSite.byField(propertyToField(property));
			IReference propertyRefernce = SSTBuilder.propertyReference(property);
			for (AbstractLocation location : queryPointerAnalysis(propertyRefernce, property.getValueType())) {
				// do not overwrite an existing definition by a real field
				if (!implicitDefinitions.containsKey(location)) {
					implicitDefinitions.put(location, propertyDefinition);
				}
			}
		}

	}

	private IFieldName propertyToField(IPropertyName property) {
		IFieldName field = FieldName.newFieldName(property.getIdentifier());
		return field;
	}

	private DummyUsage initializeUsage(ITypeName type, AbstractLocation location) {
		DummyUsage usage = new DummyUsage();

		usage.setType(type);
		usage.setClassContext(enclosingClass);
		usage.setMethodContext(currentCallpath.getFirst());

		if (location == null || !implicitDefinitions.containsKey(location)) {
			usage.setDefinitionSite(DummyDefinitionSite.unknown());
		} else {
			usage.setDefinitionSite(implicitDefinitions.get(location));
		}

		return usage;
	}

	private DummyUsage getOrCreateUsage(AbstractLocation location, ITypeName type) {
		DummyUsage usage = locationUsages.get(location);
		if (usage == null) {
			usage = initializeUsage(type, location);
			locationUsages.put(location, usage);
		} else if (usage.getType().isUnknownType() && !type.isUnknownType()) {
			// unknown types cause a lot of usages to be (wrongly) initialized with insufficient type information ->
			// update these usages once we have a concrete type
			usage.setType(type);
		}

		return usage;
	}

	private void updateDefinitions(QueryContextKey query, DummyDefinitionSite newDefinition) {
		Set<AbstractLocation> locations = pointerAnalysis.query(query);

		for (AbstractLocation location : locations) {
			DummyUsage usage = getOrCreateUsage(location, query.getType());

			DummyDefinitionSite currentDefinition = usage.getDefinitionSite();

			boolean currentDefinitionIsReturnOfNonEntryPoint = false;
			if (currentDefinition.getKind() == DefinitionSiteKind.RETURN) {
				IMethodDeclaration methodDecl = declarationMapper.get(currentDefinition.getMethod());
				currentDefinitionIsReturnOfNonEntryPoint = methodDecl != null && !methodDecl.isEntryPoint();
			}

			boolean newDefinitionHasHigherPriority = definitionSiteComparator.compare(currentDefinition,
					newDefinition) < 0;

			if (currentDefinitionIsReturnOfNonEntryPoint || newDefinitionHasHigherPriority) {
				usage.setDefinitionSite(newDefinition);
			}
		}
	}

	private void updateCallsites(QueryContextKey query, DummyCallsite callsite) {
		Set<AbstractLocation> locations = pointerAnalysis.query(query);

		for (AbstractLocation location : locations) {
			DummyUsage usage = getOrCreateUsage(location, query.getType());

			usage.addCallsite(callsite);
		}
	}

	public void registerParameter(IMethodName method, IParameterName parameter, int argIndex) {
		// skip parameter definition if we are descending into other methods: do not overwrite the definition of the top
		// most entry point method
		if (currentCallpath.size() > 1) {
			return;
		}

		QueryContextKey query = new QueryContextKey(SSTBuilder.variableReference(parameter.getName()), null,
				parameter.getValueType(), currentCallpath);
		DummyDefinitionSite newDefinition = DummyDefinitionSite.byParam(method, argIndex);

		updateDefinitions(query, newDefinition);
	}

	private void registerMethodDefinition(DummyDefinitionSite definitionSite, TypeName methodType) {
		if (currentStatement instanceof IExpressionStatement) {
			// method called without saving returned value -> cannot have any calls
			return;
		} else if (!(currentStatement instanceof IAssignment)) {
			LOGGER.error("Cannot register {} definition site: target is no assignment", definitionSite.getKind());
			return;
		}

		IAssignment assignStmt = (IAssignment) currentStatement;
		ITypeName boundType = typeCollector.getType(assignStmt.getReference());
		// fall back to the type generated by the method if no bound type can be found or it is unknown
		TypeName type;
		if (boundType != null && !boundType.isUnknownType()) {
			type = boundType;
		} else if (methodType.isTypeParameter()) {
			// return type of a method can be a generic type parameter which does not give us any information -> skip
			// definition
			// TODO alternatively, the type could be set to unknown. however, updating the definitions for all types
			// will cause imprecision, especially for exceptions which are currently mapped to an unknown definition
			LOGGER.warn("Discarding {} definition site due to generic type parameter", definitionSite.getKind());
			return;
		} else {
			type = methodType;
		}
		QueryContextKey query = new QueryContextKey(assignStmt.getReference(), currentStatement, type, currentCallpath);

		updateDefinitions(query, definitionSite);
	}

	public void registerConstant(IConstantValueExpression constExpr) {
		if (!(currentStatement instanceof IAssignment)) {
			LOGGER.error("Cannot register constant definition site: target is no assignment");
			return;
		}

		IAssignment assignStmt = (IAssignment) currentStatement;
		ITypeName type = typeCollector.getType(assignStmt.getReference());
		QueryContextKey query = new QueryContextKey(assignStmt.getReference(), currentStatement, type, currentCallpath);
		DummyDefinitionSite newDefinition = DummyDefinitionSite.byConstant();

		updateDefinitions(query, newDefinition);
	}

	public void registerConstructor(IMethodName method) {
		DummyDefinitionSite newDefinition = DummyDefinitionSite.byConstructor(method);
		registerMethodDefinition(newDefinition, method.getDeclaringType());
	}

	public void registerPotentialReturnDefinitionSite(IMethodName method) {
		DummyDefinitionSite newDefinition = DummyDefinitionSite.byReturn(method);
		registerMethodDefinition(newDefinition, method.getReturnType());
	}

	public boolean isNonEntryPointMethod(MethodName method) {
		IMethodDeclaration methodDecl = declarationMapper.get(method);
		return methodDecl != null && !methodDecl.isEntryPoint();
	}

	public void registerParameterCallsite(IMethodName method, IReference parameterExpr, int argIndex) {
		ITypeName type = typeCollector.getType(parameterExpr);
		// fall back to the type of the formal parameter if the type of the actual one is not available
		if (type == null) {
			type = method.getParameters().get(argIndex).getValueType();
		}

		QueryContextKey query = new QueryContextKey(parameterExpr, currentStatement, type, currentCallpath);
		DummyCallsite callsite = DummyCallsite.parameterCallsite(method, argIndex);

		updateCallsites(query, callsite);
	}

	public void registerReceiverCallsite(IMethodName method, IReference receiver) {
		ITypeName type = typeCollector.getType(receiver);
		// fall back to the type of the declaring class if the receiver reference is not bound to any type
		if (type == null) {
			type = method.getDeclaringType();
		}
		QueryContextKey query = new QueryContextKey(receiver, currentStatement, type, currentCallpath);
		DummyCallsite callsite = DummyCallsite.receiverCallsite(method);

		updateCallsites(query, callsite);
	}

	public void processDescent(IMethodName method, UsageExtractionVisitor visitor) {
		IMethodDeclaration methodDecl = declarationMapper.get(method);
		if (methodDecl != null && descentStrategy.descent(methodDecl, currentCallpath)) {
			LOGGER.debug("Descending into {}", method.getName());
			methodDecl.accept(visitor, this);
		}
	}

	public void processDescent(IPropertyName property) {
		IPropertyDeclaration propertyDecl = declarationMapper.get(property);
		if (propertyDecl != null) {
			if (!propertyDecl.getGet().isEmpty() || !propertyDecl.getSet().isEmpty()) {
				if (descentStrategy.descent(propertyDecl, currentCallpath)) {
					// TODO get or set statements?
				}
			}
		}
	}

}
