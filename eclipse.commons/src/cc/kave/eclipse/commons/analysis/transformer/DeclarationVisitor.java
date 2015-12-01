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
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MemberName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.eclipse.commons.analysis.completiontarget.CompletionTargetMarker;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;
import cc.kave.eclipse.namefactory.NodeFactory;

public class DeclarationVisitor extends ASTVisitor {

	private SST context;
	private final Set<MethodName> entryPoints;
	private final CompletionTargetMarker marker;

	public DeclarationVisitor(SST context, Set<MethodName> entryPoints, CompletionTargetMarker marker) {
		this.context = context;
		this.entryPoints = entryPoints;
		this.marker = marker;
	}

	@Override
	public boolean visit(FieldDeclaration node) {

		if (node != null) {
			List<VariableDeclarationFragment> fragments = node.fragments();

			for (VariableDeclarationFragment fragment : fragments) {

				FieldName name = (FieldName) NodeFactory.createNodeName(fragment);

				// if (isNestedDeclaration(name, context)) {
				// TODO: return super.visit(node);
				// }

				cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration fieldDeclaration = new cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration();
				fieldDeclaration.setName(name);

				context.getFields().add(fieldDeclaration);

			}
		}

		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration decl) {

		if (decl.isConstructor()) {
			constructorHelper(decl);
		} else {
			methodDeclHelper(decl);
		}

		return super.visit(decl);
	}

	private void methodDeclHelper(MethodDeclaration decl) {
		if (decl != null) {
			MethodName methodName = (MethodName) NodeFactory.createNodeName(decl);

			// if (isNestedDeclaration(methodName, context))
			// {
			// return;
			// }

			cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration sstDecl = new cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration();
			sstDecl.setName(methodName);
			sstDecl.setEntryPoint(entryPoints.contains(methodName));

			context.getMethods().add(sstDecl);

			if (decl == marker.getAffectedNode()) {
				ExpressionStatement expStatement = new ExpressionStatement();
				expStatement.setExpression(new CompletionExpression());
				sstDecl.getBody().add(expStatement);
			}

			if (!Modifier.isAbstract(decl.getModifiers())) {
				BodyVisitor bodyVisitor = new BodyVisitor(new UniqueVariableNameGenerator(), marker);
				decl.accept(bodyVisitor);
				// Execute.AndSupressExceptions(
				// delegate { decl.Accept(bodyVisitor, sstDecl.Body); });
			}
		}
	}

	private void constructorHelper(MethodDeclaration decl) {
		UniqueVariableNameGenerator nameGen = new UniqueVariableNameGenerator();
		ExpressionVisitor exprVisit = new ExpressionVisitor(nameGen);

		if (decl != null) {
			MethodName methodName = (MethodName) NodeFactory.createNodeName(decl);

			// if (isNestedDeclaration(methodName, context))
			// {
			// TODO: return super.visit(decl);
			// }

			cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration sstDecl = new cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration();
			sstDecl.setName(methodName);
			sstDecl.setEntryPoint(entryPoints.contains(methodName));

			context.getMethods().add(sstDecl);

			if (decl == marker.getAffectedNode()) {
				ExpressionStatement expStatement = new ExpressionStatement();
				expStatement.setExpression(new CompletionExpression());
				sstDecl.getBody().add(expStatement);
			}

			// if (decl.Initializer != null)
			// {
			// Name name = CsMethodName.getUnknownName();
			//
			// var substitution = decl.DeclaredElement.IdSubstitution;
			// var resolvedRef = decl.Initializer.Reference.Resolve();
			// if (resolvedRef.DeclaredElement != null)
			// {
			// name =
			// resolvedRef.DeclaredElement.GetName<IMethodName>(substitution);
			// }
			//
			// var args = Lists.NewList<ISimpleExpression>();
			// foreach (var p in decl.Initializer.Arguments)
			// {
			// var expr = exprVisit.ToSimpleExpression(p.Value, sstDecl.Body);
			// args.Add(expr);
			// }
			//
			// var varId = new VariableReference().Identifier; // default value
			// if (decl.Initializer.Instance != null)
			// {
			// var tokenType = decl.Initializer.Instance.GetTokenType();
			// var isThis = CSharpTokenType.THIS_KEYWORD == tokenType;
			// var isBase = CSharpTokenType.BASE_KEYWORD == tokenType;
			//
			// varId = isThis ? "this" : isBase ? "base" : varId;
			// }
			//
			// sstDecl.Body.Add(
			// new ExpressionStatement
			// {
			// Expression = new InvocationExpression
			// {
			// Reference = new VariableReference {Identifier = varId},
			// MethodName = name,
			// Parameters = args
			// }
			// });
			// }
			//
			// if (!Modifier.isAbstract(decl.getModifiers())) {
			// BodyVisitor bodyVisitor = new BodyVisitor(nameGen, marker);
			// decl.accept(bodyVisitor);
			// Execute.AndSupressExceptions(
			// delegate { decl.Accept(bodyVisitor, sstDecl.Body); });
			// }
		}
	}

	private static boolean isNestedDeclaration(DelegateTypeName name, SST context) {
		return !name.getDeclaringType().equals(context.getEnclosingType());
	}

	private static boolean isNestedDeclaration(MemberName name, SST context) {
		return !name.getDeclaringType().equals(context.getEnclosingType());
	}
}
