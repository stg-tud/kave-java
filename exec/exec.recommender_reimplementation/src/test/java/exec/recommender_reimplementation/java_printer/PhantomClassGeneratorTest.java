package exec.recommender_reimplementation.java_printer;

import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public class PhantomClassGeneratorTest extends PhantomClassVisitorBaseTest {

	private PhantomClassGenerator phantomClassGenerator;

	@Before
	public void setUp() throws Exception {
		phantomClassGenerator = new PhantomClassGenerator();
	}

	@Test
	public void addsMethodDeclarationsToOverridenClasses() {
		ITypeName thisType = Names.newType("TestClass,P");
		ITypeName superType = Names.newType("SuperClass,P");
		ITypeName firstType = Names.newType("FirstClass,P");

		IMethodDeclaration methodDecl = methodDecl(method(type("T1"), thisType, "m1"));
		TypeShape typeShape = new TypeShape();
		MethodHierarchy methodHierarchy = new MethodHierarchy();
		methodHierarchy.setElement(method(type("T1"), thisType, "m1"));
		methodHierarchy.setSuper(method(type("T1"), superType, "m1"));
		methodHierarchy.setFirst(method(type("T1"), firstType, "m1"));
		typeShape.setMethodHierarchies(Sets.newHashSet(methodHierarchy));
		
		SST sst = createSSTWithMethods(thisType, methodDecl);

		Context context = new Context();
		context.setSST(sst);
		context.setTypeShape(typeShape);

		Set<ISST> actual = phantomClassGenerator.convert(Sets.newHashSet(context));

		Set<ISST> expected = Sets.newHashSet(
				createSSTWithMethods(superType,
						methodDecl(method(type("T1"), superType, "m1"), returnStatement(constant("null")))),
				createSSTWithMethods(firstType,
						methodDecl(method(type("T1"), firstType, "m1"), returnStatement(constant("null")))),
				createSSTWithMethods(type("T1")), createSSTWithMethods(Names.newType("p:object")));

		assertEquals(expected, actual);
	}

}
