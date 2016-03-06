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
package exec.csharp.queries;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.recommenders.names.IMethodName;
import cc.recommenders.names.ITypeName;
import cc.recommenders.names.VmMethodName;
import cc.recommenders.names.VmTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public abstract class AbstractQueryBuilderTest {

	private IQueryBuilder<Usage, Query> sut;

	@Before
	public void setup() {
		sut = createQueryBuilder();
	}

	@Test
	public void allInformationAreCopied() {
		Usage start = createUsage();
		Usage end = createUsage("a");
		Query q = assertSingleQuery(start, end);
		assertEquals(end.getType(), q.getType());
		assertEquals(end.getClassContext(), q.getClassContext());
		assertEquals(end.getMethodContext(), q.getMethodContext());
		assertEquals(end.getDefinitionSite(), q.getDefinitionSite());
	}

	protected abstract IQueryBuilder<Usage, Query> createQueryBuilder();

	protected Query assertSingleQuery(Usage start, Usage end) {
		List<Query> actuals = assertQueries(1, start, end);
		return actuals.get(0);
	}

	protected List<Query> assertQueries(int expectedNum, Usage start, Usage end) {
		List<Query> actuals = sut.createQueries(start, end);
		assertEquals(expectedNum, actuals.size());
		return actuals;
	}

	protected void assertMethods(Query q, String... methods) {
		Set<CallSite> actual = q.getReceiverCallsites();
		Set<CallSite> expected = Sets.newLinkedHashSet();
		for (String method : methods) {
			expected.add(call(method));
		}
		assertEquals(expected, actual);
	}

	private CallSite call(String method) {
		String m = String.format("LT.%s()V", method);
		return CallSites.createReceiverCallSite(m);
	}

	protected Query createUsage(String... methodNames) {
		Query q = new Query();
		q.setType(VmTypeName.get("LT"));
		q.setClassContext(VmTypeName.get("LC"));
		q.setMethodContext(VmMethodName.get("LC.ctx()V"));
		q.setDefinition(DefinitionSites.createDefinitionByConstant());
		for (String m : methodNames) {
			q.addCallSite(call(m));
		}
		return q;
	}

	protected Usage createUsageWithDifferentDef(String... methodNames) {
		Query q = createUsage(methodNames);
		q.setDefinition(DefinitionSites.createUnknownDefinitionSite());
		return q;
	}

	protected class UsageThatShouldBeIgnored implements Usage {

		private Set<CallSite> calls = Sets.newLinkedHashSet();

		public UsageThatShouldBeIgnored(String... methodNames) {
			for (String methodName : methodNames) {
				CallSite m = CallSites.createReceiverCallSite("LT." + methodName + "()V");
				calls.add(m);
			}
		}

		@Override
		public ITypeName getType() {
			throw new RuntimeException();
		}

		@Override
		public Set<CallSite> getReceiverCallsites() {
			return calls;
		}

		@Override
		public Set<CallSite> getParameterCallsites() {
			throw new RuntimeException();
		}

		@Override
		public IMethodName getMethodContext() {
			throw new RuntimeException();
		}

		@Override
		public DefinitionSite getDefinitionSite() {
			throw new RuntimeException();
		}

		@Override
		public ITypeName getClassContext() {
			throw new RuntimeException();
		}

		@Override
		public Set<CallSite> getAllCallsites() {
			throw new RuntimeException();
		}
	}
}