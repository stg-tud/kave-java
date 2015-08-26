package cc.kave.commons.model.groum.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.ISubGroum;

public class SubGroum extends Groum implements ISubGroum {
	protected IGroum parent;

	public SubGroum(IGroum parent) {
		super();
		this.parent = parent;
	}

	public SubGroum() {
		super();
		this.parent = null;
	}

	public void setParent(IGroum parent) {
		this.parent = parent;
	}

	@Override
	public IGroum getParent() {
		return parent;
	}

	@Deprecated
	public ISubGroum extensibleWithOld(ISubGroum groum) {
		INode leaf = getLeaf();
		INode root = groum.getRoot();
		Set<INode> successors = parent.getSuccessors(leaf);
		if (successors.size() == 0)
			return null;
		else {
			if (successors.iterator().next().equals(root)) {
				ISubGroum extendedSubgroum = (ISubGroum) this.clone();
				extendedSubgroum.addVertex(root);
				extendedSubgroum.addEdge(leaf, root);
				return extendedSubgroum;
			} else {
				return null;
			}
		}

	}
	
	@Override
	public List<ISubGroum> extensibleWithMultiple(ISubGroum groum) {
		INode extendingNode = groum.getAllNodes().iterator().next();

		if (extendingNode == null)
			return null;

		List<ISubGroum> extendedGroums = new LinkedList<>();
		List<INode> extendingNodes = new LinkedList<>();

		for (INode node : this.getAllNodes()) {
			Set<INode> successors = parent.getSuccessors(node);
			if (successors.size() == 0)
				continue;
			else {
				for (INode candidate : successors) {
					if (!(this.containsNode(candidate)))
						if (candidate.equals(extendingNode)) {
							if (extendingNodes.isEmpty())
								extendingNodes.add(candidate);
							else {						
								boolean isnewnode = true;
								for (INode extnode: extendingNodes) {
									if (extnode == candidate) {
										isnewnode=false;
										continue;																			
									}
								}
								if (isnewnode)
									extendingNodes.add(candidate);
							}							
						}
				}
			}
		}
		// Build extended groums
		for (INode extnode: extendingNodes) {
			ISubGroum extendedSubgroum = (ISubGroum) this.clone();
			extendedSubgroum.addVertex(extnode);
			for (INode node: this.getAllNodes()) {
				Set<INode>nodesuccessors = parent.getSuccessors(node);
				for (INode successor: nodesuccessors) {
					if (successor == extnode) {
						extendedSubgroum.addEdge(node, extnode);
						continue;
					}					
				}				
			}
			extendedGroums.add(extendedSubgroum);
		}		
		
		if (extendedGroums.isEmpty()) 
			return null;
		else
			return extendedGroums;
	}
	

	@Override
	public ISubGroum extensibleWith(ISubGroum groum) {
		INode extendingNode = groum.getAllNodes().iterator().next();

		if (extendingNode == null)
			return null;

		ISubGroum extendedSubgroum = (ISubGroum) this.clone();
		boolean extended = false;
		for (INode node : this.getAllNodes()) {

			Set<INode> successors = parent.getSuccessors(node);
			if (successors.size() == 0)
				continue;
			else {
				for (INode candidate : successors) {
					if (!(this.containsNode(candidate)))
						if (candidate.equals(extendingNode)) {
							if (!extendedSubgroum.containsNode(candidate)) {
								extendedSubgroum.addVertex(candidate);
								extended = true;
							}
							extendedSubgroum.addEdge(node, candidate);							
						} else {
							continue;
						}
				}
			}
		}
		if (extended) 
			return extendedSubgroum;
		else
			return null;
	}
	
	
	public ISubGroum extensibleWithGreedy(ISubGroum groum) {
		INode extendingNode = groum.getAllNodes().iterator().next();

		if (extendingNode == null)
			return null;

		for (INode node : this.getAllNodes()) {

			Set<INode> successors = parent.getSuccessors(node);
			if (successors.size() == 0)
				continue;
			else {
				for (INode candidate : successors) {
					if (!(this.containsNode(candidate)))
						if (candidate.equals(extendingNode)) {
							ISubGroum extendedSubgroum = (ISubGroum) this.clone();
							extendedSubgroum.addVertex(candidate);
							extendedSubgroum.addEdge(node, candidate);
							return extendedSubgroum;
						} else {
							continue;
						}
				}
			}
		}
		return null;
	}

	@Deprecated
	public ISubGroum extensibleWithOneEdge(ISubGroum groum) {
		INode leaf = getLeaf();

		INode extendingNode = groum.getAllNodes().iterator().next();
		if (extendingNode == null)
			return null;

		Set<INode> successors = parent.getSuccessors(leaf);
		if (successors.size() == 0)
			return null;
		else {
			INode candidate = successors.iterator().next();
			if (candidate.equals(extendingNode)) {
				ISubGroum extendedSubgroum = (ISubGroum) this.clone();
				extendedSubgroum.addVertex(candidate);
				extendedSubgroum.addEdge(leaf, candidate);
				return extendedSubgroum;
			} else {
				return null;
			}
		}

	}

}
