/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.tokenization;

import java.util.Iterator;

import org.apache.commons.lang3.ObjectUtils;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
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
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
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
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;

public class TokenizationVisitor extends AbstractTraversingNodeVisitor<TokenizationContext, Object> {

	private ITypeShape typeShape;
	
	public TokenizationVisitor(ITypeShape typeShape) {
		this.typeShape = typeShape;
	}
	
	@Override
	public Object visit(ISST sst, TokenizationContext c) {
		if(sst.isPartialClass()) {
			c.pushKeyword("partial");
		}
		
		// class classifier
		ITypeName enclosingType = sst.getEnclosingType();
		if(enclosingType.isInterfaceType()) {
			c.pushKeyword("interface");
		}
		else if(enclosingType.isEnumType()) {
			c.pushKeyword("enum");
		}
		else if(enclosingType.isStructType()) {
			c.pushKeyword("struct");
		}
		else {
			c.pushKeyword("class");
		}
		
		c.pushType(enclosingType);
		
		// handle class extensions
		if(typeShape != null && typeShape.getTypeHierarchy().hasSupertypes()) {
			c.pushColon();
			
			ITypeHierarchy typeHierarchy = typeShape.getTypeHierarchy();
			
			if(typeHierarchy.hasSuperclass() && typeHierarchy.getExtends() != null) {
				c.pushType(typeHierarchy.getExtends().getElement());
				
				if(typeHierarchy.isImplementingInterfaces()) {
					c.pushComma();
				}
			}
			
			for (Iterator<ITypeHierarchy> iterator = typeHierarchy.getImplements().iterator(); iterator.hasNext();) {
				ITypeHierarchy interfaceTypeHierarchy = (ITypeHierarchy) iterator.next();
				
				c.pushType(interfaceTypeHierarchy.getElement());
				
				if(iterator.hasNext()) {
					c.pushComma();
				}
							
			}
			
		}
	
		c.pushOpeningCurlyBracket();
		
		super.visit(sst, c);
		
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	/* Declarations */
	
	@Override
	public Object visit(IDelegateDeclaration decl, TokenizationContext c) {
		c.pushKeyword("delegate")
		.pushType(decl.getName())
		.pushParameters(decl.getName().getParameters())
		.pushSemicolon();
		
		return null;
	}
	
	@Override
	public Object visit(IEventDeclaration decl, TokenizationContext c) {
		c.pushKeyword("event")
		.pushType(decl.getName().getHandlerType())
		.pushToken(decl.getName().getName())
		.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IFieldDeclaration decl, TokenizationContext c) {
		if(decl.getName().isStatic()) {
			c.pushKeyword("static");
		}
		
		c.pushType(decl.getName().getValueType())
		.pushToken(decl.getName().getName())
		.pushSemicolon();
				
		return null;
	}
	
	@Override
	public Object visit(IMethodDeclaration decl, TokenizationContext c) {
		if(decl.getName().isStatic()) {
			c.pushKeyword("static");
		}
		
		c.pushType(decl.getName().getReturnType())
		.pushToken(decl.getName().getName());
		
		if(decl.getName().hasTypeParameters()) {
			c.pushTypeParameters(decl.getName().getTypeParameters());
		}
		
		c.pushParameters(decl.getName().getParameters())
		.pushOpeningCurlyBracket();
		
		visit(decl.getBody(), c);
		
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	@Override
	public Object visit(IPropertyDeclaration decl, TokenizationContext c) {
		if(decl.getName().isStatic()) {
			c.pushKeyword("static");
		}
		
		c.pushType(decl.getName().getValueType())
		.pushToken(decl.getName().getName());
		
		boolean hasBody = !decl.getGet().isEmpty() || !decl.getSet().isEmpty();
		
		c.pushOpeningCurlyBracket();
		
		if(hasBody) {
			
			if(decl.getName().hasGetter()) {
				c.pushOpeningCurlyBracket()
				.pushKeyword("get");
				
				visit(decl.getGet(), c);
				
				c.pushClosingCurlyBracket();
			}
			
			if(decl.getName().hasSetter()) {
				c.pushOpeningCurlyBracket()
				.pushKeyword("set");
				
				visit(decl.getSet(), c);
				
				c.pushClosingCurlyBracket();
			}
		}
		else {
			c.pushKeyword("get")
			.pushSemicolon()
			.pushKeyword("set")
			.pushSemicolon();
		}
		
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	
	/* Statements */
	
	@Override
	public Object visit(IAssignment stmt, TokenizationContext c) {
		stmt.getReference().accept(this, c);
		
		c.pushOperator("=");
		
		stmt.getExpression().accept(this, c);
		
		c.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IBreakStatement stmt, TokenizationContext c) {
		c.pushKeyword("break").pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IContinueStatement stmt, TokenizationContext c) {
		c.pushKeyword("continue").pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IEventSubscriptionStatement stmt, TokenizationContext c) {
		stmt.getReference().accept(this, c);
		
		switch (stmt.getOperation()) {
		case Add:
			c.pushOperator("+=");
			break;
		case Remove:
			c.pushOperator("-=");
			break;
		}
		
		stmt.getExpression().accept(this, c);
		
		c.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IExpressionStatement stmt, TokenizationContext c) {
		stmt.getExpression().accept(this, c);
		c.pushSemicolon();
		return null;
	}
	
	
	@Override
	public Object visit(IGotoStatement stmt, TokenizationContext c) {
		c.pushKeyword("goto").pushToken(stmt.getLabel()).pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(ILabelledStatement stmt, TokenizationContext c) {
		c.pushToken(stmt.getLabel()).pushColon();
		stmt.getStatement().accept(this, c);
		return null;
	}

	
	@Override
	public Object visit(IReturnStatement stmt, TokenizationContext c) {
		c.pushKeyword("return");
		if(!stmt.isVoid()) stmt.getExpression().accept(this, c);
		c.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IThrowStatement stmt, TokenizationContext c) {
		c.pushKeyword("throw");
		if(!stmt.isReThrow()) stmt.getReference().accept(this, c);
		c.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IUnknownStatement unknownStmt, TokenizationContext c) {
		c.pushUnknown().pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IVariableDeclaration stmt, TokenizationContext c) {
		c.pushType(stmt.getType());
		stmt.getReference().accept(this, c);
		c.pushSemicolon();
		return null;
	}
	
	/* Blocks */
	
	@Override
	public Object visit(IDoLoop block, TokenizationContext c) {
		c.pushKeyword("do").pushOpeningCurlyBracket();
		visit(block.getBody(), c);
		c.pushClosingCurlyBracket().pushOpeningBracket().pushKeyword("while");
		block.getCondition().accept(this, c);
		c.pushClosingBracket();
		
		return null;
	}
	
	@Override
	public Object visit(IForEachLoop block, TokenizationContext c) {
		c.pushKeyword("foreach")
		.pushOpeningBracket()
		.pushType(block.getDeclaration().getType());
		block.getDeclaration().getReference().accept(this, c);
		c.pushKeyword("in");
		block.getLoopedReference().accept(this, c);
		c.pushClosingBracket();
		
		c.pushOpeningCurlyBracket();
		visit(block.getBody(), c);
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	@Override
	public Object visit(IForLoop block, TokenizationContext c) {
		c.pushKeyword("for").pushOpeningBracket();
		visit(block.getInit(), c);
		c.pushSemicolon();
		block.getCondition().accept(this, c);
		c.pushSemicolon();
		visit(block.getStep(), c);
		c.pushClosingBracket()
		
		.pushOpeningCurlyBracket();
		visit(block.getBody(), c);
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	@Override
	public Object visit(IIfElseBlock block, TokenizationContext c) {
		c.pushKeyword("if").pushOpeningBracket();
		block.getCondition().accept(this, c);
		c.pushClosingBracket().pushOpeningCurlyBracket();
		visit(block.getThen(), c);
		c.pushClosingCurlyBracket();
		
		if(!block.getElse().isEmpty()) {
			c.pushKeyword("else").pushOpeningCurlyBracket();
			visit(block.getElse(), c);
			c.pushClosingCurlyBracket();
		}
				
		return null;
	}
	
	@Override
	public Object visit(ILockBlock block, TokenizationContext c) {
		c.pushKeyword("lock").pushOpeningBracket();
		block.getReference().accept(this, c);
		c.pushClosingBracket()
		
		.pushOpeningCurlyBracket();
		visit(block.getBody(), c);
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	@Override
	public Object visit(ISwitchBlock block, TokenizationContext c) {
		c.pushKeyword("switch").pushOpeningBracket();
		block.getReference().accept(this, c);
		c.pushClosingBracket();
		
		c.pushOpeningCurlyBracket();
		
		for (ICaseBlock caseBlock : block.getSections()) {
			c.pushKeyword("case");
			caseBlock.getLabel().accept(this, c);
			c.pushColon();
			visit(caseBlock.getBody(), c);
		}

		if(!block.getDefaultSection().isEmpty()) {
			c.pushKeyword("default").pushColon();
			visit(block.getDefaultSection(), c);
		}
		
		c.pushClosingCurlyBracket();

		return null;
	}
	
	@Override
	public Object visit(ITryBlock block, TokenizationContext c) {
		c.pushKeyword("try").pushOpeningCurlyBracket();
		visit(block.getBody(), c);
		c.pushClosingCurlyBracket();
		
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			c.pushKeyword("catch");
            
			if (catchBlock.getKind() != CatchBlockKind.General)
            {
                c.pushOpeningBracket()
                 .pushType(catchBlock.getParameter().getValueType());

                if (catchBlock.getKind() != CatchBlockKind.Unnamed)
                {
                    c.pushToken(catchBlock.getParameter().getName());
                }

                c.pushClosingBracket();
            }
            
			c.pushOpeningCurlyBracket();
			visit(catchBlock.getBody(), c);
			c.pushClosingCurlyBracket();
		}
		
		if(!block.getFinally().isEmpty()) {
			c.pushKeyword("finally").pushOpeningCurlyBracket();
			visit(block.getFinally(), c);
			c.pushClosingCurlyBracket();
		}
		
		return null;
	}
	
	@Override
	public Object visit(IUncheckedBlock block, TokenizationContext c) {
		c.pushKeyword("unchecked").pushOpeningCurlyBracket();
		visit(block.getBody(), c);
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	@Override
	public Object visit(IUnsafeBlock block, TokenizationContext c) {
		c.pushKeyword("unsafe").pushOpeningCurlyBracket();
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	@Override
	public Object visit(IUsingBlock block, TokenizationContext c) {
		c.pushKeyword("using").pushOpeningBracket();
		block.getReference().accept(this, c);
		c.pushClosingBracket();
		
		c.pushOpeningCurlyBracket();
		visit(block.getBody(), c);
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	@Override
	public Object visit(IWhileLoop block, TokenizationContext c) {
		c.pushKeyword("while").pushOpeningBracket();
		block.getCondition().accept(this, c);
		c.pushClosingBracket();
		
		c.pushOpeningCurlyBracket();
		visit(block.getBody(), c);
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	/* Expressions */
	
	@Override
	public Object visit(IBinaryExpression expr, TokenizationContext c) {
		expr.getLeftOperand().accept(this, c);
		// TODO convert operator to C# syntax 
		c.pushOperator(expr.getOperator().name());
				expr.getRightOperand().accept(this, c);
		return null;
	}
	
	@Override
	public Object visit(ICastExpression expr, TokenizationContext c) {
		if(expr.getOperator() != CastOperator.SafeCast) {		
			c.pushOpeningBracket().pushType(expr.getTargetType()).pushClosingBracket();
			expr.getReference().accept(this, c);
		}
		else {
			expr.getReference().accept(this, c);		
			c.pushKeyword("as")
			.pushType(expr.getTargetType());
		}
		
		return null;
	}
	
	@Override
	public Object visit(ICompletionExpression entity, TokenizationContext c) {
		if(entity.getVariableReference() != null) {
			c.pushToken(entity.getVariableReference().getIdentifier()).pushPeriod();
		}
		else if (entity.getTypeReference() != null) {
			c.pushType(entity.getTypeReference()).pushPeriod();
		}
		
		c.pushToken(entity.getToken());
		return null;
	}
	
	@Override
	public Object visit(IComposedExpression expr, TokenizationContext c) {
		c.pushKeyword("composed").pushOpeningBracket();
		
		for (Iterator<IVariableReference> iterator = expr.getReferences().iterator(); iterator.hasNext();) {
			IVariableReference varRef= (IVariableReference) iterator.next();
			
			varRef.accept(this, c);
			
			if(iterator.hasNext()) c.pushComma();
		}
		
		c.pushClosingBracket();
		return null;
	}
	
	@Override
	public Object visit(IConstantValueExpression expr, TokenizationContext c) {
		c.pushToken(ObjectUtils.firstNonNull(expr.getValue(), "..."));
		return null;
	}
	
	@Override
	public Object visit(IIfElseExpression expr, TokenizationContext c) {
		expr.getCondition().accept(this, c);
		c.pushOperator("?");
		expr.getThenExpression().accept(this, c);
		c.pushColon();
		expr.getElseExpression().accept(this, c);
		return null;
	}
	
	@Override
	public Object visit(IIndexAccessExpression expr, TokenizationContext c) {
		expr.getReference().accept(this, c);
		
		c.pushOpeningSquareBracket();
		
		for (Iterator<ISimpleExpression> iterator = expr.getIndices().iterator(); iterator.hasNext();) {
			ISimpleExpression simpleExpression = (ISimpleExpression) iterator.next();
			
			simpleExpression.accept(this, c);
			
			if(iterator.hasNext()) c.pushComma();
		}
		
		c.pushClosingSquareBracket();
		
		return null;
	}
	
	@Override
	public Object visit(IInvocationExpression expr, TokenizationContext c) {
		if(expr.getMethodName().isConstructor()) {
			c.pushKeyword("new").pushType(expr.getMethodName().getDeclaringType());			
		}
		else {
			if(expr.getMethodName().isStatic()) {
				c.pushType(expr.getMethodName().getDeclaringType());
			}
			else {
				expr.getReference().accept(this, c);
			}
			
			c.pushPeriod().pushToken(expr.getMethodName().getName());
		}
		
		c.pushOpeningBracket();
		
		// add parameters
		for (Iterator<ISimpleExpression> iterator = expr.getParameters().iterator(); iterator.hasNext();) {
			ISimpleExpression simpleExpression = (ISimpleExpression) iterator.next();
			
			simpleExpression.accept(this, c);
			
			if(iterator.hasNext()) c.pushComma();
		}
		
		c.pushClosingBracket();
		
		return null;
	}
	
	@Override
	public Object visit(ILambdaExpression expr, TokenizationContext c) {
		c.pushParameters(expr.getName().getParameters()).pushOperator("=>");
		c.pushOpeningCurlyBracket();
		visit(expr.getBody(), c);
		c.pushClosingCurlyBracket();
		return null;
	}
	
	@Override
	public Object visit(ILoopHeaderBlockExpression expr, TokenizationContext c) {
		c.pushOpeningCurlyBracket();
		visit(expr.getBody(), c);
		c.pushClosingCurlyBracket();
		return null;
	}
	
	@Override
	public Object visit(INullExpression expr, TokenizationContext c) {
		c.pushKeyword("null");
		return null;
	}
	
	@Override
	public Object visit(IReferenceExpression expr, TokenizationContext c) {
		expr.getReference().accept(this, c);
		return null;
	}
	
	@Override
	public Object visit(ITypeCheckExpression expr, TokenizationContext c) {
		expr.getReference().accept(this, c);
		c.pushKeyword("is").pushType(expr.getType());
		return null;
	}
	
	@Override
	public Object visit(IUnaryExpression expr, TokenizationContext c) {
		UnaryOperator operator = expr.getOperator();
		
		if(operator == UnaryOperator.PostIncrement|| operator == UnaryOperator.PostDecrement) {
			expr.getOperand().accept(this, c);
			c.pushOperator(operator.name());
		}
		else {
			c.pushOperator(operator.name());
			expr.getOperand().accept(this, c);
		}
		
		return null;
	}
	
	@Override
	public Object visit(IUnknownExpression unknownExpr, TokenizationContext c) {
		c.pushUnknown();
		return null;
	}
	
	/* References */
	
	@Override
	public Object visit(IEventReference eventRef, TokenizationContext c) {
		if(eventRef.getEventName().isStatic()) {
			c.pushType(eventRef.getEventName().getDeclaringType());
		}
		else {
			c.pushToken(eventRef.getReference().getIdentifier());
		}
		
		c.pushPeriod().pushToken(eventRef.getEventName().getName());
		return null;
	}
	
	@Override
	public Object visit(IFieldReference fieldRef, TokenizationContext c) {
		if(fieldRef.getFieldName().isStatic()) {
			c.pushType(fieldRef.getFieldName().getDeclaringType());
		}
		else {
			c.pushToken(fieldRef.getReference().getIdentifier());
		}
		
		c.pushPeriod().pushToken(fieldRef.getFieldName().getName());
		return null;
	}
	
	@Override
	public Object visit(IIndexAccessReference indexAccessRef, TokenizationContext c) {
		indexAccessRef.getExpression().accept(this,c);
		return null;
	}
	
	@Override
	public Object visit(IMethodReference methodRef, TokenizationContext c) {
		if(methodRef.getMethodName().isStatic()) {
			c.pushType(methodRef.getMethodName().getDeclaringType());
		}
		else {
			c.pushToken(methodRef.getReference().getIdentifier());
		}
		
		c.pushPeriod().pushToken(methodRef.getMethodName().getName());
		return null;
	}
	
	@Override
	public Object visit(IPropertyReference propertyRef, TokenizationContext c) {
		if(propertyRef.getPropertyName().isStatic()) {
			c.pushType(propertyRef.getPropertyName().getDeclaringType());
		}
		else {
			c.pushToken(propertyRef.getReference().getIdentifier());
		}
		
		c.pushPeriod().pushToken(propertyRef.getPropertyName().getName());
		return null;
	}
	
	@Override
	public Object visit(IUnknownReference unknownRef, TokenizationContext c) {
		c.pushUnknown();
		return null;
	}
	
	@Override
	public Object visit(IVariableReference varRef, TokenizationContext c) {
		c.pushToken(varRef.getIdentifier());
		return null;
	}
}
