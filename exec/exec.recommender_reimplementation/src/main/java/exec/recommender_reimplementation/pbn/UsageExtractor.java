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
package exec.recommender_reimplementation.pbn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningContext;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.recommenders.usages.Usage;

public class UsageExtractor {
	
	public List<Usage> extractUsageFromContext(Context context) {
		ISST sst = context.getSST();
		
		ISST inlinedSST = inlineSST(sst);
		
		context.setSST(inlinedSST);
		
		PointsToContext pointsToContext = performPointsToAnalysis(context);
			
		Set<IMethodDeclaration> entrypoints = inlinedSST.getEntryPoints();
		
		List<Usage> usageList = new ArrayList<>();
		
		for (IMethodDeclaration methodDecl : entrypoints) {
			methodDecl.accept(new PBNAnalysisVisitor(pointsToContext), usageList);
		}
		
		return usageList;
	}

	private PointsToContext performPointsToAnalysis(Context context) {
		PointsToAnalysis pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);
		return pointsToAnalysis.compute(context);
	}

	private ISST inlineSST(ISST sst) {
		InliningContext inlining = new InliningContext();
		sst.accept(inlining.getVisitor(), inlining);
		ISST inlinedSST = inlining.getSST();
		return inlinedSST;
	}
}
