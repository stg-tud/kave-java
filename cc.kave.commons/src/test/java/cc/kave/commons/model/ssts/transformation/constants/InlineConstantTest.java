/*
 * Copyright 2015 Carina Oberle
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
package cc.kave.commons.model.ssts.transformation.constants;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.transformation.constants.InlineConstantContext;
import cc.kave.commons.model.ssts.impl.transformation.constants.InlineConstantVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningBaseTest;

public class InlineConstantTest extends InliningBaseTest {
	private InlineConstantVisitor visitor;
	private InlineConstantContext context;
	private String field;

	@Before
	public void setup() {
		visitor = new InlineConstantVisitor();
		context = new InlineConstantContext();
		field = "[T1,P1,1] [System.Int32,P2,1].f";
	}

	@Test
	public void testInlineConstant() {
		Set<IFieldDeclaration> fields = declareFields(field);
		MethodDeclaration method = new MethodDeclaration();
		List<IStatement> body = new ArrayList<IStatement>();
		body.add(returnStatement(refExpr(refField(field)), false));
		method.setBody(body);
		ISST sst = buildSST(fields, method);

		List<IStatement> bodyInlined = new ArrayList<IStatement>();
		bodyInlined.add(returnStatement(new ConstantValueExpression(), false));

		sst.accept(visitor, context);
		assertEquals(sst.getFields(), fields);
		assertEquals(sst.getMethods().size(), 1);
		assertEquals(sst.getMethods().iterator().next().getBody(), bodyInlined);
	}
}
