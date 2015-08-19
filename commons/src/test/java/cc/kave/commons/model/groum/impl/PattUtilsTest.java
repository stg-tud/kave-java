package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.nodes.IActionNode;
import cc.kave.commons.model.groum.nodes.impl.ActionNode;
import cc.kave.commons.model.pattexplore.Utils;

public class PattUtilsTest {

	@Test
	public void splitsCorrectly() {
		IGroum groum = Fixture.getExampleGroum();
		List<ISubGroum> subgroums = Utils.breakdown(groum);
		assertTrue(subgroums.size() == 13);
	}

	@Test
	public void removesCorrectly() {
		IGroum groum = Fixture.getExampleGroum();
		List<ISubGroum> subgroums = Utils.breakdown(groum);
		ActionNode node = new ActionNode("StringBuffer", IActionNode.CONTRUCTOR);
		node.addDependency("strbuf");
		ISubGroum reference = new SubGroum(null);
		reference.addVertex(node);
		subgroums = Utils.removeEqualPatterns(reference, subgroums);
		assertTrue(subgroums.size() == 11);
	}

	@Test
	@Ignore
	public void removesCorrectlyIndeed() {

		List<ISubGroum> subgroums = new LinkedList<>();

		for (IGroum groum : Fixture.getListOfXGroums(25)) {
			subgroums.addAll(Utils.breakdown(groum));

		}
		ActionNode node = new ActionNode("25", "25");
		ISubGroum subgroum = new SubGroum(null);
		subgroum.addVertex(node);

		subgroums = Utils.removeEqualPatterns(subgroum, subgroums);
		assertTrue(subgroums.size() == 24);
	}

	@Test
	public void computesFrequency() {
		IGroum groum = Fixture.getExampleGroum();
		ActionNode node = new ActionNode("StringBuffer", IActionNode.CONTRUCTOR);
		node.addDependency("strbuf");
		ISubGroum reference = new SubGroum(null);
		reference.addVertex(node);
		assertEquals(Utils.getFrequency(reference, Utils.breakdown(groum)), 2);
	}

	@Test
	public void computesIsomorphGroups() {
		ActionNode a1 = new ActionNode("A", "A");
		ActionNode a2 = new ActionNode("A", "A");
		ActionNode b1 = new ActionNode("B", "B");
		ActionNode b2 = new ActionNode("B", "B");
		ActionNode c1 = new ActionNode("C", "C");
		ActionNode c2 = new ActionNode("C", "C");
		ActionNode d1 = new ActionNode("D", "D");
		ActionNode d2 = new ActionNode("D", "D");
		ActionNode e1 = new ActionNode("E", "E");
		ActionNode e2 = new ActionNode("E", "E");

		ISubGroum aGroum = new SubGroum(null);
		aGroum.addVertex(a1);
		aGroum.addVertex(a2);

		ISubGroum bGroum1 = new SubGroum(null);
		bGroum1.addVertex(b1);
		bGroum1.addVertex(b2);

		ISubGroum bGroum2 = new SubGroum(null);
		bGroum2.addVertex(b1);
		bGroum2.addVertex(b2);

		ISubGroum cGroum1 = new SubGroum(null);
		cGroum1.addVertex(c1);
		cGroum1.addVertex(c2);

		ISubGroum cGroum2 = new SubGroum(null);
		cGroum2.addVertex(c1);
		cGroum2.addVertex(c2);

		ISubGroum cGroum3 = new SubGroum(null);
		cGroum3.addVertex(c1);
		cGroum3.addVertex(c2);

		ISubGroum dGroum1 = new SubGroum(null);
		dGroum1.addVertex(d1);
		dGroum1.addVertex(d2);

		ISubGroum dGroum2 = new SubGroum(null);
		dGroum2.addVertex(d1);
		dGroum2.addVertex(d2);

		ISubGroum dGroum3 = new SubGroum(null);
		dGroum3.addVertex(d1);
		dGroum3.addVertex(d2);

		ISubGroum dGroum4 = new SubGroum(null);
		dGroum4.addVertex(d1);
		dGroum4.addVertex(d2);

		ISubGroum eGroum = new SubGroum(null);
		eGroum.addVertex(e1);
		eGroum.addVertex(e2);

		List<ISubGroum> list = new LinkedList<>();
		list.add(dGroum1);
		list.add(bGroum1);
		list.add(cGroum3);
		list.add(aGroum);
		list.add(cGroum1);
		list.add(cGroum2);
		list.add(eGroum);
		list.add(dGroum4);
		list.add(dGroum3);
		list.add(bGroum2);
		list.add(dGroum2);

		List<List<ISubGroum>> result = Utils.makeIsomorphGroups(list);
		// System.out.println(result);
		assertTrue(result.size() == 5);
	}

	@Test
	public void breakdownworks() {
		List<IGroum> groums = Fixture.getListOfXGroums(10);
		List<ISubGroum> subgroums = new LinkedList<>();
		for (IGroum groum : groums) {
			subgroums.addAll(Utils.breakdown(groum));
		}
		Collections.sort(subgroums);
		// for (ISubGroum subgroum : subgroums) {
		// System.out.println(subgroum + "--> " + subgroum.getParent());
		// }
		assertTrue(subgroums.size() == 55);
	}
}
