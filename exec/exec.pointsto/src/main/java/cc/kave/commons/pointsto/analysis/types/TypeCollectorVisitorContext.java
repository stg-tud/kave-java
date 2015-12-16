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
package cc.kave.commons.pointsto.analysis.types;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.pointsto.LanguageOptions;
import cc.kave.commons.pointsto.ScopedMap;

public class TypeCollectorVisitorContext {

	private static final Logger LOGGER = Logger.getLogger(TypeCollectorVisitorContext.class.getName());

	private ScopedMap<String, ITypeName> symbolTable = new ScopedMap<>();

	private IdentityHashMap<IReference, ITypeName> referenceTypes = new IdentityHashMap<>();
	private Set<ITypeName> allTypes = new HashSet<>();

	public IdentityHashMap<IReference, ITypeName> getReferenceTypes() {
		return referenceTypes;
	}

	public Set<ITypeName> getTypes() {
		return allTypes;
	}

	public void initializeSymbolTable(Context context) {
		symbolTable.enter();

		// add implicitly available variables
		ITypeHierarchy typeHierarchy = context.getTypeShape().getTypeHierarchy();
		LanguageOptions languageOptions = LanguageOptions.getInstance();
		declare(languageOptions.getThisName(), typeHierarchy.getElement());
		declare(languageOptions.getSuperName(), languageOptions.getSuperType(typeHierarchy));

		// TODO this might not be necessary as property and field references contain a type

		// add fields
		for (IFieldDeclaration fieldDecl : context.getSST().getFields()) {
			IFieldName field = fieldDecl.getName();
			declare(field.getName(), field.getValueType());
		}

		// add properties
		for (IPropertyDeclaration propertyDecl : context.getSST().getProperties()) {
			IPropertyName property = propertyDecl.getName();
			declare(property.getName(), property.getValueType());
		}
	}

	private void declare(String identifier, ITypeName type) {
		symbolTable.create(identifier, type);
		allTypes.add(type);
	}

	public void enterMethod(IMethodDeclaration method) {
		symbolTable.enter();
		for (IParameterName parameter : method.getName().getParameters()) {
			declareParameter(parameter);
		}
	}

	public void leaveMethod() {
		symbolTable.leave();
	}

	public void enterScope() {
		symbolTable.enter();
	}

	public void leaveScope() {
		symbolTable.leave();
	}

	public void declareVariable(IVariableDeclaration varDecl) {
		if (varDecl.isMissing()) {
			LOGGER.log(Level.SEVERE, "Cannot declare a missing variable");
		} else {
			ITypeName type = varDecl.getType();
			declare(varDecl.getReference().getIdentifier(), type);

			// TODO
			referenceTypes.put(varDecl.getReference(), type);
		}
	}

	public void declareParameter(IParameterName parameter) {
		if (parameter.isUnknown()) {
			LOGGER.log(Level.SEVERE, "Cannot declare an unknown parameter");
		} else {
			declare(parameter.getName(), parameter.getValueType());
		}
	}

	public void useVariableReference(IVariableReference reference) {
		if (reference.isMissing()) {
			LOGGER.log(Level.SEVERE, "Skipping a reference to a missing variable");
			return;
		}

		ITypeName type = symbolTable.get(reference.getIdentifier());
		if (type == null) {
			LOGGER.log(Level.SEVERE, "Skipping a reference to an unknown variable");
		} else {
			referenceTypes.put(reference, type);
		}
	}

	public void useFieldReference(IFieldReference reference) {
		IFieldName field = reference.getFieldName();
		if (field.isUnknown()) {
			LOGGER.log(Level.SEVERE, "Skipping a reference to an unknown field");
		} else {
			ITypeName type = field.getValueType();
			referenceTypes.put(reference, type);
			allTypes.add(type);
		}
	}

	public void usePropertyReference(IPropertyReference reference) {
		IPropertyName property = reference.getPropertyName();
		if (property.isUnknown()) {
			LOGGER.log(Level.WARNING, "Skipping a reference to an unknown property");
		} else {
			ITypeName type = property.getValueType();
			referenceTypes.put(reference, type);
			allTypes.add(type);
		}
	}

}
