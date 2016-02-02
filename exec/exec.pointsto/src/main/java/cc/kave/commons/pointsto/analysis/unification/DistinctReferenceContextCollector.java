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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.analysis.DistinctReferenceVisitorContext;
import cc.kave.commons.pointsto.analysis.reference.DistinctFieldReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctPropertyReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReference;

public class DistinctReferenceContextCollector extends DistinctReferenceVisitorContext {

	private Logger LOGGER = LoggerFactory.getLogger(DistinctReferenceContextCollector.class);

	private IMethodName currentMethod;
	private IStatement currentStatement;

	private Multimap<DistinctReference, IMethodName> referenceToMethods = HashMultimap.create();
	private Multimap<DistinctReference, IStatement> referenceToStmts = HashMultimap.create();

	public DistinctReferenceContextCollector(Context context) {
		super(context);
	}

	public Collection<IMethodName> getMethods(DistinctReference ref) {
		return referenceToMethods.get(ref);
	}

	public Collection<IStatement> getStatements(DistinctReference ref) {
		return referenceToStmts.get(ref);
	}

	public void setCurrentMethod(IMethodName method) {
		this.currentMethod = method;
	}

	public void setCurrentStatement(IStatement stmt) {
		this.currentStatement = stmt;
	}

	public void useReference(IVariableReference varRef) {
		if (varRef.isMissing()) {
			LOGGER.warn("Skipping missing variable reference");
			return;
		}

		DistinctReference ref = namesToReferences.get(varRef.getIdentifier());
		if (ref == null) {
			LOGGER.error("Found a variable without a prior declaration: {}", varRef.getIdentifier());
		} else {
			referenceToMethods.put(ref, currentMethod);
			referenceToStmts.put(ref, currentStatement);
		}
	}

	public void useReference(IFieldReference fieldRef) {
		DistinctReference ref = new DistinctFieldReference(fieldRef,
				namesToReferences.get(fieldRef.getReference().getIdentifier()));
		referenceToMethods.put(ref, currentMethod);
		referenceToStmts.put(ref, currentStatement);
	}

	public void useReference(IPropertyReference propertyRef) {
		DistinctReference ref = new DistinctPropertyReference(propertyRef,
				namesToReferences.get(propertyRef.getReference().getIdentifier()));
		referenceToMethods.put(ref, currentMethod);
		referenceToStmts.put(ref, currentStatement);
	}

}
