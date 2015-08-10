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
		

		// 5x A
		// 7x B
		// 7x C
		// 5x D
		// 4x E

		// 3x AB
		// 4x BC

		IGroum aGroum = new SubGroum(null);
		aGroum.addVertex(new ActionNode("A", "A"));
		aGroum.addVertex(new ActionNode("B", "B"));
		aGroum.addVertex(new ActionNode("C", "C"));

		IGroum bGroum1 = new SubGroum(null);
		bGroum1.addVertex(new ActionNode("A", "A"));
		bGroum1.addVertex(new ActionNode("B", "B"));
		bGroum1.addVertex(new ActionNode("C", "C"));

		IGroum bGroum2 = new SubGroum(null);
		bGroum2.addVertex(new ActionNode("A", "A"));
		bGroum2.addVertex(new ActionNode("B", "B"));

		IGroum cGroum1 = new SubGroum(null);
		cGroum1.addVertex(new ActionNode("B", "B"));
		cGroum1.addVertex(new ActionNode("B", "B"));

		IGroum cGroum2 = new SubGroum(null);
		cGroum2.addVertex(new ActionNode("A", "A"));
		cGroum2.addVertex(new ActionNode("B", "B"));
		cGroum2.addVertex(new ActionNode("C", "C"));
		cGroum2.addVertex(new ActionNode("D", "D"));

		IGroum cGroum3 = new SubGroum(null);
		cGroum3.addVertex(new ActionNode("C", "C"));
		cGroum3.addVertex(new ActionNode("D", "D"));

		IGroum dGroum1 = new SubGroum(null);
		dGroum1.addVertex(new ActionNode("D", "D"));
		dGroum1.addVertex(new ActionNode("E", "E"));

		IGroum dGroum2 = new SubGroum(null);
		dGroum2.addVertex(new ActionNode("C", "C"));
		dGroum2.addVertex(new ActionNode("D", "D"));
		dGroum2.addVertex(new ActionNode("E", "E"));

		IGroum dGroum3 = new SubGroum(null);
		dGroum3.addVertex(new ActionNode("B", "B"));
		dGroum3.addVertex(new ActionNode("C", "C"));
		dGroum3.addVertex(new ActionNode("D", "D"));
		dGroum3.addVertex(new ActionNode("E", "E"));

		IGroum dGroum4 = new SubGroum(null);
		dGroum4.addVertex(new ActionNode("E", "E"));
		dGroum4.addVertex(new ActionNode("D", "D"));

		IGroum eGroum = new SubGroum(null);
		eGroum.addVertex(new ActionNode("C", "C"));
		eGroum.addVertex(new ActionNode("B", "B"));
		eGroum.addVertex(new ActionNode("A", "A"));

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
