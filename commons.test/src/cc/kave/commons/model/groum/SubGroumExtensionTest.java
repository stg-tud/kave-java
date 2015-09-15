package cc.kave.commons.model.groum;

import java.util.Collections;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static cc.kave.commons.model.groum.GroumTestUtils.*;
import static cc.kave.commons.model.groum.GroumBuilder.*;
public class SubGroumExtensionTest {

	@Test
	public void noExtension() {
		Node[] nodes = createNodes("A");
		Groum groum = buildGroum(nodes).build();
		SubGroum uut = createSubGroum(groum, nodes[0]);
		
		List<SubGroum> extensions = uut.computeExtensions(createNodes("B")[0]);
		
		assertEquals(0, extensions.size());
	}
	
	@Test
	public void oneExtension() {
		Node[] nodes = createNodes("A", "B");
		Groum groum = buildGroum(nodes).withEdge(nodes[0], nodes[1]).build();
		SubGroum uut = new SubGroum(groum, Collections.singleton(nodes[0]));
		
		List<SubGroum> extensions = uut.computeExtensions(nodes[1]);
		
		SubGroum extension = createSubGroum(groum, nodes[0], nodes[1]);
		assertThat(extensions, containsSubGroum(extension));
	}
	
	@Test
	public void oneExtensionOneRelevantEdge() {
		Node[] nodes = createNodes("A", "B", "C");
		Groum groum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2])
				.withEdge(nodes[2], nodes[1]).build();
		SubGroum uut = createSubGroum(groum, nodes[0]);
		
		List<SubGroum> extensions = uut.computeExtensions(nodes[1]);
		
		SubGroum extension = createSubGroum(groum, nodes[0], nodes[1]);
		assertThat(extensions, containsSubGroum(extension));
	}
	
	@Test
	public void oneExtensionWithMultipeEdges() {
		Node[] nodes = createNodes("A", "B", "C");
		Groum groum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2])
				.withEdge(nodes[2], nodes[1]).build();
		SubGroum uut = createSubGroum(groum, nodes[0], nodes[2]);
		
		List<SubGroum> extensions = uut.computeExtensions(nodes[1]);
		
		SubGroum extension = createSubGroum(groum, nodes[0], nodes[1], nodes[2]);
		assertThat(extensions, containsSubGroum(extension));
	}
	
	@Test
	public void multipleExtensions() {
		Node[] nodes = createNodes("A", "B", "B");
		Groum groum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2]).build();
		SubGroum uut = createSubGroum(groum, nodes[0]);
		
		List<SubGroum> extensions = uut.computeExtensions(nodes[1]);
		
		SubGroum extension1 = createSubGroum(groum, nodes[0], nodes[1]);
		SubGroum extension2 = createSubGroum(groum, nodes[0], nodes[2]);
		
		assertThat(extensions, containsSubGroums(extension1, extension2));
	}
	
	private static Matcher<Iterable<SubGroum>> containsSubGroum(SubGroum subGroum) {
		return containsSubGroums(subGroum);
	}
	
	private static Matcher<Iterable<SubGroum>> containsSubGroums(SubGroum... subGroums) {
		return new BaseMatcher<Iterable<SubGroum>>() {

			@Override
			public boolean matches(Object item) {
				if (item instanceof Iterable<?>) {
					@SuppressWarnings("unchecked")
					Iterable<SubGroum> others = (Iterable<SubGroum>) item;
					for (SubGroum subgroum : subGroums) {
						if (!contains(others, subgroum)) return false;
					}
					return true;
				}
				return false;
			}

			private boolean contains(Iterable<SubGroum> others, SubGroum subgroum) {
				for (IGroum element : others) {
						SubGroum other = (SubGroum) element;
						if (new DFSGroumComparator().compare(subgroum, other) == 0)
							return true;
				}
				return false;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("a collection containing the SubGroums ");
				description.appendValueList("[", ",", "]", subGroums);
			}
		};
	}
}
