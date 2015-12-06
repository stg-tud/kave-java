/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.tests;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.recommenders.io.ReadingArchive;
import cc.recommenders.io.WritingArchive;

public class SerializationTest {

	@Test
	public void testInstanceEquality() {
		VariableReference aRef = new VariableReference();
		aRef.setIdentifier("a");

		// VariableReference bRef = new VariableReference();
		// bRef.setIdentifier("b");

		ComposedExpression composedExpr = new ComposedExpression();
		composedExpr.setReferences(Arrays.asList(aRef, aRef));

		ExpressionStatement exprStatement = new ExpressionStatement();
		exprStatement.setExpression(composedExpr);

		File tempZip = null;
		try {
			tempZip = File.createTempFile("referenceTest", ".zip");
			System.out.println(tempZip.toString());
			WritingArchive writer = new WritingArchive(tempZip);
			writer.add(composedExpr, "aa.json");
			writer.add(exprStatement);
			writer.add(exprStatement);
			writer.close();

			
			ReadingArchive reader = new ReadingArchive(tempZip);

			ComposedExpression readExpr = reader.getNext(ComposedExpression.class);
			List<IVariableReference> references = readExpr.getReferences();

			Assert.assertTrue(reader.getNext(ExpressionStatement.class) == reader.getNext(ExpressionStatement.class));

			Assert.assertEquals(2, references.size());
			Assert.assertTrue(references.get(0) == references.get(1));
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tempZip != null && tempZip.exists()) {
				tempZip.delete();
			}
		}

	}
}
