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

package namefactory;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsInterfaceTypeName;
import cc.kave.commons.model.names.csharp.CsLocalVariableName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class NodeFactory {

	public static Name getNodeName(ASTNode node) {
		StringBuilder sb = new StringBuilder();

		switch (node.getNodeType()) {
		case ASTNode.METHOD_DECLARATION:
			MethodDeclaration methodNode = (MethodDeclaration) node;
			IMethodBinding method = methodNode.resolveBinding();
			BindingFactory.modifierHelper(sb, method);

			sb.append("[");

			if (method.isConstructor()) {
				sb.append(CsTypeName.newTypeName(BindingFactory.getBindingName(method.getDeclaringClass()))
						.getIdentifier());
			} else {
				sb.append(
						CsTypeName.newTypeName(BindingFactory.getBindingName(method.getReturnType())).getIdentifier());
			}

			sb.append("] ");
			sb.append("[");
			sb.append(
					CsTypeName.newTypeName(BindingFactory.getBindingName(method.getDeclaringClass())).getIdentifier());
			sb.append("].");

			if (method.isConstructor()) {
				sb.append(".ctor");
			} else {
				sb.append(method.getName());
			}

			sb.append("(");

			ITypeBinding[] parameterTypes = method.getParameterTypes();
			for (int i = 0; i < parameterTypes.length; i++) {
				StringBuilder param = new StringBuilder();
				param.append("[");
				param.append(BindingFactory.getBindingName(parameterTypes[i]));
				param.append("] ");
				param.append(((SingleVariableDeclaration) methodNode.parameters().get(i)).getName().getIdentifier());

				CsParameterName.newParameterName(param.toString());
				sb.append(param.toString());

				if (i < parameterTypes.length - 1) {
					sb.append(", ");
				}
			}
			sb.append(")");

			return CsMethodName.newMethodName(sb.toString());

		case ASTNode.FIELD_DECLARATION:
			FieldDeclaration fieldNode = (FieldDeclaration) node;

			Object field = fieldNode.fragments().get(0);
			if (field instanceof VariableDeclarationFragment) {
				BindingFactory.modifierHelper(sb, ((VariableDeclarationFragment) field).resolveBinding());
				sb.append("[");
				sb.append(BindingFactory
						.getBindingName(((VariableDeclarationFragment) field).resolveBinding().getType()));
				sb.append("] ");
				sb.append("[ ].");
				sb.append(((VariableDeclarationFragment) field).getName().getIdentifier());
			}
			return CsFieldName.newFieldName(sb.toString());

		case ASTNode.VARIABLE_DECLARATION_STATEMENT:
			VariableDeclarationStatement variableStatementNode = (VariableDeclarationStatement) node;

			Object variableStatement = variableStatementNode.fragments().get(0);
			if (variableStatement instanceof VariableDeclarationFragment) {
				sb.append("[");
				sb.append(BindingFactory
						.getBindingName(((VariableDeclarationFragment) variableStatement).resolveBinding().getType()));
				sb.append("] ");
				sb.append(((VariableDeclarationFragment) variableStatement).getName().getIdentifier());
				return CsLocalVariableName.newLocalVariableName(sb.toString());
			}

			return CsLocalVariableName.newLocalVariableName("");

		case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
			VariableDeclarationStatement variableExpressionNode = (VariableDeclarationStatement) node;

			Object variableExpression = variableExpressionNode.fragments().get(0);
			if (variableExpression instanceof VariableDeclarationFragment) {
				sb.append("[");
				sb.append(BindingFactory
						.getBindingName(((VariableDeclarationFragment) variableExpression).resolveBinding().getType()));
				sb.append("] ");
				sb.append(((VariableDeclarationFragment) variableExpression).getName().getIdentifier());
				return CsLocalVariableName.newLocalVariableName(sb.toString());
			}

			return CsLocalVariableName.newLocalVariableName("");

		case ASTNode.IMPORT_DECLARATION:
			ImportDeclaration importNode = (ImportDeclaration) node;
			return CsTypeName.newTypeName(BindingFactory.getBindingName(importNode.resolveBinding()));

		case ASTNode.TYPE_DECLARATION:
			TypeDeclaration typeNode = (TypeDeclaration) node;
			return CsTypeName.newTypeName(BindingFactory.getBindingName(typeNode.resolveBinding()));

		case ASTNode.PACKAGE_DECLARATION:
			PackageDeclaration packageNode = (PackageDeclaration) node;
			return CsNamespaceName.newNamespaceName(packageNode.resolveBinding().getName());
		}
		return null;
	}

	private void addPrefix(ASTNode node, StringBuilder sb) {

		if (node instanceof TypeDeclaration && ((TypeDeclaration) node).isInterface()) {
			sb.append("i: ");
		} else if (node instanceof EnumDeclaration) {
			sb.append("e: ");
		}

		switch ("node") {
		case "interface":
			sb.append("i:");
			break;
		case "struct":
			sb.append("s:");
			break;
		case "enum":
			sb.append("e:");
			break;
		}

	}

	public static class BindingFactory {

		public static String getBindingName(IBinding binding) {
			StringBuilder sb = new StringBuilder();

			switch (binding.getKind()) {

			// IPackageBinding
			case 1:
				((IPackageBinding) binding).getName();
				break;
			// ITypeBinding
			case 2:
				ITypeBinding type = (ITypeBinding) binding;
				String qualifiedName = type.getQualifiedName();

				if (type.isParameterizedType()) {
					sb.append(qualifiedName.substring(0, qualifiedName.indexOf("<")));
					sb.append("`");
					sb.append(type.getTypeArguments().length);
					sb.append("[");

					// TODO: generics for later work
					// for (ITypeBinding t : type.getTypeArguments()) {
					// sb.append("[T -> ");
					// sb.append(t.getQualifiedName());
					// sb.append("],");
					// }
					sb.append("GenericsPlaceholder");

					sb.deleteCharAt(sb.toString().length() - 1);
					sb.append("]");
				} else {
					sb.append(qualifiedName);
				}

				sb.append(", ");

				// String temp = type.getDeclaringClass().getQualifiedName() +
				// "."
				// + type.getName();
				sb.append(getAssemblyName(qualifiedName));

				return sb.toString();

			// IVariableBinding
			case 3:
				((IVariableBinding) binding).getType().getQualifiedName();
				break;
			// IMethodBinding
			case 4:
				((IMethodBinding) binding).getName();
				break;
			// IAnnotationBinding
			case 5:
				((IAnnotationBinding) binding).getAnnotationType().getQualifiedName();
				break;
			// IMemberValuePair
			case 6:
				((IMemberValuePair) binding).getMemberName();
				break;
			default:
				return null;
			}
			return null;
		}

		public static StringBuilder modifierHelper(StringBuilder sb, IBinding binding) {
			int modifier = binding.getModifiers();

			if (Modifier.isStatic(modifier))
				sb.append("static ");
			if (Modifier.isFinal(modifier))
				sb.append("final ");
			if (Modifier.isAbstract(modifier))
				sb.append("abstract ");
			if (Modifier.isSynchronized(modifier))
				sb.append("synchronized ");

			return sb;
		}

		// TODO: Change returntype
		public static String getAssemblyName(String qualifiedName) {
			Class<?> c = null;

			try {
				c = Class.forName(qualifiedName);
				System.out.println(c.getPackage().getImplementationVersion());
				System.out.println(c.getPackage().getSpecificationVersion());
			} catch (ClassNotFoundException e) {
				return "Unknown_Assembly";
			}

			if (c.getProtectionDomain().getCodeSource() == null) {
				return "rt.jar";
			}

			String path = c.getProtectionDomain().getCodeSource().getLocation().getPath();

			if (path.endsWith(".jar")) {
				return path.substring(path.lastIndexOf("/") + 1);
			} else if (path.endsWith("/bin/")) {
				String project = path.substring(0, path.length() - 5);
				return project.substring(project.lastIndexOf("/") + 1, project.length());
			}
			

			// TODO: version has to be implemented
			CsAssemblyName.newAssemblyName(path);
			
			return path;
		}
	}
}
