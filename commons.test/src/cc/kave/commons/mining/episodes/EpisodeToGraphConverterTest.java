package cc.kave.commons.mining.episodes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.MethodName;

public class EpisodeToGraphConverterTest {

	private Episode episode;
	private List<Event> eventMapping;
	private EpisodeToGraphConverter sut;
	
	@Before
	public void setup() {
		episode = new Episode();
		episode.setFrequency(3);
		episode.setNumEvents(4);
		episode.addStringsOfFacts("1", "2", "3", "4", "1>2", "1>3", "1>4", "2>4", "3>4");
		
		Event event1 = new Event();
		event1.setMethod(mock(MethodName.class));
		Event event2 = new Event();
		event2.setKind(EventKind.INVOCATION);
		event2.setMethod(mock(MethodName.class));
		Event event3 = new Event();
		event3.setKind(EventKind.METHOD_START);
		event3.setMethod(mock(MethodName.class));
		Event event4 = new Event();
		event4.setKind(EventKind.INVOCATION);
		event4.setMethod(mock(MethodName.class));
		Event event5 = new Event();
		event5.setKind(EventKind.METHOD_START);
		event5.setMethod(mock(MethodName.class));
		
		eventMapping = new LinkedList<Event>();
		eventMapping.add(event1);
		eventMapping.add(event2);
		eventMapping.add(event3);
		eventMapping.add(event4);
		eventMapping.add(event5);
		
		sut = new EpisodeToGraphConverter();
	}
	
	@Test
	public void graphTest() {
		DirectedGraph<Fact, DefaultEdge> expected = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class); 
		expected.addVertex(new Fact("1"));
		expected.addVertex(new Fact("2"));
		expected.addVertex(new Fact("3"));
		expected.addVertex(new Fact("4"));
		
		expected.addEdge(new Fact("1"), new Fact("2"));
		expected.addEdge(new Fact("1"), new Fact("3"));
		expected.addEdge(new Fact("1"), new Fact("4"));
		expected.addEdge(new Fact("2"), new Fact("4"));
		expected.addEdge(new Fact("3"), new Fact("4"));
		
		Fact factlabel1 = new Fact("1. " + eventMapping.get(1).getMethod().getName());
		Fact factlabel2 = new Fact("2. " + eventMapping.get(2).getMethod().getName());
		Fact factlabel3 = new Fact("3. " + eventMapping.get(3).getMethod().getName());
		Fact factlabel4 = new Fact("4. " + eventMapping.get(4).getMethod().getName());
		
		expected.addVertex(factlabel1);
		expected.addVertex(factlabel2);
		expected.addVertex(factlabel3);
		expected.addVertex(factlabel4);
		
		expected.addEdge(factlabel1, factlabel2);
		expected.addEdge(factlabel2, factlabel3);
		expected.addEdge(factlabel3, factlabel4);
		
		DirectedGraph<Fact, DefaultEdge> actuals = sut.convert(episode, eventMapping);
		
		assertEquals(expected.toString(), actuals.toString());
	}
}
