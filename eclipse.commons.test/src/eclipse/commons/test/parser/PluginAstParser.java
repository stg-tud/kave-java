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

package eclipse.commons.test.parser;

import java.util.ArrayList;
import java.util.HashSet;
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
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.eclipse.commons.analysis.transformer.DeclarationVisitor;
import cc.kave.eclipse.commons.analysis.completiontarget.*;

public class PluginAstParser {

	private static final String JAVA_NATURE = "org.eclipse.jdt.core.javanature";
	private CompilationUnit parsed;
	private List<IJavaProject> javaProjects;
	private SST context = new SST();

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

		DeclarationVisitor declVisitor = new DeclarationVisitor(context, new HashSet<>(), null);
		
		parsed.accept(declVisitor);
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
	
	public boolean containsField(FieldDeclaration decl){
		return context.getFields().contains(decl);
	}
}
