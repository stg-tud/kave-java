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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.dummies.DummyUsage;
import cc.recommenders.usages.DefinitionSiteKind;

public class PointsToUsageExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointsToUsageExtractor.class);

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
		String className = context.getSST().getEnclosingType().getName();
		for (IMethodDeclaration methodDecl : context.getSST().getEntryPoints()) {
			visitor.visitEntryPoint(methodDecl, visitorContext);

			List<DummyUsage> rawUsages = visitorContext.getUsages();
			List<DummyUsage> processedUsages = processUsages(rawUsages, context.getTypeShape());
			contextUsages.addAll(processedUsages);

			LOGGER.info("Extracted {} usages from {}:{}", processedUsages.size(), className,
					methodDecl.getName().getName());
			collector.onEntryPointUsagesExtracted(methodDecl, processedUsages);
		}

		return contextUsages;
	}

	private List<DummyUsage> processUsages(List<DummyUsage> rawUsages, ITypeShape typeShape) {
		List<DummyUsage> processedUsages = pruneUsages(rawUsages);
		rewriteUsages(processedUsages, typeShape);
		return processedUsages;
	}

	private List<DummyUsage> pruneUsages(List<DummyUsage> usages) {
		List<DummyUsage> retainedUsages = new ArrayList<>(usages.size());

		for (DummyUsage usage : usages) {
			// prune usages that have no call sites or have an unknown type
			if (!usage.getAllCallsites().isEmpty() && !usage.getType().isUnknownType()) {
				retainedUsages.add(usage);
			}
		}

		return retainedUsages;
	}

	private void rewriteUsages(List<DummyUsage> usages, ITypeShape typeShape) {
		rewriteThisUsages(usages, typeShape);
	}

	private void rewriteThisUsages(List<DummyUsage> usages, ITypeShape typeShape) {
		ITypeHierarchy typeHierarchy = typeShape.getTypeHierarchy();
		if (typeHierarchy.hasSuperclass()) {
			ITypeName superType = typeHierarchy.getExtends().getElement();

			for (DummyUsage usage : usages) {
				// change type of usages referring to the enclosing class to the super class
				if (usage.getDefinitionSite().getKind() == DefinitionSiteKind.THIS) {
					if (superType != null) {
						// TODO maybe add check whether this is safe (call sites do not refer to 'this')
						usage.setType(superType);
					}
				}
			}

		}
	}
}
