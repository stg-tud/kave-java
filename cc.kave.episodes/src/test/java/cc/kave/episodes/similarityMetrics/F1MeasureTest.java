package cc.kave.episodes.similarityMetrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cc.kave.episodes.model.events.Fact;

import com.google.common.collect.Sets;

public class F1MeasureTest {

	private Set<Fact> query;
	private Set<Fact> pattern;
	
	@Before
	public void setup() {
		query = Sets.newHashSet();
		pattern = Sets.newHashSet();
	}
	
	@Test
	public void empty() {
		double actuals = F1Measure.calcF1(query, pattern);
		
		assertTrue(actuals == 1.0);
	}
	
	@Test
	public void emptyPattern() {
		query = Sets.newHashSet(new Fact(1), new Fact(2), new Fact("1>2"));
		
		double actuals = F1Measure.calcF1(query, pattern);
		
		assertTrue(actuals == 0.0);
	}
	
	@Test
	public void emptyQuery() {
		pattern = Sets.newHashSet(new Fact(1), new Fact(2), new Fact("1>2"));
		
		double actuals = F1Measure.calcF1(query, pattern);
		
		assertTrue(actuals == 0.0);
	}
	
	@Test
	public void sameFacts() {
		query = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("1>2"), new Fact("1>3"), new Fact("2>3"));
		pattern = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("1>2"), new Fact("1>3"));
		
		double expected = fract(10.0, 11.0);
		
		double actuals = F1Measure.calcF1(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void diffOrder() {
		query = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("1>3"), new Fact("2>3"), new Fact("1>2"));
		pattern = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("3>1"), new Fact("3>2"));
		
		double expected = fract(6.0, 11.0);
		
		double actuals = F1Measure.calcF1(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void different() {
		query = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("1>2"), new Fact("1>3"), new Fact("2>3"));
		pattern = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("4"), new Fact("1>2"), new Fact("1>3"),
				new Fact("1>4"), new Fact("2>4"), new Fact("3>4"));
		
		double expected = fract(2.0, 3.0);
		
		double actuals = F1Measure.calcF1(query, pattern);
		
		assertEquals(expected, actuals, 0.001);
	}
	
	private double fract(double numerator, double denominator) {
		return numerator / denominator;
	}
}
