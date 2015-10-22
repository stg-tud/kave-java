package cc.kave.commons.mining.episodes;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;

public class EpisodeToGraphConverter {

	public DirectedGraph<Fact, DefaultEdge> convert(Episode episode) {
		DirectedGraph<Fact, DefaultEdge> graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);

		for (Fact fact : episode.getFacts()) {
			if (fact.getRawFact().length() == 1) {
				graph.addVertex(fact);
			} else {
				String[] events = fact.getRawFact().split(">");
				graph.addEdge(new Fact(events[0]), new Fact(events[1]));
			}
		}
		return graph;
	}
}
