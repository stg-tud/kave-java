package cc.kave.commons.model.groum;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.Test;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;

import com.google.common.collect.Multimap;

import static org.junit.Assert.assertEquals;

import static cc.kave.commons.model.groum.GroumTestUtils.*;

public class GroumTest {
	
	@Test
	public void producesSubGroumsOfSizeOne() {
		Node[] nodes = createNodes("A", "B");
		Groum groum = createGroum(nodes);
		
		Multimap<SubGroum, SubGroum> atomicSubGroums = groum.getAtomicSubGroums();
		
		assertContainsAll(atomicSubGroums,
				createSubGroum(groum, nodes[0]),
				createSubGroum(groum, nodes[1]));
	}
	
	@Test
	public void producesMultipleSubGroumsForEqualNodes() {
		Node[] nodes = createNodes("A", "A");
		Groum groum = createGroum(nodes);
		
		Multimap<SubGroum, SubGroum> atomicSubGroums = groum.getAtomicSubGroums();
		
		assertContainsAll(atomicSubGroums,
				createSubGroum(groum, nodes[0]),
				createSubGroum(groum, nodes[1]));
	}

	private SubGroum createSubGroum(Groum groum, Node node) {
		SubGroum subGroum = new SubGroum(groum);
		subGroum.addNode(node);
		return subGroum;
	}
	
	static void assertContainsAll(Multimap<SubGroum, SubGroum> actuals, Groum... expecteds) {
		TreeSet<Groum> actual = new TreeSet<Groum>(new DFSGroumComparator());
		actual.addAll(actuals.values());
		TreeSet<Groum> expected = new TreeSet<Groum>(new DFSGroumComparator());
		expected.addAll(Arrays.asList(expecteds));
		assertEquals(expected, actual);
	}
}
