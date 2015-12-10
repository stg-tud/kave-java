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
package cc.kave.commons.pointsto.analysis.types;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IReference;

public class TypeCollector {

	private IdentityHashMap<IReference, TypeName> referenceTypes = new IdentityHashMap<>();

	public TypeCollector(Context context) {
		 TypeCollectorVisitor visitor = new TypeCollectorVisitor();
		 TypeCollectorVisitorContext visitorContext = new TypeCollectorVisitorContext();
		
		 visitorContext.initializeSymbolTable(context);
		 visitor.visit(context.getSST(), visitorContext);
		
		 referenceTypes = visitorContext.getReferenceTypes();
	}

	public TypeName getType(IReference reference) {
		return referenceTypes.get(reference);
	}

	public Set<TypeName> getTypes() {
		return new HashSet<>(referenceTypes.values());
	}
}
