package cc.kave.commons.model.groum.comparator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cc.kave.commons.model.groum.Node;

class Path implements Comparable<Path> {
	private final List<Node> path;

	public Path(Node... nodes) {
		this(Arrays.asList(nodes));
	}

	private Path(List<Node> path) {
		this.path = path;
	}

	public Path getExtension(Node node) {
		List<Node> extension = new LinkedList<>(path);
		extension.add(node);
		return new Path(extension);
	}

	public Path getTail() {
		LinkedList<Node> tail = new LinkedList<>(path);
		if (!tail.isEmpty())
			tail.removeFirst();
		return new Path(tail);
	}

	public boolean isEmpty() {
		return path.isEmpty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.stream().mapToInt(node -> node.getId().hashCode()).sum());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		return compareTo(other) == 0;
	}

	@Override
	public int compareTo(Path other) {
		int sizeCompare = Integer.compare(path.size(), other.path.size());
		if (sizeCompare != 0) {
			return sizeCompare;
		}
		for (int i = 0; i < path.size(); i++) {
			int nodeCompare = path.get(i).compareTo(other.path.get(i));
			if (nodeCompare != 0) {
				return nodeCompare;
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		return "(" + StringUtils.join(path, "-") + ")";
	}
}