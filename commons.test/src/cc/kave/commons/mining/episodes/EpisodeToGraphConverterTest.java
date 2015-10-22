package cc.kave.commons.mining.episodes;

import static org.junit.Assert.*;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.mining.episodes.EpisodeToGraphConverter;
import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;

public class EpisodeToGraphConverterTest {

	private Episode episode;
	private EpisodeToGraphConverter sut;
	
	@Before
	public void setup() {
		episode = new Episode();
		episode.setFrequency(3);
		episode.setNumEvents(4);
		episode.addStringsOfFacts("a", "b", "c", "d", "a>b", "a>c", "a>d", "b>d", "c>d");
		
		sut = new EpisodeToGraphConverter();
	}
	
	@Test
	public void graphTest() {
		DirectedGraph<Fact, DefaultEdge> expected = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class); 
		expected.addVertex(new Fact("a"));
		expected.addVertex(new Fact("b"));
		expected.addVertex(new Fact("c"));
		expected.addVertex(new Fact("d"));
		
		expected.addEdge(new Fact("a"), new Fact("b"));
		expected.addEdge(new Fact("a"), new Fact("c"));
		expected.addEdge(new Fact("a"), new Fact("d"));
		expected.addEdge(new Fact("b"), new Fact("d"));
		expected.addEdge(new Fact("c"), new Fact("d"));
		
		DirectedGraph<Fact, DefaultEdge> actuals = sut.convert(episode);
		
		assertEquals(expected.toString(), actuals.toString());
	}
}
