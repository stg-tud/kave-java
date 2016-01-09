package cc.kave.commons.model.ssts.transformation.loops;

import static org.junit.Assert.assertEquals;
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
		List<IStatement> normalized = forEachLoop.accept(visitor, context);

		assertEquals(4, normalized.size());
		assertTrue(normalized.get(0) instanceof IVariableDeclaration);
		assertTrue(normalized.get(1) instanceof IAssignment);
		assertTrue(normalized.get(2) instanceof IVariableDeclaration);
		assertTrue(normalized.get(3) instanceof IWhileLoop);
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

		List<IStatement> normalized = forEachLoop.accept(visitor, context);

		assertEquals(4, normalized.size());
		assertTrue(normalized.get(0) instanceof IVariableDeclaration);

		IVariableDeclaration iteratorDecl = (IVariableDeclaration) normalized.get(0);
		ITypeName iteratorType = iteratorDecl.getType();
		assertTrue(iteratorType.hasTypeParameters());
		List<ITypeName> typeParameters = iteratorType.getTypeParameters();
		assertEquals(1, typeParameters.size());
		assertEquals(elementType, typeParameters.get(0).getTypeParameterType());
	}

	@Test
	public void testIteratorInitialization() {
		IVariableReference loopedReference = SSTUtil.variableReference("collection");
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setLoopedReference(loopedReference);
		List<IStatement> normalized = forEachLoop.accept(visitor, context);

		assertEquals(4, normalized.size());
		assertTrue(normalized.get(0) instanceof IVariableDeclaration);
		assertTrue(normalized.get(1) instanceof IAssignment);
		IVariableDeclaration declaration = (IVariableDeclaration) normalized.get(0);
		IAssignment assignment = (IAssignment) normalized.get(1);
		assertEquals(declaration.getReference(), assignment.getReference());
		assertTrue(assignment.getExpression() instanceof IInvocationExpression);

		IInvocationExpression invocation = (IInvocationExpression) assignment.getExpression();
		assertEquals(loopedReference, invocation.getReference());
		assertEquals("iterator", invocation.getMethodName().getIdentifier());
		assertTrue(invocation.getParameters().isEmpty());
	}

	@Test
	public void testElementDeclaration() {
		VariableDeclaration declaration = new VariableDeclaration();
		ITypeName elementType = TypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0");
		declaration.setType(elementType);
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setDeclaration(declaration);
		List<IStatement> normalized = forEachLoop.accept(visitor, context);

		assertEquals(4, normalized.size());
		assertTrue(normalized.get(2) instanceof IVariableDeclaration);
		assertEquals(elementType, ((IVariableDeclaration) normalized.get(2)).getType());
	}

	@Test
	public void testLoopCondition() {
		IVariableReference loopedReference = SSTUtil.variableReference("collection");
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setLoopedReference(loopedReference);
		List<IStatement> normalized = forEachLoop.accept(visitor, context);

		assertEquals(4, normalized.size());
		assertTrue(normalized.get(0) instanceof IVariableDeclaration);
		assertTrue(normalized.get(3) instanceof IWhileLoop);
		IVariableDeclaration iteratorDecl = (IVariableDeclaration) normalized.get(0);
		IWhileLoop whileLoop = (IWhileLoop) normalized.get(3);

		assertTrue(whileLoop.getCondition() instanceof LoopHeaderBlockExpression);
		List<IStatement> conditionBlock = ((LoopHeaderBlockExpression) whileLoop.getCondition()).getBody();
		assertEquals(1, conditionBlock.size());
		assertTrue(conditionBlock.get(0) instanceof IExpressionStatement);
		IAssignableExpression assignableExpression = ((IExpressionStatement) conditionBlock.get(0)).getExpression();
		assertTrue(assignableExpression instanceof IInvocationExpression);

		IInvocationExpression invocation = (IInvocationExpression) assignableExpression;
		assertTrue(invocation.getParameters().isEmpty());
		assertEquals(iteratorDecl.getReference(), invocation.getReference());
		assertEquals("hasNext", invocation.getMethodName().getIdentifier());
	}

	public void testLoopBody() {
		List<IStatement> forEachBody = new ArrayList<IStatement>();
		forEachBody.add(new Assignment());
		forEachBody.add(new ReturnStatement());
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setBody(forEachBody);
		List<IStatement> normalized = forEachLoop.accept(visitor, context);

		assertEquals(4, normalized.size());
		assertTrue(normalized.get(3) instanceof IWhileLoop);
		List<IStatement> whileBody = ((IWhileLoop) normalized.get(3)).getBody();
		int whileBodySize = whileBody.size();
		assertEquals(3, whileBodySize);
		assertEquals(forEachBody, whileBody.subList(whileBodySize - 2, whileBodySize));
	}
}
