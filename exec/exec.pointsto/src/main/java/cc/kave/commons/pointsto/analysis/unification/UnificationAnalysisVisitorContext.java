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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jgrapht.alg.util.UnionFind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.MemberName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.pointsto.LanguageOptions;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.DistinctReferenceVisitorContext;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedSSTNodeException;
import cc.kave.commons.pointsto.analysis.reference.DistinctFieldReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctIndexAccessReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctMemberReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctPropertyParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctPropertyReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReferenceCreationVisitor;
import cc.kave.commons.pointsto.extraction.DeclarationMapper;
import cc.recommenders.assertions.Asserts;

public class UnificationAnalysisVisitorContext extends DistinctReferenceVisitorContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnificationAnalysisVisitorContext.class);

	private LanguageOptions languageOptions = LanguageOptions.getInstance();

	private DistinctReferenceCreationVisitor distinctReferenceCreationVisitor = new DistinctReferenceCreationVisitor();

	private Multimap<SetRepresentative, SetRepresentative> pending = HashMultimap.create();
	private UnionFind<SetRepresentative> unionFind = new UnionFind<>(new HashSet<>());

	private Map<DistinctReference, ReferenceLocation> referenceLocations = new HashMap<>();

	private IAssignment lastAssignment;

	private DeclarationMapper declarationMapper;
	private MemberName currentMember;
	private Multimap<MemberName, DistinctReference> returnedReferences = HashMultimap.create();
	private Multimap<MemberName, DistinctReference> requestedReturnReferences = HashMultimap.create();
	private Multimap<PropertyName, ReferenceLocation> requestedReturnLocations = HashMultimap.create();

	private DistinctAssignmentHandler distinctAssignmentHandler = new DistinctAssignmentHandler();

	public UnificationAnalysisVisitorContext(Context context) {
		super(context);
		declarationMapper = new DeclarationMapper(context);

		createImplicitLocations(context);
	}

	public Map<DistinctReference, AbstractLocation> getReferenceLocations() {
		Map<DistinctReference, AbstractLocation> result = new HashMap<>(referenceLocations.size());
		Map<SetRepresentative, AbstractLocation> refToAbstractLocation = new HashMap<>(referenceLocations.size());

		for (Map.Entry<DistinctReference, ReferenceLocation> entry : referenceLocations.entrySet()) {
			DistinctReference reference = entry.getKey();

			SetRepresentative ecr = unionFind.find(entry.getValue().getLocation().getSetRepresentative());

			AbstractLocation abstractLocation = refToAbstractLocation.get(ecr);
			if (abstractLocation == null) {
				abstractLocation = new AbstractLocation();
				refToAbstractLocation.put(ecr, abstractLocation);
			}

			result.put(reference, abstractLocation);
		}

		return result;
	}

	private void createImplicitLocations(Context context) {
		DistinctReference thisRef = namesToReferences.get(languageOptions.getThisName());
		allocate(thisRef);
		DistinctReference superRef = namesToReferences.get(languageOptions.getSuperName());

		// create locations and let 'this' and 'super' point to the same object
		copy(superRef, thisRef);
	}

	public void finalize() {
		// connect destination of method / property invocations to the returned references
		for (Map.Entry<MemberName, DistinctReference> requestedRefEntry : requestedReturnReferences.entries()) {
			Collection<DistinctReference> returnedRefs = returnedReferences.get(requestedRefEntry.getKey());
			DistinctReference destRef = requestedRefEntry.getValue();

			if (returnedRefs.isEmpty()) {
				// no returned location available (probably a call to a method of another class)
				allocate(destRef);
			} else {
				for (DistinctReference returnedRef : returnedRefs) {
					distinctAssignmentHandler.process(destRef, returnedRef);
				}
			}
		}

		// connect destination of custom property getters to the returned references
		for (Map.Entry<PropertyName, ReferenceLocation> requestedLocEntry : requestedReturnLocations.entries()) {
			Collection<DistinctReference> returnedRefs = returnedReferences.get(requestedLocEntry.getKey());
			ReferenceLocation destLocation = requestedLocEntry.getValue();

			for (DistinctReference returnedRef : returnedRefs) {
				IReference sstRef = returnedRef.getReference();
				if (sstRef instanceof IVariableReference) {
					copy(destLocation, getOrCreateLocation(returnedRef));
				} else if (sstRef instanceof IFieldReference) {
					IFieldReference fieldRef = (IFieldReference) sstRef;
					readMember(destLocation, fieldRef, fieldRef.getFieldName());
				} else if (sstRef instanceof IPropertyReference) {
					IPropertyReference propertyRef = (IPropertyReference) sstRef;
					if (treatPropertyAsField(propertyRef)) {
						copy(destLocation, getOrCreateLocation(returnedRef));
					} else {
						readMember(destLocation, propertyRef, propertyRef.getPropertyName());
					}
				} else if (sstRef instanceof IUnknownReference) {
					LOGGER.error("Ignoring unknown reference");
				} else {
					throw new UnexpectedSSTNodeException(sstRef);
				}
			}
		}

		finalizePendingJoins();
	}

	private void finalizePendingJoins() {
		for (SetRepresentative rep : pending.keySet()) {
			for (SetRepresentative x : pending.get(rep)) {
				join(rep, x);
			}
		}
		pending.clear();
	}

	}

	private ReferenceLocation createReferenceLocation() {
		SetRepresentative bottomRepresentative = new SetRepresentative();
		BottomLocation bottom = new BottomLocation(bottomRepresentative);

		SetRepresentative refRepresentative = new SetRepresentative();
		ReferenceLocation refLocation = new ReferenceLocation(bottom, refRepresentative);

		unionFind.addElement(bottomRepresentative);
		unionFind.addElement(refRepresentative);
		return refLocation;
	}

	private ReferenceLocation getOrCreateLocation(DistinctReference reference) {
		ReferenceLocation location = referenceLocations.get(reference);

		if (location == null) {
			location = createReferenceLocation();
			referenceLocations.put(reference, location);
		}

		return location;
	}

	public void registerReference(DistinctReference reference, ReferenceLocation location) {
		if (!referenceLocations.containsKey(reference)) {
			referenceLocations.put(reference, location);
		}
	}

	private void setLocation(SetRepresentative setRepresentative, Location newLoc) {
		setRepresentative.setLocation(newLoc);
		newLoc.setSetRepresentative(setRepresentative);

		for (SetRepresentative x : pending.get(setRepresentative)) {
			join(setRepresentative, x);
		}
		pending.removeAll(setRepresentative);

	}

	private void cjoin(SetRepresentative rep1, SetRepresentative rep2) {
		if (rep2.getLocation().isBottom()) {
			pending.put(rep2, rep1);
		} else {
			join(rep1, rep2);
		}
	}

	private void join(SetRepresentative rep1, SetRepresentative rep2) {
		Location loc1 = rep1.getLocation();
		Location loc2 = rep2.getLocation();

		unionFind.union(rep1, rep2);
		SetRepresentative unionRep = unionFind.find(rep1);
		loc1.setSetRepresentative(unionRep);
		loc2.setSetRepresentative(unionRep);

		if (loc1.isBottom()) {
			unionRep.setLocation(loc2);
			if (loc2.isBottom()) {
				if (unionRep != rep1) {
					pending.putAll(unionRep, pending.get(rep1));
				}
				if (unionRep != rep2) {
					pending.putAll(unionRep, pending.get(rep2));
				}
			} else {
				for (SetRepresentative x : pending.get(rep1)) {
					join(unionRep, x);
				}
				pending.removeAll(rep1);
			}
		} else {
			unionRep.setLocation(loc1);
			if (loc2.isBottom()) {
				for (SetRepresentative x : pending.get(rep2)) {
					join(unionRep, x);
				}
				pending.removeAll(rep2);
			} else {
				// both locations are not bottom -> must be references
				unify((ReferenceLocation) loc1, (ReferenceLocation) loc2);
			}

		}
	}

	private void unify(ReferenceLocation refLoc1, ReferenceLocation refLoc2) {
		Location loc1 = refLoc1.getLocation();
		Location loc2 = refLoc2.getLocation();

		SetRepresentative rep1 = unionFind.find(loc1.getSetRepresentative());
		SetRepresentative rep2 = unionFind.find(loc2.getSetRepresentative());

		if (rep1 != rep2) {
			join(rep1, rep2);
		}

	}

	public void setLastAssignment(IAssignment assignment) {
		lastAssignment = assignment;
	}

	public void allocate(IInvocationExpression invocation) {
		if (lastAssignment == null || lastAssignment.getExpression() != invocation) {
			// allocated object not bound to a name
			return;
		}

		IAssignableReference targetRef = lastAssignment.getReference();
		if (targetRef instanceof IUnknownReference) {
			LOGGER.error("Ignoring an allocation due to an unknown reference");
			return;
		}

		DistinctReference distinctReference = lastAssignment.getReference().accept(distinctReferenceCreationVisitor,
				namesToReferences);
		allocate(distinctReference);
	}

	private void allocate(DistinctReference destRef) {
		ReferenceLocation location = (ReferenceLocation) unionFind
				.find(getOrCreateLocation(destRef).getSetRepresentative()).getLocation();
		Location derefLocation = location.getLocation();

		if (derefLocation.isBottom()) {
			ReferenceLocation allocatedLocation = createReferenceLocation();
			setLocation(derefLocation.getSetRepresentative(), allocatedLocation);
		}
	}

	private ReferenceLocation readDereference(ReferenceLocation destLocation, ReferenceLocation srcLocation) {
		SetRepresentative destDerefRep = unionFind.find(destLocation.getLocation().getSetRepresentative());
		SetRepresentative srcDerefRep = unionFind.find(srcLocation.getLocation().getSetRepresentative());

		if (srcDerefRep.getLocation().isBottom()) {
			setLocation(srcDerefRep, new ReferenceLocation(destDerefRep.getLocation(), destDerefRep));
		} else {
			ReferenceLocation fieldLoc = (ReferenceLocation) srcDerefRep.getLocation();
			SetRepresentative fieldDerefRep = unionFind.find(fieldLoc.getLocation().getSetRepresentative());
			if (destDerefRep != fieldDerefRep) {
				cjoin(destDerefRep, fieldDerefRep);
			}
		}

		return (ReferenceLocation) srcDerefRep.getLocation();
	}

	private ReferenceLocation writeDereference(ReferenceLocation destLocation, ReferenceLocation srcLocation) {
		SetRepresentative destDerefRep = unionFind.find(destLocation.getLocation().getSetRepresentative());
		SetRepresentative srcDerefRep = unionFind.find(srcLocation.getLocation().getSetRepresentative());

		if (destDerefRep.getLocation().isBottom()) {
			setLocation(destDerefRep, new ReferenceLocation(srcDerefRep.getLocation(), srcDerefRep));
		} else {
			ReferenceLocation fieldLoc = (ReferenceLocation) destDerefRep.getLocation();
			SetRepresentative fieldDerefRep = unionFind.find(fieldLoc.getLocation().getSetRepresentative());
			if (fieldDerefRep != srcDerefRep) {
				cjoin(fieldDerefRep, srcDerefRep);
			}
		}

		return (ReferenceLocation) destDerefRep.getLocation();
	}

	public void copy(IVariableReference dest, IVariableReference src) {
		copy(dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void copy(DistinctReference dest, DistinctReference src) {
		Asserts.assertNotNull(dest);
		Asserts.assertNotNull(src);
		ReferenceLocation destLocation = getOrCreateLocation(dest);
		ReferenceLocation srcLocation = getOrCreateLocation(src);
		copy(destLocation, srcLocation);
	}

	private void copy(ReferenceLocation destLocation, ReferenceLocation srcLocation) {
		SetRepresentative derefDestRep = unionFind.find(destLocation.getLocation().getSetRepresentative());
		SetRepresentative derefSrcRep = unionFind.find(srcLocation.getLocation().getSetRepresentative());

		if (derefDestRep != derefSrcRep) {
			cjoin(derefDestRep, derefSrcRep);
		}
	}

	public void assign(IFieldReference dest, IFieldReference src) {
		assign((DistinctFieldReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctFieldReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctFieldReference dest, DistinctFieldReference src) {
		ReferenceLocation tempLoc = createReferenceLocation();
		readMember(tempLoc, src);
		writeMember(dest, tempLoc);
	}

	public void assign(IPropertyReference dest, IPropertyReference src) {
		assign((DistinctPropertyReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctPropertyReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctPropertyReference dest, DistinctPropertyReference src) {
		ReferenceLocation tempLoc = createReferenceLocation();
		PropertyName srcProperty = src.getReference().getPropertyName();
		if (treatPropertyAsField(src.getReference())) {
			readMember(tempLoc, src);
		} else {
			requestedReturnLocations.put(srcProperty, tempLoc);
		}

		PropertyName destProperty = dest.getReference().getPropertyName();
		if (treatPropertyAsField(dest.getReference())) {
			writeMember(dest, tempLoc);
		} else {
			DistinctReference destPropertyParameter = new DistinctPropertyParameterReference(languageOptions,
					destProperty);
			copy(getOrCreateLocation(destPropertyParameter), tempLoc);
		}
	}

	public void assign(IPropertyReference dest, IFieldReference src) {
		assign((DistinctPropertyReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctFieldReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctPropertyReference dest, DistinctFieldReference src) {
		ReferenceLocation tempLoc = createReferenceLocation();
		readMember(tempLoc, src);

		PropertyName destProperty = dest.getReference().getPropertyName();
		if (treatPropertyAsField(dest.getReference())) {
			writeMember(dest, tempLoc);
		} else {
			DistinctReference destPropertyParameter = new DistinctPropertyParameterReference(languageOptions,
					destProperty);
			copy(getOrCreateLocation(destPropertyParameter), tempLoc);
		}
	}

	public void assign(IFieldReference dest, IPropertyReference src) {
		assign((DistinctFieldReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctPropertyReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctFieldReference dest, DistinctPropertyReference src) {
		ReferenceLocation tempLoc = createReferenceLocation();
		PropertyName srcProperty = src.getReference().getPropertyName();
		if (treatPropertyAsField(src.getReference())) {
			readMember(tempLoc, src);
		} else {
			requestedReturnLocations.put(srcProperty, tempLoc);
		}

		writeMember(dest, tempLoc);
	}

	public void assign(IFieldReference dest, IIndexAccessReference src) {
		assign((DistinctFieldReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctIndexAccessReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctFieldReference dest, DistinctIndexAccessReference src) {
		ReferenceLocation tempLoc = createReferenceLocation();
		readArray(tempLoc, src);
		writeMember(dest, tempLoc);
	}

	public void assign(IPropertyReference dest, IIndexAccessReference src) {
		assign((DistinctPropertyReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctIndexAccessReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctPropertyReference dest, DistinctIndexAccessReference src) {
		ReferenceLocation tempLoc = createReferenceLocation();
		readArray(tempLoc, src);

		PropertyName destProperty = dest.getReference().getPropertyName();
		if (treatPropertyAsField(dest.getReference())) {
			writeMember(dest, tempLoc);
		} else {
			DistinctReference destPropertyParameter = new DistinctPropertyParameterReference(languageOptions,
					destProperty);
			copy(getOrCreateLocation(destPropertyParameter), tempLoc);
		}
	}

	public void assign(IIndexAccessReference dest, IFieldReference src) {
		assign((DistinctIndexAccessReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctFieldReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctIndexAccessReference dest, DistinctFieldReference src) {
		ReferenceLocation tempLoc = createReferenceLocation();
		readMember(tempLoc, src);
		writeArray(dest, tempLoc);
	}

	public void assign(IIndexAccessReference dest, IPropertyReference src) {
		assign((DistinctIndexAccessReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctPropertyReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctIndexAccessReference dest, DistinctPropertyReference src) {
		ReferenceLocation tempLoc = createReferenceLocation();

		PropertyName srcProperty = src.getReference().getPropertyName();
		if (treatPropertyAsField(src.getReference())) {
			readMember(tempLoc, src);
		} else {
			requestedReturnLocations.put(srcProperty, tempLoc);
		}

		writeArray(dest, tempLoc);
	}

	public void assign(IIndexAccessReference dest, IIndexAccessReference src) {
		assign((DistinctIndexAccessReference) dest, (DistinctIndexAccessReference) src);
	}

	private void assign(DistinctIndexAccessReference dest, DistinctIndexAccessReference src) {
		ReferenceLocation tempLoc = createReferenceLocation();
		readArray(tempLoc, src);
		writeArray(dest, tempLoc);
	}

	public void readField(IVariableReference destRef, IFieldReference fieldRef) {
		readField(destRef.accept(distinctReferenceCreationVisitor, namesToReferences), fieldRef);
	}

	private void readField(DistinctReference destRef, IFieldReference fieldRef) {
		readMember(getOrCreateLocation(destRef), fieldRef, fieldRef.getFieldName());
	}

	private void readField(DistinctReference destRef, DistinctFieldReference fieldRef) {
		readMember(getOrCreateLocation(destRef), fieldRef);
	}

	private void readMember(ReferenceLocation destLocation, IMemberReference memberRef, MemberName member) {
		DistinctReference distinctMemberRef = memberRef.accept(distinctReferenceCreationVisitor, namesToReferences);
		readMember(destLocation, (DistinctMemberReference) distinctMemberRef);
	}

	private void readMember(ReferenceLocation destLocation, DistinctMemberReference memberRef) {
		if (memberRef.isStaticMember()) {
			// model static members (fields, properties) as global variables
			copy(destLocation, getOrCreateLocation(memberRef));
		} else {

			ReferenceLocation srcLocation = getOrCreateLocation(memberRef.getBaseReference());
			ReferenceLocation srcDerefLocation = readDereference(destLocation, srcLocation);

			registerReference(memberRef, (ReferenceLocation) srcDerefLocation);
		}
	}

	public void writeField(IFieldReference fieldRef, IVariableReference srcRef) {
		writeField(fieldRef, srcRef.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void writeField(IFieldReference fieldRef, DistinctReference srcRef) {
		writeField((DistinctFieldReference) fieldRef.accept(distinctReferenceCreationVisitor, namesToReferences),
				srcRef);
	}

	private void writeField(DistinctFieldReference fieldRef, DistinctReference srcRef) {
		ReferenceLocation srcLocation = getOrCreateLocation(srcRef);
		writeMember(fieldRef, srcLocation);
	}

	private void writeMember(DistinctMemberReference memberRef, ReferenceLocation srcLocation) {
		if (memberRef.isStaticMember()) {
			// model static members (fields, properties) as global variables
			copy(getOrCreateLocation(memberRef), srcLocation);
		} else {

			ReferenceLocation destLocation = getOrCreateLocation(memberRef.getBaseReference());
			ReferenceLocation destDerefLocation = writeDereference(destLocation, srcLocation);

			registerReference(memberRef, destDerefLocation);
		}
	}

	public void readProperty(IVariableReference destRef, IPropertyReference propertyRef) {
		readProperty(destRef.accept(distinctReferenceCreationVisitor, namesToReferences), propertyRef);
	}

	private void readProperty(DistinctReference destRef, IPropertyReference propertyRef) {
		readProperty(destRef,
				(DistinctPropertyReference) propertyRef.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void readProperty(DistinctReference destRef, DistinctPropertyReference propertyRef) {
		PropertyName property = propertyRef.getReference().getPropertyName();
		if (treatPropertyAsField(propertyRef.getReference())) {
			ReferenceLocation destLocation = getOrCreateLocation(destRef);
			readMember(destLocation, propertyRef);
		} else {
			requestedReturnReferences.put(property, destRef);
		}
	}

	public void writeProperty(IPropertyReference propertyRef, IVariableReference srcRef) {
		writeProperty(propertyRef, srcRef.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void writeProperty(IPropertyReference propertyRef, DistinctReference srcRef) {
		writeProperty(
				(DistinctPropertyReference) propertyRef.accept(distinctReferenceCreationVisitor, namesToReferences),
				srcRef);
	}

	private void writeProperty(DistinctPropertyReference propertyRef, DistinctReference srcRef) {
		PropertyName property = propertyRef.getReference().getPropertyName();
		if (treatPropertyAsField(propertyRef.getReference())) {
			ReferenceLocation srcLocation = getOrCreateLocation(srcRef);
			writeMember(propertyRef, srcLocation);
		} else {
			// map parameter of the property setter to the source
			copy(new DistinctPropertyParameterReference(languageOptions, property), srcRef);
		}
	}

	private boolean treatPropertyAsField(IPropertyReference propertyRef) {
		// treat properties of other classes and auto-implemented properties as field accesses
		IPropertyDeclaration propertyDecl = declarationMapper.get(propertyRef.getPropertyName());
		return propertyDecl == null || languageOptions.isAutoImplementedProperty(propertyDecl);
	}

	public void readArray(IVariableReference destRef, IIndexAccessReference srcRef) {
		DistinctReference dest = destRef.accept(distinctReferenceCreationVisitor, namesToReferences);
		DistinctIndexAccessReference src = (DistinctIndexAccessReference) srcRef
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		readArray(dest, src);
	}

	private void readArray(DistinctReference destRef, DistinctIndexAccessReference srcRef) {
		ReferenceLocation destLocation = getOrCreateLocation(destRef);
		readArray(destLocation, srcRef);
	}

	private void readArray(ReferenceLocation destLocation, DistinctIndexAccessReference srcRef) {
		ReferenceLocation srcLocation = getOrCreateLocation(srcRef.getBaseReference());
		readDereference(destLocation, srcLocation);
	}

	public void writeArray(IIndexAccessReference destRef, IVariableReference srcRef) {
		DistinctIndexAccessReference dest = (DistinctIndexAccessReference) destRef
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		DistinctReference src = srcRef.accept(distinctReferenceCreationVisitor, namesToReferences);
		writeArray(dest, src);
	}

	private void writeArray(DistinctIndexAccessReference destRef, DistinctReference srcRef) {
		ReferenceLocation srcLocation = getOrCreateLocation(srcRef);
		writeArray(destRef, srcLocation);
	}

	private void writeArray(DistinctIndexAccessReference destRef, ReferenceLocation srcLocation) {
		ReferenceLocation destLocation = getOrCreateLocation(destRef.getBaseReference());
		writeDereference(destLocation, srcLocation);
	}

	public void setCurrentMember(MemberName member) {
		this.currentMember = member;
	}

	public void registerParameterReference(DistinctReference formalParameter, IVariableReference actualParameter) {
		copy(formalParameter, actualParameter.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	public void registerParameterReference(DistinctReference formalParameter, IFieldReference actualParameter) {
		readField(formalParameter, actualParameter);
	}

	public void registerParameterReference(DistinctReference formalParameter, IPropertyReference actualParameter) {
		readProperty(formalParameter, actualParameter);
	}

	public void registerReturnReference(IReference ref) {
		if (ref instanceof IUnknownReference) {
			LOGGER.error("Ignoring returning of an unknown reference");
		} else {
			returnedReferences.put(currentMember, ref.accept(distinctReferenceCreationVisitor, namesToReferences));
		}
	}

	public void requestReturnReference(IInvocationExpression invocation) {
		MethodName method = invocation.getMethodName();
		if (lastAssignment == null || lastAssignment.getExpression() != invocation) {
			// returned object ignored
			return;
		}

		IAssignableReference targetRef = lastAssignment.getReference();
		if (targetRef instanceof IUnknownReference) {
			LOGGER.error("Ignoring unknown reference");
			return;
		}

		DistinctReference distRef = targetRef.accept(distinctReferenceCreationVisitor, namesToReferences);
		requestedReturnReferences.put(method, distRef);
	}

	private class DistinctAssignmentHandler extends AssignmentHandler<DistinctReference> {

		@Override
		protected IReference getReference(DistinctReference entry) {
			return entry.getReference();
		}

		@Override
		protected void assignVarToVar(DistinctReference dest, DistinctReference src) {
			copy(dest, src);
		}

		@Override
		protected void assignFieldToVar(DistinctReference dest, DistinctReference src) {
			readField(dest, (DistinctFieldReference) src);
		}

		@Override
		protected void assignPropToVar(DistinctReference dest, DistinctReference src) {
			readProperty(dest, (DistinctPropertyReference) src);
		}

		@Override
		protected void assignVarToField(DistinctReference dest, DistinctReference src) {
			writeField((DistinctFieldReference) dest, src);
		}

		@Override
		protected void assignFieldToField(DistinctReference dest, DistinctReference src) {
			assign((DistinctFieldReference) dest, (DistinctFieldReference) src);
		}

		@Override
		protected void assignPropToField(DistinctReference dest, DistinctReference src) {
			assign((DistinctFieldReference) dest, (DistinctPropertyReference) src);
		}

		@Override
		protected void assignVarToProp(DistinctReference dest, DistinctReference src) {
			writeProperty((DistinctPropertyReference) dest, src);
		}

		@Override
		protected void assignFieldToProp(DistinctReference dest, DistinctReference src) {
			assign((DistinctPropertyReference) dest, (DistinctFieldReference) src);
		}

		@Override
		protected void assignPropToProp(DistinctReference dest, DistinctReference src) {
			assign((DistinctPropertyReference) dest, (DistinctPropertyReference) src);
		}

		@Override
		protected void assignArrayToVar(DistinctReference dest, DistinctReference src) {
			readArray(dest, (DistinctIndexAccessReference) src);
		}

		@Override
		protected void assignArrayToField(DistinctReference dest, DistinctReference src) {
			assign((DistinctFieldReference) dest, (DistinctIndexAccessReference) src);
		}

		@Override
		protected void assignArrayToProp(DistinctReference dest, DistinctReference src) {
			assign((DistinctPropertyReference) dest, (DistinctIndexAccessReference) src);
		}

		@Override
		protected void assignVarToArray(DistinctReference dest, DistinctReference src) {
			writeArray((DistinctIndexAccessReference) dest, src);
		}

		@Override
		protected void assignFieldToArray(DistinctReference dest, DistinctReference src) {
			assign((DistinctIndexAccessReference) dest, (DistinctFieldReference) src);
		}

		@Override
		protected void assignPropToArray(DistinctReference dest, DistinctReference src) {
			assign((DistinctIndexAccessReference) dest, (DistinctPropertyReference) src);
		}

		@Override
		protected void assignArrayToArray(DistinctReference dest, DistinctReference src) {
			assign((DistinctIndexAccessReference) dest, (DistinctIndexAccessReference) src);
		}

	}

}
