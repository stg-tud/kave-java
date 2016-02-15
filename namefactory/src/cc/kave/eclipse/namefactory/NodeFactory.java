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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsLocalVariableName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class NodeFactory {

	public static Name createNodeName(ASTNode node) {

		switch (node.getNodeType()) {

		case ASTNode.METHOD_DECLARATION:
			return createMethodDeclName((MethodDeclaration) node);

		case ASTNode.METHOD_INVOCATION:
			return createMethodInvName(node);

		case ASTNode.SUPER_METHOD_INVOCATION:
			return createSuperMethodInvName(node);

		case ASTNode.VARIABLE_DECLARATION_FRAGMENT:
			return createVariableName(node.getParent());

		case ASTNode.QUALIFIED_NAME:
			return createQualifiedName(node);

		case ASTNode.SINGLE_VARIABLE_DECLARATION:
			return createSingleVariableDeclName(node);

		case ASTNode.IMPORT_DECLARATION:
			ImportDeclaration importNode = (ImportDeclaration) node;
			return CsTypeName.newTypeName(BindingFactory.getBindingName(importNode.resolveBinding()));

		case ASTNode.PACKAGE_DECLARATION:
			PackageDeclaration packageNode = (PackageDeclaration) node;
			return CsNamespaceName.newNamespaceName(packageNode.resolveBinding().getName());

		case ASTNode.FIELD_DECLARATION:
			return createVariableName(node);

		case ASTNode.VARIABLE_DECLARATION_STATEMENT:
			return createVariableName(node);

		case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
			return createVariableName(node);

		case ASTNode.CLASS_INSTANCE_CREATION:
			String newName = methodNameHelper(null, ((ClassInstanceCreation) node).resolveConstructorBinding(), false);
			return CsMethodName.newMethodName(newName);

		case ASTNode.FIELD_ACCESS:
			return createFieldAccessName(node);

		default:
			return null;
		}
	}

	private static Name createFieldAccessName(ASTNode node) {
		StringBuilder sb = new StringBuilder();
		FieldAccess fieldAccess = (FieldAccess) node;
		sb.append(modifierHelper(fieldAccess.resolveFieldBinding()));
		sb.append("[").append(getBindingName(fieldAccess.resolveFieldBinding().getType())).append("] ");
		sb.append("[").append(getBindingName(fieldAccess.resolveFieldBinding().getDeclaringClass())).append("].");
		sb.append(fieldAccess.getName().getIdentifier());
		
		return CsFieldName.newFieldName(sb.toString());
	}

	private static Name createVariableName(ASTNode node) {
		StringBuilder sb = new StringBuilder();

		switch (node.getNodeType()) {

		case ASTNode.FIELD_DECLARATION:
			FieldDeclaration fieldNode = (FieldDeclaration) node;

			Object field = fieldNode.fragments().get(0);
			if (field instanceof VariableDeclarationFragment) {
				sb.append(modifierHelper(((VariableDeclarationFragment) field).resolveBinding()));
				sb.append("[");
				sb.append(BindingFactory
						.getBindingName(((VariableDeclarationFragment) field).resolveBinding().getType()));
				sb.append("] [");
				sb.append(getDeclaringType(fieldNode));
				sb.append("].");
				sb.append(((VariableDeclarationFragment) field).getName().getIdentifier());

				return CsFieldName.newFieldName(sb.toString());
			}
			break;

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
			break;

		case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
			VariableDeclarationExpression variableExpressionNode = (VariableDeclarationExpression) node;

			Object variableExpression = variableExpressionNode.fragments().get(0);
			if (variableExpression instanceof VariableDeclarationFragment) {
				sb.append("[");
				sb.append(BindingFactory
						.getBindingName(((VariableDeclarationFragment) variableExpression).resolveBinding().getType()));
				sb.append("] ");
				sb.append(((VariableDeclarationFragment) variableExpression).getName().getIdentifier());
				return CsLocalVariableName.newLocalVariableName(sb.toString());
			}
			break;
		}
		throw new RuntimeException("should not happen");
	}

	private static Name createSuperMethodInvName(ASTNode node) {
		StringBuilder sb = new StringBuilder();
		SuperMethodInvocation superInvocation = (SuperMethodInvocation) node;
		IMethodBinding superMethodInvBinding = superInvocation.resolveMethodBinding();
		sb.append(methodNameHelper(null, superMethodInvBinding, true));
		return CsMethodName.newMethodName(sb.toString());
	}

	private static Name createMethodInvName(ASTNode node) {
		StringBuilder sb = new StringBuilder();
		MethodInvocation invocation = (MethodInvocation) node;
		IMethodBinding methodInvBinding = invocation.resolveMethodBinding();
		sb.append(methodNameHelper(null, methodInvBinding, true));
		return CsMethodName.newMethodName(sb.toString());
	}

	private static Name createQualifiedName(ASTNode node) {
		StringBuilder sb = new StringBuilder();
		QualifiedName qualifiedNameNode = (QualifiedName) node;

		sb.append("[");
		String typeName = BindingFactory.getBindingName(qualifiedNameNode.getName().resolveTypeBinding());
		sb.append(CsTypeName.newTypeName(typeName).getIdentifier());
		sb.append("] [");
		sb.append(getDeclaringType(qualifiedNameNode));
		sb.append("].");
		sb.append(qualifiedNameNode.getName().getIdentifier());

		return CsFieldName.newFieldName(sb.toString());
	}

	private static Name createSingleVariableDeclName(ASTNode node) {
		StringBuilder sb = new StringBuilder();
		SingleVariableDeclaration singleVariable = (SingleVariableDeclaration) node;

		sb.append("[");
		String typename = BindingFactory
				.getBindingName(((SingleVariableDeclaration) singleVariable).resolveBinding().getType());
		sb.append(CsTypeName.newTypeName(typename).getIdentifier());
		sb.append("] ");
		sb.append(((SingleVariableDeclaration) singleVariable).getName().getIdentifier());

		if (node.getParent() instanceof MethodDeclaration) {
			return CsParameterName.newParameterName(sb.toString());
		} else {
			return CsLocalVariableName.newLocalVariableName(sb.toString());
		}
	}

	private static Name createMethodDeclName(MethodDeclaration methodNode) {
		IMethodBinding methodBinding = methodNode.resolveBinding();
		return CsMethodName.newMethodName(methodNameHelper(methodNode, methodBinding, true));
	}

	private static String methodNameHelper(MethodDeclaration methodNode, IMethodBinding method, boolean override) {
		if (override && hasOverrideAnnotation(method)) {
			getSuperMethodNames(method);
		}

		StringBuilder sb = new StringBuilder();

		sb.append(modifierHelper(method));

		sb.append("[");

		String declaringClass = BindingFactory.getBindingName(method.getDeclaringClass());
		if (method.isConstructor()) {
			sb.append(CsTypeName.newTypeName(declaringClass).getIdentifier());
		} else {
			String returnType = BindingFactory.getBindingName(method.getReturnType());
			sb.append(CsTypeName.newTypeName(returnType).getIdentifier());
		}

		sb.append("] [");
		sb.append(CsTypeName.newTypeName(declaringClass).getIdentifier());
		sb.append("].");

		if (method.isConstructor()) {
			sb.append(".ctor");
		} else {
			sb.append(method.getName());
		}

		sb.append("(");

		String[] parameterNames = createParameterNames(methodNode, method);

		for (int i = 0; i < parameterNames.length; i++) {
			sb.append(parameterNames[i]);

			if (i < parameterNames.length - 1) {
				sb.append(", ");
			}
		}

		sb.append(")");

		return sb.toString();
	}

	/**
	 * 
	 * @param method
	 *            Expects a MethodDeclaration, MethodInvocation or a
	 *            SuperMethodInvocation node.
	 * @return Returns an array of all parameterNames.
	 */
	protected static String[] createParameterNames(ASTNode method) {
		if (method instanceof MethodDeclaration) {
			MethodDeclaration methodDecl = (MethodDeclaration) method;
			return createParameterNames(methodDecl, methodDecl.resolveBinding());
		} else if (method instanceof MethodInvocation) {
			MethodInvocation methodInv = (MethodInvocation) method;
			return createParameterNames(null, methodInv.resolveMethodBinding());
		} else if (method instanceof SuperMethodInvocation) {
			SuperMethodInvocation superMethodInv = (SuperMethodInvocation) method;
			return createParameterNames(null, superMethodInv.resolveMethodBinding());
		}
		return null;
	}

	private static String[] createParameterNames(MethodDeclaration methodDecl, IMethodBinding method) {
		ITypeBinding[] parameterTypes = method.getParameterTypes();
		String[] parameters = new String[parameterTypes.length];

		for (int i = 0; i < parameterTypes.length; i++) {
			StringBuilder param = new StringBuilder();

			if (i == parameterTypes.length - 1 && method.isVarargs()) {
				param.append("params ");
			}

			param.append("[");
			param.append(BindingFactory.getBindingName(parameterTypes[i]));
			param.append("] ");

			if (methodDecl != null) {
				param.append(((SingleVariableDeclaration) methodDecl.parameters().get(i)).getName().getIdentifier());
			} else {
				param.append("?");
			}
			parameters[i] = CsParameterName.newParameterName(param.toString()).getIdentifier();
		}

		return parameters;
	}

	/**
	 * 
	 * @param method
	 *            The method binding whose superclasses are searched trough for
	 *            this bindings super method
	 * @return Returns an array with two MethodName. They can be null if they do
	 *         not exist.
	 */
	protected static MethodName[] getSuperMethodNames(IMethodBinding method) {
		MethodName[] methodNames = new CsMethodName[2];
		IMethodBinding firstMethod = null, topLevelMethod = null;

		List<ITypeBinding> types = getTypeHierarchy(method);

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

		methodNames[0] = CsMethodName.newMethodName(methodNameHelper(null, firstMethod, false));

		if (topLevelMethod != null) {
			methodNames[1] = CsMethodName.newMethodName(methodNameHelper(null, topLevelMethod, false));
		}

		return methodNames;
	}

	private static boolean hasOverrideAnnotation(IMethodBinding method) {
		for (IAnnotationBinding iAnnotationBinding : method.getAnnotations()) {
			if (iAnnotationBinding.getName().equals("Override"))
				return true;
		}
		return false;
	}

	private static List<ITypeBinding> getTypeHierarchy(IMethodBinding method) {
		List<ITypeBinding> types = new ArrayList<ITypeBinding>();
		ITypeBinding current = method.getDeclaringClass();
		types.add(current);

		while (current.getSuperclass() != null) {
			types.add(current.getSuperclass());
			current = current.getSuperclass();
		}
		return types;
	}

	private static String getDeclaringType(ASTNode node) {
		return BindingFactory
				.getBindingName(((TypeDeclaration) ((CompilationUnit) node.getRoot()).types().get(0)).resolveBinding());
	}

	private static String modifierHelper(IBinding binding) {
		int modifier = binding.getModifiers();

		if (Modifier.isStatic(modifier))
			return "static ";
		else {
			return "";
		}
	}

	public static TypeName getBindingName(ITypeBinding binding) {
		return CsTypeName.newTypeName(BindingFactory.getBindingName(binding));
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
				sb.append(addPrefix(type));

				if (type.isParameterizedType()) {
					sb.append(qualifiedName.substring(0, qualifiedName.indexOf("<")));
					sb.append("`");
					sb.append(type.getTypeArguments().length);
					sb.append("[");

					// TODO: generics for later work
					for (ITypeBinding t : type.getTypeArguments()) {
						sb.append("[T -> T");
						// sb.append(getBindingName(t));
						sb.append("],");
					}

					sb.deleteCharAt(sb.toString().length() - 1);
					sb.append("]");
				} else {
					if (isPrimitivType(qualifiedName)) {
						sb.append("%" + qualifiedName);
					} else {
						sb.append(qualifiedName);
					}
				}

				sb.append(", ");
				sb.append(getAssemblyName(qualifiedName));
				break;

			// IVariableBinding
			case 3:
				IVariableBinding var = (IVariableBinding) binding;
				sb.append("[").append(getBindingName(var.getType())).append("] ");

				if (var.isField()) {
					sb.append("[").append(getBindingName(var.getDeclaringClass())).append("].");
				}

				sb.append(var.getName());
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
			return sb.toString();
		}

		public static TypeName getTypeName(ITypeBinding binding) {
			return CsTypeName.newTypeName(getBindingName(binding));
		}

		private static boolean isPrimitivType(String name) {
			String[] primitiveTypes = { "byte", "short", "char", "int", "long", "float", "double", "boolean", "void" };
			for (String type : primitiveTypes) {
				if (type.equals(name))
					return true;
			}
			return false;
		}

		private static String addPrefix(ITypeBinding type) {
			if (type.isInterface()) {
				return "i: ";
			} else if (type.isEnum()) {
				return "e: ";
			}
			return "";
		}

		private static String getAssemblyName(String qualifiedName) {
			Class<?> c = null;

			if (isPrimitivType(qualifiedName)) {
				return "rt.jar, " + String.class.getPackage().getSpecificationVersion();
			}

			try {
				c = Class.forName(qualifiedName);
			} catch (ClassNotFoundException e) {
				return "?";
			}

			if (c.getProtectionDomain().getCodeSource() == null) {
				return "rt.jar, " + c.getPackage().getSpecificationVersion();
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
