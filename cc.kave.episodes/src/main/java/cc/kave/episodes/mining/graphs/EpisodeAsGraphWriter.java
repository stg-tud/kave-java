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
package cc.kave.episodes.mining.graphs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.episodes.Fact;

public class EpisodeAsGraphWriter {

	public void write(DirectedGraph<Fact, DefaultEdge> graph, String fileName) throws IOException {

		VertexNameProvider<Fact> vertexId = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				if (fact.isLabel()) {
					return "<LabelNode>";
				} else {
					return Integer.toString(fact.getFactID());
				}
			}
		};

		VertexNameProvider<Fact> vertexName = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				return fact.toString();
			}
		};

		ComponentAttributeProvider<Fact> vertexFormat = new ComponentAttributeProvider<Fact>() {
			public Map<String, String> getComponentAttributes(Fact fact) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				if (fact.isLabel()) {
					map.put("shape", "rectangular");
				}
				return map;
			}

		};

		DOTExporter<Fact, DefaultEdge> exporter = new DOTExporter<Fact, DefaultEdge>(vertexId, vertexName, null,
				vertexFormat, null);

		exporter.export(new FileWriter(fileName), graph);
	}
}
