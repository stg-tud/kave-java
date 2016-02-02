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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.alg.util.UnionFind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMemberName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.pointsto.LanguageOptions;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.DistinctReferenceVisitorContext;
import cc.kave.commons.pointsto.analysis.FailSafeNodeVisitor;
import cc.kave.commons.pointsto.analysis.reference.DistinctFieldReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctIndexAccessReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctLambdaParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctMemberReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctPropertyParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctPropertyReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReferenceCreationVisitor;
import cc.kave.commons.pointsto.analysis.unification.identifiers.LocationIdentifier;
import cc.kave.commons.pointsto.analysis.unification.identifiers.LocationIdentifierFactory;
import cc.kave.commons.pointsto.analysis.unification.locations.BottomLocation;
import cc.kave.commons.pointsto.analysis.unification.locations.ExtendedReferenceLocation;
import cc.kave.commons.pointsto.analysis.unification.locations.FunctionLocation;
import cc.kave.commons.pointsto.analysis.unification.locations.Location;
import cc.kave.commons.pointsto.analysis.unification.locations.ReferenceLocation;
import cc.kave.commons.pointsto.analysis.unification.locations.SimpleReferenceLocation;
import cc.kave.commons.pointsto.extraction.DeclarationMapper;
import cc.recommenders.assertions.Asserts;

public class UnificationAnalysisVisitorContext extends DistinctReferenceVisitorContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnificationAnalysisVisitorContext.class);

	private LanguageOptions languageOptions = LanguageOptions.getInstance();

	private DistinctReferenceCreationVisitor distinctReferenceCreationVisitor = new DistinctReferenceCreationVisitor();

	private Multimap<SetRepresentative, SetRepresentative> pending = HashMultimap.create();
	private UnionFind<SetRepresentative> unionFind = new UnionFind<>(new HashSet<>());

	private Map<DistinctReference, ReferenceLocation> referenceLocations = new HashMap<>();
	private Map<DistinctMemberReference, SetRepresentative> memberLocations = new HashMap<>();

	private IAssignment lastAssignment;
	private IMemberName currentMember;
	private Deque<Pair<ILambdaExpression, ReferenceLocation>> lambdaStack = new ArrayDeque<>();

	private DeclarationMapper declarationMapper;

	private Map<IMemberName, ReferenceLocation> returnLocations = new HashMap<>();

	private DestinationLocationVisitor destLocationVisitor = new DestinationLocationVisitor();
	private SourceLocationVisitor srcLocationVisitor = new SourceLocationVisitor();

	private LocationIdentifierFactory identifierFactory;

	private Set<Pair<ReferenceLocation, ReferenceLocation>> joinLocationsOf = new HashSet<>();

	public UnificationAnalysisVisitorContext(Context context, LocationIdentifierFactory identifierFactory) {
		super(context);
		declarationMapper = new DeclarationMapper(context);
		this.identifierFactory = identifierFactory;

		createImplicitLocations(context);
	}

	public Map<DistinctReference, AbstractLocation> getReferenceLocations() {
		int totalReferences = referenceLocations.size() + memberLocations.size();
		Map<DistinctReference, AbstractLocation> result = new HashMap<>(totalReferences);
		Map<SetRepresentative, AbstractLocation> refToAbstractLocation = new HashMap<>(totalReferences);

		for (Map.Entry<DistinctReference, ReferenceLocation> entry : referenceLocations.entrySet()) {
			DistinctReference reference = entry.getKey();
			ReferenceLocation location = entry.getValue();

			SetRepresentative ecr;
			if (reference instanceof DistinctMemberReference) {
				ecr = unionFind.find(location.getSetRepresentative());
			} else {
				Location valueLocation = unionFind
						.find(location.getLocation(LocationIdentifier.VALUE).getSetRepresentative()).getLocation();
				Location funLocation = location.getLocation(LocationIdentifier.FUNCTION);
				if (funLocation != null) {
					funLocation = unionFind.find(funLocation.getSetRepresentative()).getLocation();
				}
				ecr = (valueLocation.isBottom() && funLocation != null && !funLocation.isBottom())
						? funLocation.getSetRepresentative() : valueLocation.getSetRepresentative();
			}

			AbstractLocation abstractLocation = refToAbstractLocation.get(ecr);
			if (abstractLocation == null) {
				abstractLocation = new AbstractLocation();
				refToAbstractLocation.put(ecr, abstractLocation);
			}

			result.put(reference, abstractLocation);
		}

		for (Map.Entry<DistinctMemberReference, SetRepresentative> entry : memberLocations.entrySet()) {
			DistinctReference reference = entry.getKey();
			SetRepresentative ecr = unionFind.find(entry.getValue());

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
		finalizePendingJoins();
	}

	private void finalizePendingJoins() {
		while (!pending.isEmpty()) {
			Map.Entry<SetRepresentative, SetRepresentative> entry = pending.entries().iterator().next();
			join(entry.getKey(), entry.getValue());
			pending.remove(entry.getKey(), entry.getValue());
		}
	}

	private LocationIdentifier getIdentifierForSimpleRefLoc(ITypeName type) {
		if (type.isDelegateType()) {
			return LocationIdentifier.FUNCTION;
		} else {
			return LocationIdentifier.VALUE;
		}
	}

	private BottomLocation createBottomLocation() {
		SetRepresentative bottomRep = new SetRepresentative();
		unionFind.addElement(bottomRep);
		return new BottomLocation(bottomRep);
	}

	private ReferenceLocation createSimpleReferenceLocation() {
		SetRepresentative valueBottomRep = new SetRepresentative();
		SetRepresentative funBottomRep = new SetRepresentative();

		SetRepresentative refRepresentative = new SetRepresentative();
		ReferenceLocation refLocation = new SimpleReferenceLocation(refRepresentative,
				new BottomLocation(valueBottomRep), new BottomLocation(funBottomRep));

		unionFind.addElement(valueBottomRep);
		unionFind.addElement(funBottomRep);
		unionFind.addElement(refRepresentative);
		return refLocation;
	}

	private ReferenceLocation createExtendedReferenceLocation() {
		SetRepresentative setRep = new SetRepresentative();
		unionFind.addElement(setRep);
		return new ExtendedReferenceLocation(setRep);
	}

	private Set<LocationIdentifier> getCommonIdentifiers(ReferenceLocation refLoc1, ReferenceLocation refLoc2) {
		Set<LocationIdentifier> identifiers = new HashSet<>(refLoc1.getIdentifiers());
		identifiers.retainAll(refLoc2.getIdentifiers());
		return identifiers;
	}

	private Set<LocationIdentifier> harmonizeIdentifiers(ReferenceLocation refLoc1, ReferenceLocation refLoc2) {
		Set<LocationIdentifier> loc1Identifiers = refLoc1.getIdentifiers();
		Set<LocationIdentifier> loc2Identifiers = refLoc2.getIdentifiers();

		{
			Set<LocationIdentifier> missingInLoc1 = new HashSet<>(loc2Identifiers);
			missingInLoc1.removeAll(loc1Identifiers);
			for (LocationIdentifier identifier : missingInLoc1) {
				refLoc1.setLocation(identifier, createBottomLocation());
			}
		}

		{
			Set<LocationIdentifier> missingInLoc2 = new HashSet<>(loc1Identifiers);
			missingInLoc2.removeAll(loc2Identifiers);
			for (LocationIdentifier identifier : missingInLoc2) {
				refLoc2.setLocation(identifier, createBottomLocation());
			}
		}

		return loc1Identifiers;
	}

	private FunctionLocation createFunctionLocation(ILambdaExpression lambdaExpr) {
		List<IParameterName> lambdaParameters = lambdaExpr.getName().getParameters();
		List<ReferenceLocation> parameterLocations = new ArrayList<>(lambdaParameters.size());

		for (IParameterName formalParameter : lambdaParameters) {
			DistinctReference distRef = new DistinctLambdaParameterReference(formalParameter, lambdaExpr);
			ReferenceLocation formalParameterLocation = getOrCreateLocation(distRef);

			parameterLocations.add(formalParameterLocation);
		}

		Pair<ILambdaExpression, ReferenceLocation> currentLambdaEntry = lambdaStack.getFirst();
		Asserts.assertTrue(lambdaExpr == currentLambdaEntry.getKey());

		SetRepresentative functionRep = new SetRepresentative();
		unionFind.addElement(functionRep);
		return new FunctionLocation(parameterLocations, currentLambdaEntry.getValue(), functionRep);
	}

	private FunctionLocation createFunctionLocation(IMethodName method) {
		SetRepresentative functionRep = new SetRepresentative();
		unionFind.addElement(functionRep);
		return createFunctionLocation(method, functionRep);
	}

	private FunctionLocation createFunctionLocation(IMethodName method, SetRepresentative functionRep) {
		List<IParameterName> methodParameters = method.getParameters();
		List<ReferenceLocation> parameterLocations = new ArrayList<>(methodParameters.size());

		for (IParameterName formalParameter : methodParameters) {
			DistinctMethodParameterReference distRef = new DistinctMethodParameterReference(formalParameter, method);
			ReferenceLocation formalParameterLocation = getOrCreateLocation(distRef);

			parameterLocations.add(formalParameterLocation);
		}

		return new FunctionLocation(parameterLocations, getOrCreateReturnLocation(method), functionRep);
	}

	private ReferenceLocation getOrCreateLocation(DistinctReference reference) {
		ReferenceLocation location = referenceLocations.get(reference);

		if (location == null) {
			location = createSimpleReferenceLocation();
			referenceLocations.put(reference, location);
		}

		return location;
	}

	private ReferenceLocation getOrCreateReturnLocation(IMemberName member) {
		ReferenceLocation location = returnLocations.get(member);

		if (location == null) {
			location = createSimpleReferenceLocation();
			returnLocations.put(member, location);
		}

		return location;
	}

	private void registerMemberLocation(DistinctMemberReference memberRef, Location location) {
		if (!memberLocations.containsKey(memberRef)) {
			memberLocations.put(memberRef, location.getSetRepresentative());
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
		rep1 = unionFind.find(rep1);
		rep2 = unionFind.find(rep2);
		if (rep1 == rep2) {
			// prevent indefinite loop when finalizing the set of pending joins
			return;
		}

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
				unify(loc1, loc2);
			}

		}
	}

	private void unify(Location loc1, Location loc2) {
		if (loc1 instanceof ReferenceLocation && loc2 instanceof ReferenceLocation) {
			unify((ReferenceLocation) loc1, (ReferenceLocation) loc2);
		} else if (loc1 instanceof FunctionLocation && loc2 instanceof FunctionLocation) {
			unify((FunctionLocation) loc1, (FunctionLocation) loc2);
		}
	}

	private void unify(ReferenceLocation refLoc1, ReferenceLocation refLoc2) {
		Set<LocationIdentifier> commonIdentifiers = harmonizeIdentifiers(refLoc1, refLoc2);

		for (LocationIdentifier identifier : commonIdentifiers) {
			Location loc1 = refLoc1.getLocation(identifier);
			Location loc2 = refLoc2.getLocation(identifier);

			SetRepresentative rep1 = unionFind.find(loc1.getSetRepresentative());
			SetRepresentative rep2 = unionFind.find(loc2.getSetRepresentative());

			if (rep1 != rep2) {
				join(rep1, rep2);
			}
		}
	}

	private void unify(FunctionLocation funLoc1, FunctionLocation funLoc2) {
		List<ReferenceLocation> fun1Parameters = funLoc1.getParameterLocations();
		List<ReferenceLocation> fun2Parameters = funLoc2.getParameterLocations();

		int minParametersSize = Math.min(fun1Parameters.size(), fun2Parameters.size());
		int maxParametersSize = Math.max(fun1Parameters.size(), fun2Parameters.size());
		for (int i = 0; i < minParametersSize; ++i) {
			unify(fun1Parameters.get(i), fun2Parameters.get(i));
		}

		// function locations have a different number of parameters: copy over the additional ones
		if (minParametersSize != maxParametersSize) {
			List<ReferenceLocation> minParameters = (fun1Parameters.size() == minParametersSize) ? fun1Parameters
					: fun2Parameters;
			List<ReferenceLocation> maxParameters = (fun1Parameters.size() == maxParametersSize) ? fun1Parameters
					: fun2Parameters;

			minParameters.addAll(maxParameters.subList(minParametersSize, maxParametersSize));
		}

		unify(funLoc1.getReturnLocation(), funLoc2.getReturnLocation());
	}

	public ReferenceLocation getLocation(IVariableReference varRef) {
		DistinctReference distRef = varRef.accept(distinctReferenceCreationVisitor, namesToReferences);
		return getOrCreateLocation(distRef);
	}

	public void setCurrentMember(IMemberName member) {
		this.currentMember = member;
	}

	public void setLastAssignment(IAssignment assignment) {
		lastAssignment = assignment;
	}

	public IAssignableReference getDestinationForExpr(IAssignableExpression expr) {
		if (lastAssignment == null || lastAssignment.getReference() instanceof IUnknownReference
				|| lastAssignment.getExpression() != expr) {
			return null;
		} else {
			return lastAssignment.getReference();
		}
	}

	public void enterLambda(ILambdaExpression lambdaExpr) {
		ReferenceLocation lambdaReturnLocation = createSimpleReferenceLocation();
		lambdaStack.addFirst(ImmutablePair.of(lambdaExpr, lambdaReturnLocation));
	}

	public void leaveLambda() {
		lambdaStack.removeFirst();
	}

	public void allocate(IAssignableReference destRef) {
		if (destRef instanceof IUnknownReference) {
			LOGGER.error("Ignoring an allocation due to an unknown reference");
			return;
		}

		ReferenceLocation destLocation = destRef.accept(destLocationVisitor, null);
		allocate(destLocation);
	}

	private void allocate(DistinctReference destRef) {
		allocate(getOrCreateLocation(destRef));
	}

	private void allocate(ReferenceLocation destLocation) {
		Location derefDestLocation = unionFind
				.find(destLocation.getLocation(LocationIdentifier.VALUE).getSetRepresentative()).getLocation();

		if (derefDestLocation.isBottom()) {
			ReferenceLocation allocatedLocation = createExtendedReferenceLocation();
			setLocation(derefDestLocation.getSetRepresentative(), allocatedLocation);
			destLocation.setLocation(LocationIdentifier.VALUE, allocatedLocation);
		}
	}

	private Location readDereference(ReferenceLocation destLocation, ReferenceLocation srcLocation,
			LocationIdentifier destIdentifier, LocationIdentifier srcIdentifier) {
		SetRepresentative destDerefRep = unionFind
				.find(destLocation.getLocation(destIdentifier).getSetRepresentative());
		SetRepresentative srcDerefRep = unionFind
				.find(srcLocation.getLocation(LocationIdentifier.VALUE).getSetRepresentative());

		if (srcDerefRep.getLocation().isBottom()) {
			ReferenceLocation srcDerefLoc = new ExtendedReferenceLocation(srcDerefRep);
			Location dereferenceTargetLoc = destDerefRep.getLocation();
			if (dereferenceTargetLoc.isBottom()) {
				dereferenceTargetLoc = new ExtendedReferenceLocation(destDerefRep);
				setLocation(destDerefRep, dereferenceTargetLoc);
				destLocation.setLocation(destIdentifier, dereferenceTargetLoc);
			}
			srcDerefLoc.setLocation(srcIdentifier, dereferenceTargetLoc);
			setLocation(srcDerefRep, srcDerefLoc);
			srcLocation.setLocation(LocationIdentifier.VALUE, srcDerefLoc);

			return destDerefRep.getLocation();
		} else {
			ReferenceLocation srcDerefLocation = (ReferenceLocation) srcDerefRep.getLocation();
			Location dereferenceTargetLoc = srcDerefLocation.getLocation(srcIdentifier);
			if (dereferenceTargetLoc == null) {
				dereferenceTargetLoc = createExtendedReferenceLocation();
				srcDerefLocation.setLocation(srcIdentifier, dereferenceTargetLoc);
			}

			SetRepresentative dereferenceTargetRep = unionFind.find(dereferenceTargetLoc.getSetRepresentative());
			if (destDerefRep != dereferenceTargetRep) {
				cjoin(destDerefRep, dereferenceTargetRep);
			}

			return dereferenceTargetRep.getLocation();
		}
	}

	private Location writeDereference(ReferenceLocation destLocation, ReferenceLocation srcLocation,
			LocationIdentifier destIdentifier, LocationIdentifier srcIdentifier) {
		SetRepresentative destDerefRep = unionFind
				.find(destLocation.getLocation(LocationIdentifier.VALUE).getSetRepresentative());
		SetRepresentative srcDerefRep = unionFind.find(srcLocation.getLocation(srcIdentifier).getSetRepresentative());

		if (destDerefRep.getLocation().isBottom()) {
			ReferenceLocation destDerefLoc = new ExtendedReferenceLocation(destDerefRep);
			Location dereferenceTargetLoc = srcDerefRep.getLocation();
			if (dereferenceTargetLoc.isBottom()) {
				dereferenceTargetLoc = new ExtendedReferenceLocation(srcDerefRep);
				setLocation(srcDerefRep, dereferenceTargetLoc);
				srcLocation.setLocation(srcIdentifier, dereferenceTargetLoc);
			}
			destDerefLoc.setLocation(destIdentifier, srcDerefRep.getLocation());
			setLocation(destDerefRep, destDerefLoc);
			destLocation.setLocation(LocationIdentifier.VALUE, destDerefLoc);

			return srcDerefRep.getLocation();
		} else {
			ReferenceLocation destDerefLocation = (ReferenceLocation) destDerefRep.getLocation();
			Location dereferenceTargetLoc = destDerefLocation.getLocation(destIdentifier);
			if (dereferenceTargetLoc == null) {
				dereferenceTargetLoc = createExtendedReferenceLocation();
				destDerefLocation.setLocation(destIdentifier, dereferenceTargetLoc);
			}

			SetRepresentative dereferenceTargetRep = unionFind.find(dereferenceTargetLoc.getSetRepresentative());

			if (dereferenceTargetRep != srcDerefRep) {
				cjoin(dereferenceTargetRep, srcDerefRep);
			}

			return dereferenceTargetRep.getLocation();
		}
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
		for (LocationIdentifier identifier : harmonizeIdentifiers(destLocation, srcLocation)) {
			copy(destLocation, srcLocation, identifier);
		}
		joinLocationsOf.add(ImmutablePair.of(destLocation, srcLocation));
	}

	private void copy(ReferenceLocation destLocation, ReferenceLocation srcLocation, LocationIdentifier identifier) {
		SetRepresentative derefDestRep = unionFind.find(destLocation.getLocation(identifier).getSetRepresentative());
		SetRepresentative derefSrcRep = unionFind.find(srcLocation.getLocation(identifier).getSetRepresentative());

		if (derefDestRep != derefSrcRep) {
			cjoin(derefDestRep, derefSrcRep);
		}
	}

	public void storeFunction(IReference destRef, ILambdaExpression lambdaExpr) {
		Asserts.assertTrue(lambdaExpr == lambdaStack.getFirst().getKey());

		ReferenceLocation destLocation = destRef.accept(destLocationVisitor, null);
		Location derefDestLocation = unionFind
				.find(destLocation.getLocation(LocationIdentifier.FUNCTION).getSetRepresentative()).getLocation();

		if (derefDestLocation.isBottom()) {
			FunctionLocation functionLocation = createFunctionLocation(lambdaExpr);
			setLocation(derefDestLocation.getSetRepresentative(), functionLocation);
			destLocation.setLocation(LocationIdentifier.FUNCTION, functionLocation);
		} else {
			FunctionLocation functionLocation = (FunctionLocation) derefDestLocation;

			List<IParameterName> lambdaParameters = lambdaExpr.getName().getParameters();
			List<DistinctReference> distLambdaParameters = new ArrayList<>(lambdaParameters.size());
			for (IParameterName lambdaParameter : lambdaParameters) {
				distLambdaParameters.add(new DistinctLambdaParameterReference(lambdaParameter, lambdaExpr));
			}

			ReferenceLocation lambdaReturnLocation = lambdaStack.getFirst().getValue();
			ITypeName lambdaReturnType = lambdaStack.getFirst().getKey().getName().getReturnType();

			updateFunctionLocation(functionLocation, distLambdaParameters, lambdaReturnType, lambdaReturnLocation);
		}

	}

	public void storeFunction(IReference destRef, IMethodReference methodRef) {
		ReferenceLocation destLocation = destRef.accept(destLocationVisitor, null);
		storeFunction(destLocation, methodRef);
	}

	private void storeFunction(ReferenceLocation destLocation, IMethodReference methodRef) {
		IMethodName method = methodRef.getMethodName();

		Location derefDestLocation = unionFind
				.find(destLocation.getLocation(LocationIdentifier.FUNCTION).getSetRepresentative()).getLocation();

		if (derefDestLocation.isBottom()) {
			FunctionLocation functionLocation = createFunctionLocation(method);
			setLocation(derefDestLocation.getSetRepresentative(), functionLocation);
			destLocation.setLocation(LocationIdentifier.FUNCTION, functionLocation);
		} else {
			FunctionLocation functionLocation = (FunctionLocation) derefDestLocation;

			List<IParameterName> methodParameters = method.getParameters();
			List<DistinctReference> distMethodParameters = new ArrayList<>(methodParameters.size());
			for (IParameterName parameter : methodParameters) {
				distMethodParameters.add(new DistinctMethodParameterReference(parameter, method));
			}

			updateFunctionLocation(functionLocation, distMethodParameters, method.getReturnType(),
					getOrCreateReturnLocation(method));
		}
	}

	private void updateFunctionLocation(FunctionLocation functionLocation, List<DistinctReference> newParameters,
			ITypeName newReturnType, ReferenceLocation newReturnLocation) {
		List<ReferenceLocation> funLocParameters = functionLocation.getParameterLocations();
		Asserts.assertEquals(funLocParameters.size(), newParameters.size());

		for (int i = 0; i < newParameters.size(); ++i) {
			DistinctReference distParameterRef = newParameters.get(i);
			ReferenceLocation newParameterLocation = getOrCreateLocation(distParameterRef);
			LocationIdentifier newParameterIdentifier = getIdentifierForSimpleRefLoc(distParameterRef.getType());
			SetRepresentative derefNewParameterLocationRep = unionFind
					.find(newParameterLocation.getLocation(newParameterIdentifier).getSetRepresentative());
			SetRepresentative derefFunLocParameterRep = unionFind
					.find(funLocParameters.get(i).getLocation(newParameterIdentifier).getSetRepresentative());

			if (derefFunLocParameterRep != derefNewParameterLocationRep) {
				join(derefNewParameterLocationRep, derefFunLocParameterRep);
			}
		}

		LocationIdentifier newReturnIdentifier = getIdentifierForSimpleRefLoc(newReturnType);
		SetRepresentative derefNewReturnLocRep = unionFind
				.find(newReturnLocation.getLocation(newReturnIdentifier).getSetRepresentative());
		SetRepresentative derefFunLocReturnRep = unionFind
				.find(functionLocation.getReturnLocation().getLocation(newReturnIdentifier).getSetRepresentative());
		if (derefFunLocReturnRep != derefNewReturnLocRep) {
			join(derefFunLocReturnRep, derefNewReturnLocRep);
		}
	}

	public FunctionLocation invokeDelegate(IInvocationExpression invocation) {
		IMethodName method = invocation.getMethodName();

		DistinctReference receiverRef = invocation.getReference().accept(distinctReferenceCreationVisitor,
				namesToReferences);
		ReferenceLocation receiverLoc = getOrCreateLocation(receiverRef);
		Location derefReceiverLoc = unionFind
				.find(receiverLoc.getLocation(LocationIdentifier.FUNCTION).getSetRepresentative()).getLocation();

		FunctionLocation functionLocation;
		if (derefReceiverLoc.isBottom()) {
			functionLocation = createFunctionLocation(method);
			setLocation(derefReceiverLoc.getSetRepresentative(), functionLocation);
			receiverLoc.setLocation(LocationIdentifier.FUNCTION, functionLocation);
		} else {
			if (derefReceiverLoc instanceof FunctionLocation) {
				functionLocation = (FunctionLocation) derefReceiverLoc;
			} else {
				// location got initialized by readDereference or writeDereference to an ExtendedRefernceLocation as we
				// cannot simply extract the method name at that stage
				functionLocation = createFunctionLocation(method, derefReceiverLoc.getSetRepresentative());
				derefReceiverLoc.getSetRepresentative().setLocation(functionLocation);
				receiverLoc.setLocation(LocationIdentifier.FUNCTION, functionLocation);
			}
		}

		return functionLocation;
	}

	public List<ReferenceLocation> getMethodParameterLocations(IMethodName method) {
		List<IParameterName> formalParameters = method.getParameters();
		List<ReferenceLocation> parameterLocations = new ArrayList<>(formalParameters.size());
		for (IParameterName parameter : formalParameters) {
			DistinctReference distParameterRef = new DistinctMethodParameterReference(parameter, method);
			parameterLocations.add(getOrCreateLocation(distParameterRef));
		}

		return parameterLocations;
	}

	public ReferenceLocation getMethodReturnLocation(IMethodName method) {
		return getOrCreateReturnLocation(method);
	}

	public void assign(IFieldReference dest, IFieldReference src) {
		assign((DistinctFieldReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctFieldReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctFieldReference dest, DistinctFieldReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readMember(tempLoc, src);
		writeMember(dest, tempLoc);
	}

	public void assign(IPropertyReference dest, IPropertyReference src) {
		assign((DistinctPropertyReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctPropertyReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctPropertyReference dest, DistinctPropertyReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		IPropertyName srcProperty = src.getReference().getPropertyName();
		if (treatPropertyAsField(src.getReference())) {
			readMember(tempLoc, src);
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(srcProperty);
			copy(tempLoc, returnLocation);
		}

		IPropertyName destProperty = dest.getReference().getPropertyName();
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
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readMember(tempLoc, src);

		IPropertyName destProperty = dest.getReference().getPropertyName();
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
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		IPropertyName srcProperty = src.getReference().getPropertyName();
		if (treatPropertyAsField(src.getReference())) {
			readMember(tempLoc, src);
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(srcProperty);
			copy(tempLoc, returnLocation);
		}

		writeMember(dest, tempLoc);
	}

	public void assign(IFieldReference dest, IIndexAccessReference src) {
		assign((DistinctFieldReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctIndexAccessReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctFieldReference dest, DistinctIndexAccessReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readArray(tempLoc, src);
		writeMember(dest, tempLoc);
	}

	public void assign(IPropertyReference dest, IIndexAccessReference src) {
		assign((DistinctPropertyReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctIndexAccessReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctPropertyReference dest, DistinctIndexAccessReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readArray(tempLoc, src);

		IPropertyName destProperty = dest.getReference().getPropertyName();
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
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readMember(tempLoc, src);
		writeArray(dest, tempLoc);
	}

	public void assign(IIndexAccessReference dest, IPropertyReference src) {
		assign((DistinctIndexAccessReference) dest.accept(distinctReferenceCreationVisitor, namesToReferences),
				(DistinctPropertyReference) src.accept(distinctReferenceCreationVisitor, namesToReferences));
	}

	private void assign(DistinctIndexAccessReference dest, DistinctPropertyReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();

		IPropertyName srcProperty = src.getReference().getPropertyName();
		if (treatPropertyAsField(src.getReference())) {
			readMember(tempLoc, src);
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(srcProperty);
			copy(tempLoc, returnLocation);
		}

		writeArray(dest, tempLoc);
	}

	public void assign(IIndexAccessReference dest, IIndexAccessReference src) {
		DistinctIndexAccessReference distDestRef = (DistinctIndexAccessReference) dest
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		DistinctIndexAccessReference distSrcRef = (DistinctIndexAccessReference) src
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		assign(distDestRef, distSrcRef);
	}

	private void assign(DistinctIndexAccessReference dest, DistinctIndexAccessReference src) {
		ReferenceLocation tempLoc = createSimpleReferenceLocation();
		readArray(tempLoc, src);
		writeArray(dest, tempLoc);
	}

	public void readField(IVariableReference destRef, IFieldReference fieldRef) {
		readField(destRef.accept(distinctReferenceCreationVisitor, namesToReferences), fieldRef);
	}

	private void readField(DistinctReference destRef, IFieldReference fieldRef) {
		readMember(getOrCreateLocation(destRef), fieldRef);
	}

	private void readMember(ReferenceLocation destLocation, IMemberReference memberRef) {
		DistinctMemberReference distinctMemberRef = (DistinctMemberReference) memberRef
				.accept(distinctReferenceCreationVisitor, namesToReferences);
		readMember(destLocation, distinctMemberRef);
	}

	private void readMember(ReferenceLocation destLocation, DistinctMemberReference memberRef) {
		if (memberRef.isStaticMember()) {
			// model static members (fields, properties) as global variables
			copy(destLocation, getOrCreateLocation(memberRef));
		} else {

			ReferenceLocation srcLocation = getOrCreateLocation(memberRef.getBaseReference());
			LocationIdentifier destIdentifier = getIdentifierForSimpleRefLoc(memberRef.getType());
			LocationIdentifier srcIdentifier = identifierFactory.create(memberRef.getReference());
			Location srcDerefLocation = readDereference(destLocation, srcLocation, destIdentifier, srcIdentifier);

			registerMemberLocation(memberRef, srcDerefLocation);
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
			LocationIdentifier destIdentifier = identifierFactory.create(memberRef.getReference());
			// we live in a type safe environment (src must be a delegate type as well if the member is one)
			LocationIdentifier srcIdentifier = getIdentifierForSimpleRefLoc(memberRef.getType());
			Location destDerefLocation = writeDereference(destLocation, srcLocation, destIdentifier, srcIdentifier);

			registerMemberLocation(memberRef, destDerefLocation);
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
		ReferenceLocation destLocation = getOrCreateLocation(destRef);
		IPropertyName property = propertyRef.getReference().getPropertyName();
		if (treatPropertyAsField(propertyRef.getReference())) {
			readMember(destLocation, propertyRef);
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(property);
			copy(destLocation, returnLocation);
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
		IPropertyName property = propertyRef.getReference().getPropertyName();
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
		LocationIdentifier destIdentifier = getIdentifierForSimpleRefLoc(srcRef.getType());
		LocationIdentifier srcIdentifier = identifierFactory.create(srcRef);
		readDereference(destLocation, srcLocation, destIdentifier, srcIdentifier);
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
		LocationIdentifier destIdentifier = identifierFactory.create(destRef);
		LocationIdentifier srcIdentifier = getIdentifierForSimpleRefLoc(destRef.getType());
		writeDereference(destLocation, srcLocation, destIdentifier, srcIdentifier);
	}

	public void registerParameterReference(ReferenceLocation parameterLocation, IVariableReference actualParameter) {
		copy(parameterLocation, getLocation(actualParameter));
	}

	public void registerParameterReference(ReferenceLocation parameterLocation, IFieldReference actualParameter) {
		readMember(parameterLocation, actualParameter);
	}

	public void registerParameterReference(ReferenceLocation parameterLocation, IPropertyReference actualParameter) {
		readMember(parameterLocation, actualParameter);
	}

	public void registerParameterReference(ReferenceLocation parameterLocation, IMethodReference actualParameter) {
		storeFunction(parameterLocation, actualParameter);
	}

	public void registerReturnReference(IReference ref) {
		if (ref instanceof IUnknownReference) {
			LOGGER.error("Ignoring returning of an unknown reference");
		} else {
			ReferenceLocation returnLocation = getOrCreateReturnLocation(currentMember);
			ReferenceLocation srcLocation = ref.accept(srcLocationVisitor, null);

			copy(returnLocation, srcLocation);
		}
	}

	public void storeReturn(IAssignableReference destRef, ReferenceLocation returnLocation) {
		ReferenceLocation destLocation = destRef.accept(destLocationVisitor, null);
		copy(destLocation, returnLocation);
	}

	private class DestinationLocationVisitor extends FailSafeNodeVisitor<Void, ReferenceLocation> {

		@Override
		public ReferenceLocation visit(IVariableReference varRef, Void context) {
			return getLocation(varRef);
		}

		@Override
		public ReferenceLocation visit(IFieldReference fieldRef, Void context) {
			ReferenceLocation tempLoc = createSimpleReferenceLocation();
			DistinctFieldReference distRef = (DistinctFieldReference) fieldRef.accept(distinctReferenceCreationVisitor,
					namesToReferences);
			writeMember(distRef, tempLoc);
			return tempLoc;
		}

		@Override
		public ReferenceLocation visit(IPropertyReference propertyRef, Void context) {
			ReferenceLocation tempLoc = createSimpleReferenceLocation();

			IPropertyName property = propertyRef.getPropertyName();
			if (treatPropertyAsField(propertyRef)) {
				DistinctPropertyReference distRef = (DistinctPropertyReference) propertyRef
						.accept(distinctReferenceCreationVisitor, namesToReferences);
				writeMember(distRef, tempLoc);
			} else {
				DistinctReference destPropertyParameter = new DistinctPropertyParameterReference(languageOptions,
						property);
				copy(getOrCreateLocation(destPropertyParameter), tempLoc);
			}

			return tempLoc;
		}

		@Override
		public ReferenceLocation visit(IIndexAccessReference indexAccessRef, Void context) {
			ReferenceLocation tempLoc = createSimpleReferenceLocation();
			DistinctIndexAccessReference distRef = (DistinctIndexAccessReference) indexAccessRef
					.accept(distinctReferenceCreationVisitor, namesToReferences);
			writeArray(distRef, tempLoc);
			return tempLoc;
		}

	}

	private class SourceLocationVisitor extends FailSafeNodeVisitor<Void, ReferenceLocation> {

		@Override
		public ReferenceLocation visit(IVariableReference varRef, Void context) {
			return getLocation(varRef);
		}

		@Override
		public ReferenceLocation visit(IFieldReference fieldRef, Void context) {
			ReferenceLocation tempLoc = createSimpleReferenceLocation();
			DistinctFieldReference distRef = (DistinctFieldReference) fieldRef.accept(distinctReferenceCreationVisitor,
					namesToReferences);
			readMember(tempLoc, distRef);
			return tempLoc;
		}

		@Override
		public ReferenceLocation visit(IPropertyReference propertyRef, Void context) {
			ReferenceLocation tempLoc = createSimpleReferenceLocation();

			IPropertyName property = propertyRef.getPropertyName();
			if (treatPropertyAsField(propertyRef)) {
				DistinctPropertyReference distRef = (DistinctPropertyReference) propertyRef
						.accept(distinctReferenceCreationVisitor, namesToReferences);
				readMember(tempLoc, distRef);
			} else {
				ReferenceLocation returnLocation = getOrCreateReturnLocation(property);
				copy(tempLoc, returnLocation);
			}

			return tempLoc;
		}

		@Override
		public ReferenceLocation visit(IIndexAccessReference indexAccessRef, Void context) {
			ReferenceLocation tempLoc = createSimpleReferenceLocation();
			DistinctIndexAccessReference distRef = (DistinctIndexAccessReference) indexAccessRef
					.accept(distinctReferenceCreationVisitor, namesToReferences);
			readArray(tempLoc, distRef);
			return tempLoc;
		}
	}

}
