package cc.kave.commons.model.groum;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.nodes.ActionNode;
import cc.kave.commons.model.groum.nodes.ControlNode;

public class Fixture {
	public static Groum getPapersExampleGroum() {
		Groum groum = new Groum();

		ActionNode sbInit = new ActionNode("StringBuffer", "<init>");
		groum.addNode(sbInit);

		ActionNode frInit = new ActionNode("FileReader", "<init>");
		groum.addNode(frInit);
		groum.addEdge(sbInit, frInit);

		ActionNode brInit = new ActionNode("BufferedReader", "<init>");
		groum.addNode(brInit);
		groum.addEdge(frInit, brInit);

		ActionNode brReadline = new ActionNode("BufferedReader", "readLine");
		groum.addNode(brReadline);
		groum.addEdge(brInit, brReadline);

		ControlNode while1 = new ControlNode("WHILE");
		groum.addNode(while1);
		groum.addEdge(brReadline, while1);

		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		groum.addNode(sbAppend);
		groum.addEdge(while1, sbAppend);

		ActionNode sbLength = new ActionNode("StringBuffer", "length");
		groum.addNode(sbLength);
		groum.addEdge(sbAppend, sbLength);

		ControlNode if1 = new ControlNode("IF");
		groum.addNode(if1);
		groum.addEdge(sbLength, if1);

		ActionNode sbToString = new ActionNode("StringBuffer", "toString");
		groum.addNode(sbToString);
		groum.addEdge(if1, sbToString);

		ActionNode brClose = new ActionNode("BufferedReader", "close");
		groum.addNode(brClose);
		groum.addEdge(sbToString, brClose);

		return groum;

	}

	public static Groum getExampleGroum() {
		Groum groum = new Groum();

		ActionNode sbInit = new ActionNode("StringBuffer", "<init>");
		groum.addNode(sbInit);

		ActionNode frInit = new ActionNode("FileReader", "<init>");
		groum.addNode(frInit);
		groum.addEdge(sbInit, frInit);

		ActionNode brInit = new ActionNode("BufferedReader", "<init>");
		groum.addNode(brInit);
		groum.addEdge(frInit, brInit);

		ActionNode brReadline = new ActionNode("BufferedReader", "readLine");
		groum.addNode(brReadline);
		groum.addEdge(brInit, brReadline);

		ControlNode while1 = new ControlNode("WHILE");
		groum.addNode(while1);
		groum.addEdge(brReadline, while1);

		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		groum.addNode(sbAppend);
		groum.addEdge(while1, sbAppend);

		ActionNode sbLength = new ActionNode("StringBuffer", "length");
		groum.addNode(sbLength);
		groum.addEdge(sbAppend, sbLength);

		ControlNode if1 = new ControlNode("IF");
		groum.addNode(if1);
		groum.addEdge(sbLength, if1);

		ActionNode sbToString = new ActionNode("StringBuffer", "toString");
		groum.addNode(sbToString);
		groum.addEdge(if1, sbToString);

		ActionNode brClose = new ActionNode("BufferedReader", "close");
		groum.addNode(brClose);
		groum.addEdge(sbToString, brClose);

		ActionNode sbInit1 = new ActionNode("StringBuffer", "<init>");
		groum.addNode(sbInit1);
		groum.addEdge(brClose, sbInit1);

		ActionNode frInit1 = new ActionNode("FileReader", "<init>");
		groum.addNode(frInit1);
		groum.addEdge(sbInit1, frInit1);

		ActionNode brInit1 = new ActionNode("BufferedReader", "<init>");
		groum.addNode(brInit1);
		groum.addEdge(frInit1, brInit1);

		return groum;

	}

	public static <E extends Groum> List<Groum> getGroumsOfSizeX(int x, List<E> groums) {
		List<Groum> group = new LinkedList<>();

		for (Groum groum : groums) {

			if (groum.getAllNodes().size() == x) {
				group.add(groum);
			}
		}

		return group;
	}

	/*
	 * 29 Nodes in 11 Groums
	 */
	public static List<Groum> getListOfXGroums(int x) {
		List<Groum> groumlist = new LinkedList<>();

		for (int i = 1; i <= x; i++) {
			groumlist.add(Fixture.createConnectedGroumOfSize(i));
		}

		return groumlist;
	}

	/*
	 * 29 Nodes in 11 Groums
	 */
	public static List<Groum> getListOfXGroumsReverse(int x) {
		List<Groum> groumlist = new LinkedList<>();

		for (int i = x; i > 0; i--) {
			groumlist.add(Fixture.createConnectedGroumOfSize(i));
		}

		return groumlist;
	}

	public static INode createActionNodeInstance(String name) {
		return new ActionNode(name, name);
	}

	public static Groum createConnectedGroumOfSize(int size) {
		if (size == 0)
			return null;

		Groum groum = new Groum();
		INode previous = createActionNodeInstance(String.valueOf(1));
		groum.addNode(previous);

		for (int i = 2; i <= size; i++) {
			INode next = createActionNodeInstance(String.valueOf(i));
			groum.addNode(next);
			groum.addEdge(previous, next);
			previous = next;
		}
		return groum;
	}

	public static Groum createConnectedGroumOfSize(int low, int high) {
		if (high < low)
			return null;

		Groum groum = new Groum();
		INode previous = createActionNodeInstance(String.valueOf(low));
		groum.addNode(previous);

		for (int i = low + 1; i <= high; i++) {
			INode next = createActionNodeInstance(String.valueOf(i));
			groum.addNode(next);
			groum.addEdge(previous, next);
			previous = next;
		}
		return groum;
	}
}
