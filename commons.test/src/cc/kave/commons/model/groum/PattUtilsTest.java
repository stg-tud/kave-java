package cc.kave.commons.model.groum;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.pattexplore.Utils;

public class PattUtilsTest {

	@Test
	public void splitsCorrectly() {
		Groum groum = Fixture.getExampleGroum();
		List<SubGroum> subgroums = Utils.breakdown(groum);
		assertTrue(subgroums.size() == 13);
	}

	@Test
	public void breakdownworks() {
		List<Groum> groums = Fixture.getListOfXGroums(10);
		List<SubGroum> subgroums = new LinkedList<>();
		for (Groum groum : groums) {
			subgroums.addAll(Utils.breakdown(groum));
		}
		Collections.sort(subgroums);
		assertTrue(subgroums.size() == 55);
	}
}
