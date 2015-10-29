package cc.kave.commons.mining.episodes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

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
		event1.setType(mock(TypeName.class));
		Event event2 = new Event();
		event2.setKind(EventKind.INVOCATION);
		event2.setMethod(mock(MethodName.class));
		event2.setType(mock(TypeName.class));
		Event event3 = new Event();
		event3.setKind(EventKind.METHOD_START);
		event3.setMethod(mock(MethodName.class));
		event3.setType(mock(TypeName.class));
		Event event4 = new Event();
		event4.setKind(EventKind.INVOCATION);
		event4.setMethod(mock(MethodName.class));
		event4.setType(mock(TypeName.class));
		Event event5 = new Event();
		event5.setKind(EventKind.METHOD_START);
		event5.setMethod(mock(MethodName.class));
		event5.setType(mock(TypeName.class));
		
		eventMapping = new LinkedList<Event>();
		eventMapping.add(event1);
		eventMapping.add(event2);
		eventMapping.add(event3);
		eventMapping.add(event4);
		eventMapping.add(event5);
		
		sut = new EpisodeToGraphConverter();
	}
	
	@Test
	@Ignore
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
		
		String labels = "";
		
		MethodName method1 = eventMapping.get(1).getMethod();
		String out1 = toLabel(method1);
		MethodName method2 = eventMapping.get(2).getMethod();
		String out2 = toLabel(method2);
		MethodName method3 = eventMapping.get(3).getMethod();
		String out3 = toLabel(method3);
		MethodName method4 = eventMapping.get(4).getMethod();
		String out4 = toLabel(method4);
		
		labels += "1. " + out1 + "\\l";
		labels += "2. " + out2 + "\\l";
		labels += "3. " + out3 + "\\l";
		labels += "4. " + out4 + "\\l";
		
		Fact labelNode = new Fact(labels);
		expected.addVertex(labelNode);
		
		DirectedGraph<Fact, DefaultEdge> actuals = sut.convert(episode, eventMapping);
		
		assertEquals(expected.toString(), actuals.toString());
	}
	
	private String toLabel(MethodName method) {
		StringBuilder sb = new StringBuilder();

		sb.append(method.getDeclaringType().getName());
		sb.append('.');
		sb.append(method.getName());
		sb.append('(');
		boolean isFirst = true;
		for (ParameterName p : method.getParameters()) {
			if (!isFirst) {
				sb.append(", ");
			}
			isFirst = false;
			sb.append(p.getValueType().getName());
			sb.append(' ');
			sb.append(p.getName());
		}
		sb.append(')');
		sb.append(" : ");
		sb.append(method.getReturnType().getName());

		return sb.toString();
	}
}
