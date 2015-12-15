package cc.kave.commons.model.ssts.transformation.loops;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.statements.IAssignment;

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

		assertEquals(3, normalized.size());
		assertEquals(init.get(0), normalized.get(0));
		assertEquals(init.get(1), normalized.get(1));
		assertTrue(normalized.get(2) instanceof IWhileLoop);
	}

	@Test
	public void testNoLoopInit() {
		ForLoop forLoop = new ForLoop();
		List<IStatement> normalized = forLoop.accept(visitor, context);

		assertEquals(1, normalized.size());
		assertTrue(normalized.get(0) instanceof IWhileLoop);
	}

	@Test
	public void testLoopStep() {
		List<IStatement> step = Lists.newArrayList(assign(variableReference(loopVarA), constant("0")));
		ForLoop forLoop = new ForLoop();
		forLoop.setStep(step);

		List<IStatement> normalized = forLoop.accept(visitor, context);

		assertEquals(1, normalized.size());
		assertTrue(normalized.get(0) instanceof IWhileLoop);
		List<IStatement> whileBody = ((IWhileLoop) normalized.get(0)).getBody();
		assertEquals(step.size(), whileBody.size());
		assertEquals(step, whileBody.subList(whileBody.size() - 1, whileBody.size()));
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

		assertEquals(3, normalized.size());
		assertEquals(varDecl, normalized.get(0));
		assertEquals(assignment, normalized.get(1));
		assertTrue(normalized.get(2) instanceof IWhileLoop);
		assertEquals(condition, ((IWhileLoop) normalized.get(2)).getCondition());
		assertEquals(Lists.newArrayList(stmt, assignment), ((IWhileLoop) normalized.get(2)).getBody());
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

		assertEquals(1, normalized.size());
		assertTrue(normalized.get(0) instanceof IWhileLoop);
		List<IStatement> whileBody = ((IWhileLoop) normalized.get(0)).getBody();
		int whileBodySize = whileBody.size();
		assertEquals(3, whileBodySize);
		assertEquals(step, whileBody.subList(0, 1));
		assertEquals(continueStmt, whileBody.get(1));
		assertEquals(step, whileBody.subList(whileBodySize - 1, whileBodySize));
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

		assertEquals(1, normalized.size());
		assertTrue(normalized.get(0) instanceof IWhileLoop);
		List<IStatement> whileBody = ((IWhileLoop) normalized.get(0)).getBody();
		int whileBodySize = whileBody.size();
		assertEquals(2, whileBodySize);
		assertTrue(whileBody.get(0) instanceof IIfElseBlock);
		
		IIfElseBlock ifElseNormalized = (IIfElseBlock) whileBody.get(0);
		List<IStatement> thenNormalized = ifElseNormalized.getThen();
		List<IStatement> elseNormalized = ifElseNormalized.getElse();
		assertEquals(2, thenNormalized.size());
		assertEquals(step.get(0), thenNormalized.get(0));
		assertEquals(continueStmt, thenNormalized.get(1));
		assertEquals(ifElseBlock.getElse(), elseNormalized);
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

		assertEquals(3, normalized.size());
		assertEquals(varDeclB, normalized.get(0));
		assertEquals(assignB, normalized.get(1));
		assertTrue(normalized.get(2) instanceof IWhileLoop);

		IWhileLoop whileLoop = (IWhileLoop) normalized.get(2);
		assertEquals(condition, whileLoop.getCondition());
		assertEquals(4, whileLoop.getBody().size());
		assertEquals(varDeclA, whileLoop.getBody().get(0));
		assertEquals(assignA, whileLoop.getBody().get(1));
		assertTrue(whileLoop.getBody().get(2) instanceof IWhileLoop);
		assertEquals(assignB, whileLoop.getBody().get(3));
	}
}
