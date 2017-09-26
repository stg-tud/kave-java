/**
 * Copyright 2017 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.model.ssts.impl.visitor;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.utils.naming.ProjectNormalizationNameRewriter;

public class NameFilteringVisitor extends AbstractThrowingNodeVisitor<Void, ISSTNode> {

	private ProjectNormalizationNameRewriter rw = new ProjectNormalizationNameRewriter();

	@Override
	public ISSTNode visit(ISST sst, Void context) {
		SST sst2 = new SST();
		sst2.setEnclosingType(rw.rewrite(sst.getEnclosingType()));

		for (IMethodDeclaration md : sst.getMethods()) {
			IMethodDeclaration newMd = (IMethodDeclaration) md.accept(this, null);
			sst2.getMethods().add(newMd);
		}

		/// TODO: Fields, properties, etc...

		return sst2;
	}

	@Override
	public ISSTNode visit(IMethodDeclaration md, Void context) {
		MethodDeclaration newMd = new MethodDeclaration();
		newMd.setName(rw.rewrite(md.getName()));
		newMd.setEntryPoint(md.isEntryPoint());
		for (IStatement stmt : md.getBody()) {
			IStatement newStmt = (IStatement) stmt.accept(this, null);
			newMd.getBody().add(newStmt);
		}
		return newMd;
	}

	@Override
	public ISSTNode visit(IExpressionStatement stmt, Void context) {
		ExpressionStatement newStmt = new ExpressionStatement();
		IAssignableExpression newExpr = (IAssignableExpression) stmt.getExpression().accept(this, null);
		newStmt.setExpression(newExpr);
		return newStmt;
	}

	// TODO: support all other statements...

	@Override
	public ISSTNode visit(IInvocationExpression expr, Void context) {

		IMethodName newName = rw.rewrite(expr.getMethodName());
		// TODO: reference + parameters!!

		InvocationExpression newExpr = new InvocationExpression();
		newExpr.setMethodName(newName);
		return newExpr;
	}

	// TODO: support all other <see>IExpression</see> and <see>IReference</see>...

}