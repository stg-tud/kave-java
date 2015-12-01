/**
 * Copyright 2015 Waldemar Graf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.kave.eclipse.commons.analysis.transformer;

import org.eclipse.jdt.core.dom.ASTVisitor;

import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.eclipse.commons.analysis.completiontarget.CompletionTargetMarker;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;

public class BodyVisitor extends ASTVisitor {

	private CompletionTargetMarker marker;
	private ExpressionVisitor exprVisitor;
	private UniqueVariableNameGenerator nameGen;

	public static ExpressionStatement getEmptyCompletionExpression() {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(new CompletionExpression());
		return expressionStatement;
	}

	public BodyVisitor(UniqueVariableNameGenerator nameGen, CompletionTargetMarker marker) {
		this.marker = marker;
		this.nameGen = nameGen;
		exprVisitor = new ExpressionVisitor(nameGen);
	}
}
