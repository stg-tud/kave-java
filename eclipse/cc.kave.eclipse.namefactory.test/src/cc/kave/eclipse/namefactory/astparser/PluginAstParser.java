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

package cc.kave.eclipse.namefactory.astparser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;

import cc.kave.eclipse.namefactory.visitors.ImportVisitor;
import cc.kave.eclipse.namefactory.visitors.MethodDeclarationVisitor;
import cc.kave.eclipse.namefactory.visitors.MethodInvocationVisitor;
import cc.kave.eclipse.namefactory.visitors.PackageVisitor;
import cc.kave.eclipse.namefactory.visitors.QualifiedNameVisitor;
import cc.kave.eclipse.namefactory.visitors.VariableDeclarationVisitor;

public class PluginAstParser {

	private static final String JAVA_NATURE = "org.eclipse.jdt.core.javanature";
	private CompilationUnit parsed;
	private List<IJavaProject> javaProjects;
	private VariableDeclarationVisitor variableDeclarationVisitor;
	private MethodDeclarationVisitor methodDeclarationVisitor;
	private MethodInvocationVisitor methodInvocationVisitor;
	private PackageVisitor packageVisitor;
	private ImportVisitor importVisitor;
	private QualifiedNameVisitor qualifiedNameVisitor;

	/**
	 * Creates an AST and passes some visitors for retrieving some AST data.
	 * 
	 * @param projectName
	 *            The name of the java project
	 * @param packageName
	 *            The packagename which contains the compilationunit
	 * @param cuName
	 *            The name of the compilationunit which get parsed, for example
	 *            "Test.java"
	 */
	public PluginAstParser(String projectName, String qualifiedName) {
		javaProjects = getJavaProjects();
		initializeAst(projectName, qualifiedName);
	}

	private void initializeAst(String projectName, String qualifiedName) {
		ICompilationUnit compilationUnit = getCompilationunit(projectName, qualifiedName);
		parsed = parse(compilationUnit);

		variableDeclarationVisitor = new VariableDeclarationVisitor();
		methodDeclarationVisitor = new MethodDeclarationVisitor();
		methodInvocationVisitor = new MethodInvocationVisitor();
		packageVisitor = new PackageVisitor();
		importVisitor = new ImportVisitor();
		qualifiedNameVisitor = new QualifiedNameVisitor();

		parsed.accept(variableDeclarationVisitor);
		parsed.accept(methodDeclarationVisitor);
		parsed.accept(qualifiedNameVisitor);
		parsed.accept(methodInvocationVisitor);
		parsed.accept(packageVisitor);
		parsed.accept(importVisitor);
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the
	 * Java source file
	 * 
	 * @param unit
	 * @return
	 */
	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}

	/**
	 * 
	 * @return A complete list of all java projects in the current workspace.
	 */
	private List<IJavaProject> getJavaProjects() {
		List<IJavaProject> javaProjects = new ArrayList<IJavaProject>();
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

		for (IProject project : myWorkspaceRoot.getProjects()) {
			try {
				if (project.isNatureEnabled(JAVA_NATURE)) {
					IJavaProject javaProject = JavaCore.create(project);
					javaProjects.add(javaProject);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return javaProjects;
	}

	private ICompilationUnit getCompilationunit(String project, String qualifiedName) {
		String[] split = qualifiedName.split(";");

		for (IJavaProject iJavaProject : javaProjects) {
			if (iJavaProject.getElementName().equals(project)) {
				try {
					return iJavaProject.findType(split[0], split[1]).getCompilationUnit();
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param signature
	 *            Expects a method signature or only the name of the method but
	 *            returns only the first match.
	 * @return The method with a matching signature.
	 */
	public MethodDeclaration getMethod(String signature) {
		return methodDeclarationVisitor.getMethod(signature);
	}

	public Expression getMethodInvocation(String signature) {
		return methodInvocationVisitor.getMethod(signature);
	}

	public ASTNode getField(String name) {
		return variableDeclarationVisitor.getField(name);
	}

	public ASTNode getVariable(String name) {
		return variableDeclarationVisitor.getVariable(name);
	}

	public PackageDeclaration getPackage() {
		return packageVisitor.getPackage();
	}

	public ImportDeclaration getImport(String name) {
		return importVisitor.getImport(name);
	}

	public QualifiedName getName(String name) {
		return qualifiedNameVisitor.getName(name);
	}
}
