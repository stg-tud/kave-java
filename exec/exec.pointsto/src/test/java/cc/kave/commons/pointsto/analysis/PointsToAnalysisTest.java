/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.analysis;

import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static java.util.Collections.emptySet;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.ImmutableSet;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.tests.AnalysesProvider;
import cc.kave.commons.pointsto.tests.TestBuilder;

@RunWith(Parameterized.class)
public class PointsToAnalysisTest extends TestBuilder {

	@Parameters
	public static Collection<Object[]> data() {
		return AnalysesProvider.ANALYSES_AS_PARAMETERS;
	}

	private final PointsToAnalysisFactory analysisFactory;

	public PointsToAnalysisTest(PointsToAnalysisFactory analysisFactory) {
		this.analysisFactory = analysisFactory;
	}

	@Test
	public void enumerableForEachReferenceShouldObtainLocation() {
		// foreach (B entry : p0)
		// entry.M1()

		ITypeName enclosingType = type("ET");
		IMethodDeclaration enclosingMethod = declareMethod(method(enclosingType, "Entry", type("A")), true,
				forEachLoop(declare("entry", type("B")), "p0", exprStmt(invoke("entry", method(type("B"), "M1")))));
		Context ctxt = context(enclosingType, ImmutableSet.of(enclosingMethod), emptySet(), emptySet());
		PointsToAnalysis analysis = analysisFactory.create();
		analysis.compute(ctxt);

		IForEachLoop loop = (IForEachLoop) enclosingMethod.getBody().get(0);
		PointsToQuery query = new PointsToQuery(variableReference("entry"), type("B"), loop.getBody().get(0),
				enclosingMethod.getName());
		assertEquals(1, analysis.query(query).size());
	}

	@Test
	public void arrayForEachReferenceShouldObtainLocation() {
		// foreach (A entry : p0)
		// entry.M1()

		ITypeName enclosingType = type("ET");
		IMethodDeclaration enclosingMethod = declareMethod(method(enclosingType, "Entry", type("A[]")), true,
				forEachLoop(declare("entry", type("A")), "p0", exprStmt(invoke("entry", method(type("A"), "M1")))));
		Context ctxt = context(enclosingType, ImmutableSet.of(enclosingMethod), emptySet(), emptySet());
		PointsToAnalysis analysis = analysisFactory.create();
		analysis.compute(ctxt);

		IForEachLoop loop = (IForEachLoop) enclosingMethod.getBody().get(0);
		PointsToQuery query = new PointsToQuery(variableReference("entry"), type("A"), loop.getBody().get(0),
				enclosingMethod.getName());
		assertEquals(1, analysis.query(query).size());
	}
}
