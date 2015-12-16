package cc.kave.commons.mining.episodes;

import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;

public class EpisodeToGraphConverter {

	public DirectedGraph<Fact, DefaultEdge> convert(Episode episode, List<Event> eventMapping) {
		DirectedGraph<Fact, DefaultEdge> graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);

		String labels = "";

		for (Fact fact : episode.getFacts()) {
			if (!fact.getRawFact().contains(">")) {
				graph.addVertex(fact);
			} else {
				String[] events = fact.getRawFact().split(">");
				graph.addEdge(new Fact(events[0]), new Fact(events[1]));
			}
		}
		TopologicalOrderIterator<Fact, DefaultEdge> toi = new TopologicalOrderIterator<>(graph);
		while (toi.hasNext()) {
			Fact fact = toi.next();
			int index = Integer.parseInt(fact.getRawFact());
			IMethodName method = eventMapping.get(index).getMethod();
			EventKind kind = eventMapping.get(index).getKind();

			String out = toLabel(method);
			labels += fact.getRawFact() + ". " + kind.toString() + ": " + out + "\\l";
		}
		Fact labelFact = new Fact(labels);
		graph.addVertex(labelFact);

		return graph;
	}

	private String toLabel(IMethodName method) {
		StringBuilder sb = new StringBuilder();

		sb.append(method.getDeclaringType().getName());
		sb.append('.');
		sb.append(method.getName());
		sb.append('(');
		boolean isFirst = true;
		for (IParameterName p : method.getParameters()) {
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
