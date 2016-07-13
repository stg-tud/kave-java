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

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;

public class HistoryExtractor {
	
	public static Set<ConcreteHistory> extractHistories(Context context) {
		PointsToContext pointsToContext = performPointsToAnalysis(context);
		
		ISST sst = context.getSST();
		
		Set<ConcreteHistory> concreteHistorySet = new HashSet<>();
		
		for (IMethodDeclaration methodDecl : sst.getMethods()) {
			HistoryMap historyMap = new HistoryMap();
			methodDecl.accept(new RaychevAnalysisVisitor(pointsToContext), historyMap);
			
			for (AbstractHistory abstractHistory : historyMap.values()) {
				concreteHistorySet.addAll(abstractHistory.getHistorySet());
			}

		}
		
		return concreteHistorySet;
	}
	
	public static PointsToContext performPointsToAnalysis(Context context) {
		PointsToAnalysis pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);
		return pointsToAnalysis.compute(context);
	}
}
