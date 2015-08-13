package cc.kave.commons.model.groum.impl;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.nodes.IActionNode;
import cc.kave.commons.model.groum.nodes.IControlNode;
import cc.kave.commons.model.groum.nodes.impl.ActionNode;
import cc.kave.commons.model.groum.nodes.impl.ControlNode;

public class Fixture_Groumtest {
	public static IGroum getPapersExampleGroum() {
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

		ActionNode brReadline = new ActionNode("BufferedReader", "readLine");
		brReadline.addDependency("in");
		groum.addVertex(brReadline);
		groum.addEdge(brInit, brReadline);

		ControlNode while1 = new ControlNode(IControlNode.WHILE_NODE);
		groum.addVertex(while1);
		groum.addEdge(brReadline, while1);

		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		sbAppend.addDependency("strbuf");
		groum.addVertex(sbAppend);
		groum.addEdge(while1, sbAppend);

		ActionNode sbLength = new ActionNode("StringBuffer", "length");
		sbLength.addDependency("strbuf");
		groum.addVertex(sbLength);
		groum.addEdge(sbAppend, sbLength);

		ControlNode if1 = new ControlNode(IControlNode.IF_NODE);
		groum.addVertex(if1);
		groum.addEdge(sbLength, if1);

		ActionNode sbToString = new ActionNode("StringBuffer", "toString");
		sbToString.addDependency("strbuf");
		groum.addVertex(sbToString);
		groum.addEdge(if1, sbToString);

		ActionNode brClose = new ActionNode("BufferedReader", "close");
		brClose.addDependency("in");
		groum.addVertex(brClose);
		groum.addEdge(sbToString, brClose);

		return groum;

	}

	public static IGroum getExampleGroum() {
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

		ActionNode brReadline = new ActionNode("BufferedReader", "readLine");
		brReadline.addDependency("in");
		groum.addVertex(brReadline);
		groum.addEdge(brInit, brReadline);

		ControlNode while1 = new ControlNode(IControlNode.WHILE_NODE);
		groum.addVertex(while1);
		groum.addEdge(brReadline, while1);

		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		sbAppend.addDependency("strbuf");
		groum.addVertex(sbAppend);
		groum.addEdge(while1, sbAppend);

		ActionNode sbLength = new ActionNode("StringBuffer", "length");
		sbLength.addDependency("strbuf");
		groum.addVertex(sbLength);
		groum.addEdge(sbAppend, sbLength);

		ControlNode if1 = new ControlNode(IControlNode.IF_NODE);
		groum.addVertex(if1);
		groum.addEdge(sbLength, if1);

		ActionNode sbToString = new ActionNode("StringBuffer", "toString");
		sbToString.addDependency("strbuf");
		groum.addVertex(sbToString);
		groum.addEdge(if1, sbToString);

		ActionNode brClose = new ActionNode("BufferedReader", "close");
		brClose.addDependency("in");
		groum.addVertex(brClose);
		groum.addEdge(sbToString, brClose);

		ActionNode sbInit1 = new ActionNode("StringBuffer", IActionNode.CONTRUCTOR);
		sbInit1.addDependency("strbuf");
		groum.addVertex(sbInit1);
		groum.addEdge(brClose, sbInit1);

		ActionNode frInit1 = new ActionNode("FileReader", IActionNode.CONTRUCTOR);
		groum.addVertex(frInit1);
		groum.addEdge(sbInit1, frInit1);

		ActionNode brInit1 = new ActionNode("BufferedReader", IActionNode.CONTRUCTOR);
		brInit1.addDependency("in");
		groum.addVertex(brInit1);
		groum.addEdge(frInit1, brInit1);

		return groum;

	}

	public static <E extends IGroum> List<IGroum> getGroumsOfSizeX(int x, List<E> groums) {
		List<IGroum> group = new LinkedList<>();

		for (IGroum groum : groums) {

			if (groum.getAllNodes().size() == x) {
				group.add(groum);
			}
		}

		return group;
	}

	/*
	 * 29 Nodes in 11 Groums
	 */
	public static List<IGroum> getListOfXGroums(int x) {
		List<IGroum> groumlist = new LinkedList<>();

		for (int i = 1; i <= x; i++) {
			groumlist.add(Fixture_Groumtest.createConnectedGroumOfSize(i));
		}

		return groumlist;
	}

	/*
	 * 29 Nodes in 11 Groums
	 */
	public static List<IGroum> getListOfXGroumsReverse(int x) {
		List<IGroum> groumlist = new LinkedList<>();

		for (int i = x; i > 0; i--) {
			groumlist.add(Fixture_Groumtest.createConnectedGroumOfSize(i));
		}

		return groumlist;
	}

	public static IActionNode createActionNodeInstance(String name) {
		return new ActionNode(name, name);
	}

	public static IGroum createConnectedGroumOfSize(int size) {
		if (size == 0)
			return null;

		IGroum groum = new Groum();
		INode previous = createActionNodeInstance(String.valueOf(1));
		groum.addVertex(previous);

		for (int i = 2; i <= size; i++) {
			INode next = createActionNodeInstance(String.valueOf(i));
			groum.addVertex(next);
			groum.addEdge(previous, next);
			previous = next;
		}
		return groum;
	}
}
