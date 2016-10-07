package cc.kave.episodes.similarityMetrics;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cc.kave.episodes.model.Episode;

public class F1FactsTest {

	private F1Facts sut;
	
	@Before
	public void setup() {
		sut = new F1Facts();
	}
	
	@Test
	public void empty() {
		Episode query = new Episode();
		Episode pattern = new Episode();
		
		double expected = 0.0;
		double actuals = sut.calcF1Facts(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void emptyQuery() {
		Episode query = new Episode();
		Episode pattern = createEpisode("1", "2", "3", "1>2", "1>3");
		
		double expected = 0.0;
		double actuals = sut.calcF1Facts(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void emptyPattern() {
		Episode query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");
		Episode pattern = new Episode();
		
		double expected = 0.0;
		double actuals = sut.calcF1Facts(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void same() {
		Episode query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");
		Episode pattern = createEpisode("1", "2", "3", "1>2", "1>3");
		
		double expected = 1.0;
		double actuals = sut.calcF1Facts(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void different1() {
		Episode query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");
		Episode pattern = createEpisode("1", "2", "3", "4", "1>2", "1>3", "1>4", "2>4", "3>4");
		
		double expected = 5.0 / 7.0;
		double actuals = sut.calcF1Facts(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void different2() {
		Episode query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");
		Episode pattern = createEpisode("1", "2", "3", "3>1", "3>2", "1>2");
		
		double expected = 2.0 / 3.0;
		double actuals = sut.calcF1Facts(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void different3() {
		Episode query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");
		Episode pattern = createEpisode("1", "2", "3", "4", "3>1", "3>2", "3>4", "1>4", "2>4");
		
		double expected = 3.0 / 7.0;
		double actuals = sut.calcF1Facts(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void different4() {
		Episode query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");
		Episode pattern = createEpisode("1", "3", "4", "1>3", "1>4");
		
		double expected = 6.0 / 11.0;
		double actuals = sut.calcF1Facts(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	@Test
	public void oneNodeEpisode() {
		Episode query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");
		Episode pattern = createEpisode("2");
		
		double expected = 2.0 / 7.0;
		double actuals = sut.calcF1Facts(query, pattern);
		
		assertTrue(expected == actuals);
	}
	
	private Episode createEpisode(String... strings) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(strings);
		return episode;
	}
}
