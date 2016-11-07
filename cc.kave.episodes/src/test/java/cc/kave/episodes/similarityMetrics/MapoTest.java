package cc.kave.episodes.similarityMetrics;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cc.kave.episodes.model.events.Fact;

import com.google.common.collect.Sets;

public class MapoTest {

	private Set<Fact> query;
	private Set<Fact> pattern;

	@Before
	public void setup() {
		query = Sets.newHashSet();
		pattern = Sets.newHashSet();
	}
	
	@Test
	public void emptySets() {
		double actuals = Mapo.calcMapo(query, pattern);
		
		assertTrue(actuals == 1.0);
	}
	
	@Test
	public void emptyQuery() {
		pattern = Sets.newHashSet(new Fact(1), new Fact(2), new Fact("1>2"));
		
		double actuals = Mapo.calcMapo(query, pattern);
		
		assertTrue(actuals == 0.0);
	}
	
	@Test
	public void emptyPattern() {
		query = Sets.newHashSet(new Fact(1), new Fact(2), new Fact("1>2"));
		
		double actuals = Mapo.calcMapo(query, pattern);
		
		assertTrue(actuals == 0.0);
	}

	@Test
	public void same() {
		query = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("1>2"), new Fact("1>3"), new Fact("2>3"));
		pattern = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("1>2"), new Fact("1>3"));

		double expected = fract(5.0, 6.0);
		double actuals = Mapo.calcMapo(query, pattern);

		assertTrue(expected == actuals);
	}

	@Test
	public void diffOrder() {
		query = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("1>3"), new Fact("2>3"), new Fact("1>2"));
		pattern = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("3>1"), new Fact("3>2"));

		double expected = fract(3.0, 8.0);
		double actuals = Mapo.calcMapo(query, pattern);

		assertTrue(expected == actuals);
	}

	@Test
	public void different() {
		query = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("1>2"), new Fact("1>3"), new Fact("2>3"));
		pattern = Sets.newHashSet(new Fact("1"), new Fact("2"), new Fact("3"),
				new Fact("4"), new Fact("1>2"), new Fact("1>3"),
				new Fact("1>4"), new Fact("2>4"), new Fact("3>4"));

		double expected = fract(5.0, 10.0);
		double actuals = Mapo.calcMapo(query, pattern);

		assertTrue(expected == actuals);
	}

	private Double fract(double numerator, double denominator) {
		return numerator / denominator;
	}
}
