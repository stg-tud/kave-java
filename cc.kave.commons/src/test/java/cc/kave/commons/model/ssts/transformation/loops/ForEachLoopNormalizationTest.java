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
package cc.kave.commons.model.ssts.transformation.loops;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class ForEachLoopNormalizationTest extends LoopNormalizationTest {

	@Test
	public void testSimpleForEachToWhile() {
		ForEachLoop forEachLoop = new ForEachLoop();
		List<IStatement> normalized = forEachLoop.accept(visitor, null);

		assertThat(normalized.size(), equalTo(4));
		assertThat(normalized.get(0), instanceOf(IVariableDeclaration.class));
		assertThat(normalized.get(1), instanceOf(IAssignment.class));
		assertThat(normalized.get(2), instanceOf(IVariableDeclaration.class));
		assertThat(normalized.get(3), instanceOf(IWhileLoop.class));
	}

	@Test
	public void testIteratorDeclaration() {
		IVariableReference loopedReference = SSTUtil.variableReference("collection");
		VariableDeclaration declaration = new VariableDeclaration();
		ITypeName elementType = TypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0");
		declaration.setType(elementType);
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setDeclaration(declaration);
		forEachLoop.setLoopedReference(loopedReference);

		List<IStatement> normalized = forEachLoop.accept(visitor, null);

		assertThat(normalized.size(), equalTo(4));
		assertThat(normalized.get(0), instanceOf(IVariableDeclaration.class));

		IVariableDeclaration iteratorDecl = (IVariableDeclaration) normalized.get(0);
		ITypeName iteratorType = iteratorDecl.getType();
		assertTrue(iteratorType.hasTypeParameters());
		List<ITypeName> typeParameters = iteratorType.getTypeParameters();
		assertThat(typeParameters.size(), equalTo(1));
		assertThat(typeParameters.get(0).getTypeParameterType(), equalTo(elementType));
	}

	@Test
	public void testIteratorInitialization() {
		IVariableReference loopedReference = SSTUtil.variableReference("collection");
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setLoopedReference(loopedReference);
		List<IStatement> normalized = forEachLoop.accept(visitor, null);

		assertThat(normalized.size(), equalTo(4));
		assertThat(normalized.get(0), instanceOf(IVariableDeclaration.class));
		assertThat(normalized.get(1), instanceOf(IAssignment.class));
		
		IVariableDeclaration declaration = (IVariableDeclaration) normalized.get(0);
		
		IAssignment assignment = (IAssignment) normalized.get(1);
		assertThat(assignment.getReference(), equalTo(declaration.getReference()));
		assertThat(assignment.getExpression(), instanceOf(IInvocationExpression.class));

		IInvocationExpression invocation = (IInvocationExpression) assignment.getExpression();
		assertThat(invocation.getReference(), equalTo(loopedReference));
		assertThat(invocation.getMethodName().getIdentifier(), equalTo("iterator"));
		assertThat(invocation.getParameters(), equalTo(empty()));
	}

	@Test
	public void testElementDeclaration() {
		VariableDeclaration declaration = new VariableDeclaration();
		ITypeName elementType = TypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0");
		declaration.setType(elementType);
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setDeclaration(declaration);
		List<IStatement> normalized = forEachLoop.accept(visitor, null);

		assertThat(normalized.size(), equalTo(4));
		assertThat(normalized.get(2), instanceOf(IVariableDeclaration.class));
		assertThat(((IVariableDeclaration) normalized.get(2)).getType(), equalTo(elementType));
	}

	@Test
	public void testLoopCondition() {
		IVariableReference loopedReference = SSTUtil.variableReference("collection");
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setLoopedReference(loopedReference);
		List<IStatement> normalized = forEachLoop.accept(visitor, null);

		assertThat(normalized.size(), equalTo(4));
		assertThat(normalized.get(0), instanceOf(IVariableDeclaration.class));
		assertThat(normalized.get(3), instanceOf(IWhileLoop.class));
		IVariableDeclaration iteratorDecl = (IVariableDeclaration) normalized.get(0);
		IWhileLoop whileLoop = (IWhileLoop) normalized.get(3);

		assertThat(whileLoop.getCondition(), instanceOf(LoopHeaderBlockExpression.class));
		List<IStatement> conditionBlock = ((LoopHeaderBlockExpression) whileLoop.getCondition()).getBody();
		assertThat(conditionBlock.size(), equalTo(1));
		assertThat(conditionBlock.get(0), instanceOf(IExpressionStatement.class));
		IAssignableExpression assignableExpression = ((IExpressionStatement) conditionBlock.get(0)).getExpression();
		assertThat(assignableExpression, instanceOf(IInvocationExpression.class));

		IInvocationExpression invocation = (IInvocationExpression) assignableExpression;
		assertThat(invocation.getParameters(), equalTo(empty()));
		assertThat(invocation.getReference(), equalTo(iteratorDecl.getReference()));
		assertThat(invocation.getMethodName().getIdentifier(), equalTo("hasNext"));
	}

	public void testLoopBody() {
		List<IStatement> forEachBody = new ArrayList<IStatement>();
		forEachBody.add(new Assignment());
		forEachBody.add(new ReturnStatement());
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setBody(forEachBody);
		List<IStatement> normalized = forEachLoop.accept(visitor, null);

		assertThat(normalized.size(), equalTo(4));
		assertThat(normalized.get(3), instanceOf(IWhileLoop.class));
		List<IStatement> whileBody = ((IWhileLoop) normalized.get(3)).getBody();
		int whileBodySize = whileBody.size();
		assertThat(whileBodySize, equalTo(3));
		assertThat(whileBody.subList(whileBodySize - 2, whileBodySize), equalTo(forEachBody));
	}
}
