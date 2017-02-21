package cc.kave.episodes.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.recommenders.exceptions.AssertionException;

public class ThresholdsTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private Thresholds sut;
	
	@Before 
	public void setup() {
		sut = new Thresholds(2, 0.0);
	}
	
	@Test
	public void invalidFrequency() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Not valid frequency value!");
		sut = new Thresholds(0, 0.0);
	}
	
	@Test
	public void invalidEntropy() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Entropy is a probability value!");
		sut = new Thresholds(2, 3);
	}
	
	@Test
	public void defaultValues() {
		assertTrue(sut.getFrequency() == 2);
		assertTrue(sut.getEntropy() == 0.0);
		assertTrue(sut.getNoGenPatterns() == 0);
		assertTrue(sut.getNoSpecPatterns() == 0);
		assertTrue(sut.getFraction() == 0.0);
	}
	
	@Test
	public void valuesCanBeIncreased() {
		sut.addGenPattern();
		sut.addSpecPattern();
		sut.addSpecPattern();
		sut.addGenPattern();
		sut.addGenPattern();
		
		assertTrue(sut.getNoGenPatterns() == 3);
		assertTrue(sut.getNoSpecPatterns() == 2);
		assertTrue(sut.getFraction() == (3.0 / 5.0));
	}
	
	@Test
	public void equality() {
		Thresholds a = new Thresholds(5, 0.3);
		Thresholds b = new Thresholds(5, 0.3);
		
		assertEquals(a, b);
		assertTrue(a.getFrequency() == b.getFrequency());
		assertTrue(a.getEntropy() == b.getEntropy());
		assertTrue(a.getNoGenPatterns() == b.getNoGenPatterns());
		assertTrue(a.getNoSpecPatterns() == b.getNoSpecPatterns());
		assertTrue(a.getFraction() == b.getFraction());
	}
	
	@Test
	public void different() {
		Thresholds a = new Thresholds(7, 0.5);
		Thresholds b = new Thresholds(4, 1.0);
		
		assertNotEquals(a, b);
		assertTrue(a.getFrequency() != b.getFrequency());
		assertTrue(a.getEntropy() != b.getEntropy());
		assertTrue(a.getNoGenPatterns() == b.getNoGenPatterns());
		assertTrue(a.getNoSpecPatterns() == b.getNoSpecPatterns());
		assertTrue(a.getFraction() == b.getFraction());
	}
	
	@Test
	public void different_freq() {
		Thresholds a = new Thresholds(7, 0.5);
		Thresholds b = new Thresholds(4, 0.5);
		
		assertNotEquals(a, b);
		assertTrue(a.getFrequency() != b.getFrequency());
		assertTrue(a.getEntropy() == b.getEntropy());
		assertTrue(a.getNoGenPatterns() == b.getNoGenPatterns());
		assertTrue(a.getNoSpecPatterns() == b.getNoSpecPatterns());
		assertTrue(a.getFraction() == b.getFraction());
	}
	
	@Test
	public void different_entropy() {
		Thresholds a = new Thresholds(4, 0.5);
		Thresholds b = new Thresholds(4, 1.0);
		
		assertNotEquals(a, b);
		assertTrue(a.getFrequency() == b.getFrequency());
		assertTrue(a.getEntropy() != b.getEntropy());
		assertTrue(a.getNoGenPatterns() == b.getNoGenPatterns());
		assertTrue(a.getNoSpecPatterns() == b.getNoSpecPatterns());
		assertTrue(a.getFraction() == b.getFraction());
	}
}
