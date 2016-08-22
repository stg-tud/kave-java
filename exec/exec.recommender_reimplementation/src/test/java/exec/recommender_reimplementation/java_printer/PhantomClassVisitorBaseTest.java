package exec.recommender_reimplementation.java_printer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import exec.recommender_reimplementation.java_printer.javaPrinterTestSuite.JavaPrintingVisitorBaseTest;

public class PhantomClassVisitorBaseTest extends JavaPrintingVisitorBaseTest {

	PhantomClassVisitor sut;

	public void assertEmptySSTs(ISST testSST, ITypeName... expectedTypes) {
		Map<ITypeName, SST> actual = generatePhantomClasses(testSST);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		for (ITypeName typeName : expectedTypes) {
			SST expectedSST = new SST();
			expectedSST.setEnclosingType(typeName);
			expected.put(typeName, expectedSST);
		}
		assertEquals(expected, actual);
	}

	public void assertNoSSTsGenerated(ISST testSST) {
		Map<ITypeName, SST> actual = generatePhantomClasses(testSST);
		assertTrue(actual.isEmpty());
	}

	public void assertGeneratedFieldsInSST(ISST testSST, ITypeName expectedTypeName, IFieldName... fieldNames) {
		Map<ITypeName, SST> actual = generatePhantomClasses(testSST);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(expectedTypeName);
		for (IFieldName fieldName : fieldNames) {
			expectedSST.getFields().add(fieldDecl(fieldName));
		}
		expected.put(expectedTypeName, expectedSST);
		assertEquals(expected, actual);
	}

	public void assertGeneratedPropertiesInSST(ISST testSST, ITypeName expectedTypeName,
			IPropertyName... propertyNames) {
		Map<ITypeName, SST> actual = generatePhantomClasses(testSST);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(expectedTypeName);
		for (IPropertyName propertyName : propertyNames) {
			expectedSST.getProperties().add(propertyDecl(propertyName));
		}
		expected.put(expectedTypeName, expectedSST);
		assertEquals(expected, actual);
	}

	public void assertGeneratedMethodsInSST(ISST testSST, ITypeName expectedTypeName,
			IMethodDeclaration... methodDecls) {
		Map<ITypeName, SST> actual = generatePhantomClasses(testSST);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = createSSTWithMethods(expectedTypeName, methodDecls);

		expected.put(expectedTypeName, expectedSST);
		assertEquals(expected, actual);
	}

	public void assertGeneratedSSTs(ISST testSST, ISST... expectedSSTs) {
		Map<ITypeName, SST> actual = generatePhantomClasses(testSST);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		for (ISST expectedSST : expectedSSTs) {
			expected.put(expectedSST.getEnclosingType(), (SST) expectedSST);
		}
		assertEquals(expected, actual);
	}

	protected SST createSSTWithMethods(ITypeName typeName, IMethodDeclaration... methodDecls) {
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(typeName);
		expectedSST.setMethods(Sets.newHashSet(methodDecls));
		return expectedSST;
	}

	protected SST createSSTWithFields(ITypeName typeName, IFieldDeclaration... fieldDecls) {
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(typeName);
		expectedSST.setFields(Sets.newHashSet(fieldDecls));
		return expectedSST;
	}

	protected SST createSSTWithProperties(ITypeName typeName, IPropertyDeclaration... propertyDecls) {
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(typeName);
		expectedSST.setProperties(Sets.newHashSet(propertyDecls));
		return expectedSST;
	}

	private Map<ITypeName, SST> generatePhantomClasses(ISST testSST) {
		Map<ITypeName, SST> phantomClasses = Maps.newHashMap();
		testSST.accept(sut, phantomClasses);
		return phantomClasses;
	}
}
