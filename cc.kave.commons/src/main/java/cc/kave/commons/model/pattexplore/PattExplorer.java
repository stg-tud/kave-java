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
package cc.kave.commons.model.pattexplore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.Node;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.SubGroumMultiSet;

public class PattExplorer {
	int threshold;

	public PattExplorer(int threshold) {
		this.threshold = threshold;
	}

	public Set<IGroum> explore(Iterable<Groum> D) {
		SubGroumMultiSet atoms = getFrequentAtomicSubGroums(D);
		Set<Node> atomNodes = getPatternRoots(atoms);
		SubGroumMultiSet explored = explore(atoms, atomNodes);
		return explored.getPatterns();
	}

	private SubGroumMultiSet getFrequentAtomicSubGroums(Iterable<Groum> D) {
		return getAllAtomicSubGroums(D).getFrequentSubSet(threshold);
	}

	private SubGroumMultiSet getAllAtomicSubGroums(Iterable<Groum> D) {
		SubGroumMultiSet L = new SubGroumMultiSet();
		for (Groum groum : D) {
			L.addAll(SubGroum.getAtomicSubGroums(groum));
		}
		return L;
	}

	private Set<Node> getPatternRoots(SubGroumMultiSet s) {
		Set<Node> roots = new HashSet<>();
		for (IGroum pattern : s.getPatterns()) {
			roots.add(pattern.getRoot());
		}
		return roots;
	}

	private SubGroumMultiSet explore(SubGroumMultiSet extensiblePatterns, Set<Node> atomNodes) {
		SubGroumMultiSet explored = new SubGroumMultiSet(extensiblePatterns);
		for (IGroum pattern : extensiblePatterns.getPatterns()) {
			explored.addAll(explore(pattern, extensiblePatterns, atomNodes));
		}
		return explored;
	}

	private SubGroumMultiSet explore(IGroum base, SubGroumMultiSet patterns, Set<Node> atomNodes) {
		Set<SubGroum> baseInstances = patterns.getPatternInstances(base);
		SubGroumMultiSet newPatterns = new SubGroumMultiSet();
		for (Node atomNode : atomNodes) {
			SubGroumMultiSet frequentExtensions = computeFrequentExtensions(baseInstances, atomNode);
			newPatterns.addAll(frequentExtensions);
			newPatterns.addAll(explore(frequentExtensions, atomNodes));
		}
		return newPatterns;
	}

	private SubGroumMultiSet computeFrequentExtensions(Set<SubGroum> baseInstances, Node atomNode) {
		SubGroumMultiSet extensions = computeExtensions(baseInstances, atomNode);
		return extensions.getFrequentSubSet(threshold);
	}

	private SubGroumMultiSet computeExtensions(Set<SubGroum> instances, Node extension) {
		SubGroumMultiSet extensions = new SubGroumMultiSet();
		for (SubGroum occurrence : instances) {
			List<SubGroum> candidates = occurrence.computeExtensions(extension);
			for (SubGroum candidate : candidates) {
				extensions.add(candidate);
			}
		}
		return extensions;
	}
}
