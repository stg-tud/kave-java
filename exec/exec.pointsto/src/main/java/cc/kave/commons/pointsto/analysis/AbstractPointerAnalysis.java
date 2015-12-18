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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public abstract class AbstractPointerAnalysis implements PointerAnalysis {

	protected Multimap<QueryContextKey, AbstractLocation> contextToLocations = HashMultimap.create();

	@Override
	public Set<AbstractLocation> query(QueryContextKey query) {
		Collection<AbstractLocation> locations = contextToLocations.get(query);
		
		if (locations.isEmpty()) {
			// conservative assumption: may point to any known location
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Failed to find any matching entries for " + query.toString());
			locations = contextToLocations.values();
		}

		return new HashSet<>(locations);
	}

}
