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
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMemberName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedNameException;

public class DeclarationMapper {

	private Map<IMethodName, IMethodDeclaration> methods;
	private Map<IFieldName, IFieldDeclaration> fields;
	private Map<IPropertyName, IPropertyDeclaration> properties;

	public DeclarationMapper(Context context) {
		this(context.getSST());
	}

	public DeclarationMapper(ISST sst) {
		methods = new HashMap<>(sst.getMethods().size());
		for (IMethodDeclaration methodDecl : sst.getMethods()) {
			methods.put(methodDecl.getName(), methodDecl);
		}

		fields = new HashMap<>(sst.getFields().size());
		for (IFieldDeclaration fieldDecl : sst.getFields()) {
			fields.put(fieldDecl.getName(), fieldDecl);
		}

		properties = new HashMap<>(sst.getProperties().size());
		for (IPropertyDeclaration propertyDecl : sst.getProperties()) {
			properties.put(propertyDecl.getName(), propertyDecl);
		}
	}

	public IMethodDeclaration get(IMethodName method) {
		return methods.get(method);
	}

	public IFieldDeclaration get(IFieldName field) {
		return fields.get(field);
	}

	public IPropertyDeclaration get(IPropertyName property) {
		return properties.get(property);
	}

	public IMemberDeclaration get(IMemberName member) {
		if (member instanceof IFieldName) {
			return get((IFieldName) member);
		} else if (member instanceof IPropertyName) {
			return get((IPropertyName) member);
		}
		throw new UnexpectedNameException(member);
	}
}
