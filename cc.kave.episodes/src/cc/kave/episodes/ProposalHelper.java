package cc.kave.episodes;

import java.util.Comparator;
import java.util.TreeSet;

import com.google.common.collect.Sets;

import cc.recommenders.datastructures.Tuple;

public class ProposalHelper {
	public static <T extends Comparable<T>> TreeSet<Tuple<T, Double>> createSortedSet() {
		final TreeSet<Tuple<T, Double>> res = Sets.newTreeSet(new Comparator<Tuple<T, Double>>() {
			@Override
			public int compare(final Tuple<T, Double> o1, final Tuple<T, Double> o2) {
				// higher probabilities will be sorted above lower ones
				int valueOrdering = Double.compare(o2.getSecond(), o1.getSecond());
				boolean areValuesEqual = valueOrdering == 0;
				if (areValuesEqual) {
					int orderOfFirstTupleMember = o1.getFirst().compareTo(o2.getFirst());
					return orderOfFirstTupleMember;
				} else {
					return valueOrdering;
				}
			}
		});
		return res;
	}

	public static <T> TreeSet<Tuple<T, Double>> createSortedSetNonAlphabet() {
		// if double values are the same, then any order is ok
		final TreeSet<Tuple<T, Double>> res = Sets.newTreeSet(new Comparator<Tuple<T, Double>>() {
			@Override
			public int compare(final Tuple<T, Double> o1, final Tuple<T, Double> o2) {
				// higher probabilities will be sorted above lower ones
				int valueOrdering = Double.compare(o2.getSecond(), o1.getSecond());
				boolean areValuesEqual = valueOrdering == 0;
				if (areValuesEqual) {
					return -1;
				} else {
					return valueOrdering;
				}
			}
		});
		return res;
	}
}