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
package exec.recommender_reimplementation.frequency_recommender;

import static exec.recommender_reimplementation.frequency_recommender.TestUtil.CreateTestMethodDeclaration;
import static exec.recommender_reimplementation.frequency_recommender.TestUtil.method1;
import static exec.recommender_reimplementation.frequency_recommender.TestUtil.method2;
import static exec.recommender_reimplementation.frequency_recommender.TestUtil.method3;

import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;

public class FrequencyModelGeneratorTest {

	private FrequencyModelGenerator uut = new FrequencyModelGenerator();
	
	@Test
	public void GenerateFrequencyModelTest() {
		SST testSST = new SST();
		Set<IMethodDeclaration> methods = Sets.newHashSet();
		methods.add(CreateTestMethodDeclaration(true));
		methods.add(CreateTestMethodDeclaration(false));
		methods.add(CreateTestMethodDeclaration(true));
		testSST.setMethods(methods);
		
		Map<IMethodName,MethodFrequency> generatedModel = uut.generateMethodFrequencyModelForSST(testSST, Maps.newHashMap());
		
		Assert.assertEquals(new MethodFrequency(method1, 4), generatedModel.get(method1));
		Assert.assertEquals(new MethodFrequency(method2, 6), generatedModel.get(method2));
		Assert.assertEquals(new MethodFrequency(method3, 4), generatedModel.get(method3));		
	}
	
}
