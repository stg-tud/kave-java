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
package cc.kave.commons.model.groum;

import org.jgrapht.graph.DefaultEdge;

public class LabelledEdge extends DefaultEdge {
	private static final long serialVersionUID = 2878169640473163752L;

	String label = "";

	/**
	 * For internal use only
	 */
	@Deprecated
	public LabelledEdge() {
	}

	public LabelledEdge(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
