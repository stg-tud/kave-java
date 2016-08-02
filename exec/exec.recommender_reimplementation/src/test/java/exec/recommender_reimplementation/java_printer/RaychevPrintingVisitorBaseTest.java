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

import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.typeshapes.ITypeShape;
import exec.recommender_reimplementation.java_printer.javaPrinterTestSuite.JavaPrintingVisitorBaseTest;

public class RaychevPrintingVisitorBaseTest extends JavaPrintingVisitorBaseTest {
	
	protected void assertPrintWithCustomContext(ISSTNode sst, ITypeShape typeShape, String... expectedLines) {
		sut = new RaychevQueryPrintingVisitor(sst,false);
		assertPrintWithCustomContext(sst, typeShape, String.join("\n", expectedLines));
	}

	protected void assertPrintWithCustomContext(ISSTNode sst, JavaPrintingContext context, String... expectedLines) {
		sut = new RaychevQueryPrintingVisitor(sst,false);
		assertPrintWithCustomContext(sst, context, String.join("\n", expectedLines));
	}
}
