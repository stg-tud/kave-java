package cc.kave.episodes.similarityMetrics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TriangleNumberTest {

	private TriangleNumber sut;
	
	@Before
	public void setup() {
		sut = new TriangleNumber();
	}
	
	@Test
	public void negative() {
		int number = -5;
		assertTrue(sut.calculate(number) == 0);
	}
	
	@Test
	public void zero() {
		int number = 0;
		assertTrue(sut.calculate(number) == 0);
	}
	
	@Test
	public void one() {
		int number = 1;
		assertTrue(sut.calculate(number) == 0);
	}
	
	@Test
	public void two() {
		int number = 2;
		assertTrue(sut.calculate(number) == 1);
	}
	
	@Test
	public void odd() {
		int number = 5;
		assertTrue(sut.calculate(number) == 10);
	}
	
	@Test
	public void even() {
		int number = 10;
		assertTrue(sut.calculate(number) == 45);
	}
}
