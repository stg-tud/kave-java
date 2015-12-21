package cc.kave.commons.model.ssts.impl.statements;

import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;

public class EventSubscriptionStatementTest {
	
	@Test
	public void testDefaultValues(){
		IEventSubscriptionStatement sut = new EventSubscriptionStatement();
		assertThat(new UnknownReference(), equalTo(sut.getReference())); 
		assertThat(EventSubscriptionOperation.Add, equalTo(sut.getOperation()));
		assertThat(new UnknownExpression(), equalTo(sut.getExpression()));
		Assert.assertFalse(0 == sut.hashCode());
		Assert.assertFalse(1 == sut.hashCode());
	}
	
	@Test
	public void testSettingValues(){
		EventSubscriptionStatement sut = new EventSubscriptionStatement();
		sut.setReference(SSTUtil.variableReference("x"));
		sut.setOperation(EventSubscriptionOperation.Remove);
		sut.setExpression(new ConstantValueExpression());
		
		assertThat(SSTUtil.variableReference("x"), equalTo(sut.getReference()));
		assertThat(new ConstantValueExpression(), equalTo(sut.getExpression()));
		assertThat(EventSubscriptionOperation.Remove, equalTo(sut.getOperation()));
	}
	
	@Test
	public void testEqualitiy_Default(){
		EventSubscriptionStatement a = new EventSubscriptionStatement();
		EventSubscriptionStatement b = new EventSubscriptionStatement();
		
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}
	
	@Test
	public void testEquality_ReallyTheSame(){
		EventSubscriptionStatement a = new EventSubscriptionStatement();
		EventSubscriptionStatement b = new EventSubscriptionStatement();
		a.setReference(SSTUtil.variableReference("x"));
		a.setOperation(EventSubscriptionOperation.Remove);
		a.setExpression(new ConstantValueExpression());
		b.setReference(SSTUtil.variableReference("x"));
		b.setOperation(EventSubscriptionOperation.Remove);
		b.setExpression(new ConstantValueExpression());
		
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}
	
	@Test
	public void testEquality_DifferentReference(){
		EventSubscriptionStatement a = new EventSubscriptionStatement();
		EventSubscriptionStatement b = new EventSubscriptionStatement();
		a.setReference(SSTUtil.variableReference("x"));
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}
	
	@Test
	public void testEquality_DifferentOperation(){
		EventSubscriptionStatement a = new EventSubscriptionStatement();
		EventSubscriptionStatement b = new EventSubscriptionStatement();
		a.setOperation(EventSubscriptionOperation.Remove);
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}
	
	@Test
	public void testEquality_DifferentExpression(){
		EventSubscriptionStatement a = new EventSubscriptionStatement();
		EventSubscriptionStatement b = new EventSubscriptionStatement();
		a.setExpression(new ConstantValueExpression());
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}
	
	@Test
	public void testVisitorIsImplemented(){
		EventSubscriptionStatement sut = new EventSubscriptionStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}
}