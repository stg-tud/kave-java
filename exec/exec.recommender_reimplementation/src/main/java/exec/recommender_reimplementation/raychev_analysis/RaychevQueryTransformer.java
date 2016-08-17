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
package exec.recommender_reimplementation.raychev_analysis;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.statements.IAssignment;
import exec.recommender_reimplementation.java_printer.JavaNameUtils;
import exec.recommender_reimplementation.java_transformation.JavaTransformationVisitor;

public class RaychevQueryTransformer {

	public ISST transfromIntoQuery(ISST sst) {
		JavaTransformationVisitor transformationVisitor = new JavaTransformationVisitor(sst);
		ISST transformedSST = transformationVisitor.transform(sst, ISST.class);

		IMethodDeclaration enclosingMethod = findEnclosingMethodWithCompletionExpression(transformedSST);
		if (enclosingMethod != null) {
			IMethodDeclaration testMethodClone = createTestMethodClone(enclosingMethod);
			testMethodClone.accept(new NestedCompletionExpressionElimination(), null);
			ISST enclosingSST = createEnclosingSST(testMethodClone, sst);
			return enclosingSST;
		}
		return null;
	}

	private ISST createEnclosingSST(IMethodDeclaration testMethodClone, ISST sst) {
		SST newSST = new SST();
		newSST.setEnclosingType(changeEnclosingType(sst.getEnclosingType()));
		newSST.getMethods().add(testMethodClone);
		newSST.getMethods().addAll(sst.getMethods());
		newSST.getFields().addAll(sst.getFields());
		return newSST;
	}

	private IMethodDeclaration createTestMethodClone(IMethodDeclaration enclosingMethod) {
		MethodDeclaration clone = new MethodDeclaration();
		IMethodName methodName = enclosingMethod.getName();
		clone.setEntryPoint(enclosingMethod.isEntryPoint());
		clone.setBody(Lists.newArrayList(enclosingMethod.getBody()));
		clone.setName(JavaNameUtils.createMethodName(methodName.getReturnType(),
				changeEnclosingType(methodName.getDeclaringType()), "test", false,
				methodName.getParameters().toArray(new IParameterName[0])));
		return clone;
	}

	private IMethodDeclaration findEnclosingMethodWithCompletionExpression(ISST transformedSST) {
		for (IMethodDeclaration methodDecl : transformedSST.getMethods()) {
			if (containsCompletionExpression(methodDecl)) {
				return methodDecl;
			}
		}
		return null;
	}

	private ITypeName changeEnclosingType(ITypeName typeName) {
		ITypeName newTypeName = TypeName.newTypeName(
				MessageFormat.format("{0}.Query_{1},{2}", "com.example.fill", typeName.getName(),
						typeName.getAssembly()));
		return newTypeName;
	}

	public boolean containsCompletionExpression(IMethodDeclaration methodDecl) {
		ContainsCompletionExpressionVisitor visitor = new ContainsCompletionExpressionVisitor();

		ContainsCompletionExpressionContext context = new ContainsCompletionExpressionContext();
		methodDecl.accept(visitor, context);
		return context.hasCompletionExpression();
	}

	public class ContainsCompletionExpressionContext {
		private boolean hasCompletionExpression;

		public boolean hasCompletionExpression() {
			return hasCompletionExpression;
		}

		public void setHasCompletionExpression(boolean value) {
			this.hasCompletionExpression = value;
		}

	}

	public class ContainsCompletionExpressionVisitor
			extends AbstractTraversingNodeVisitor<ContainsCompletionExpressionContext, Void> {

		@Override
		public Void visit(ICompletionExpression entity, ContainsCompletionExpressionContext context) {
			context.setHasCompletionExpression(true);
			return null;
		}
	}

	public class NestedCompletionExpressionElimination extends AbstractTraversingNodeVisitor<Void, Void> {
		@Override
		protected void visit(List<IStatement> body, Void context) {
			List<IStatement> statementsClone = new ArrayList<>(body);
			for (IStatement stmt : statementsClone) {
				if (stmt instanceof IAssignment) {
					IStatement replacementNode = transformAssignment((IAssignment) stmt);
					body.add(body.indexOf(stmt), replacementNode);
					body.remove(stmt);
					continue;
				}
				stmt.accept(this, context);
			}
		}

		private IStatement transformAssignment(IAssignment assignment) {
			if (assignment.getExpression() instanceof ICompletionExpression) {
				ExpressionStatement exprStmt = new ExpressionStatement();
				exprStmt.setExpression(assignment.getExpression());
				return exprStmt;
			}
			return assignment;
		}
	}
}
