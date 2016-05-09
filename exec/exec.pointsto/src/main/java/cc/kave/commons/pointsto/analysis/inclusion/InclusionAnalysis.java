/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.analysis.inclusion;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Multimap;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.AbstractPointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.FallbackIndexAccessAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.contexts.EmptyContextFactory;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintEdge;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintGraph;
import cc.kave.commons.pointsto.analysis.references.DistinctIndexAccessReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.references.conversion.DistinctReferenceContextCollector;
import cc.kave.commons.pointsto.analysis.references.conversion.DistinctReferenceContextCollectorVisitor;
import cc.kave.commons.pointsto.analysis.references.conversion.QueryKeyTransformer;
import cc.kave.commons.pointsto.analysis.visitors.ThisReferenceOption;

/**
 * A field-sensitive inclusion analysis.
 * 
 * @see <a href="http://dx.doi.org/10.1145/504282.504286">Points-to Analysis for Java Using Annotated Constraints</a> by
 *      Rountev, Milanova, and Ryder <br>
 *      <a href="http://dx.doi.org/10.1109/ICSM.2005.24">Annotated Inclusion Constraints for Precise Flow Analysis</a>
 *      by Milanova and Ryder
 */
public class InclusionAnalysis extends AbstractPointsToAnalysis {

	@Override
	public PointsToContext compute(Context context) {
		checkContextBinding();

		ConstraintGenerationVisitor visitor = new ConstraintGenerationVisitor();
		ConstraintGenerationVisitorContext visitorContext = new ConstraintGenerationVisitorContext(context,
				new EmptyContextFactory());
		context.getSST().accept(visitor, visitorContext);

		ConstraintGraph graph = visitorContext.createConstraintGraph();
		graph.computeClosure();
		Multimap<DistinctReference, ConstraintEdge> ls = graph.computeLeastSolution();
		Map<ConstructedTerm, AbstractLocation> locations = new HashMap<>();

		DistinctReferenceContextCollector contextCollector = new DistinctReferenceContextCollector(context,
				ThisReferenceOption.PER_MEMBER);
		context.getSST().accept(new DistinctReferenceContextCollectorVisitor(), contextCollector);
		QueryKeyTransformer queryKeyTransformer = new QueryKeyTransformer(true);

		Set<AbstractLocation> redundantFallbackObjects = new HashSet<>();
		for (DistinctReference distRef : ls.keySet()) {
			// System.out.println(distRef.toString() + ":");

			Collection<ConstraintEdge> edges = ls.get(distRef);
			Set<AbstractLocation> fallbackObjects = new HashSet<>(edges.size());
			boolean hasAnyNonFallbackObjects = false;
			for (ConstraintEdge edge : edges) {
				ConstructedTerm cTerm = (ConstructedTerm) edge.getTarget().getSetExpression();
				AbstractLocation location = locations.get(cTerm);
				if (location == null) {
					location = new AbstractLocation();
					locations.put(cTerm, location);
				}
				// System.out.println("\t\t" + edge.toString());
				// System.out.println("\t\t" + location.toString());

				if (distRef instanceof DistinctIndexAccessReference && cTerm instanceof RefTerm
						&& ((RefTerm) cTerm).getAllocationSite() instanceof FallbackIndexAccessAllocationSite) {
					fallbackObjects.add(location);
				} else {
					hasAnyNonFallbackObjects = true;
				}

				List<PointsToQuery> queries = distRef.accept(queryKeyTransformer, contextCollector);
				for (PointsToQuery query : queries) {
					contextToLocations.put(query, location);
				}
			}
			// a fallback object is redundant if its spawning index access has other 'normal' objects
			// in that case the fallback object is completely removed from the end result (including other points-to
			// sets)
			if (hasAnyNonFallbackObjects && !fallbackObjects.isEmpty()) {
				redundantFallbackObjects.addAll(fallbackObjects);
			}
		}

		// remove locations that have been marked as redundant
		contextToLocations.values().removeAll(redundantFallbackObjects);

		return new PointsToContext(context, this);
	}

}
