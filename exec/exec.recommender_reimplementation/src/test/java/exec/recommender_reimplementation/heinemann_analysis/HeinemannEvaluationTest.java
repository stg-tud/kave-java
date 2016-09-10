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
package exec.recommender_reimplementation.heinemann_analysis;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.intType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.stringType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class HeinemannEvaluationTest extends PBNAnalysisBaseTest {

	HeinemannEvaluation heinemannEvaluation;
	
	@Before
	public void setup() {
		HeinemannRecommender recommender = new HeinemannRecommender(createSampleModel(), 0.8);
		heinemannEvaluation = new HeinemannEvaluation(recommender, 4, false, false);
	}
	
	@Test
	public void evaluateSetIntegrationTest() {
		setupDefaultEnclosingMethod(
				varDecl("num", type("string")),
				varDecl("str", type("string")),
				assign("str", invoke("str", method(stringType, type("A"), "foo"))),
				varDecl("someNumber", type("int")),
				assign("someNumber", invoke("someNumber", method(intType, type("B"), "m2"))));
		
		double actual = heinemannEvaluation.evaluateSet(Lists.newArrayList(context));
		
		assertEquals(0.5, actual, 0.0);
	}
	
	private Map<ITypeName, Set<Entry>> createSampleModel() {
		Map<ITypeName, Set<Entry>> model = new HashMap<>();
		
		model.put(type("A"), Sets.newHashSet(
				createEntry(method(stringType, type("A"), "foo"), "string", "str", "num"),
				createEntry(method(stringType, type("A"), "bar"), "string", "str", "int")));
		model.put(type("B"), Sets.newHashSet(
				createEntry(method(voidType, type("C"), "m3"), "string", "str", "int", "num2"),
				createEntry(method(stringType, type("C"), "m4"), "string", "some","str"),			
				createEntry(method(stringType, type("C"), "m5"), "string", "other", "str")			
				));
		
		return model;
	}
	
	
	private Entry createEntry(IMethodName method, String... strings) {
		return new Entry(Sets.newHashSet(strings), method);
	}

}
