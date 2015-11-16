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

package cc.kave.rs.commons.analysis.transformer;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class DeclarationVisitor implements ISSTNodeVisitor<SST, Boolean> {


	
	public void visitFieldDeclaration(IFieldDeclaration decl, SST context)
    {
		if (decl != null)
        {
//            var name = decl.DeclaredElement.GetName<IFieldName>();
//
//            if (IsNestedDeclaration(name, context))
//            {
//                return;
//            }
//
//            context.Fields.Add(new FieldDeclaration {Name = name});
        }
    }

	@Override
	public Boolean visit(ISST sst, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IDelegateDeclaration stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IEventDeclaration stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IFieldDeclaration stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IMethodDeclaration stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IPropertyDeclaration stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IVariableDeclaration stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IAssignment stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IBreakStatement stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IContinueStatement stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IExpressionStatement stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IGotoStatement stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ILabelledStatement stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IReturnStatement stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IThrowStatement stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IEventSubscriptionStatement stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IDoLoop block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IForEachLoop block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IForLoop block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IIfElseBlock block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ILockBlock stmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ISwitchBlock block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ITryBlock block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IUncheckedBlock block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IUnsafeBlock block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IUsingBlock block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IWhileLoop block, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ICompletionExpression entity, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IComposedExpression expr, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IIfElseExpression expr, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IInvocationExpression entity, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ILambdaExpression expr, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ILoopHeaderBlockExpression expr, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IConstantValueExpression expr, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(INullExpression expr, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IReferenceExpression expr, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IEventReference eventRef, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IFieldReference fieldRef, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IMethodReference methodRef, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IPropertyReference methodRef, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IVariableReference varRef, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IUnknownReference unknownRef, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IUnknownExpression unknownExpr, SST context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IUnknownStatement unknownStmt, SST context) {
		// TODO Auto-generated method stub
		return null;
	}
}
