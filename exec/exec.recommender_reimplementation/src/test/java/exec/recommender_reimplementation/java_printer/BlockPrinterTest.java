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
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

public class BlockPrinterTest extends JavaPrintingVisitorBaseTest {

	@Test
	public void testForEachLoop() {
		assertPrint(forEachLoop("e", "elements", new ContinueStatement()),
				"for (? e : elements)", "{", "    continue;", "}");
	}

	@Test
	public void testUsingBlock() {
		assertPrint(usingBlock(varRef("variable"), new BreakStatement()),
				"try (variable)", "{", "    break;", "}");
	}

	@Test
	public void testLockBlock() {
		assertPrint(lockBlock("variable", new ContinueStatement()),
				"synchronized (variable)", "{", "    continue;", "}");
	}

}
