package namefactory;

import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class NameFactory {

	public static String getNodeName(MethodDeclaration node) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.resolveBinding().getDeclaringClass().getQualifiedName());
		sb.append(".").append(node.getName().toString());
		return sb.toString();
	}

	public static String getNodeName(FieldDeclaration node) {
		Object o = node.fragments().get(0);
		StringBuilder sb = new StringBuilder();
		if (o instanceof VariableDeclarationFragment) {
			sb.append(
					((VariableDeclarationFragment) o).resolveBinding()
							.getDeclaringClass().getQualifiedName())
					.append(".");
			sb.append(((VariableDeclarationFragment) o).getName()
					.getFullyQualifiedName());
		}
		return sb.toString();
	}

	public static String getNodeName(PackageDeclaration packageDeclaration) {
		return packageDeclaration.getName().getFullyQualifiedName();
	}

	public static String getNodeName(ImportDeclaration importDeclaration) {
		return importDeclaration.getName().getFullyQualifiedName();
	}

	public static String getReturnType(MethodDeclaration methodDeclaration) {
		return methodDeclaration.resolveBinding().getReturnType()
				.getQualifiedName();
	}

	public static String getParameterType(MethodDeclaration node) {
		ITypeBinding[] bindings = node.resolveBinding().getParameterTypes();
		StringBuilder sb = new StringBuilder();

		for (ITypeBinding iTypeBinding : bindings) {
			sb.append(iTypeBinding.getQualifiedName()).append("; ");
		}

		return sb.toString();
	}
	
	public static String getStatement(MethodDeclaration node, int i){
		return node.getBody().statements().get(i).toString();
	}

	/*
	 * Checks if the node is part of java.lang.* and returns the type otherwise
	 * it searches for the fully qualified name in the imports.
	 */
	@Deprecated
	public static String getQualifiedTypeName(ITypeBinding node,
			List<ImportDeclaration> imports) {
		String returnType = node.getQualifiedName();

		if (returnType.startsWith("java.lang"))
			return returnType;

		StringBuilder sb = new StringBuilder();
		returnType = node.getName();

		for (ImportDeclaration importDeclaration : imports) {
			String nodeName = getNodeName(importDeclaration);
			if (nodeName.endsWith("." + returnType)) {
				return sb.append(getNodeName(importDeclaration)).toString();
			}
		}
		return "";
	}
}
