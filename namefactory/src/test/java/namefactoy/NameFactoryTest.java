package namefactoy;

import static org.junit.Assert.assertEquals;

import java.io.File;

import namefactory.ASTCreator;
import namefactory.InformationVisitor;
import namefactory.NameFactory;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class NameFactoryTest {

	private InformationVisitor visitor;

	@BeforeClass
	public static void setupClass(){
		System.out.println("before class");
	}
	
	@Before
	public void setup() {
		System.out.println("before");
		visitor = new ASTCreator().getVisitor();
	}

	@Test
	public void testMethodDeclaration() {
		String expected = "de.vogella.jdt.astsimple.handler.GetInfo.refreshPackage";
		String actual = NameFactory.getNodeName(visitor.getMethods().get(1));
		assertEquals(expected, actual);
	}

	@Test
	public void testFieldDeclaration() {
		String expected = "de.vogella.jdt.astsimple.handler.GetInfo.testNumber";
		String actual = NameFactory.getNodeName(visitor.getFields().get(1));
		assertEquals(expected, actual);
	}

	@Test
	public void testPackageDeclaration() {
		String expected = "de.vogella.jdt.astsimple.handler";
		String actual = NameFactory.getNodeName(visitor.getPackages().get(0));
		assertEquals(expected, actual);
	}

	@Test
	public void testReturnTypeDeclaration() {
		String expected = "org.eclipse.jdt.core.dom.CompilationUnit";
		String actual = NameFactory.getReturnType(visitor.getMethods().get(2));
		assertEquals(expected, actual);
	}

	@Test
	public void testParameterTypeDeclaration() {
		String expected = "org.eclipse.jdt.core.IPackageFragment; java.lang.String; ";
		String actual = NameFactory.getParameterType(visitor.getMethods()
				.get(2));
		assertEquals(expected, actual);
	}

	@Test
	public void testImportDeclaration() {
		String expected = "org.eclipse.core.commands.ExecutionEvent";
		String actual = NameFactory.getNodeName(visitor.getImports().get(4));
		assertEquals(expected, actual);
	}

	@Test
	public void testJarReturnTypeDeclaration() {
		String expected = "org.apache.commons.io.FileUtils";
		String actual = NameFactory.getReturnType(visitor.getMethods().get(7));
		assertEquals(expected, actual);
	}

	@Test
	public void testStatement() {
		String expected = "List<CompilationUnit> units=new ArrayList<CompilationUnit>();\n";
		String actual = NameFactory
				.getStatement(visitor.getMethods().get(3), 0);
		assertEquals(expected, actual);
	}

	@Test
	public void testStatement2() {
		String expected = "IProject project=root.getProjects()[0];\n";
		String actual = NameFactory
				.getStatement(visitor.getMethods().get(4), 2);
		assertEquals(expected, actual);
	}

	@Test
	public void testVariableDeclarationtStatement() {
		String expected = "project";
		String actual = ((VariableDeclarationFragment) ((VariableDeclarationStatement) visitor
				.getMethods().get(4).getBody().statements().get(2)).fragments()
				.get(0)).resolveBinding().getName();
		assertEquals(expected, actual);
	}
}
