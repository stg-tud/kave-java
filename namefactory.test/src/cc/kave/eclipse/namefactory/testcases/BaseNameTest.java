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

package cc.kave.eclipse.namefactory.testcases;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;

import cc.kave.commons.model.names.Name;
import cc.kave.eclipse.namefactory.NodeFactory;
import cc.kave.eclipse.namefactory.astparser.PluginAstParser;

/**
 * Qualified names in this class are separated by the char ';' with the
 * following structure: "packageName;className;methodName" or
 * "packageName;className;methodSignature" for example.
 * 
 *
 */
public class BaseNameTest {

	private final String defaultTestProject = "test.project.a";

	protected static void assertName(Name expected, ASTNode node) {
		Name actual = NodeFactory.createNodeName(node);
		assertEquals(expected, actual);
	}

	protected MethodDeclaration findMethodDeclaration(String qualifiedName) {
		return findMethod(defaultTestProject, qualifiedName);
	}

	protected MethodDeclaration findMethod(String projectName, String qualifiedName) {
		String[] split = qualifiedName.split(";");
		String qName = split[0] + ";" + split[1] + ".java";
		String signature = split[2];

		PluginAstParser p = new PluginAstParser(projectName, qName);
		return p.getMethod(signature);
	}

	protected Expression findMethodInvocation(String qualifiedName) {
		return findMethodInvocation(defaultTestProject, qualifiedName);
	}

	protected Expression findMethodInvocation(String projectName, String qualifiedName) {
		String[] split = qualifiedName.split(";");
		String qName = split[0] + ";" + split[1] + ".java";
		String signature = split[2];

		PluginAstParser p = new PluginAstParser(projectName, qName);
		return p.getMethodInvocation(signature);
	}

	protected ASTNode findField(String qualifiedName) {
		return findField(defaultTestProject, qualifiedName);
	}

	protected ASTNode findField(String projectName, String qualifiedName) {
		String[] split = qualifiedName.split(";");
		String qName = split[0] + ";" + split[1] + ".java";
		String identifier = split[2];

		PluginAstParser p = new PluginAstParser(projectName, qName);
		return p.getField(identifier);
	}

	protected ASTNode findVariable(String qualifiedName) {
		return findVariable(defaultTestProject, qualifiedName);
	}

	protected ASTNode findVariable(String projectName, String qualifiedName) {
		String[] split = qualifiedName.split(";");
		String qName = split[0] + ";" + split[1] + ".java";
		String identifier = split[2];

		PluginAstParser p = new PluginAstParser(projectName, qName);
		return p.getVariable(identifier);
	}

	protected ASTNode findPackage(String qualifiedName) {
		return findPackage(defaultTestProject, qualifiedName);
	}

	protected ASTNode findPackage(String projectName, String qualifiedName) {
		String[] split = qualifiedName.split(";");
		String qName = split[0] + ";" + split[1] + ".java";

		PluginAstParser p = new PluginAstParser(projectName, qName);
		return p.getPackage();
	}

	protected ASTNode findImport(String qualifiedName) {
		return findImport(defaultTestProject, qualifiedName);
	}

	protected ASTNode findImport(String projectName, String qualifiedName) {
		String[] split = qualifiedName.split(";");
		String qName = split[0] + ";" + split[1] + ".java";
		String identifier = split[2];

		PluginAstParser p = new PluginAstParser(projectName, qName);
		return p.getImport(identifier);
	}

	protected QualifiedName findQualifiedName(String qualifiedName) {
		return findQualifiedName(defaultTestProject, qualifiedName);
	}

	protected QualifiedName findQualifiedName(String projectName, String qualifiedName) {
		String[] split = qualifiedName.split(";");
		String qName = split[0] + ";" + split[1] + ".java";
		String identifier = split[2];

		PluginAstParser p = new PluginAstParser(projectName, qName);
		return p.getName(identifier);
	}
}