package cc.kave.commons.model.groum;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.nodes.ActionNode;
import cc.kave.commons.model.groum.nodes.ControlNode;
import cc.kave.commons.model.groum.nodes.IActionNode;
import cc.kave.commons.model.groum.nodes.IControlNode;
import cc.kave.commons.model.pattexplore.NaivSubgraphStrategy;

public class NaivSubgraphStrategyTest {

	@Test
	public void identifiesInducedSubgraph() {
		Groum groum = new Groum();

		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		sbAppend.addDependency("strbuf");
		groum.addVertex(sbAppend);

		ActionNode sbLength = new ActionNode("StringBuffer", "length");
		sbLength.addDependency("strbuf");
		groum.addVertex(sbLength);
		groum.addEdge(sbAppend, sbLength);

		ControlNode if1 = new ControlNode(IControlNode.IF_NODE);
		groum.addVertex(if1);
		groum.addEdge(sbLength, if1);

		Groum parentGroum = (Groum) Fixture.getPapersExampleGroum();
		parentGroum.setSubgraphStrategy(new NaivSubgraphStrategy());
		List<ISubGroum> subgraphs = parentGroum.getSubgraphs(groum);
		// System.out.println(subgraphs.get(0));
		assertTrue(subgraphs.size() == 1);

	}

	@Test
	public void identifiesSingleNodeAsInducedSubgraph() {
		Groum groum = new Groum();

		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		sbAppend.addDependency("strbuf");
		groum.addVertex(sbAppend);

		Groum parentGroum = (Groum) Fixture.getPapersExampleGroum();
		parentGroum.setSubgraphStrategy(new NaivSubgraphStrategy());
		List<ISubGroum> subgraphs = parentGroum.getSubgraphs(groum);
		// System.out.println(subgraphs.get(0));
		assertTrue(subgraphs.size() == 1);
	}

	@Test
	public void identifiesFalseSubgraph() {
		Groum groum = new Groum();

		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		sbAppend.addDependency("strbuf");
		groum.addVertex(sbAppend);

		ControlNode if1 = new ControlNode(IControlNode.IF_NODE);
		groum.addVertex(if1);
		groum.addEdge(sbAppend, if1);

		ActionNode sbLength = new ActionNode("StringBuffer", "length");
		sbLength.addDependency("strbuf");
		groum.addVertex(sbLength);
		groum.addEdge(if1, sbLength);

		Groum parentGroum = (Groum) Fixture.getPapersExampleGroum();
		parentGroum.setSubgraphStrategy(new NaivSubgraphStrategy());
		List<ISubGroum> subgraphs = parentGroum.getSubgraphs(groum);
		// System.out.println(subgraphs.get(0));
		assertTrue(subgraphs.size() == 0);
	}

	@Test
	public void identifiesMultipleOccurences() {
		Groum groum = new Groum();
		ActionNode sbInit = new ActionNode("StringBuffer", IActionNode.CONTRUCTOR);
		sbInit.addDependency("strbuf");
		groum.addVertex(sbInit);

		ActionNode frInit = new ActionNode("FileReader", IActionNode.CONTRUCTOR);
		groum.addVertex(frInit);
		groum.addEdge(sbInit, frInit);

		ActionNode brInit = new ActionNode("BufferedReader", IActionNode.CONTRUCTOR);
		brInit.addDependency("in");
		groum.addVertex(brInit);
		groum.addEdge(frInit, brInit);

		Groum parentGroum = (Groum) Fixture.getExampleGroum();
		parentGroum.setSubgraphStrategy(new NaivSubgraphStrategy());
		List<ISubGroum> subgraphs = parentGroum.getSubgraphs(groum);
		// System.out.println(subgraphs.get(0));

		List<INode> list = new LinkedList<>();
		for (ISubGroum agroum : subgraphs) {
			list.addAll(agroum.getAllNodes());
		}
		list.add(brInit);
		list.add(frInit);
		list.add(brInit);
		String out = "";
		for (INode node : list) {
			out = out + node.hashCode() + "; ";
		}
		// System.out.println(out);
		assertTrue(subgraphs.size() == 2 && subgraphs.get(0).getParent() == parentGroum
				&& subgraphs.get(1).getParent() == parentGroum);
	}

	@Test
	public void subgroumEqualityWorks() {
		Groum groum = new Groum();
		ActionNode sbInit = new ActionNode("StringBuffer", IActionNode.CONTRUCTOR);
		sbInit.addDependency("strbuf");
		groum.addVertex(sbInit);

		ActionNode frInit = new ActionNode("FileReader", IActionNode.CONTRUCTOR);
		groum.addVertex(frInit);
		groum.addEdge(sbInit, frInit);

		ActionNode brInit = new ActionNode("BufferedReader", IActionNode.CONTRUCTOR);
		brInit.addDependency("in");
		groum.addVertex(brInit);
		groum.addEdge(frInit, brInit);

		Groum parentGroum = (Groum) Fixture.getExampleGroum();
		parentGroum.setSubgraphStrategy(new NaivSubgraphStrategy());
		List<ISubGroum> subgraphs = parentGroum.getSubgraphs(groum);
		// System.out.println(subgraphs.get(0));

		List<INode> list = new LinkedList<>();
		for (ISubGroum agroum : subgraphs) {
			list.addAll(agroum.getAllNodes());
		}
		list.add(brInit);
		list.add(frInit);
		list.add(brInit);
		String out = "";
		for (INode node : list) {
			out = out + node.hashCode() + "; ";
		}
		// System.out.println(out);
		assertTrue(subgraphs.size() == 2 && subgraphs.get(0).equals(subgraphs.get(1)));
	}

	@Test
	public void findsLeaf() {
		Groum groum = new Groum();

		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		sbAppend.addDependency("strbuf");
		groum.addVertex(sbAppend);

		ActionNode sbLength = new ActionNode("StringBuffer", "length");
		sbLength.addDependency("strbuf");
		groum.addVertex(sbLength);
		groum.addEdge(sbAppend, sbLength);

		ControlNode if1 = new ControlNode(IControlNode.IF_NODE);
		groum.addVertex(if1);
		groum.addEdge(sbLength, if1);

		Groum parentGroum = (Groum) Fixture.getPapersExampleGroum();
		parentGroum.setSubgraphStrategy(new NaivSubgraphStrategy());
		List<ISubGroum> subgraphs = parentGroum.getSubgraphs(groum);
		assertTrue(subgraphs.get(0).getLeaf().equals(if1));
	}

	@Test
	public void extensibleWorks() {
		Groum groum = new Groum();

		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		sbAppend.addDependency("strbuf");
		groum.addVertex(sbAppend);

		ActionNode sbLength = new ActionNode("StringBuffer", "length");
		sbLength.addDependency("strbuf");
		groum.addVertex(sbLength);
		groum.addEdge(sbAppend, sbLength);

		ControlNode if1 = new ControlNode(IControlNode.IF_NODE);
		groum.addVertex(if1);
		groum.addEdge(sbLength, if1);

		Groum parentGroum = (Groum) Fixture.getPapersExampleGroum();
		parentGroum.setSubgraphStrategy(new NaivSubgraphStrategy());
		ISubGroum subgroum = parentGroum.getSubgraphs(groum).get(0);

		ISubGroum testGroum = new SubGroum(null);
		ActionNode testNode = new ActionNode("StringBuffer", "toString");
		testNode.addDependency("strbuf");
		testGroum.addVertex(testNode);

		ISubGroum extendedSubGroum = subgroum.extensibleWith(testGroum).get(0);

		assertTrue(extendedSubGroum.getAllNodes().size() == 4 && extendedSubGroum.getLeaf().equals(testNode));
	}

}
