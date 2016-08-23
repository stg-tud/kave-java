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

import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;

public class FrequencyModelGenerator {
	
	public Map<IMethodName, MethodFrequency> generateMethodFrequencyModelForSST(ISST sst, Map<IMethodName, MethodFrequency> frequencyModel) {
		Set<IMethodDeclaration> entryPoints = sst.getEntryPoints();
		for (IMethodDeclaration methodDecl : entryPoints) {
			methodDecl.accept(new MethodFrequencyVisitor(), frequencyModel);
		}
		return frequencyModel;
	}
	
	public void generateMethodFrequencyModelForContext(Context context, Map<IMethodName, MethodFrequency> frequencyModel) {
		generateMethodFrequencyModelForSST(context.getSST(), frequencyModel);
	}
		
}
