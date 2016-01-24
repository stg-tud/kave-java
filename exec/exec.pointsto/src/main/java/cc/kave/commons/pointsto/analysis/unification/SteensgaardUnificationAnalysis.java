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
package cc.kave.commons.pointsto.analysis.unification;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.AbstractPointerAnalysis;
import cc.kave.commons.pointsto.analysis.Callpath;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.QueryContextKey;
import cc.kave.commons.pointsto.analysis.QueryStrategy;
import cc.kave.commons.pointsto.analysis.reference.DistinctReference;

public class SteensgaardUnificationAnalysis extends AbstractPointerAnalysis {

	private static final Logger LOGGER = LoggerFactory.getLogger(SteensgaardUnificationAnalysis.class);

	@Override
	public PointsToContext compute(Context context) {
		checkContextBinding();

		UnificationAnalysisVisitor visitor = new UnificationAnalysisVisitor();
		UnificationAnalysisVisitorContext visitorContext = new UnificationAnalysisVisitorContext(context);

		visitor.visit(context.getSST(), visitorContext);
		Map<DistinctReference, AbstractLocation> referenceToLocation = visitorContext.getReferenceLocations();

		// collect information needed to create the query keys
		DistinctReferenceContextCollector contextCollector = new DistinctReferenceContextCollector(context);
		new DistinctReferenceContextCollectorVisitor().visit(context.getSST(), contextCollector);

		QueryKeyTransformer transformer = new QueryKeyTransformer(true);
		for (Map.Entry<DistinctReference, AbstractLocation> refLoc : referenceToLocation.entrySet()) {
			List<QueryContextKey> queryKeys = refLoc.getKey().accept(transformer, contextCollector);

			for (QueryContextKey queryKey : queryKeys) {
				contextToLocations.put(queryKey, refLoc.getValue());
			}
		}

		return new PointsToContext(context, this);
	}

	@Override
	public Set<AbstractLocation> query(QueryContextKey query) {
		IReference reference = normalizeReference(query.getReference());
		TypeName type = normalizeType(query.getType());
		// use the current method for the query
		Callpath methodPath = null;
		if (query.getCallpath() != null) {
			MethodName method = normalizeMethod(query.getCallpath().getLast());
			methodPath = (method != null) ? new Callpath(method) : null;
		}

		Collection<AbstractLocation> locations = contextToLocations
				.get(new QueryContextKey(reference, query.getStmt(), type, methodPath));
		if (locations.isEmpty()) {
			// drop method
			locations = contextToLocations.get(new QueryContextKey(reference, query.getStmt(), type, null));
			if (!locations.isEmpty()) {
				return new HashSet<>(locations);
			}

			// drop statements
			locations = contextToLocations.get(new QueryContextKey(reference, null, type, methodPath));
			if (!locations.isEmpty()) {
				return new HashSet<>(locations);
			}

			// drop statements & method
			locations = contextToLocations.get(new QueryContextKey(reference, null, type, null));
			if (locations.isEmpty()) {
				LOGGER.warn("Failed to find a location after dropping both statement and method");
			}
		}

		if (locations.isEmpty() && queryStrategy == QueryStrategy.EXHAUSTIVE) {
			locations = contextToLocations.values();
		}

		return new HashSet<>(locations);
	}

}
