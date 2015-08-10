package cc.kave.commons.model.groum.impl;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.IGroum;
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

	/*
	 * 29 Nodes in 11 Groums
	 */
	public static List<IGroum> getTestList() {
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

		// 5x A
		// 7x B
		// 7x C
		// 5x D
		// 4x E

		// 3x AB
		// 4x BC

		IGroum aGroum = new SubGroum(null);
		aGroum.addVertex(a1);
		aGroum.addVertex(b1);
		aGroum.addVertex(c1);

		IGroum bGroum1 = new SubGroum(null);
		bGroum1.addVertex(a2);
		bGroum1.addVertex(b2);
		bGroum1.addVertex(c2);

		IGroum bGroum2 = new SubGroum(null);
		bGroum2.addVertex(a1);
		bGroum2.addVertex(b1);

		IGroum cGroum1 = new SubGroum(null);
		cGroum1.addVertex(b1);
		cGroum1.addVertex(b2);

		IGroum cGroum2 = new SubGroum(null);
		cGroum2.addVertex(a1);
		cGroum2.addVertex(b2);
		cGroum2.addVertex(c1);
		cGroum2.addVertex(d1);

		IGroum cGroum3 = new SubGroum(null);
		cGroum3.addVertex(c1);
		cGroum3.addVertex(d2);

		IGroum dGroum1 = new SubGroum(null);
		dGroum1.addVertex(d1);
		dGroum1.addVertex(e2);

		IGroum dGroum2 = new SubGroum(null);
		dGroum2.addVertex(c2);
		dGroum2.addVertex(d1);
		dGroum2.addVertex(e2);

		IGroum dGroum3 = new SubGroum(null);
		dGroum3.addVertex(b1);
		dGroum3.addVertex(c2);
		dGroum3.addVertex(d2);
		dGroum3.addVertex(e2);

		IGroum dGroum4 = new SubGroum(null);
		dGroum4.addVertex(e1);
		dGroum4.addVertex(d2);

		IGroum eGroum = new SubGroum(null);
		eGroum.addVertex(c1);
		eGroum.addVertex(b1);
		eGroum.addVertex(a2);

		List<IGroum> list = new LinkedList<>();
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

		return list;
	}

}
