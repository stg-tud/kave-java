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

import java.util.Set;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;

/**
 * A {@link PointerAnalysis} that assumes that all variables of a specific type point to one {@link AbstractLocation}.
 *
 */
public class TypeAliasedAnalysis extends AbstractPointerAnalysis {

	@Override
	public PointsToContext compute(Context context) {
		TypeCollector typeCollector = new TypeCollector(context);
		for (TypeName type : typeCollector.getTypes()) {
			QueryContextKey key = new QueryContextKey(null, null, type, null);
			contextToLocations.put(key, new AbstractLocation());
		}

		return new PointsToContext(context, this);
	}

	@Override
	public Set<AbstractLocation> query(QueryContextKey query) {
		// lower query to used format
		return super.query(new QueryContextKey(null, null, query.getType(), null));
	}

}
