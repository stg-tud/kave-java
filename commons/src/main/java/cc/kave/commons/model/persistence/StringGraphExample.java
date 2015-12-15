package cc.kave.commons.model.persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class StringGraphExample {

	public static void main(String[] args) throws IOException {

		String fact1 = "\"Context.Init<>()\"";
		String fact2 = "\"Context.lookup()\"";
		String fact3 = "\"DataSource.getConnection()\"";
		String fact4 = "\"Connection.prepareStatement()\"";
		String fact5 = "\"PreparedStatement.setInt()\"";
		String fact6 = "\"PreparedStatement.setString()\"";
		String fact7 = "\"PreparedStatement.executeUpdate()\"";
		String fact8 = "\"PreparedStatement.close()\"";
		String fact9 = "\"Connection.close()\"";

		DirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

		graph.addVertex(fact1);
		graph.addVertex(fact2);
		graph.addVertex(fact3);
		graph.addVertex(fact4);
		graph.addVertex(fact5);
		graph.addVertex(fact6);
		graph.addVertex(fact7);
		graph.addVertex(fact8);
		graph.addVertex(fact9);

		graph.addEdge(fact1, fact2);
		graph.addEdge(fact2, fact3);
		graph.addEdge(fact3, fact4);
		graph.addEdge(fact4, fact5);
		graph.addEdge(fact4, fact6);
		graph.addEdge(fact5, fact7);
		graph.addEdge(fact6, fact7);
		graph.addEdge(fact7, fact8);
		graph.addEdge(fact8, fact9);

		VertexNameProvider<String> vertexName = new VertexNameProvider<String>() {
			public String getVertexName(String fact) {
				return fact;
			}
		};

		ComponentAttributeProvider<String> vertexFormat = new ComponentAttributeProvider<String>() {
			public Map<String, String> getComponentAttributes(String fact) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				map.put("shape", "rectangular");
				return map;
			}
		};

		DOTExporter<String, DefaultEdge> exporter = new DOTExporter<String, DefaultEdge>(vertexName, null, null,
				vertexFormat, null);

		String fileIndex = "/Users/ervinacergani/Documents/PhD_work/episode-miner/graphs/testGraph.dot";

		exporter.export(new FileWriter(fileIndex), graph);

		System.out.println("Done!");
	}
}
