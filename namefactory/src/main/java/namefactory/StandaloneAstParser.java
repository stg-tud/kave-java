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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import visitors.InformationVisitor;
import visitors.MethodDeclarationVisitor;

public class StandaloneAstParser {

	private InformationVisitor visitor = new InformationVisitor();
	private MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();
	private CompilationUnit cu;

	public StandaloneAstParser() {
		String path = "D:\\Eclipse Workspace\\de.vogella.jdt.astsimple\\src\\de\\vogella\\jdt\\astsimple\\handler\\GetInfo.java";
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);

		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

		String unitName = "GetInfo.java";
		parser.setUnitName(unitName);

		String source;
		try {
			source = readFile(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String[] sources = { "D:\\Eclipse Workspace\\de.vogella.jdt.astsimple\\src" };
		String[] classpath = getJarPaths("D:\\Eclipse\\plugins");
		classpath = Arrays.copyOf(classpath, classpath.length + 1);
		classpath[classpath.length - 1] = getRtJar();

		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
		parser.setSource(source.toCharArray());

		cu = (CompilationUnit) parser.createAST(null);

		if (cu.getAST().hasBindingsRecovery()) {
			System.out.println("Binding activated.");
		}

		cu.accept(visitor);
		cu.accept(methodVisitor);
	}

	private String[] getJarPaths(String path) {
		File file = new File(path);
		String[] jarPaths = file.list();

		for (int i = 0; i < jarPaths.length; i++) {
			jarPaths[i] = path + "\\" + jarPaths[i];
		}
		return jarPaths;
	}

	private String getRtJar() {
		File file = new File(System.getenv("JAVA_HOME"));

		for (File f : file.getParentFile().listFiles()) {
			if (f.getName().startsWith("jre")) {
				return f.getPath().concat("\\lib\\rt.jar");
			}
		}

		return "";
	}

	private String readFile(String path) throws IOException {
		File file = new File(path);
		String source = FileUtils.readFileToString(file);
		return source;
	}

	public InformationVisitor getVisitor() {
		return visitor;
	}

	public CompilationUnit getCompilationUnit() {
		return cu;
	}

	public void accept(ASTVisitor visitor) {
		cu.accept(visitor);
	}

	public void runTests() {
		NodeFactoryTest nodeFactoryTest = new NodeFactoryTest();
		try {
			nodeFactoryTest.runAllTests();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public MethodDeclaration getMethodDeclaration(String projectName, String qualifiedName, String methodSignature) {
		return methodVisitor.getMethod(methodSignature);
	}
}