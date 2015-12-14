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
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.WhileStatement;

import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.eclipse.commons.analysis.completiontarget.CompletionTargetMarker;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;

public class ExpressionVisitor extends ASTVisitor {

	private final UniqueVariableNameGenerator nameGen;
	private final CompletionTargetMarker marker;
	// private final ToAssignableReference toAssignableRef;

	public ExpressionVisitor(UniqueVariableNameGenerator nameGen, CompletionTargetMarker marker) {
		this.nameGen = nameGen;
		this.marker = marker;
	}

	public IAssignableReference toAssignableRef(ASTNode expr, List<IStatement> body) {
		if (expr == null) {
			return new UnknownReference();
		}

		// var reference = ToReference(expr, body);
		// var assignableRef = reference as IAssignableReference ?? new
		// UnknownReference();
		// return assignableRef;
		return null;
	}

//	private IReference ToReference(ASTNode expr,
//	            List<IStatement> body)
//	        {
//	            if (expr == null || expr is IThisExpression)
//	            {
//	                return new VariableReference {Identifier = "this"};
//	            }
//
//	            if (expr is IBaseExpression)
//	            {
//	                return new VariableReference {Identifier = "base"};
//	            }
//
//	            if (expr is IPredefinedTypeExpression)
//	            {
//	                // (= qualifier is static type)
//	                return new VariableReference();
//	            }
//
//	            var invExpr = expr as IInvocationExpression;
//	            if (invExpr != null)
//	            {
//	                var assInv = VisitInvocationExpression(invExpr, body);
//
//	                var tmpVar = new VariableReference {Identifier = _nameGen.GetNextVariableName()};
//	                var type = invExpr.GetExpressionType().ToIType().GetName();
//	                body.Add(new VariableDeclaration {Reference = tmpVar, Type = type});
//
//	                body.Add(
//	                    new Assignment
//	                    {
//	                        Reference = tmpVar,
//	                        Expression = assInv
//	                    });
//
//	                return tmpVar;
//	            }
//
//	            var refExpr = expr as IReferenceExpression;
//	            if (refExpr != null)
//	            {
//	                var hasQualifier = refExpr.QualifierExpression != null;
//
//	                IVariableReference baseRef = new VariableReference();
//	                if (hasQualifier && refExpr.QualifierExpression.IsClassifiedAsVariable)
//	                {
//	                    baseRef = CreateVariableReference(refExpr.QualifierExpression, body);
//	                }
//
//	                var resolveResult = refExpr.Reference.Resolve();
//	                var elem = resolveResult.DeclaredElement;
//	                if (elem == null)
//	                {
//	                    return new UnknownReference();
//	                }
//
//	                var field = elem as IField;
//	                if (field != null)
//	                {
//	                    return new FieldReference
//	                    {
//	                        FieldName = field.GetName<IFieldName>(),
//	                        Reference = baseRef
//	                    };
//	                }
//
//	                var property = elem as IProperty;
//	                if (property != null)
//	                {
//	                    return new PropertyReference
//	                    {
//	                        PropertyName = property.GetName<IPropertyName>(),
//	                        Reference = baseRef
//	                    };
//	                }
//
//	                var @event = elem as IEvent;
//	                if (@event != null)
//	                {
//	                    return new EventReference
//	                    {
//	                        EventName = @event.GetName<IEventName>(),
//	                        Reference = baseRef
//	                    };
//	                }
//
//	                var method = elem as IMethod;
//	                if (method != null)
//	                {
//	                    return new MethodReference
//	                    {
//	                        MethodName = method.GetName<IMethodName>(),
//	                        Reference = baseRef
//	                    };
//	                }
//
//	                var localVar = elem as ILocalVariable;
//	                var parameter = elem as IParameter;
//	                if (localVar != null || parameter != null)
//	                {
//	                    return new VariableReference {Identifier = elem.ShortName};
//	                }
//	            }
//
//	            var elementAccessExpr = expr as IElementAccessExpression;
//	            if (elementAccessExpr != null)
//	            {
//	                return new IndexAccessReference
//	                {
//	                    Expression = (IIndexAccessExpression) VisitElementAccessExpression(elementAccessExpr, body)
//	                };
//	            }
//
//	            return new UnknownReference();
//	        }
	
	 public ISimpleExpression toSimpleExpression(ASTNode csExpr, List<IStatement> body)
     {
		// if (csExpr == null)
		// {
		// return new UnknownExpression();
		// }
		//
		// var expr = csExpr.Accept(this, body);
		//
		// if (expr == null)
		// {
		// return new UnknownExpression();
		// }
		//
		// if (expr is ISimpleExpression)
		// {
		// return expr as ISimpleExpression;
		// }
		//
		// VariableReference newRef = new VariableReference();
		// newRef.setIdentifier(nameGen.getNextVariableName());
		// var exprIType = csExpr.GetExpressionType().ToIType();
		// // TODO write test for this null check
		// var exprType = exprIType == null ? CsTypeName.UNKNOWN_NAME :
		// exprIType.GetName();
		// body.add(
		// new VariableDeclaration
		// {
		// Reference = newRef,
		// Type = exprType
		// });
		// body.add(
		// new Assignment
		// {
		// Reference = newRef,
		// Expression = expr
		// });
		// return new ReferenceExpression {Reference = newRef};
		return null;
     }

	 // TODO: Method stub, Change stmt type
	public ILoopHeaderExpression toLoopHeaderExpression(WhileStatement stmt, List<IStatement> body) {
		// TODO Auto-generated method stub
		return null;
	}

}
