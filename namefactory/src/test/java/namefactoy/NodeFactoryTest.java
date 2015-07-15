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

package namefactoy;

import static org.junit.Assert.*;

import java.net.URL;
import java.security.CodeSource;

import namefactory.ASTCreator;
import namefactory.InformationVisitor;
import namefactory.NodeFactory;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.junit.BeforeClass;
import org.junit.Test;

import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsParameterName;

public class NodeFactoryTest {

	private static InformationVisitor visitor;

	@BeforeClass
	public static void setup() {
		visitor = new ASTCreator().getVisitor();
	}

	// FieldDeclaration -> CsTypeName
	@Test
	public void csTypeName() {
		ITypeBinding typeBinding = ((FieldDeclaration) visitor.getFields().get(
				0)).getType().resolveBinding();
		String actual = NodeFactory.BindingFactory.getBindingName(typeBinding);
		String expected = "java.lang.String, java.lang, VersionPlaceholder";

		assertEquals(expected, actual);
	}

	// MethodDeclaration -> CsMethodName
	@Test
	public void csMethodName() {
		String actual = NodeFactory.getNodeName(visitor.getMethods().get(2))
				.getIdentifier();
		String expected = "[org.eclipse.jdt.core.dom.CompilationUnit, org.eclipse.jdt.core.dom, VersionPlaceholder]"
				+ " [de.vogella.jdt.astsimple.handler.GetInfo, de.vogella.jdt.astsimple.handler, VersionPlaceholder]"
				+ ".findClass([org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core, VersionPlaceholder] packages, "
				+ "[java.lang.String, java.lang, VersionPlaceholder] className)";

		assertEquals(expected, actual);
	}

	// PackageDeclaration -> CsNamespaceName
	@Test
	public void csNamespaceName() {
		String actual = NodeFactory.getNodeName(visitor.getPackages())
				.getIdentifier();
		String expected = "de.vogella.jdt.astsimple.handler";
		assertEquals(expected, actual);
	}

	// MethodDeclaration -> CsParameterName
	@Test
	public void csParameterName() {
		CsMethodName methodName = (CsMethodName) NodeFactory
				.getNodeName(visitor.getMethods().get(2));
		String actual = methodName.getParameters().get(0).getIdentifier();
		String expected = "[org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core, VersionPlaceholder] packages";
		assertEquals(expected, actual);
	}

	// Implementierung fehlt
	/*
	 @Test
	 public void csAssemblyName() {
	 CodeSource src = String.class.getProtectionDomain().getCodeSource();
	 URL jar = null;
	 if (src != null) {
	 jar = src.getLocation();
	 }
	 String actual = jar.toString();
	 String expected =
	 "[org.eclipse.jdt.core.dom.CompilationUnit, org.eclipse.jdt.core.dom.CompilationUnit, VersionPlaceholder]";
	
	 assertEquals(expected, actual);
	 }
	
	@Test
	public void csAssemblyName() {
		String actual = "";
		String expected = "";
		assertEquals(expected, actual);
	}
	
	@Test
	public void csAssemblyVersion() {
		String actual = "";
		String expected = "";
		assertEquals(expected, actual);
	}
	
	@Test
	public void csFieldName() {
		String actual = "";
		String expected = "";
		assertEquals(expected, actual);
	}
	
	@Test
	public void csLocalVariableName() {
		String actual = "";
		String expected = "";
		assertEquals(expected, actual);
	}
	*/
}
