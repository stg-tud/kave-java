/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.dummies.DummyUsage;

public class PointsToUsageExtractor {

	private static final Logger LOGGER = Logger.getLogger(PointsToUsageExtractor.class.getName());

	private UsageStatisticsCollector collector;

	public PointsToUsageExtractor() {
		this.collector = new UsageStatisticsCollector() {

			@Override
			public void onProcessContext(Context context) {

			}

			@Override
			public void merge(UsageStatisticsCollector other) {

			}

			@Override
			public void onEntryPointUsagesExtracted(IMethodDeclaration entryPoint, List<DummyUsage> usages) {

			}
		};
	}

	public PointsToUsageExtractor(UsageStatisticsCollector collector) {
		this.collector = collector;
	}

	public UsageStatisticsCollector getStatisticsCollector() {
		return collector;
	}

	public List<DummyUsage> extract(PointsToContext context) {
		collector.onProcessContext(context);

		List<DummyUsage> contextUsages = new ArrayList<>();
		UsageExtractionVisitor visitor = new UsageExtractionVisitor();
		UsageExtractionVisitorContext visitorContext = new UsageExtractionVisitorContext(context);
		for (IMethodDeclaration methodDecl : context.getSST().getEntryPoints()) {
//			visitor.visitEntryPoint(methodDecl, visitorContext);

			List<DummyUsage> entryPointUsages = visitorContext.getUsages();
			contextUsages.addAll(entryPointUsages);

			LOGGER.log(Level.INFO,
					"Extracted " + entryPointUsages.size() + " usages from " + methodDecl.getName().getIdentifier());
			collector.onEntryPointUsagesExtracted(methodDecl, entryPointUsages);
		}

		return contextUsages;
	}
}
