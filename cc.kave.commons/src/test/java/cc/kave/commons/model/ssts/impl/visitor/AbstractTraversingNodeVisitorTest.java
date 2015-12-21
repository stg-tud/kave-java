package cc.kave.commons.model.ssts.impl.visitor;

import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class AbstractTraversingNodeVisitorTest {

	private AbstractTraversingNodeVisitor<Integer, Void> sut;

	private IStatement stmt1;
	private IStatement stmt2;
	private IStatement stmt3;
	private ILoopHeaderExpression loopHeaderExpr;
	private ISimpleExpression expr1;

	@Before
	public void setup() {
		stmt1 = mock(BreakStatement.class);
		stmt2 = mock(ContinueStatement.class);
		stmt3 = mock(ContinueStatement.class);
		expr1 = mock(ConstantValueExpression.class);
		loopHeaderExpr = mock(LoopHeaderBlockExpression.class);
		sut = new TestVisitor();
	}

	@Test
	public void sst() {
		IDelegateDeclaration d = mock(IDelegateDeclaration.class);
		IEventDeclaration e = mock(IEventDeclaration.class);
		IFieldDeclaration f = mock(IFieldDeclaration.class);
		IMethodDeclaration m = mock(IMethodDeclaration.class);
		IPropertyDeclaration p = mock(IPropertyDeclaration.class);

		SST sst = new SST();
		sst.getDelegates().add(d);
		sst.getEvents().add(e);
		sst.getFields().add(f);
		sst.getMethods().add(m);
		sst.getProperties().add(p);

		assertVisitor(sst, d, e, f, m, p);
	}

	// ######## member declarations ###########################################

	@Test
	public void delegateDeclaration() {
		assertVisitor(new DelegateDeclaration());
	}

	@Test
	public void eventDeclaration() {
		assertVisitor(new EventDeclaration());
	}

	@Test
	public void fieldDeclaration() {
		assertVisitor(new FieldDeclaration());
	}

	@Test
	public void methodDeclaration() {
		MethodDeclaration decl = new MethodDeclaration();
		decl.getBody().add(stmt1);
		assertVisitor(decl, stmt1);
	}

	@Test
	public void propertyDeclaration() {
		PropertyDeclaration decl = new PropertyDeclaration();
		decl.getGet().add(stmt1);
		decl.getSet().add(stmt2);
		assertVisitor(decl, stmt1, stmt2);
	}

	// ######## blocks ###########################################

	@Test
	public void doLoop() {
		DoLoop node = new DoLoop();
		node.getBody().add(stmt1);
		node.setCondition(loopHeaderExpr);
		assertVisitor(node, loopHeaderExpr, stmt1);
	}

	@Test
	public void forEachLoop() {
		IVariableDeclaration decl = mock(IVariableDeclaration.class);
		IVariableReference ref = mock(IVariableReference.class);
		ForEachLoop node = new ForEachLoop();
		node.setDeclaration(decl);
		node.setLoopedReference(ref);
		node.getBody().add(stmt1);
		assertVisitor(node, decl, ref, stmt1);
	}

	@Test
	public void forLoop() {
		ForLoop node = new ForLoop();
		node.getInit().add(stmt1);
		node.setCondition(loopHeaderExpr);
		node.getStep().add(stmt2);
		node.getBody().add(stmt3);
		assertVisitor(node, stmt1, loopHeaderExpr, stmt2, stmt3);
	}

	@Test
	public void IfElseBlock() {
		IfElseBlock node = new IfElseBlock();
		node.setCondition(expr1);
		node.getThen().add(stmt1);
		node.getElse().add(stmt2);
		assertVisitor(node, expr1, stmt1, stmt2);
	}

	@Test
	public void LockBlock() {
	}

	@Test
	public void SwitchBlock() {
	}

	@Test
	public void TryBlock() {
	}

	@Test
	public void UncheckedBlock() {
	}

	@Test
	public void UnsafeBlock() {
	}

	@Test
	public void UsingBlock() {
	}

	@Test
	public void WhileLoop() {
	}

	// ######## statements ###########################################
	// ######## references ###########################################

	// ######## test helper ###########################################

	private void assertVisitor(ISSTNode node, ISSTNode... ns) {
		Void res = node.accept(sut, 12);

		Assert.assertNull(res);

		for (ISSTNode n : ns) {
			Mockito.verify(n).accept(sut, 12);
		}
	}

	public static class TestVisitor extends AbstractTraversingNodeVisitor<Integer, Void> {
	}
}