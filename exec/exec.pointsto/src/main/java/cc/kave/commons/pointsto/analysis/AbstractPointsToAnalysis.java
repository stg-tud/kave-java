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
package cc.kave.commons.pointsto.analysis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.pointsto.analysis.visitors.ReferenceNormalizationVisitor;

public abstract class AbstractPointsToAnalysis implements PointsToAnalysis {

	protected Multimap<PointsToQuery, AbstractLocation> contextToLocations = HashMultimap.create();

	protected QueryStrategy queryStrategy;

	private ReferenceNormalizationVisitor referenceNormalizationVisitor = new ReferenceNormalizationVisitor();

	public AbstractPointsToAnalysis() {
		this.queryStrategy = QueryStrategy.MINIMIZE_USAGE_DEFECTS;
	}

	@Override
	public QueryStrategy getQueryStrategy() {
		return queryStrategy;
	}

	@Override
	public void setQueryStrategy(QueryStrategy strategy) {
		this.queryStrategy = strategy;
	}

	/**
	 * Checks whether this {@link PointsToAnalysis} has already been bound to a {@link Context} and throws an
	 * {@link IllegalStateException} accordingly.
	 */
	protected void checkContextBinding() {
		if (!contextToLocations.isEmpty()) {
			throw new IllegalStateException("Analysis has already been bound to a context");
		}
	}

	@Override
	public Set<AbstractLocation> query(PointsToQuery query) {
		Collection<AbstractLocation> locations = contextToLocations.get(query);

		if (locations.isEmpty()) {
			// conservative assumption: may point to any known location
			LoggerFactory.getLogger(AbstractPointsToAnalysis.class).warn("Failed to find any matching entries for {}",
					query);
			locations = (queryStrategy == QueryStrategy.EXHAUSTIVE) ? contextToLocations.values()
					: Collections.emptyList();
		}

		return new HashSet<>(locations);
	}

	protected ITypeName normalizeType(ITypeName type) {
		if (type == null || type.isUnknownType() || type.isTypeParameter()) {
			return null;
		} else {
			return type;
		}
	}

	protected IMethodName normalizeMethod(IMethodName method) {
		// TODO replace with isUnknown once it is overridden
		return method.equals(MethodName.UNKNOWN_NAME) ? null : method;
	}

	protected IReference normalizeReference(IReference reference) {
		return reference.accept(referenceNormalizationVisitor, null);
	}

}
