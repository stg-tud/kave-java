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

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.IReference;

/**
 * A {@link PointerAnalysis} that assumes that all variables of a specific type point to one {@link AbstractLocation}.
 *
 */
public class TypeAliasedAnalysis extends AbstractPointerAnalysis {

	private static final Logger LOGGER = Logger.getLogger(TypeAliasedAnalysis.class.getName());

	private IdentityHashMap<IReference, AbstractLocation> referenceLocations;

	public TypeAliasedAnalysis() {
		referenceLocations = new IdentityHashMap<>();
	}

	@Override
	public PointsToContext compute(Context context) {
		TypeAliasedVisitor visitor = new TypeAliasedVisitor();
		TypeAliasedVisitorContext visitorContext = new TypeAliasedVisitorContext();

		visitorContext.initializeSymbolTable(context);
		visitor.visit(context.getSST(), visitorContext);

		referenceLocations = visitorContext.getReferenceLocations();
		return new PointsToContext(context, this);
	}

	@Override
	public Set<AbstractLocation> query(QueryContextKey query) {
		// lower query to used format
		return super.query(new QueryContextKey(null, null, query.getType(), null));
	}

	public Set<AbstractLocation> query(IReference reference, Callpath callpath) {
		AbstractLocation location = referenceLocations.get(reference);
		if (location == null) {
			// assume that the reference could point to any known location
			LOGGER.log(Level.INFO, "Unknown reference " + reference.toString());
			return new HashSet<>(referenceLocations.values());
		}

		return Sets.newHashSet(location);
	}

}
