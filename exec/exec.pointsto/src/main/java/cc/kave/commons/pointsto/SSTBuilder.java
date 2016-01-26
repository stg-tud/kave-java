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
package cc.kave.commons.pointsto;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class SSTBuilder {

	public static IVariableReference variableReference(String name) {
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier(name);
		return varRef;
	}

	public static IFieldReference fieldReference(IFieldName field) {
		IVariableReference thisReference = variableReference(LanguageOptions.getInstance().getThisName());
		return fieldReference(thisReference, field);
	}

	public static IFieldReference fieldReference(IVariableReference reference, FieldName field) {
		FieldReference fieldRef = new FieldReference();
		fieldRef.setReference(reference);
		fieldRef.setFieldName(field);
		return fieldRef;
	}

	public static IPropertyReference propertyReference(IPropertyName property) {
		IVariableReference thisReference = variableReference(LanguageOptions.getInstance().getThisName());
		return propertyReference(thisReference, property);
	}

	public static IPropertyReference propertyReference(IVariableReference reference, PropertyName property) {
		PropertyReference propertyRef = new PropertyReference();
		propertyRef.setReference(reference);
		propertyRef.setPropertyName(property);
		return propertyRef;
	}

	public static IIndexAccessReference indexAccessReference(IIndexAccessExpression expr) {
		IndexAccessReference reference = new IndexAccessReference();
		reference.setExpression(expr);
		return reference;
	}
}
