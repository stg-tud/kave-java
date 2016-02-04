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
package cc.kave.commons.pointsto.analysis.visitors;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.pointsto.LanguageOptions;
import cc.kave.commons.pointsto.ScopedMap;
import cc.kave.commons.pointsto.analysis.reference.DistinctCatchBlockParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctKeywordReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctLambdaParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctPropertyParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctVariableReference;

public abstract class DistinctReferenceVisitorContext implements ScopingVisitorContext {

	private LanguageOptions languageOptions = LanguageOptions.getInstance();

	protected ScopedMap<String, DistinctReference> namesToReferences = new ScopedMap<>();

	public DistinctReferenceVisitorContext(Context context) {
		namesToReferences.enter();
		createImplicitReferences(context);
	}

	private void createImplicitReferences(Context context) {
		ITypeHierarchy typeHierarchy = context.getTypeShape().getTypeHierarchy();
		DistinctReference thisRef = new DistinctKeywordReference(languageOptions.getThisName(),
				typeHierarchy.getElement());
		namesToReferences.create(languageOptions.getThisName(), thisRef);
		DistinctReference superRef = new DistinctKeywordReference(languageOptions.getSuperName(),
				languageOptions.getSuperType(typeHierarchy));
		namesToReferences.create(languageOptions.getSuperName(), superRef);
	}

	@Override
	public void enterScope() {
		namesToReferences.enter();
	}

	@Override
	public void leaveScope() {
		namesToReferences.leave();
	}

	@Override
	public void declareParameter(IParameterName parameter, IMethodName method) {
		namesToReferences.create(parameter.getName(), new DistinctMethodParameterReference(parameter, method));
	}

	@Override
	public void declareParameter(IParameterName parameter, ILambdaExpression lambdaExpr) {
		namesToReferences.create(parameter.getName(), new DistinctLambdaParameterReference(parameter, lambdaExpr));
	}

	@Override
	public void declareParameter(IParameterName parameter, ICatchBlock catchBlock) {
		namesToReferences.create(parameter.getName(), new DistinctCatchBlockParameterReference(catchBlock));
	}

	@Override
	public void declarePropertySetParameter(IPropertyDeclaration propertyDecl) {
		namesToReferences.create(languageOptions.getPropertyParameterName(),
				new DistinctPropertyParameterReference(languageOptions, propertyDecl.getName()));
	}

	@Override
	public void declareVariable(IVariableDeclaration varDecl) {
		namesToReferences.createOrUpdate(varDecl.getReference().getIdentifier(),
				new DistinctVariableReference(varDecl));
	}

}
