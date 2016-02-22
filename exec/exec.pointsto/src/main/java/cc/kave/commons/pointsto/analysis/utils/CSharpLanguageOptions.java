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
package cc.kave.commons.pointsto.analysis.utils;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;

public class CSharpLanguageOptions extends LanguageOptions {

	@Override
	public String getThisName() {
		return "this";
	}

	@Override
	public String getSuperName() {
		return "base";
	}

	@Override
	public ITypeName getTopClass() {
		return TypeName.newTypeName("System.Object, mscorlib");
	}

	@Override
	public ITypeName getSuperType(ITypeHierarchy typeHierarchy) {
		if (typeHierarchy.hasSuperclass()) {
			return typeHierarchy.getExtends().getElement();
		} else {
			return this.getTopClass();
		}
	}

	@Override
	public String getPropertyParameterName() {
		return "value";
	}

	@Override
	public IMethodName addLambda(IMethodName method) {
		return MethodName
				.newMethodName(LambdaNameHelper.addLambdaToMethodName(method.getIdentifier(), method.getName()));
	}

	@Override
	public ITypeName addLambda(ITypeName type) {
		return TypeName.newTypeName(LambdaNameHelper.addLambdaToTypeName(type.getIdentifier()));
	}

	@Override
	public IMethodName removeLambda(IMethodName method) {
		return MethodName.newMethodName(LambdaNameHelper.removeLambda(method.getIdentifier()));
	}

	@Override
	public IFieldName propertyToField(IPropertyName property) {
		String propertyIdentifier = property.getIdentifier();
		// remove 'get set' and trailing '()'
		String fieldIdentifier = propertyIdentifier.substring(propertyIdentifier.indexOf('['));
		fieldIdentifier = fieldIdentifier.substring(0, fieldIdentifier.lastIndexOf('('));

		if (property.isStatic()) {
			fieldIdentifier = "static " + fieldIdentifier;
		}

		return FieldName.newFieldName(fieldIdentifier);
	}

	@Override
	public boolean isDelegateInvocation(IMethodName invokedMethod) {
		return invokedMethod.getName().equals("Invoke") && invokedMethod.getDeclaringType().isDelegateType();
	}

}
