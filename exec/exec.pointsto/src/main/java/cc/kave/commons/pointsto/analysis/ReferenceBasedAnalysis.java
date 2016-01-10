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
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.pointsto.LanguageOptions;
import cc.kave.commons.pointsto.SSTBuilder;

public class ReferenceBasedAnalysis extends AbstractPointerAnalysis {

	private ReferenceNormalizationVisitor referenceNormalizationVisitor = new ReferenceNormalizationVisitor();

	@Override
	public PointsToContext compute(Context context) {
		checkContextBinding();

		Multimap<IReference, TypeName> referenceTypes = HashMultimap.create();
		ReferenceCollectionVisitor collectionVisitor = new ReferenceCollectionVisitor();
		collectionVisitor.visit(context.getSST(), referenceTypes);

		LanguageOptions languageOptions = LanguageOptions.getInstance();
		ITypeHierarchy typeHierarchy = context.getTypeShape().getTypeHierarchy();
		referenceTypes.put(SSTBuilder.variableReference(languageOptions.getThisName()), typeHierarchy.getElement());
		referenceTypes.put(SSTBuilder.variableReference(languageOptions.getSuperName()),
				languageOptions.getSuperType(typeHierarchy));

		for (Map.Entry<IReference, TypeName> entry : referenceTypes.entries()) {
			QueryContextKey query = new QueryContextKey(entry.getKey(), null, entry.getValue(), null);
			if (!contextToLocations.containsKey(query)) {
				contextToLocations.put(query, new AbstractLocation());
			}
		}

		return new PointsToContext(context, this);
	}

	@Override
	public Set<AbstractLocation> query(QueryContextKey query) {
		IReference reference = query.getReference();
		TypeName type = query.getType();

		reference = reference.accept(referenceNormalizationVisitor, null);

		if (type != null && type.isUnknownType()) {
			type = null;
		}

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

	private Set<AbstractLocation> queryType(TypeName type) {
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

	private static class ReferenceNormalizationVisitor extends AbstractNodeVisitor<Void, IReference> {

		@Override
		public IReference visit(IUnknownReference unknownRef, Void context) {
			return null;
		}

		@Override
		public IReference visit(IFieldReference fieldRef, Void context) {
			FieldName field = fieldRef.getFieldName();
			if (field.isUnknown()) {
				return null;
			} else if (!field.isStatic() && fieldRef.getReference().isMissing()) {
				return null;
			} else {
				return fieldRef;
			}
		}

		@Override
		public IReference visit(IVariableReference varRef, Void context) {
			return varRef.isMissing() ? null : varRef;
		}

		@Override
		public IReference visit(IPropertyReference propertyRef, Void context) {
			PropertyName property = propertyRef.getPropertyName();
			if (property.isUnknown()) {
				return null;
			} else if (!property.isStatic() && propertyRef.getReference().isMissing()) {
				return null;
			} else {
				return propertyRef;
			}
		}

	}

}
