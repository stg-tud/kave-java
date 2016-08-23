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

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.expr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public class RaychevQueryTransformerTest {
	protected RaychevQueryTransformer sut;

	@Before
	public void setUp() {
		sut = new RaychevQueryTransformer();
	}

	@Test
	public void containsCompletionExpressionTest() {
		IMethodDeclaration methodDecl = declareMethod(method(type("ReturnType"), type("T1"), "m1"), true,
				expr(new CompletionExpression()));
		Assert.assertTrue(sut.containsCompletionExpression(methodDecl));
	}

	@Test
	public void transformsIntoQueryTest() {
		SST sst = new SST();
		IMethodDeclaration methodDecl = declareMethod(method(type("ReturnType"), type("T1"), "m1"), true,
				assign(variableReference("foo"), new CompletionExpression()), returnStatement(constant("null")));
		sst.getMethods().add(methodDecl);
		sst.setEnclosingType(type("T1"));

		SST expectedSST = new SST();
		expectedSST.setMethods(Sets.newHashSet(//
				declareMethod(
						method(type("ReturnType"), type("com.example.fill.Query_T1"), "test"), true,
						expr(new CompletionExpression()), returnStatement(constant("null"))), //
				declareMethod(method(type("ReturnType"), type("T1"), "m1"), true, returnStatement(constant("null")))));
		expectedSST.setEnclosingType(type("com.example.fill.Query_T1"));

		Context expected = new Context();
		expected.setSST(expectedSST);
		TypeShape typeShape = new TypeShape();

		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(type("com.example.fill.Query_T1"));
		TypeHierarchy extendsTypeHierarchy = new TypeHierarchy();
		extendsTypeHierarchy.setElement(type("T1"));
		typeHierarchy.setExtends(extendsTypeHierarchy);

		MethodHierarchy methodHierarchy = new MethodHierarchy();
		methodHierarchy.setElement(method(type("ReturnType"), type("T1"), "m1"));
		methodHierarchy.setSuper(method(type("ReturnType"), type("T1"), "m1"));

		typeShape.setTypeHierarchy(typeHierarchy);
		typeShape.getMethodHierarchies().add(methodHierarchy);
		expected.setTypeShape(typeShape);

		Context actual = sut.transfromIntoQuery(sst);

		Assert.assertEquals(expected, actual);
	}

	protected ITypeName type(String simpleName) {
		return Names.newType(simpleName + ",P1");
	}

	protected static IMethodName method(ITypeName returnType, ITypeName declType, String simpleName,
			IParameterName... parameters) {
		String parameterStr = Joiner.on(", ")
				.join(Arrays.asList(parameters).stream().map(p -> p.getIdentifier()).toArray());
		String methodIdentifier = String.format("[%1$s] [%2$s].%3$s(%4$s)", returnType.getIdentifier(),
				declType.getIdentifier(), simpleName,
				parameterStr);
		return Names.newMethod(methodIdentifier);
	}
}
