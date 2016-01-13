package cc.kave.commons.model.groum.legacy;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.nodes.ActionNode;
import cc.kave.commons.model.groum.nodes.ControlNode;
import static cc.kave.commons.model.groum.GroumBuilder.*;

public class Fixture {
	public static Groum getPapersExampleGroum() {
		ActionNode sbInit = new ActionNode("StringBuffer", "<init>");
		ActionNode frInit = new ActionNode("FileReader", "<init>");
		ActionNode brInit = new ActionNode("BufferedReader", "<init>");
		ActionNode brReadline = new ActionNode("BufferedReader", "readLine");
		ControlNode while1 = new ControlNode("WHILE");
		ActionNode sbAppend = new ActionNode("StringBuffer", "append");
		ActionNode sbLength = new ActionNode("StringBuffer", "length");
		ControlNode if1 = new ControlNode("IF");
		ActionNode sbToString = new ActionNode("StringBuffer", "toString");
		ActionNode brClose = new ActionNode("BufferedReader", "close");

		return buildGroum(sbInit, frInit, brInit, brReadline, while1, sbAppend,
				sbLength, if1, sbToString, brClose)
				.withEdge(sbInit, frInit)
				.withEdge(frInit, brInit)
				.withEdge(brInit, brReadline)
				.withEdge(brReadline, while1)
				.withEdge(while1, sbAppend)
				.withEdge(sbAppend, sbLength)
				.withEdge(sbLength, if1)
				.withEdge(if1, sbToString)
				.withEdge(sbToString, brClose).build();

	}
}
