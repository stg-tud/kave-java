/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package exec.validate_evaluation.queryhistory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.validate_evaluation.queryhistory.QueryHistoryFixer.RemovalCallback;

public class QueryHistoryFixerTest {

	private QueryHistoryFixer sut;

	private boolean wasCalled;
	private int diff;

	@Before
	public void setup() {
		wasCalled = false;
		diff = -1;
		sut = new QueryHistoryFixer();
	}

	@Test
	public void subsequentQueriesAreFiltered() {
		Usage u = query(1);
		List<Usage> qh = Lists.newArrayList(u, u);

		sut.fix(qh, cb());

		assertEquals(Lists.newArrayList(u), qh);
	}

	@Test
	public void subsequentQueriesAreFiltered_largerExample() {
		List<Usage> qh = Lists.newArrayList(query(1), query(2), query(2), query(3));

		sut.fix(qh, cb());

		assertEquals(Lists.newArrayList(query(1), query(2), query(3)), qh);
	}

	@Test
	public void callBackIsCalledForRemovals() {
		Usage u = query(1);
		List<Usage> qh = Lists.newArrayList(u, u);

		sut.fix(qh, cb());

		assertTrue(wasCalled);
		assertEquals(1, diff);
	}

	@Test
	public void callBackIsNotCalledWhenNoRemoval() {
		Usage u = query(1);
		List<Usage> qh = Lists.newArrayList(u);

		sut.fix(qh, cb());

		assertFalse(wasCalled);
	}

	private static Usage query(int i) {
		Query q = new Query();
		q.addCallSite(CallSites.createReceiverCallSite("LT.m" + i + "()V"));
		return q;
	}

	private RemovalCallback cb() {
		return (diff) -> {
			wasCalled = true;
			this.diff = diff;
		};
	}
}