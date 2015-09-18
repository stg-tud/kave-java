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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import visitors.FieldVisitor;
import visitors.ImportVisitor;
import visitors.MethodVisitor;
import visitors.PackageVisitor;

public class PluginAstParser {

	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
	private CompilationUnit parsed;
	private List<IJavaProject> javaProjects;
	private FieldVisitor fieldVisitor;
	private MethodVisitor methodVisitor;
	private PackageVisitor packageVisitor;
	private ImportVisitor importVisitor;

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
	public PluginAstParser(String projectName, String packageName, String cuName) {
		javaProjects = getJavaProjects();
		initializeAst(projectName, packageName, cuName);
	}

	private void initializeAst(String projectName, String packageName, String cuName) {
		ICompilationUnit compilationUnit = getCompilationunit(projectName, packageName, cuName);
		parsed = parse(compilationUnit);

		fieldVisitor = new FieldVisitor();
		methodVisitor = new MethodVisitor();
		packageVisitor = new PackageVisitor();
		importVisitor = new ImportVisitor();

		parsed.accept(methodVisitor);
		parsed.accept(fieldVisitor);
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
				if (project.isNatureEnabled(JDT_NATURE)) {
					IJavaProject javaProject = JavaCore.create(project);
					javaProjects.add(javaProject);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return javaProjects;
	}

	private ICompilationUnit getCompilationunit(String project, String packageName, String cu) {
		for (IJavaProject iJavaProject : javaProjects) {
			if (iJavaProject.getElementName().equals(project)) {
				try {
					return iJavaProject.findType(packageName, cu).getCompilationUnit();
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public MethodDeclaration getMethod(String signature) {
		return methodVisitor.getMethod(signature);
	}

	public FieldDeclaration getField(String name) {
		return fieldVisitor.getField(name);
	}

	public PackageDeclaration getPackage() {
		return packageVisitor.getPackage();
	}

	public ImportDeclaration getImport(String name) {
		return importVisitor.getImport(name);
	}
}
