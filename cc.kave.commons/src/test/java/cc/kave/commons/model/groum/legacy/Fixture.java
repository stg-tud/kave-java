/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
