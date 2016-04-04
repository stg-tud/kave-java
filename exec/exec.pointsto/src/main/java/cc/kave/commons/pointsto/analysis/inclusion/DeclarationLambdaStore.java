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
package cc.kave.commons.pointsto.analysis.inclusion;

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import cc.kave.commons.model.names.IMemberName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedNameException;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.UndefinedMemberAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.ContextAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.InclusionAnnotation;
import cc.kave.commons.pointsto.analysis.references.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctPropertyParameterReference;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.extraction.DeclarationMapper;

public final class DeclarationLambdaStore {

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();

	private final Function<DistinctReference, SetVariable> variableResolver;
	private final SetVariableFactory variableFactory;

	private final ConstraintResolver constraintResolver;
	private final DeclarationMapper declMapper;

	private final Map<IMemberName, LambdaTerm> declarationLambdas = new HashMap<>();

	public DeclarationLambdaStore(Function<DistinctReference, SetVariable> variableResolver,
			SetVariableFactory variableFactory, ConstraintResolver constraintResolver, DeclarationMapper declMapper) {
		this.variableResolver = variableResolver;
		this.variableFactory = variableFactory;
		this.constraintResolver = constraintResolver;
		this.declMapper = declMapper;
	}

	public DeclarationLambdaStore(DeclarationLambdaStore other,
			Function<DistinctReference, SetVariable> variableProvider, ConstraintResolver constraintResolver) {
		this(variableProvider, other.variableFactory, constraintResolver, other.declMapper);
		declarationLambdas.putAll(other.declarationLambdas);
	}

	public SetVariableFactory getVariableFactory() {
		return variableFactory;
	}

	public LambdaTerm getDeclarationLambda(IMemberName member) {
		LambdaTerm lambda = declarationLambdas.get(member);
		if (lambda == null) {
			if (member instanceof IMethodName) {
				lambda = createDeclarationLambda((IMethodName) member);
			} else if (member instanceof IPropertyName) {
				lambda = createDeclarationLambda((IPropertyName) member);
			} else {
				throw new UnexpectedNameException(member);
			}
			declarationLambdas.put(member, lambda);
		}

		return lambda;
	}

	private LambdaTerm createDeclarationLambda(IMethodName method) {
		List<IParameterName> formalParameters = method.getParameters();
		List<SetVariable> variables = new ArrayList<>(formalParameters.size() + 2);

		if (!method.isExtensionMethod()) {
			if (method.isStatic()) {
				variables.add(ConstructedTerm.BOTTOM);
			} else {
				variables.add(variableResolver.apply(new DistinctMethodParameterReference(
						parameter(languageOptions.getThisName(), method.getDeclaringType()), method)));
			}
		}

		for (IParameterName parameter : formalParameters) {
			DistinctReference parameterDistRef = new DistinctMethodParameterReference(parameter, method);
			variables.add(variableResolver.apply(parameterDistRef));
		}

		ITypeName returnType = method.getReturnType();
		if (!returnType.isVoidType()) {
			SetVariable returnVar = variableFactory.create();
			// methods without a definition require an object to return
			if (declMapper.get(method) == null) {
				allocateReturnObject(method, returnVar, returnType);
			}
			variables.add(returnVar);
		}

		return LambdaTerm.newMethodLambda(variables, formalParameters, returnType);
	}

	private LambdaTerm createDeclarationLambda(IPropertyName property) {
		SetVariable thisVar = variableResolver.apply(new DistinctPropertyParameterReference(
				languageOptions.getThisName(), property.getDeclaringType(), property));
		SetVariable setParameterVar = variableResolver
				.apply(new DistinctPropertyParameterReference(languageOptions, property));
		SetVariable returnVar = variableFactory.create();
		// properties without a definition require an object to return
		if (declMapper.get(property) == null) {
			allocateReturnObject(property, returnVar, property.getValueType());
		}

		List<SetVariable> variables = Arrays.asList(thisVar, setParameterVar, returnVar);
		return LambdaTerm.newPropertyLambda(variables);
	}

	private void allocateReturnObject(IMemberName member, SetVariable returnVar, ITypeName type) {
		RefTerm returnObject = new RefTerm(new UndefinedMemberAllocationSite(member, type), variableFactory.create());
		constraintResolver.addConstraint(returnObject, returnVar, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);
	}
}
