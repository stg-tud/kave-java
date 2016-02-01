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
package cc.kave.commons.pointsto.analysis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.pointsto.LanguageOptions;
import cc.kave.commons.pointsto.SSTBuilder;

public class ReferenceBasedAnalysis extends AbstractPointerAnalysis {

	@Override
	public PointsToContext compute(Context context) {
		checkContextBinding();

		Multimap<IReference, ITypeName> referenceTypes = HashMultimap.create();
		ReferenceCollectionVisitor collectionVisitor = new ReferenceCollectionVisitor();
		collectionVisitor.visit(context.getSST(), referenceTypes);

		LanguageOptions languageOptions = LanguageOptions.getInstance();
		ITypeHierarchy typeHierarchy = context.getTypeShape().getTypeHierarchy();
		referenceTypes.put(SSTBuilder.variableReference(languageOptions.getThisName()), typeHierarchy.getElement());
		referenceTypes.put(SSTBuilder.variableReference(languageOptions.getSuperName()),
				languageOptions.getSuperType(typeHierarchy));

		for (Map.Entry<IReference, ITypeName> entry : referenceTypes.entries()) {
			QueryContextKey query = new QueryContextKey(entry.getKey(), null, entry.getValue(), null);
			if (!contextToLocations.containsKey(query)) {
				contextToLocations.put(query, new AbstractLocation());
			}
		}

		return new PointsToContext(context, this);
	}

	@Override
	public Set<AbstractLocation> query(QueryContextKey query) {
		IReference reference = normalizeReference(query.getReference());
		ITypeName type = normalizeType(query.getType());

		if (reference == null) {
			if (type != null) {
				return queryType(type);
			} else {
				// neither reference nor type available
				if (queryStrategy == QueryStrategy.EXHAUSTIVE) {
					return new HashSet<>(contextToLocations.values());
				} else {
					return Collections.emptySet();
				}
			}
		} else {
			if (type != null) {
				Collection<AbstractLocation> locations = contextToLocations
						.get(new QueryContextKey(reference, null, type, null));
				if (locations.isEmpty()) {
					LoggerFactory.getLogger(ReferenceBasedAnalysis.class)
							.warn("Failed to find any matching entries for {}", query);
					return queryType(type);
				} else {
					return new HashSet<>(locations);
				}
			} else {
				// only reference available
				if (queryStrategy == QueryStrategy.EXHAUSTIVE) {
					return queryReference(reference);
				} else {
					return Collections.emptySet();
				}
			}
		}
	}

	private Set<AbstractLocation> queryReference(IReference reference) {
		if (queryStrategy == QueryStrategy.EXHAUSTIVE) {
			Set<AbstractLocation> locations = new HashSet<>();
			for (Map.Entry<QueryContextKey, AbstractLocation> entry : contextToLocations.entries()) {
				if (entry.getKey().getReference().equals(reference)) {
					locations.add(entry.getValue());
				}
			}
			return locations;
		} else {
			return Collections.emptySet();
		}
	}

	private Set<AbstractLocation> queryType(ITypeName type) {
		if (queryStrategy == QueryStrategy.EXHAUSTIVE) {
			Set<AbstractLocation> locations = new HashSet<>();
			for (Map.Entry<QueryContextKey, AbstractLocation> entry : contextToLocations.entries()) {
				if (entry.getKey().getType().equals(type)) {
					locations.add(entry.getValue());
				}
			}
			return locations;
		} else {
			return Collections.emptySet();
		}
	}

}
