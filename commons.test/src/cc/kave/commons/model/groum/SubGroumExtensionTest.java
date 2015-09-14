package cc.kave.commons.model.groum;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static cc.kave.commons.model.groum.GroumTestUtils.*;
public class SubGroumExtensionTest {

	@Test
	public void noExtension() {
		Node[] nodes = createNodes("A");
		Groum groum = createGroum(nodes);
		SubGroum uut = new SubGroum(groum);
		uut.addNode(nodes[0]);
		
		List<SubGroum> extensions = uut.computeExtensions(createNodes("B")[0]);
		
		assertEquals(0, extensions.size());
	}
	
	@Test
	public void oneExtension() {
		Node[] nodes = createNodes("A", "B");
		Groum groum = createGroum(nodes);
		groum.addEdge(nodes[0], nodes[1]);
		SubGroum uut = new SubGroum(groum);
		uut.addNode(nodes[0]);
		
		List<SubGroum> extensions = uut.computeExtensions(nodes[1]);
		
		SubGroum extension = new SubGroum(groum);
		extension.addNode(nodes[0]);
		extension.addNode(nodes[1]);
		extension.addEdge(nodes[0], nodes[1]);
		
		assertThat(extensions, containsSubGroum(extension));
	}
	
	@Test
	public void oneExtensionOneRelevantEdge() {
		Node[] nodes = createNodes("A", "B", "C");
		Groum groum = createGroum(nodes);
		groum.addEdge(nodes[0], nodes[1]);
		groum.addEdge(nodes[0], nodes[2]);
		groum.addEdge(nodes[2], nodes[1]);
		SubGroum uut = new SubGroum(groum);
		uut.addNode(nodes[0]);
		
		List<SubGroum> extensions = uut.computeExtensions(nodes[1]);
		
		SubGroum extension = new SubGroum(groum);
		extension.addNode(nodes[0]);
		extension.addNode(nodes[1]);
		extension.addEdge(nodes[0], nodes[1]);
		
		assertThat(extensions, containsSubGroum(extension));
	}
	
	@Test
	public void oneExtensionWithMultipeEdges() {
		Node[] nodes = createNodes("A", "B", "C");
		Groum groum = createGroum(nodes);
		groum.addEdge(nodes[0], nodes[1]);
		groum.addEdge(nodes[0], nodes[2]);
		groum.addEdge(nodes[2], nodes[1]);
		SubGroum uut = new SubGroum(groum);
		uut.addNode(nodes[0]);
		uut.addNode(nodes[2]);
		uut.addEdge(nodes[0], nodes[2]);
		
		List<SubGroum> extensions = uut.computeExtensions(nodes[1]);
		
		SubGroum extension = new SubGroum(groum);
		extension.addNode(nodes[0]);
		extension.addNode(nodes[1]);
		extension.addNode(nodes[2]);
		extension.addEdge(nodes[0], nodes[1]);
		extension.addEdge(nodes[0], nodes[2]);
		extension.addEdge(nodes[2], nodes[1]);
		
		assertThat(extensions, containsSubGroum(extension));
	}
	
	@Test
	public void multipleExtensions() {
		Node[] nodes = createNodes("A", "B", "B");
		Groum groum = createGroum(nodes);
		groum.addEdge(nodes[0], nodes[1]);
		groum.addEdge(nodes[0], nodes[2]);
		SubGroum uut = new SubGroum(groum);
		uut.addNode(nodes[0]);
		
		List<SubGroum> extensions = uut.computeExtensions(nodes[1]);
		
		SubGroum extension1 = new SubGroum(groum);
		extension1.addNode(nodes[0]);
		extension1.addNode(nodes[1]);
		extension1.addEdge(nodes[0], nodes[1]);		
		SubGroum extension2 = new SubGroum(groum);
		extension2.addNode(nodes[0]);
		extension2.addNode(nodes[2]);
		extension2.addEdge(nodes[0], nodes[2]);
		
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
				for (SubGroum element : others) {
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
