/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.java_printer;

import org.junit.Test;

import static cc.kave.commons.model.ssts.impl.SSTUtil.*;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class StatementPrinterTest extends JavaPrintingVisitorBaseTest {
	@Test
	public void testAssignment() {
		IAssignment sst = assignmentToLocal("var", constant("true"));
		assertPrint(sst, "var = true;");
	}

	@Test
	public void testPropertySet() {
		assertPrint(
				assign(propertyReference(varRef("this"),
						"get set [PropertyType,P] [DeclaringType,P].P"),
						referenceExprToVariable("var")), //
				"setP(var);");
	}

	@Test
	public void testPropertyGet() {
		assertPrint(
				assign(varRef("var"),
						refExpr(propertyReference(varRef("this"),
								"get set [PropertyType,P] [DeclaringType,P].P"))), //
				"var = getP();");
	}

	@Test
	public void testGotoStatement() {
		GotoStatement sst = new GotoStatement();
		sst.setLabel("L");

		assertPrint(sst, "");
	}
}
