/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.persistence;

import java.io.FileWriter;
import java.io.IOException;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.episodes.Fact;

public class GraphExample {

	public static void main(String[] args) throws IOException {

		Fact fact1 = new Fact("1");
		Fact fact2 = new Fact("2");
		Fact fact3 = new Fact("3");
		Fact fact4 = new Fact("4");
		Fact fact5 = new Fact("5");
		Fact fact6 = new Fact("6");
		Fact fact7 = new Fact("7");
		Fact fact8 = new Fact("8");

		DirectedGraph<Fact, DefaultEdge> graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);

		graph.addVertex(fact1);
		graph.addVertex(fact2);
		graph.addVertex(fact3);
		graph.addVertex(fact4);

		graph.addVertex(fact5);
		graph.addVertex(fact6);
		graph.addVertex(fact7);
		graph.addVertex(fact8);

		graph.addEdge(fact2, fact3);
		graph.addEdge(fact3, fact4);
		graph.addEdge(fact1, fact5);
		graph.addEdge(fact4, fact5);
		graph.addEdge(fact1, fact6);
		graph.addEdge(fact1, fact7);
		graph.addEdge(fact3, fact8);
		graph.addEdge(fact4, fact8);

		VertexNameProvider<Fact> vertexName = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				return fact.getRawFact();
			}
		};

		DOTExporter<Fact, DefaultEdge> exporter = new DOTExporter<Fact, DefaultEdge>(vertexName, null, null);

		String fileIndex = "/Users/ervinacergani/Documents/PhD_work/episode-miner/graphs/testGraph.dot";

		exporter.export(new FileWriter(fileIndex), graph);

		System.out.println("Done!");
	}
}
