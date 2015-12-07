package cc.kave.commons.model.episodes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MethodTest {

	private Method sut;
	
	@Before
	public void setup() {
		sut = new Method();
	}
	
	@Test
	public void defaultValues() {
		assertEquals("", sut.getMethodName());
		assertEquals(Lists.newLinkedList(), sut.getFacts());
		assertEquals(0, sut.getNumberOfInvocations());
	}
	
	@Test
	public void valuesCanBeSet() {
		sut.setMethodName("methodName");
		assertTrue(sut.getMethodName().equalsIgnoreCase("methodName"));
		sut.addFact("a");
		assertEquals(Lists.newArrayList(new Fact("a")), sut.getFacts());
		sut.setNumberOfInvocations(1);
		assertEquals(1, sut.getNumberOfInvocations());
	}
	
	@Test
	public void addMultipleFacts() {
		sut.setMethodName("methodName");
		sut.addStringsOfFacts("a", "b");
		sut.setNumberOfInvocations(2);
		assertTrue(sut.getMethodName().equalsIgnoreCase("methodName"));
		assertEquals(Lists.newArrayList(new Fact("a"), new Fact("b")), sut.getFacts());
		assertTrue(sut.getNumberOfInvocations() == 2);
	}
	
	@Test
	public void equality_default() {
		Method a = new Method();
		Method b = new Method();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertTrue(a.equals(b));
	}
	
	@Test
	public void equality_reallyTheSame() {
		Method a = new Method();
		a.setMethodName("M1");
		a.setNumberOfInvocations(3);
		a.addStringsOfFacts("a", "b", "c");

		Method b = new Method();
		b.setMethodName("M1");
		b.setNumberOfInvocations(3);
		b.addStringsOfFacts("a", "b", "c");

		assertEquals(a, b);
		assertTrue(a.equals(b));
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getFacts(), b.getFacts());
		assertTrue(a.getMethodName().equalsIgnoreCase(b.getMethodName()));
		assertEquals(a.getNumberOfInvocations(), b.getNumberOfInvocations());
	}
	
	@Test
	public void equality_differentMethodNames() {
		Method a = new Method();
		a.setMethodName("M1");
		a.setNumberOfInvocations(3);
		a.addStringsOfFacts("a", "b", "c");

		Method b = new Method();
		b.setMethodName("M2");
		b.setNumberOfInvocations(3);
		b.addStringsOfFacts("a", "b", "c");

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getFacts(), b.getFacts());
		assertFalse(a.getMethodName().equalsIgnoreCase(b.getMethodName()));
		assertEquals(a.getNumberOfInvocations(), b.getNumberOfInvocations());
	}
	
	@Test
	public void equality_differentInvocations() {
		Method a = new Method();
		a.setMethodName("M1");
		a.setNumberOfInvocations(3);
		a.addStringsOfFacts("a", "b", "c");

		Method b = new Method();
		b.setMethodName("M1");
		b.setNumberOfInvocations(2);
		b.addStringsOfFacts("d", "e");

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.getFacts(), b.getFacts());
		assertTrue(a.getMethodName().equalsIgnoreCase(b.getMethodName()));
		assertNotEquals(a.getNumberOfInvocations(), b.getNumberOfInvocations());
	}
}
