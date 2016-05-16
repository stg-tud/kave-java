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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;

public class MethodFrequencyVisitorTest {

	@Test
	public void MethodFrequencyCountedCorrectlyForOneMethodDecl() {
		IMethodDeclaration testMethodDecl = CreateTestMethodDeclaration(true);
		Map<IMethodName,MethodFrequency> frequencyDict = new HashMap<IMethodName, MethodFrequency>();
		testMethodDecl.accept(new MethodFrequencyVisitor(), frequencyDict);
		Assert.assertEquals(new MethodFrequency(method1, 2), frequencyDict.get(method1));
		Assert.assertEquals(new MethodFrequency(method2, 3), frequencyDict.get(method2));
		Assert.assertEquals(new MethodFrequency(method3, 2), frequencyDict.get(method3));
	}
	

}
