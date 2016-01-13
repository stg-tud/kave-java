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

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;

public class ExpressionVisitor {

	private final UniqueVariableNameGenerator nameGen;


	public ISimpleExpression createSimpleExpression(ASTNode expression,
			List<IStatement> body) {
		
		switch (expression.getNodeType()) {
		case ASTNode.BOOLEAN_LITERAL:
			ConstantValueExpression bool = new ConstantValueExpression();
			bool.setValue(Boolean.toString(((BooleanLiteral) expression)
					.booleanValue()));
			return bool;
		case ASTNode.NUMBER_LITERAL:
			ConstantValueExpression number = new ConstantValueExpression();
			number.setValue(((NumberLiteral) expression).getToken());
			return number;
		case ASTNode.NULL_LITERAL:
			return new NullExpression();
		case ASTNode.STRING_LITERAL:
			ConstantValueExpression string = new ConstantValueExpression();
			string.setValue(((StringLiteral) expression).getLiteralValue());
			return string;
		default:

			return null;
		}
	}
	
	public IVariableReference createVariableReference(ASTNode expression,
			List<IStatement> body) {
		VariableReference ref = new VariableReference();
		
		return null;
	}

	public ExpressionVisitor(UniqueVariableNameGenerator nameGen) {
		this.nameGen = nameGen;
	}

}
