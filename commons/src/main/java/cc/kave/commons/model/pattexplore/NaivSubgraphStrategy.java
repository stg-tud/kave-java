package cc.kave.commons.model.pattexplore;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.ISubgraphStrategy;
import cc.kave.commons.model.groum.impl.Groum;
import cc.kave.commons.model.groum.impl.SubGroum;

public class NaivSubgraphStrategy implements ISubgraphStrategy {

	@Override
	public List<ISubGroum> getIsomorphSubgraphs(IGroum graph, IGroum subgraph) {
		Groum groum = (Groum) graph;
		Groum subgroum = (Groum) subgraph;

		if (!(groum.containsEqualNode(subgraph.getRoot()))) {
			return null;
		} else {
			INode subgraphRoot = subgroum.getRoot();
			List<ISubGroum> subgroums = new LinkedList<>();

			Set<INode> equalNodes = groum.getEqualNodes(subgraphRoot);
			for (INode node : equalNodes) {
				INode candidatepointer = node;
				INode referencepointer = subgraphRoot;
				SubGroum candidatesubgroum = new SubGroum(graph);
				candidatesubgroum.addVertex(candidatepointer);
				boolean failure = false;

				while (subgroum.getSuccessors(referencepointer).size() != 0) {
					if (groum.getSuccessors(candidatepointer).size() == 0) {
						failure = true;
						break;
					}
					INode prevCandidate = candidatepointer;
					candidatepointer = groum.getSuccessors(candidatepointer).iterator().next();

					INode prevReference = referencepointer;
					referencepointer = subgroum.getSuccessors(referencepointer).iterator().next();

					if (!(candidatepointer.equals(referencepointer))) {
						failure = true;
						break;
					} else {
						candidatesubgroum.addVertex(candidatepointer);
						candidatesubgroum.addEdge(prevCandidate, candidatepointer);
					}
				}
				if (!failure) {
					subgroums.add(candidatesubgroum);
				}

			}
			return subgroums;
		}

	}
}
