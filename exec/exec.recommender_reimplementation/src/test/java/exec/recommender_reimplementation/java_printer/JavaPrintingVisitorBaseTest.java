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

import org.junit.Assert;

import com.google.common.base.Strings;

import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.typeshapes.ITypeShape;

public class JavaPrintingVisitorBaseTest {
	protected JavaPrintingVisitor sut = new JavaPrintingVisitor();

	protected void assertPrintWithCustomContext(ISSTNode sst, ITypeShape typeShape, String expected) {
		JavaPrintingContext context = new JavaPrintingContext();
		context.setTypeShape(typeShape);
		int indentationLevel = context.indentationLevel;
		sst.accept(sut, context);
		String actual = context.toString();
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(indentationLevel, context.indentationLevel);
	}
	
	protected void assertPrintWithCustomContext(ISSTNode sst, JavaPrintingContext context, String expected) {
		int indentationLevel = context.indentationLevel;
		sst.accept(sut, context);
		String actual = context.toString();
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(indentationLevel, context.indentationLevel);
	}

	protected void assertPrintWithCustomContext(ISSTNode sst, ITypeShape typeShape, String... expectedLines) {
		assertPrintWithCustomContext(sst, typeShape, String.join("\n", expectedLines));
	}

	protected void assertPrintWithCustomContext(ISSTNode sst, JavaPrintingContext context, String... expectedLines) {
		assertPrintWithCustomContext(sst, context, String.join("\n", expectedLines));
	}
	
	protected void assertPrint(ISSTNode sst, String... expectedLines) {
		testPrintingWithoutIndentation(sst, expectedLines);
		
		// Expressions and references can't be indented
		if (!(sst instanceof IExpression || sst instanceof IReference)) {
			testPrintingWithIndentation(sst, expectedLines);
		}
	}

	private void testPrintingWithoutIndentation(ISSTNode sst, String[] expectedLines) {
		JavaPrintingContext context = new JavaPrintingContext();
		context.setIndentationLevel(0);
		assertPrintWithCustomContext(sst, context, expectedLines);
	}

	private void testPrintingWithIndentation(ISSTNode sst, String... expectedLines) {
		String[] indentedLines = new String[expectedLines.length];
		for (int i = 0; i < expectedLines.length; i++) {
			indentedLines[i] = Strings.isNullOrEmpty(expectedLines[i]) ? expectedLines[i]
					: "    " + expectedLines[i];
		}
		JavaPrintingContext context = new JavaPrintingContext();
		context.setIndentationLevel(1);
		assertPrintWithCustomContext(sst, context, indentedLines);
	}

	protected ConstantValueExpression constant(String value) {
		ConstantValueExpression expr = new ConstantValueExpression();
		expr.setValue(value);
		return expr;
	}

	protected VariableReference varRef(String identifier) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(identifier);
		return ref;
	}
}
