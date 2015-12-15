package cc.kave.commons.model.groum.comparator;

import java.util.Set;

import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class ExasVector implements Comparable<ExasVector> {

	private SortedMultiset<Path> nPaths;
	private SortedMultiset<PQNode> pqNodes;

	public ExasVector(SortedMultiset<Path> nPaths, SortedMultiset<PQNode> pqNodes) {
		this.nPaths = nPaths;
		this.pqNodes = pqNodes;
	}

	public SortedMultiset<Path> getNPaths() {
		return nPaths;
	}

	public SortedMultiset<PQNode> getPQNodes() {
		return pqNodes;
	}

	@Override
	public int compareTo(ExasVector other) {
		int compareNPaths = compare(nPaths, other.getNPaths());
		if (compareNPaths != 0) {
			return compareNPaths;
		}
		int comparePQNodes = compare(pqNodes, other.getPQNodes());
		if (comparePQNodes != 0) {
			return comparePQNodes;
		}
		return 0;
	}

	private static <T extends Comparable<T>> int compare(SortedMultiset<T> s1, SortedMultiset<T> s2) {
		int sizeCompare = Integer.compare(s1.size(), s2.size());
		if (sizeCompare != 0) {
			return sizeCompare;
		}
		Set<T> o1UniquePaths = s1.elementSet();
		Set<T> o2UniquePaths = s2.elementSet();
		int uniqueSizeCompare = Integer.compare(o1UniquePaths.size(), o2UniquePaths.size());
		if (uniqueSizeCompare != 0) {
			return uniqueSizeCompare;
		}
		TreeMultiset<T> pqNodes2 = TreeMultiset.create(s1);
		TreeMultiset<T> otherPQNodes2 = TreeMultiset.create(s2);

		while (!pqNodes2.isEmpty()) {
			Entry<T> o1First = pqNodes2.pollFirstEntry();
			Entry<T> o2First = otherPQNodes2.pollFirstEntry();
			int entryCompare = compare(o1First, o2First);
			if (entryCompare != 0) {
				return entryCompare;
			}
		}
		return 0;
	}

	private static <T extends Comparable<T>> int compare(Entry<T> e1, Entry<T> e2) {
		int firstCompare = e1.getElement().compareTo(e2.getElement());
		if (firstCompare != 0) {
			return firstCompare;
		}
		int firstCountCompare = Integer.compare(e1.getCount(), e2.getCount());
		if (firstCountCompare != 0) {
			return firstCountCompare;
		}
		return 0;
	}
}