package cc.kave.commons.model.names.csharp.parser;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.exceptions.AssertionException;

public class CsMethodNameTest {
	private List<MethodNameTestCase> validTestCases;
	private List<String> invalidTestCases;

	@Before
	public void loadTestCases() {
		validTestCases = TypeTestLoader.validMethodNames();
		invalidTestCases = TypeTestLoader.invalidMethodNames();
		assertNotEquals(0, validTestCases.size());
		assertNotEquals(0, invalidTestCases.size());
	}

	@Test
	public void validTestCases() {
		for (MethodNameTestCase t : validTestCases) {
		}
	}

	@Test
	public void invalidTestCases() {
		for (String t : invalidTestCases) {
			try {
				fail("Invalid name validated:" + t);
			} catch (AssertionException e) {
			}
		}
	}
}
