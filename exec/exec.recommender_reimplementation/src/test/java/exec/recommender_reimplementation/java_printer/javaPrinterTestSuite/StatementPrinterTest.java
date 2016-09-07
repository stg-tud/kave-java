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
package exec.recommender_reimplementation.java_printer.javaPrinterTestSuite;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;

public class StatementPrinterTest extends JavaPrintingVisitorBaseTest {

	@Test
	public void testThrowStatement() {
		ThrowStatement sst = new ThrowStatement();
		VariableReference ref = new VariableReference();
		ref.setIdentifier("T");
		sst.setReference(ref);
		assertPrint(sst, "throw new java.lang.Exception();");
	}
}
