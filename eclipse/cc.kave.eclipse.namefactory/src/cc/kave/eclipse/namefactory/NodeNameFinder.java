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

package cc.kave.eclipse.namefactory;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.eclipse.namefactory.NodeFactory.BindingFactory;

public class NodeNameFinder {

	/**
	 * 
	 * @param method
	 *            Expects a MethodDeclaration, MethodInvocation or a
	 *            SuperMethodInvocation node.
	 * @param parameterIndex
	 *            The parameter number starting at 0.
	 * @return Returns a ParameterName for the given index.
	 */
	public static ParameterName getParameterFromMethod(ASTNode method, int parameterIndex) {
		String[] parameterNames = NodeFactory.createParameterNames(method);
		return CsParameterName.newParameterName(parameterNames[parameterIndex]);
	}

	/**
	 * @param method
	 *            Expects a MethodDeclaration, MethodInvocation or a
	 *            SuperMethodInvocation node.
	 * @return Returns the TypeName of the declaring class.
	 */
	public static TypeName getDeclTypeFromMethod(ASTNode method) {
		String typeName = "";

		if (method instanceof MethodDeclaration) {
			typeName = BindingFactory.getBindingName(((MethodDeclaration) method).resolveBinding().getDeclaringClass());
		} else if (method instanceof MethodInvocation) {
			typeName = BindingFactory
					.getBindingName(((MethodInvocation) method).resolveMethodBinding().getDeclaringClass());
		} else if (method instanceof SuperMethodInvocation) {
			typeName = BindingFactory
					.getBindingName(((SuperMethodInvocation) method).resolveMethodBinding().getDeclaringClass());
		}
		return CsTypeName.newTypeName(typeName);
	}

	/**
	 * @param method
	 *            Expects a MethodDeclaration, MethodInvocation or a
	 *            SuperMethodInvocation node.
	 * @return Returns the TypeName of the return type.
	 */
	public static TypeName getReturnType(ASTNode method) {
		String typeName = "";

		if (method instanceof MethodDeclaration) {
			typeName = BindingFactory.getBindingName(((MethodDeclaration) method).getReturnType2().resolveBinding());
		} else if (method instanceof MethodInvocation) {
			typeName = BindingFactory.getBindingName(((MethodInvocation) method).resolveTypeBinding());
		} else if (method instanceof SuperMethodInvocation) {
			typeName = BindingFactory.getBindingName(((SuperMethodInvocation) method).resolveTypeBinding());
		}

		return CsTypeName.newTypeName(typeName);
	}

	/**
	 * 
	 * @param method
	 *            Expects a MethodDeclaration, MethodInvocation or a
	 *            SuperMethodInvocation node.
	 * @return Returns the corresponding MethodName.
	 */
	public static MethodName getSuperMethodName(ASTNode method) {
		IMethodBinding binding = null;

		if (method instanceof MethodDeclaration) {
			binding = ((MethodDeclaration) method).resolveBinding();
		} else if (method instanceof MethodInvocation) {
			((MethodInvocation) method).resolveMethodBinding();
		} else if (method instanceof SuperMethodInvocation) {
			((SuperMethodInvocation) method).resolveMethodBinding();
		} else {
			throw new RuntimeException("Unsupported NodeType");
		}

		return NodeFactory.getSuperMethodNames(binding)[0];
	}

	/**
	 * 
	 * @param method
	 *            Expects a MethodDeclaration, MethodInvocation or a
	 *            SuperMethodInvocation node.
	 * @return Returns the corresponding MethodName.
	 */
	public static MethodName getFirstMethodName(ASTNode method) {
		IMethodBinding binding = null;

		if (method instanceof MethodDeclaration) {
			binding = ((MethodDeclaration) method).resolveBinding();
		} else if (method instanceof MethodInvocation) {
			((MethodInvocation) method).resolveMethodBinding();
		} else if (method instanceof SuperMethodInvocation) {
			((SuperMethodInvocation) method).resolveMethodBinding();
		} else {
			throw new RuntimeException("Unsupported NodeType");
		}

		return NodeFactory.getSuperMethodNames(binding)[1];
	}
}
