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

import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static exec.recommender_reimplementation.java_printer.JavaNameUtils.createMethodName;

import java.text.MessageFormat;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import exec.recommender_reimplementation.java_transformation.JavaTransformationVisitor;
import exec.recommender_reimplementation.raychev_analysis.NestedCompletionExpressionEliminationVisitor.EliminationStrategy;

public class RaychevQueryTransformer {

	public Context transfromIntoQuery(ISST sst) {
		JavaTransformationVisitor transformationVisitor = new JavaTransformationVisitor(sst);
		ISST transformedSST = transformationVisitor.transform(sst, ISST.class);

		IMethodDeclaration enclosingMethod = findEnclosingMethodWithCompletionExpression(transformedSST);
		if (enclosingMethod != null) {
			IMethodDeclaration testMethodClone = createTestMethodClone(enclosingMethod);
			testMethodClone.accept(new NestedCompletionExpressionEliminationVisitor(EliminationStrategy.REPLACE), null);
			ISST querySST = createEnclosingSST(testMethodClone, enclosingMethod, sst);
			Context queryContext = createQueryContext(querySST, transformedSST, enclosingMethod.getName());
			return queryContext;
		}
		return null;
	}

	private Context createQueryContext(ISST querySST, ISST originalSST, IMethodName queryMethodName) {
		Context context = new Context();
		context.setTypeShape(createQueryTypeShape(querySST, originalSST, queryMethodName));
		context.setSST(querySST);
		return context;
	}

	private TypeShape createQueryTypeShape(ISST querySST, ISST originalSST, IMethodName queryMethodName) {
		TypeShape typeShape = new TypeShape();
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(querySST.getEnclosingType());
		TypeHierarchy extendsTypeHierarchy = new TypeHierarchy();
		extendsTypeHierarchy.setElement(originalSST.getEnclosingType());
		typeHierarchy.setExtends(extendsTypeHierarchy);

		MethodHierarchy methodHierarchy = new MethodHierarchy();
		methodHierarchy.setElement(queryMethodName);
		methodHierarchy.setSuper(queryMethodName);

		typeShape.setTypeHierarchy(typeHierarchy);
		typeShape.getMethodHierarchies().add(methodHierarchy);
		return typeShape;
	}

	private ISST createEnclosingSST(IMethodDeclaration testMethodClone, IMethodDeclaration enclosingMethod, ISST sst) {
		SST newSST = new SST();
		newSST.setEnclosingType(changeEnclosingType(sst.getEnclosingType()));
		newSST.getMethods().add(testMethodClone);

		IMethodDeclaration overriddenMethod = declareMethod(enclosingMethod.getName(), true);
		if (!enclosingMethod.getName().getReturnType().isVoidType()) {
			overriddenMethod.getBody().add(returnStatement(constant("null")));
		}
		newSST.getMethods().add(overriddenMethod);
		return newSST;
	}

	private IMethodDeclaration createTestMethodClone(IMethodDeclaration enclosingMethod) {
		MethodDeclaration clone = new MethodDeclaration();
		IMethodName methodName = enclosingMethod.getName();
		clone.setEntryPoint(enclosingMethod.isEntryPoint());
		clone.setBody(Lists.newArrayList(enclosingMethod.getBody()));
		clone.setName(createMethodName(methodName.getReturnType(),
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

}
