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

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class ForLoopNormalizationTest extends LoopNormalizationTest {
	private String loopVarA;
	private String loopVarB;

	@Before
	public void setup() {
		super.setup();
		loopVarA = "indexA";
		loopVarB = "indexB";
	}

	@Test
	public void testLoopInit() {
		List<IStatement> init = Lists.newArrayList(declareVar(loopVarA),
				assign(variableReference(loopVarA), constant("0")));
		ForLoop forLoop = new ForLoop();
		forLoop.setInit(init);

		List<IStatement> normalized = forLoop.accept(visitor, context);

		assertThat(normalized.size(), is(3));
		assertThat(normalized.get(0), is(init.get(0)));
		assertThat(normalized.get(1), is(init.get(1)));
		assertThat(normalized.get(2), instanceOf(IWhileLoop.class));
	}

	@Test
	public void testNoLoopInit() {
		ForLoop forLoop = new ForLoop();
		List<IStatement> normalized = forLoop.accept(visitor, context);

		assertThat(normalized.size(), is(1));
		assertThat(normalized.get(0), instanceOf(IWhileLoop.class));
	}

	@Test
	public void testLoopStep() {
		List<IStatement> step = Lists.newArrayList(assign(variableReference(loopVarA), constant("0")));
		ForLoop forLoop = new ForLoop();
		forLoop.setStep(step);

		List<IStatement> normalized = forLoop.accept(visitor, context);

		assertThat(normalized.size(), is(1));
		assertThat(normalized.get(0), instanceOf(IWhileLoop.class));
		List<IStatement> whileBody = ((IWhileLoop) normalized.get(0)).getBody();
		assertThat(whileBody, is(step));
	}

	@Test
	public void testSimpleForToWhile() {
		ILoopHeaderBlockExpression condition = loopHeader();
		IVariableDeclaration varDecl = declareVar(loopVarA);
		IAssignment assignment = assign(variableReference(loopVarA), constant("0"));
		IStatement stmt = new UnknownStatement();
		List<IStatement> body = Lists.newArrayList(stmt);
		List<IStatement> step = Lists.newArrayList(assignment);

		ForLoop forLoop = new ForLoop();
		forLoop.setInit(Lists.newArrayList(varDecl, assignment));
		forLoop.setStep(step);
		forLoop.setBody(body);
		forLoop.setCondition(condition);

		List<IStatement> normalized = forLoop.accept(visitor, context);

		assertThat(normalized.size(), is(3));
		assertThat(normalized.get(0), is(varDecl));
		assertThat(normalized.get(1), is(assignment));
		assertThat(normalized.get(2), instanceOf(IWhileLoop.class));
		IWhileLoop whileLoop = (IWhileLoop) normalized.get(2);
		assertThat(whileLoop.getCondition(), is(condition));
		assertThat(whileLoop.getBody(), is(Lists.newArrayList(stmt, assignment)));
	}

	@Test
	public void testContinueInsideLoopBody() {
		ContinueStatement continueStmt = new ContinueStatement();
		IAssignment assignment = assign(variableReference(loopVarA), constant("0"));
		List<IStatement> step = Lists.newArrayList(assignment);
		ForLoop forLoop = new ForLoop();
		forLoop.setBody(Lists.newArrayList(continueStmt));
		forLoop.setStep(step);

		List<IStatement> normalized = forLoop.accept(visitor, context);

		assertThat(normalized.size(), is(1));
		assertThat(normalized.get(0), instanceOf(IWhileLoop.class));
		List<IStatement> whileBody = ((IWhileLoop) normalized.get(0)).getBody();
		int whileBodySize = whileBody.size();
		assertThat(whileBodySize, is(3));
		assertThat(whileBody.subList(0, 1), is(step));
		assertThat(whileBody.get(1), is(continueStmt));
		assertThat(whileBody.subList(whileBodySize - step.size(), whileBodySize), is(step));
	}
	
	@Test
	public void testContinueInsideBodyStatement() {
		ContinueStatement continueStmt = new ContinueStatement();
		IAssignment assignment = assign(variableReference(loopVarA), constant("0"));
		IfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.setThen(Lists.newArrayList(continueStmt));
		
		List<IStatement> step = Lists.newArrayList(assignment);
		ForLoop forLoop = new ForLoop();
		forLoop.setBody(Lists.newArrayList(ifElseBlock));
		forLoop.setStep(step);
		
		List<IStatement> normalized = forLoop.accept(visitor, context);

		assertThat(normalized.size(), is(1));
		assertThat(normalized.get(0), instanceOf(IWhileLoop.class));
		List<IStatement> whileBody = ((IWhileLoop) normalized.get(0)).getBody();
		int whileBodySize = whileBody.size();
		assertThat(whileBodySize, is(2));
		assertThat(whileBody.get(0), instanceOf(IIfElseBlock.class));
		
		IIfElseBlock ifElseNormalized = (IIfElseBlock) whileBody.get(0);
		List<IStatement> thenNormalized = ifElseNormalized.getThen();
		List<IStatement> elseNormalized = ifElseNormalized.getElse();
		assertThat(thenNormalized.size(), is(2));
		assertThat(thenNormalized.get(0), is(step.get(0)));
		assertThat(thenNormalized.get(1), is(continueStmt));
		assertThat(elseNormalized, is(ifElseBlock.getElse()));
	}

	@Test
	public void testCascadedForLoops() {
		ILoopHeaderBlockExpression condition = loopHeader();
		IStatement stmt = new UnknownStatement();
		IVariableDeclaration varDeclA = declareVar(loopVarA);
		IAssignment assignA = assign(variableReference(loopVarA), constant("0"));
		List<IStatement> body = Lists.newArrayList(stmt);
		List<IStatement> step = Lists.newArrayList(assignA);

		ForLoop innerLoop = new ForLoop();
		innerLoop.setInit(Lists.newArrayList(varDeclA, assignA));
		innerLoop.setStep(step);
		innerLoop.setBody(body);
		innerLoop.setCondition(condition);

		IVariableDeclaration varDeclB = declareVar(loopVarB);
		IAssignment assignB = assign(variableReference(loopVarB), constant("0"));
		List<IStatement> bodyB = Lists.newArrayList(innerLoop);
		List<IStatement> stepB = Lists.newArrayList(assignB);

		ForLoop outerLoop = new ForLoop();
		outerLoop.setInit(Lists.newArrayList(varDeclB, assignB));
		outerLoop.setStep(stepB);
		outerLoop.setBody(bodyB);
		outerLoop.setCondition(condition);

		List<IStatement> normalized = outerLoop.accept(visitor, context);

		assertThat(normalized.size(), is(3));
		assertThat(normalized.get(0), is(varDeclB));
		assertThat(normalized.get(1), is(assignB));
		assertThat(normalized.get(2), instanceOf(IWhileLoop.class));

		IWhileLoop whileLoop = (IWhileLoop) normalized.get(2);
		assertThat(whileLoop.getCondition(), is(condition));
		List<IStatement> whileBody = whileLoop.getBody();
		assertThat(whileBody.size(), is(4));
		assertThat(whileBody.get(0), is(varDeclA));
		assertThat(whileBody.get(1), is(assignA));
		assertThat(whileBody.get(2), instanceOf(IWhileLoop.class));
		assertThat(whileBody.get(3), is(assignB));
	}
}
