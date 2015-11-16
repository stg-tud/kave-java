package commons.model.ssts.transformation.loops;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.transformation.loops.LoopNormalizationContext;
import cc.kave.commons.model.ssts.impl.transformation.loops.LoopNormalizationVisitor;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class ForLoopNormalizationTest {
	private LoopNormalizationVisitor visitor;
	private LoopNormalizationContext context;
	private String loopVarA;
	private String loopVarB;

	@Before
	public void setup() {
		visitor = new LoopNormalizationVisitor();
		context = new LoopNormalizationContext();
		loopVarA = "indexA";
		loopVarB = "indexB";
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

		assertTrue(normalized.size() == 3);
		assertTrue(normalized.get(0).equals(varDecl));
		assertTrue(normalized.get(1).equals(assignment));
		assertTrue(normalized.get(2) instanceof IWhileLoop);
		assertTrue(((IWhileLoop) normalized.get(2)).getCondition().equals(condition));
		assertTrue(((IWhileLoop) normalized.get(2)).getBody().equals(Lists.newArrayList(stmt, assignment)));
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

		assertTrue(normalized.size() == 3);
		assertTrue(normalized.get(0).equals(varDeclB));
		assertTrue(normalized.get(1).equals(assignB));
		assertTrue(normalized.get(2) instanceof IWhileLoop);
		
		IWhileLoop whileLoop = (IWhileLoop) normalized.get(2);
		assertTrue(whileLoop.getCondition().equals(condition));
		assertTrue(whileLoop.getBody().size() == 4);
		assertTrue(whileLoop.getBody().get(0).equals(varDeclA));
		assertTrue(whileLoop.getBody().get(1).equals(assignA));
		assertTrue(whileLoop.getBody().get(2) instanceof IWhileLoop);
		assertTrue(whileLoop.getBody().get(3).equals(assignB));
	}
}
