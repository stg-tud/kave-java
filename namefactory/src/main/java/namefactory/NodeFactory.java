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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsLocalVariableName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class NodeFactory {

	public static Name createNodeName(ASTNode node) {
		StringBuilder sb = new StringBuilder();

		switch (node.getNodeType()) {
		case ASTNode.METHOD_DECLARATION:
			MethodDeclaration methodNode = (MethodDeclaration) node;
			IMethodBinding methodBinding = methodNode.resolveBinding();
			sb.append(methodHelper(methodNode, methodBinding, true));

			return CsMethodName.newMethodName(sb.toString());

		case ASTNode.METHOD_INVOCATION:
			MethodInvocation invocation = (MethodInvocation) node;
			IMethodBinding methodInvBinding = invocation.resolveMethodBinding();
			sb.append(methodHelper(null, methodInvBinding, true));
			return CsMethodName.newMethodName(sb.toString());

		case ASTNode.SUPER_METHOD_INVOCATION:
			SuperMethodInvocation superInvocation = (SuperMethodInvocation) node;
			IMethodBinding superMethodInvBinding = superInvocation.resolveMethodBinding();
			sb.append(methodHelper(null, superMethodInvBinding, true));
			return CsMethodName.newMethodName(sb.toString());

		case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
			node = node.getParent();

			switch (node.getNodeType()) {

			case ASTNode.FIELD_DECLARATION:
				FieldDeclaration fieldNode = (FieldDeclaration) node;

				Object field = fieldNode.fragments().get(0);
				if (field instanceof VariableDeclarationFragment) {
					BindingFactory.modifierHelper(sb, ((VariableDeclarationFragment) field).resolveBinding());
					sb.append("[");
					sb.append(BindingFactory
							.getBindingName(((VariableDeclarationFragment) field).resolveBinding().getType()));
					sb.append("] [");
					sb.append(getDeclaringType(fieldNode));
					sb.append("].");
					sb.append(((VariableDeclarationFragment) field).getName().getIdentifier());
					return CsFieldName.newFieldName(sb.toString());
				}
				return null;

			case ASTNode.VARIABLE_DECLARATION_STATEMENT:
				VariableDeclarationStatement variableStatementNode = (VariableDeclarationStatement) node;

				Object variableStatement = variableStatementNode.fragments().get(0);
				if (variableStatement instanceof VariableDeclarationFragment) {
					sb.append("[");
					sb.append(BindingFactory.getBindingName(
							((VariableDeclarationFragment) variableStatement).resolveBinding().getType()));
					sb.append("] ");
					sb.append(((VariableDeclarationFragment) variableStatement).getName().getIdentifier());
					return CsLocalVariableName.newLocalVariableName(sb.toString());
				}
				return null;

			case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
				VariableDeclarationExpression variableExpressionNode = (VariableDeclarationExpression) node;

				Object variableExpression = variableExpressionNode.fragments().get(0);
				if (variableExpression instanceof VariableDeclarationFragment) {
					sb.append("[");
					sb.append(BindingFactory.getBindingName(
							((VariableDeclarationFragment) variableExpression).resolveBinding().getType()));
					sb.append("] ");
					sb.append(((VariableDeclarationFragment) variableExpression).getName().getIdentifier());
					return CsLocalVariableName.newLocalVariableName(sb.toString());
				}
				return null;
			}

		case ASTNode.QUALIFIED_NAME:
			QualifiedName qualifiedNameNode = (QualifiedName) node;
			sb.append("[");
			sb.append(BindingFactory.getBindingName(qualifiedNameNode.resolveTypeBinding()));
			sb.append("] [");
			sb.append(getDeclaringType(qualifiedNameNode));
			sb.append("] ");
			sb.append(qualifiedNameNode.getName().getIdentifier());

			return CsFieldName.newFieldName(sb.toString());

		case ASTNode.SINGLE_VARIABLE_DECLARATION:
			SingleVariableDeclaration singleVariable = (SingleVariableDeclaration) node;
			sb.append("[");
			sb.append(BindingFactory
					.getBindingName(((SingleVariableDeclaration) singleVariable).resolveBinding().getType()));
			sb.append("] ");
			sb.append(((SingleVariableDeclaration) singleVariable).getName().getIdentifier());

			if (node.getParent() instanceof MethodDeclaration) {
				return CsParameterName.newParameterName(sb.toString());
			} else {
				return CsLocalVariableName.newLocalVariableName(sb.toString());
			}

		case ASTNode.IMPORT_DECLARATION:
			ImportDeclaration importNode = (ImportDeclaration) node;
			return CsTypeName.newTypeName(BindingFactory.getBindingName(importNode.resolveBinding()));

		case ASTNode.TYPE_DECLARATION:
			TypeDeclaration typeNode = (TypeDeclaration) node;
			return CsTypeName.newTypeName(BindingFactory.getBindingName(typeNode.resolveBinding()));

		case ASTNode.PACKAGE_DECLARATION:
			PackageDeclaration packageNode = (PackageDeclaration) node;
			return CsNamespaceName.newNamespaceName(packageNode.resolveBinding().getName());
		default:
			return null;
		}
	}

	private static String getDeclaringType(ASTNode node) {
		return BindingFactory
				.getBindingName(((TypeDeclaration) ((CompilationUnit) node.getRoot()).types().get(0)).resolveBinding());
	}

	private static String methodHelper(MethodDeclaration methodNode, IMethodBinding method, boolean override) {
		if (override && hasOverrideAnnotation(method.getAnnotations())) {
			createOverriddenNames(method);
		}
		StringBuilder sb = new StringBuilder();
		
		BindingFactory.modifierHelper(sb, method);

		sb.append("[");

		if (method.isConstructor()) {
			sb.append(
					CsTypeName.newTypeName(BindingFactory.getBindingName(method.getDeclaringClass())).getIdentifier());
		} else {
			sb.append(CsTypeName.newTypeName(BindingFactory.getBindingName(method.getReturnType())).getIdentifier());
		}

		sb.append("] [");
		sb.append(CsTypeName.newTypeName(BindingFactory.getBindingName(method.getDeclaringClass())).getIdentifier());
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

			if (methodNode != null) {
				param.append(((SingleVariableDeclaration) methodNode.parameters().get(i)).getName().getIdentifier());
			} else {
				param.append("?");
			}
			String parameterName = CsParameterName.newParameterName(param.toString()).getIdentifier();
			sb.append(parameterName);

			if (i < parameterTypes.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(")");
		
		return sb.toString();
	}

	private static void createOverriddenNames(IMethodBinding method) {

		IMethodBinding firstMethod = null, topLevelMethod = null;

		List<ITypeBinding> types = new ArrayList<ITypeBinding>();
		ITypeBinding current = method.getDeclaringClass();
		types.add(current);

		while (current.getSuperclass() != null) {
			types.add(current.getSuperclass());
			current = current.getSuperclass();
		}

		for (int i = 0; i < types.size(); i++) {
			IMethodBinding[] declaredMethods = types.get(i).getDeclaredMethods();
			for (int j = 0; j < declaredMethods.length; j++) {
				if (declaredMethods[j].getName().equals(method.getName()) && method.overrides(declaredMethods[j])) {
					if (firstMethod == null) {
						firstMethod = declaredMethods[j];
					} else {
						topLevelMethod = declaredMethods[j];
					}
				}
			}

		}
		
		CsMethodName.newMethodName(methodHelper(null, firstMethod, false));
		
		if(topLevelMethod != null){
			CsMethodName.newMethodName(methodHelper(null, topLevelMethod, false));
		}
	}

	private static boolean hasOverrideAnnotation(IAnnotationBinding[] annoations) {
		for (IAnnotationBinding iAnnotationBinding : annoations) {
			if (iAnnotationBinding.getName().equals("Override"))
				return true;
		}
		return false;
	}

	public static class BindingFactory {

		public static String getBindingName(IBinding binding) {
			StringBuilder sb = new StringBuilder();

			switch (binding.getKind()) {

			// IPackageBinding
			case 1:
				break;
			// ITypeBinding
			case 2:
				ITypeBinding type = (ITypeBinding) binding;
				String qualifiedName = type.getQualifiedName();
				addPrefix(type, sb);

				if (type.isParameterizedType()) {
					sb.append(qualifiedName.substring(0, qualifiedName.indexOf("<")));
					sb.append("`");
					sb.append(type.getTypeArguments().length);
					sb.append("[");

					// TODO: generics for later work
					for (ITypeBinding t : type.getTypeArguments()) {
						sb.append("[T -> ");
						sb.append(getBindingName(t));
						sb.append("],");
					}

					sb.deleteCharAt(sb.toString().length() - 1);
					sb.append("]");
				} else {
					sb.append(qualifiedName);
				}

				sb.append(", ");

				sb.append(getAssemblyName(qualifiedName));

				return sb.toString();

			// IVariableBinding
			case 3:
				break;
			// IMethodBinding
			case 4:
				break;
			// IAnnotationBinding
			case 5:
				break;
			// IMemberValuePair
			case 6:
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

		private static boolean isPrimitivType(String name) {
			String[] primitiveTypes = { "byte", "short", "char", "int", "long", "float", "double", "boolean", "void" };
			for (String type : primitiveTypes) {
				if (type.equals(name))
					return true;
			}
			return false;
		}

		private static void addPrefix(ITypeBinding type, StringBuilder sb) {
			if (type.isInterface()) {
				sb.append("i: ");
			} else if (type.isEnum()) {
				sb.append("e: ");
			}
		}

		private static String getAssemblyName(String qualifiedName) {
			Class<?> c = null;

			if (isPrimitivType(qualifiedName)) {
				return "rt.jar, " + String.class.getPackage().getImplementationVersion();
			}

			try {
				c = Class.forName(qualifiedName);
			} catch (ClassNotFoundException e) {
				return "Unknown_Assembly";
			}

			if (c.getProtectionDomain().getCodeSource() == null) {
				return "rt.jar, " + c.getPackage().getImplementationVersion();
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
