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

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import namefactory.StandaloneAstParser;
import namefactory.NodeFactory;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import visitors.FieldVisitor;
import visitors.ImportVisitor;
import visitors.MethodVisitor;
import visitors.PackageVisitor;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsMethodName;

public class NodeFactoryTest {

	private static StandaloneAstParser astCreator;
	// private static InformationVisitor visitor;
	private static MethodVisitor methodVisitor;
	private static FieldVisitor fieldVisitor;
	private static ImportVisitor importVisitor;
	private static PackageVisitor packageVisitor;

	@BeforeClass
	public static void setupClass() {
		astCreator = new StandaloneAstParser();
	}

	@Before
	public void setup() {
		methodVisitor = new MethodVisitor();
		fieldVisitor = new FieldVisitor();
		importVisitor = new ImportVisitor();
		packageVisitor = new PackageVisitor();

		astCreator.accept(methodVisitor);
		astCreator.accept(fieldVisitor);
		astCreator.accept(importVisitor);
		astCreator.accept(packageVisitor);

		// try {
		// runAllTests(this);
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InvocationTargetException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void runAllTests() throws IllegalAccessException, InvocationTargetException {
		setupClass();

		methodVisitor = new MethodVisitor();
		fieldVisitor = new FieldVisitor();
		importVisitor = new ImportVisitor();
		packageVisitor = new PackageVisitor();

		astCreator.accept(methodVisitor);
		astCreator.accept(fieldVisitor);
		astCreator.accept(importVisitor);
		astCreator.accept(packageVisitor);

		for (Method m : getClass().getDeclaredMethods()) {
			Annotation[] annos = m.getAnnotations();
			if (containsTestAnnotation(annos)) {
				m.invoke(getClass());
			}
		}
	}

	private static boolean containsTestAnnotation(Annotation[] annos) {
		for (Annotation a : annos) {
			if (a.annotationType().equals(Test.class)) {
				return true;
			}
		}
		return false;
	}

	// FieldDeclaration -> CsTypeName
	@Test
	public void csTypeName() {
		ITypeBinding typeBinding = ((FieldDeclaration) fieldVisitor.getField("JDT_NATURE")).getType().resolveBinding();
		String actual = NodeFactory.BindingFactory.getBindingName(typeBinding);
		String expected = "java.lang.String, rt.jar";

		assertEquals(expected, actual);
	}

	// MethodDeclaration -> CsMethodName
	@Test
	public void csMethodName() {

		// Name actual = ReadTestMethod("TextXY", "Method3");
		// Name expected = CsMethodName.newMethodName("[T,P] [T,P].M()");
		// assertEquals(expected, actual);

		String actual = NodeFactory
				.getNodeName(methodVisitor.getMethod("findClass(IPackageFragment packages, String className)"))
				.getIdentifier();
		String expected = "[org.eclipse.jdt.core.dom.CompilationUnit, org.eclipse.jdt.core-3.10.0.jar]"
				+ " [de.vogella.jdt.astsimple.handler.GetInfo, /D:/Eclipse%20Workspace/de.vogella.jdt.astsimple/target/classes/]"
				+ ".findClass([org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core-3.10.0.jar] packages, "
				+ "[java.lang.String, rt.jar] className)";

		assertEquals(expected, actual);
	}

	private Name toName(ASTNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	private Name ReadTestMethod(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	// PackageDeclaration -> CsNamespaceName
	@Test
	public void csNamespaceName() {
		String actual = NodeFactory.getNodeName(packageVisitor.getPackage()).getIdentifier();
		String expected = "de.vogella.jdt.astsimple.handler";
		assertEquals(expected, actual);
	}

	// MethodDeclaration -> CsParameterName
	@Test
	public void csParameterName() {
		MethodName methodName = (CsMethodName) NodeFactory
				.getNodeName(methodVisitor.getMethod("findClass(IPackageFragment packages," + " String className)"));
		String actual = methodName.getParameters().get(0).getIdentifier();
		String expected = "[org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core-3.10.0.jar] packages";
		assertEquals(expected, actual);
	}

	// Implementierung fehlt
	/*
	 * @Test public void csAssemblyName() { CodeSource src =
	 * String.class.getProtectionDomain().getCodeSource(); URL jar = null; if
	 * (src != null) { jar = src.getLocation(); } String actual =
	 * jar.toString(); String expected =
	 * "[org.eclipse.jdt.core.dom.CompilationUnit, org.eclipse.jdt.core.dom.CompilationUnit, VersionPlaceholder]"
	 * ;
	 * 
	 * assertEquals(expected, actual); }
	 * 
	 * @Test public void csAssemblyName() { String actual = ""; String expected
	 * = ""; assertEquals(expected, actual); }
	 * 
	 * @Test public void csAssemblyVersion() { String actual = ""; String
	 * expected = ""; assertEquals(expected, actual); }
	 * 
	 * @Test public void csFieldName() { String actual = ""; String expected =
	 * ""; assertEquals(expected, actual); }
	 * 
	 * @Test public void csLocalVariableName() { String actual = ""; String
	 * expected = ""; assertEquals(expected, actual); }
	 */
}
