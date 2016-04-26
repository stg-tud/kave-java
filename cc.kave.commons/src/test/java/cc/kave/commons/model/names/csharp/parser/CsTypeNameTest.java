package cc.kave.commons.model.names.csharp.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.recommenders.exceptions.AssertionException;

public class CsTypeNameTest {

	private List<TypeTestCase> validTestCases;
	private List<TypeTestCase> invalidTestCases;

	@Before
	public void loadTestCases() {
		validTestCases = TypeTestLoader.validTypeNames();
		invalidTestCases = TypeTestLoader.invalidTypeNames();
		assertNotEquals(0, validTestCases.size());
		assertNotEquals(0, invalidTestCases.size());
	}

	@Test
	public void validTestCases() {
		for (TypeTestCase t : validTestCases) {
			ITypeName name = new CsTypeName(t.getIdentifier());
			assertEquals(t.getIdentifier(), name.getIdentifier());
			assertEquals(t.getIdentifier(), t.getNamespace(), name.getNamespace().getIdentifier());
			assertEquals(t.getIdentifier(), t.getAssembly(), name.getAssembly().getIdentifier());
		}
	}

	@Test
	public void invalidTestCases() {
		for (TypeTestCase t : invalidTestCases) {
			try {
				ITypeName name = new CsTypeName(t.getIdentifier());
				fail("Invalid name validated:" + t.getIdentifier());
			} catch (AssertionException e) {
			}
		}
	}
}
