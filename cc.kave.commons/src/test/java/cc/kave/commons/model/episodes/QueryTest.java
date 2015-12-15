package cc.kave.commons.model.episodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class QueryTest {

	private Query sut;
	
	@Before
	public void setup() {
		sut = new Query();
	}
	
	@Test
	public void defaultValues() {
		assertEquals(Lists.newLinkedList(), sut.getFacts());
		assertEquals(0, sut.getNumberOfFacts());
	}
	
	@Test
	public void valuesCanBeSet() {
		sut.addFact("a");
		assertEquals(Lists.newArrayList(new Fact("a")), sut.getFacts());
		sut.setNumberOfFacts(1);
		assertEquals(1, sut.getNumberOfFacts());
	}
	
	@Test
	public void addMultipleFacts() {
		sut.addStringsOfFacts("a", "b");
		sut.setNumberOfFacts(2);
		assertEquals(Lists.newArrayList(new Fact("a"), new Fact("b")), sut.getFacts());
		assertTrue(sut.getNumberOfFacts() == 2);
	}
	
	@Test
	public void equality_default() {
		Query a = new Query();
		Query b = new Query();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertTrue(a.equals(b));
	}
	
	@Test
	public void equality_reallyTheSame() {
		Query a = new Query();
		a.setNumberOfFacts(3);
		a.addStringsOfFacts("a", "b", "c");

		Query b = new Query();
		b.setNumberOfFacts(3);
		b.addStringsOfFacts("a", "b", "c");

		assertEquals(a, b);
		assertTrue(a.equals(b));
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getFacts(), b.getFacts());
		assertEquals(a.getNumberOfFacts(), b.getNumberOfFacts());
	}
	
	@Test
	public void differentQueries() {
		Query a = new Query();
		a.setNumberOfFacts(3);
		a.addStringsOfFacts("a", "b", "c");

		Query b = new Query();
		b.setNumberOfFacts(2);
		b.addStringsOfFacts("d", "e");
		
		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.getFacts(), b.getFacts());
		assertNotEquals(a.getNumberOfFacts(), b.getNumberOfFacts());
	}
}
