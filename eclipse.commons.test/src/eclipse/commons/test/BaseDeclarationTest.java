/**
 * Copyright 2015 Waldemar Graf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eclipse.commons.test;

import java.util.Set;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;

public class BaseDeclarationTest {
	
	protected static SST context;

	public FieldDeclaration getFieldDeclaration(String fieldName) {
		FieldDeclaration decl = new FieldDeclaration();
		decl.setName(CsFieldName.newFieldName(fieldName));

		return decl;
	}

	public MethodDeclaration newMethodDeclaration(String methodName) {
		MethodDeclaration decl = new MethodDeclaration();
		decl.setName(CsMethodName.newMethodName(methodName));

		return decl;
	}

	public boolean containsField(FieldDeclaration decl) {
		return context.getFields().contains(decl);
	}

	public boolean containsMethod(IMethodDeclaration expected) {
		return context.getMethods().contains(expected);
	}

	public boolean containsMethod(String name) {
		Set<IMethodDeclaration> methods = context.getMethods();

		for (IMethodDeclaration method : methods) {
			if (method.getName().equals(CsMethodName.newMethodName(name))) {
				return true;
			}
		}

		return false;
	}

	public IFieldDeclaration getField(String fieldName) {
		Set<IFieldDeclaration> fields = context.getFields();
		FieldName name = CsFieldName.newFieldName(fieldName);

		for (IFieldDeclaration field : fields) {
			if (field.getName().equals(name))
				return field;
		}

		return null;
	}

	public IMethodDeclaration getMethod(String methodName) {
		Set<IMethodDeclaration> methods = context.getMethods();
		FieldName name = CsFieldName.newFieldName(methodName);

		for (IMethodDeclaration method : methods) {
			if (method.getName().equals(name))
				return method;
		}

		return null;
	}
}
