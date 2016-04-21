/*
 * Copyright 2014 Technische Universität Darmstadt
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
package exec.csharp.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.csharp.evaluation.impl.F1ByCategory.QueryContent;

public class QueryJudgeTest {

	private Query a;
	private Query b;

	@Test
	public void numAdditions() {
		a = createQuery(1);
		b = createQuery(1, 2);
		assertEquals(1, judge().getNumAdditions());
	}

	@Test
	public void hasAdditions() {
		a = createQuery(1);
		b = createQuery(1);
		assertEquals(false, judge().hasAdditions());

		a = createQuery(1);
		b = createQuery(1, 2);
		assertEquals(true, judge().hasAdditions());
	}

	@Test
	public void numRemovals() {
		a = createQuery(1, 2);
		b = createQuery(1);
		assertEquals(1, judge().getNumRemovals());
	}

	@Test
	public void hasRemovals() {
		a = createQuery(1);
		b = createQuery(1);
		assertEquals(false, judge().hasRemovals());

		a = createQuery(1, 2);
		b = createQuery(1);
		assertEquals(true, judge().hasRemovals());
	}

	@Test
	public void hasDefChange() {
		a = createQuery(1);
		b = createQuery(1);
		assertEquals(false, judge().hasDefChange());
		b.setDefinition(DefinitionSites.createUnknownDefinitionSite());
		assertEquals(true, judge().hasDefChange());
	}

	@Test
	public void noise_simpleAddition() {
		a = createQuery(1);
		b = createQuery(1, 2);
		assertEquals(NoiseMode.NO_NOISE, judge().getNoiseMode());
	}

	@Test
	public void noise_defChange() {
		a = createQuery(1);
		b = createQuery(1);
		b.setDefinition(DefinitionSites.createUnknownDefinitionSite());
		assertEquals(NoiseMode.DEF, judge().getNoiseMode());
	}

	@Test
	public void noise_methodRemoval() {
		a = createQuery(1, 2);
		b = createQuery(1, 3);
		assertEquals(NoiseMode.REMOVAL, judge().getNoiseMode());
	}

	@Test
	public void noise_defAndMethodRemoval() {
		a = createQuery(1, 2);
		b = createQuery(1, 3);
		b.setDefinition(DefinitionSites.createUnknownDefinitionSite());
		assertEquals(NoiseMode.DEF_AND_REMOVAL, judge().getNoiseMode());
	}

	@Test
	public void noise_pureRemoval() {
		a = createQuery(1, 2);
		b = createQuery(1);
		b.setDefinition(DefinitionSites.createUnknownDefinitionSite());
		assertEquals(NoiseMode.PURE_REMOVAL, judge().getNoiseMode());
	}

	@Test
	public void judging_nq() {
		Usage a = new NoUsage();
		Usage b = createQuery(1, 2);
		QueryJudge sut = new QueryJudge(a, b);
		assertTrue(sut.hasAdditions());
		assertEquals(2, sut.getNumAdditions());
		assertFalse(sut.hasRemovals());
		assertEquals(0, sut.getNumRemovals());
		assertTrue(sut.hasDefChange());
		assertEquals(NoiseMode.NO_NOISE, sut.getNoiseMode());
	}

	@Test
	public void judging_qn() {
		Usage a = createQuery(1, 2);
		Usage b = new NoUsage();
		QueryJudge sut = new QueryJudge(a, b);
		assertFalse(sut.hasAdditions());
		assertEquals(0, sut.getNumAdditions());
		assertTrue(sut.hasRemovals());
		assertEquals(2, sut.getNumRemovals());
		assertTrue(sut.hasDefChange());
		assertEquals(NoiseMode.PURE_REMOVAL, sut.getNoiseMode());
	}

	@Test
	public void queryContent_0p1() {
		Usage a = createQuery();
		Usage b = createQuery(1);
		assertEquals(QueryContent.ZERO, new QueryJudge(a, b).getQueryContentCategorization());
	}

	@Test
	public void queryContent_1p1() {
		Usage a = createQuery(1);
		Usage b = createQuery(1, 2);
		assertEquals(QueryContent.NM, new QueryJudge(a, b).getQueryContentCategorization());
	}

	@Test
	public void queryContent_2p1() {
		Usage a = createQuery(1, 2);
		Usage b = createQuery(1, 2, 3);
		assertEquals(QueryContent.MINUS1, new QueryJudge(a, b).getQueryContentCategorization());
	}

	@Test
	public void queryContent_else() {
		Usage a = createQuery(1);
		Usage b = createQuery(1, 2, 3);
		assertEquals(QueryContent.NM, new QueryJudge(a, b).getQueryContentCategorization());
	}

	@Test
	public void queryContent_nq() {
		Usage a = new NoUsage();
		Usage b = createQuery(1);
		assertEquals(QueryContent.ZERO, new QueryJudge(a, b).getQueryContentCategorization());
	}

	@Test(expected = AssertionException.class)
	public void queryContent_qn() {
		Usage a = createQuery(1, 2, 3);
		Usage b = new NoUsage();
		new QueryJudge(a, b).getQueryContentCategorization();
	}

	private QueryJudge judge() {
		return new QueryJudge(a, b);
	}

	private Query createQuery(int... mIds) {
		Query q = new Query();
		q.setType(CoReTypeName.get("LT"));
		q.setClassContext(CoReTypeName.get("LCtx"));
		q.setMethodContext(method("Ctx", "m"));
		q.setDefinition(DefinitionSites.createDefinitionByThis());
		for (int mID : mIds) {
			ICoReMethodName m = method("T", "m" + mID);
			CallSite cs = CallSites.createReceiverCallSite(m);
			q.addCallSite(cs);
		}
		return q;
	}

	private ICoReMethodName method(String cName, String mName) {
		return CoReMethodName.get(String.format("L%s.%s()V", cName, mName));
	}
}