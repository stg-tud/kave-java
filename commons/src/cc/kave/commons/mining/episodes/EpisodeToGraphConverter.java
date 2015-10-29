package cc.kave.commons.mining.episodes;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;

public class EpisodeToGraphConverter {

	public DirectedGraph<Fact, DefaultEdge> convert(Episode episode, List<Event> eventMapping) {
		DirectedGraph<Fact, DefaultEdge> graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);

		List<Fact> labelList = new LinkedList<Fact>();

		for (Fact fact : episode.getFacts()) {
			if (!fact.getRawFact().contains(">")) {
				graph.addVertex(fact);

				int index = Integer.parseInt(fact.getRawFact());
				MethodName method = eventMapping.get(index).getMethod();

				String out = toLabel(method);
				String vertexLabel = "<" + fact.getRawFact() + ". " + out + ">";
				System.out.println(out);
				Fact f = new Fact(vertexLabel);
				labelList.add(f);
			} else {
				String[] events = fact.getRawFact().split(">");
				graph.addEdge(new Fact(events[0]), new Fact(events[1]));
			}
		}
		graph.addVertex(labelList.get(0));
		if (labelList.size() > 1) {
			for (int i = 1; i < labelList.size(); i++) {
				graph.addVertex(labelList.get(i));
				graph.addEdge(labelList.get(i - 1), labelList.get(i));
			}
		}
		return graph;
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
