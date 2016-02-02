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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.pointsto.analysis.Callpath;
import cc.kave.commons.pointsto.analysis.QueryContextKey;
import cc.kave.commons.pointsto.analysis.ReferenceNormalizationVisitor;
import cc.kave.commons.pointsto.analysis.reference.DistinctCatchBlockParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctFieldReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctIndexAccessReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctKeywordReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctLambdaParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctPropertyParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctPropertyReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReferenceVisitor;
import cc.kave.commons.pointsto.analysis.reference.DistinctVariableReference;
import cc.recommenders.assertions.Asserts;

public class QueryKeyTransformer
		implements DistinctReferenceVisitor<List<QueryContextKey>, DistinctReferenceContextCollector> {

	private boolean enableStmtsForVariables = false;

	private ReferenceNormalizationVisitor normalizationVisitor = new ReferenceNormalizationVisitor();

	public QueryKeyTransformer(boolean enableStmtsForVariables) {
		this.enableStmtsForVariables = enableStmtsForVariables;
	}

	private Callpath normalizeMethod(IMethodName method) {
		// TODO replace with isUnknown once it is overridden
		if (method == null || method.equals(MethodName.UNKNOWN_NAME)) {
			return null;
		} else {
			return new Callpath(method);
		}
	}

	private ITypeName normalizeType(ITypeName type) {
		if (type == null || type.isUnknownType() || type.isTypeParameter()) {
			return null;
		} else {
			return type;
		}
	}

	@Override
	public List<QueryContextKey> visit(DistinctKeywordReference keywordRef, DistinctReferenceContextCollector context) {
		return Arrays.asList(
				new QueryContextKey(keywordRef.getReference(), null, normalizeType(keywordRef.getType()), null));
	}

	@Override
	public List<QueryContextKey> visit(DistinctFieldReference fieldRef, DistinctReferenceContextCollector context) {
		return Arrays.asList(new QueryContextKey(fieldRef.getReference().accept(normalizationVisitor, null), null,
				normalizeType(fieldRef.getType()), null));
	}

	@Override
	public List<QueryContextKey> visit(DistinctVariableReference varRef, DistinctReferenceContextCollector context) {
		Collection<IMethodName> methods = context.getMethods(varRef);
		// if a declared variable is not used in a method, there will be no associated methods or statements
		Asserts.assertLessOrEqual(methods.size(), 1);
		Callpath methodPath = methods.isEmpty() ? null : normalizeMethod(methods.iterator().next());
		ITypeName type = normalizeType(varRef.getType());
		IReference reference = varRef.getReference().accept(normalizationVisitor, null);

		if (enableStmtsForVariables) {
			Collection<IStatement> statements = context.getStatements(varRef);
			List<QueryContextKey> queryKeys = new ArrayList<>(statements.size());

			for (IStatement stmt : statements) {
				queryKeys.add(new QueryContextKey(reference, stmt, type, methodPath));
			}

			return queryKeys;
		}

		return Arrays.asList(new QueryContextKey(reference, null, type, methodPath));
	}

	@Override
	public List<QueryContextKey> visit(DistinctPropertyReference propertyRef,
			DistinctReferenceContextCollector context) {
		return Arrays.asList(new QueryContextKey(propertyRef.getReference().accept(normalizationVisitor, null), null,
				normalizeType(propertyRef.getType()), null));
	}

	@Override
	public List<QueryContextKey> visit(DistinctPropertyParameterReference propertyParameterRef,
			DistinctReferenceContextCollector context) {
		Collection<IStatement> statements = context.getStatements(propertyParameterRef);
		ITypeName type = normalizeType(propertyParameterRef.getType());
		IReference reference = propertyParameterRef.getReference().accept(normalizationVisitor, null);
		List<QueryContextKey> queryKeys = new ArrayList<>(statements.size());

		for (IStatement stmt : statements) {
			queryKeys.add(new QueryContextKey(reference, stmt, type, null));
		}

		return queryKeys;
	}

	@Override
	public List<QueryContextKey> visit(DistinctCatchBlockParameterReference catchBlockParameterRef,
			DistinctReferenceContextCollector context) {
		Collection<IStatement> statements = context.getStatements(catchBlockParameterRef);
		Collection<IMethodName> methods = context.getMethods(catchBlockParameterRef);
		Asserts.assertEquals(1, methods.size());
		Callpath methodPath = normalizeMethod(methods.iterator().next());
		ITypeName type = normalizeType(catchBlockParameterRef.getType());
		IReference reference = catchBlockParameterRef.getReference().accept(normalizationVisitor, null);
		List<QueryContextKey> queryKeys = new ArrayList<>(statements.size());

		for (IStatement stmt : statements) {
			queryKeys.add(new QueryContextKey(reference, stmt, type, methodPath));
		}

		return queryKeys;
	}

	@Override
	public List<QueryContextKey> visit(DistinctLambdaParameterReference lambdaParameterRef,
			DistinctReferenceContextCollector context) {
		Collection<IStatement> statements = context.getStatements(lambdaParameterRef);
		Collection<IMethodName> methods = context.getMethods(lambdaParameterRef);
		// the user is free to write lambdas which do not use a parameter
		Asserts.assertLessOrEqual(methods.size(), 1);
		Callpath methodPath = methods.isEmpty() ? null : normalizeMethod(methods.iterator().next());
		ITypeName type = normalizeType(lambdaParameterRef.getType());
		IReference reference = lambdaParameterRef.getReference().accept(normalizationVisitor, null);
		List<QueryContextKey> queryKeys = new ArrayList<>(statements.size());

		for (IStatement stmt : statements) {
			queryKeys.add(new QueryContextKey(reference, stmt, type, methodPath));
		}

		return queryKeys;
	}

	@Override
	public List<QueryContextKey> visit(DistinctMethodParameterReference methodParameterRef,
			DistinctReferenceContextCollector context) {
		Callpath methodPath = normalizeMethod(methodParameterRef.getMethod());
		ITypeName type = normalizeType(methodParameterRef.getType());
		IReference reference = methodParameterRef.getReference().accept(normalizationVisitor, null);
		ArrayList<QueryContextKey> queryKeys = new ArrayList<>();

		if (enableStmtsForVariables) {
			Collection<IStatement> statements = context.getStatements(methodParameterRef);
			queryKeys.ensureCapacity(statements.size());

			for (IStatement stmt : statements) {
				queryKeys.add(new QueryContextKey(reference, stmt, type, methodPath));
			}
		}

		// parameters are available regardless of statements that use them so that they can be queried by only looking
		// at the declaring method
		queryKeys.add(new QueryContextKey(reference, null, type, methodPath));

		return queryKeys;
	}

	@Override
	public List<QueryContextKey> visit(DistinctIndexAccessReference indexAccessRef,
			DistinctReferenceContextCollector context) {
		return indexAccessRef.getBaseReference().accept(this, context);
	}

}
