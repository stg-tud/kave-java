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
import org.eclipse.jdt.core.dom.MethodDeclaration;

import visitors.MethodVisitor;

public class PluginAstCreator {

	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
	private CompilationUnit parsed;

	// @Override
	public PluginAstCreator()	{
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		IWorkspaceRoot root = workspace.getRoot();
//		// Get all projects in the workspace
//		IProject[] projects = root.getProjects();
//		// Loop over all projects
//		for (IProject project : projects) {
//			try {
//				if (project.isNatureEnabled(JDT_NATURE)) {
//					analyseMethods(project);
//				}
//			} catch (CoreException e) {
//				e.printStackTrace();
//			}
//		}
		ICompilationUnit compilationunit = getCompilationunit("A", "a", "Activator.java");
		parsed = parse(compilationunit);
		
		MethodVisitor methodVisitor = new MethodVisitor();
		parsed.accept(methodVisitor);
		System.out.println(methodVisitor.getMethods().toString());
	}

//	private void analyseMethods(IProject project) throws JavaModelException {
//		IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
//		 parse(JavaCore.create(project));
//		for (IPackageFragment mypackage : packages) {
//			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
//				createAST(mypackage);
//			}
//
//		}
//	}

	private void createAST(IPackageFragment mypackage) throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			// now create the AST for the ICompilationUnits
			CompilationUnit parse = parse(unit);
			MethodVisitor visitor = new MethodVisitor();
			parse.accept(visitor);

			for (MethodDeclaration method : visitor.getMethods()) {
				System.out.print("Method name: " + method.getName() + " Return type: " + method.getReturnType2());
			}

		}
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

	public ICompilationUnit getCompilationunit(String project, String packageFragment, String cu) {
		List<IJavaProject> javaProjects = getJavaProjects();
		
		for (IJavaProject iJavaProject : javaProjects) {
			try {
				if (iJavaProject.getElementName().equals(project)) {
					IPackageFragmentRoot[] fragmentRoot = iJavaProject.getPackageFragmentRoots();
					for (int i = 0; i < fragmentRoot.length; i++) {
						IPackageFragment fragment = fragmentRoot[i].getPackageFragment(packageFragment);
						String elementName = fragment.getElementName();
						if (fragment.getElementName().equals(packageFragment)) {
							return fragment.getCompilationUnit(cu);
						}
					}
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
