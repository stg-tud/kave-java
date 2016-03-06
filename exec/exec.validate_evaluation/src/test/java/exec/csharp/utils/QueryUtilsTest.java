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

import static exec.csharp.utils.QueryUtils.countAdditions;
import static exec.csharp.utils.QueryUtils.countRemovals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.recommenders.names.VmMethodName;
import cc.recommenders.names.VmTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;

public class QueryUtilsTest {

	@Test
	public void countAdditions_noDiff() {
		assertEquals(0, countAdditions(q(1), q(1)));
	}

	@Test
	public void countAdditions_addition() {
		assertEquals(1, countAdditions(q(1), q(1, 2)));
	}

	@Test
	public void countAdditions_removal() {
		assertEquals(0, countAdditions(q(1, 2), q(1)));
	}

	@Test
	public void countRemovals_noDiff() {
		assertEquals(0, countRemovals(q(1), q(1)));
	}

	@Test
	public void countRemovals_addition() {
		assertEquals(0, countRemovals(q(1), q(1, 2)));
	}

	@Test
	public void countRemovals_removal() {
		assertEquals(1, countRemovals(q(1, 2), q(1)));
	}

	@Test
	public void toDiffString_equalQueries() {
		Query a = q(1);
		Query b = q(1);

		assertDiffToString(a, b, "1");
	}

	@Test
	public void toDiffString_addedCalls() {
		Query a = q(1);
		Query b = q(1, 2);

		assertDiffToString(a, b, "1+1");
	}

	@Test
	public void toDiffString_removedCalls() {
		Query a = q(1, 2);
		Query b = q(1);

		assertDiffToString(a, b, "2-1");
	}

	@Test
	public void toDiffString_addedAndRemovedCalls() {
		Query a = q(1, 2);
		Query b = q(1, 3, 4);

		assertDiffToString(a, b, "2-1+2");
	}

	@Test
	public void toDiffString_differentType() {
		Query a = q(1);
		a.setType(VmTypeName.get("LT2"));
		Query b = q(1);

		assertDiffToString(a, b, "1~T");
	}

	@Test
	public void toDiffString_differentClassContext() {
		Query a = q(1);
		a.setClassContext(VmTypeName.get("LSome2"));
		Query b = q(1);

		assertDiffToString(a, b, "1~C");
	}

	@Test
	public void toDiffString_differentMethodContext() {
		Query a = q(1);
		a.setMethodContext(VmMethodName.get("LS2.m()V"));
		Query b = q(1);

		assertDiffToString(a, b, "1~M");
	}

	@Test
	public void toDiffString_differentDefinition() {
		Query a = q(1);
		a.setDefinition(DefinitionSites.createDefinitionByThis());
		Query b = q(1);

		assertDiffToString(a, b, "1~D");
	}

	@Test
	public void toDiffString_integration() {
		Query a = q(1, 2);
		a.setType(VmTypeName.get("LT2"));
		a.setClassContext(VmTypeName.get("LSome2"));
		a.setMethodContext(VmMethodName.get("LS2.m()V"));
		a.setDefinition(DefinitionSites.createDefinitionByThis());
		Query b = q(1, 3, 4);

		assertDiffToString(a, b, "2-1+2~TCMD");
	}

	private void assertDiffToString(Query a, Query b, String expected) {
		String actual = QueryUtils.toDiffString(a, b);
		assertEquals(expected, actual);
	}

	private Query q(int... mIds) {
		Query q = new Query();
		q.setType(VmTypeName.get("Ln/T"));
		q.setClassContext(VmTypeName.get("LSome"));
		q.setMethodContext(VmMethodName.get("LS.m()V"));
		q.setDefinition(DefinitionSites.createDefinitionByConstant());
		for (int mId : mIds) {
			CallSite cs = CallSites.createReceiverCallSite("Ln/T.m" + mId + "()V");
			q.addCallSite(cs);
		}
		return q;
	}
}