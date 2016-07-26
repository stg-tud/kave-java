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
package exec.recommender_reimplementation.raychev_analysis;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.stringType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static exec.recommender_reimplementation.raychev_analysis.Interaction.RETURN;
import static exec.recommender_reimplementation.raychev_analysis.InteractionType.METHOD_CALL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Before;

import cc.kave.commons.model.names.IMethodName;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class RaychevAnalysisBaseTest extends PBNAnalysisBaseTest{
	protected HistoryMap historyMap;

	@Before
	public void setup() {
		historyMap = new HistoryMap();
	}
	
	protected void assertConcreteHistoryContainsInteraction(ConcreteHistory concreteHistory, IMethodName methodName,
			int position) {
		assertThat(concreteHistory.getHistory(), Matchers.contains(//
				new Interaction(methodName, position, InteractionType.METHOD_CALL)));
	}

	protected void assertHistories(AbstractHistory... abstractHistories) {
		Collection<AbstractHistory> values = historyMap.values();
		Set<AbstractHistory> actuals = Sets.newHashSet(values);
		Set<AbstractHistory> expecteds = Sets.newHashSet(abstractHistories);
		assertEquals(expecteds, actuals);
	}

	protected AbstractHistory createAbstractHistory(Interaction... interactions) {
		Set<ConcreteHistory> historySet = new HashSet<>();
		historySet.add(new ConcreteHistory(interactions));
		return new AbstractHistory(Lists.newArrayList(interactions), historySet);
	}

	protected AbstractHistory createAbstractHistory(List<Interaction> abstractHistory,
			Set<ConcreteHistory> concreteHistories) {
		return new AbstractHistory(abstractHistory, concreteHistories);
	}

	protected ConcreteHistory createConcreteHistory(Interaction... interactions) {
		return new ConcreteHistory(interactions);
	}

	protected Interaction callInDefaultContextAsReceiver(String simpleMethodName) {
		return callAtPosition(method(voidType, DefaultClassContext, simpleMethodName), 0);
	}

	protected Interaction callInDefaultContextAsParameter(String simpleMethodName, int position) {
		return callAtPosition(method(voidType, DefaultClassContext, simpleMethodName), position);
	}

	protected Interaction callInDefaultContextAsReturn(String simpleMethodName) {
		return callAtPosition(method(voidType, DefaultClassContext, simpleMethodName), RETURN);
	}

	protected Interaction callAtPosition(IMethodName methodName, int position) {
		return new Interaction(methodName, position, METHOD_CALL);
	}

	protected Interaction constructorCall() {
		return callAtPosition(method(voidType, stringType, ".ctor"), 0);
	}
}
