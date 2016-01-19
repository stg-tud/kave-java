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

import java.util.HashMap;
import java.util.Map;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;

public class DeclarationMapper {

	private Map<MethodName, IMethodDeclaration> methods;
	private Map<FieldName, IFieldDeclaration> fields;
	private Map<PropertyName, IPropertyDeclaration> properties;

	public DeclarationMapper(Context context) {
		methods = new HashMap<>(context.getSST().getMethods().size());
		for (IMethodDeclaration methodDecl : context.getSST().getMethods()) {
			methods.put(methodDecl.getName(), methodDecl);
		}

		fields = new HashMap<>(context.getSST().getFields().size());
		for (IFieldDeclaration fieldDecl : context.getSST().getFields()) {
			fields.put(fieldDecl.getName(), fieldDecl);
		}

		properties = new HashMap<>(context.getSST().getProperties().size());
		for (IPropertyDeclaration propertyDecl : context.getSST().getProperties()) {
			properties.put(propertyDecl.getName(), propertyDecl);
		}
	}

	public IMethodDeclaration get(MethodName method) {
		return methods.get(method);
	}

	public IFieldDeclaration get(FieldName field) {
		return fields.get(field);
	}

	public IPropertyDeclaration get(PropertyName property) {
		return properties.get(property);
	}
}
