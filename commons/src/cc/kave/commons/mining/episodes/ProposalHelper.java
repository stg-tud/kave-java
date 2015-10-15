package cc.kave.commons.mining.episodes;

import java.util.Comparator;
import java.util.TreeSet;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Episode;
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

	public static TreeSet<Tuple<Episode, Double>> createEpisodesSortedSet() {
		final TreeSet<Tuple<Episode, Double>> res = Sets.newTreeSet(new Comparator<Tuple<Episode, Double>>() {
			@Override
			public int compare(final Tuple<Episode, Double> o1, final Tuple<Episode, Double> o2) {
				int valueOrdering = Double.compare(o2.getSecond(), o1.getSecond());
				boolean areValuesEqual = valueOrdering == 0;
				if (areValuesEqual) {
					int frequencyOrdering = Integer.compare(o2.getFirst().getFrequency(), o1.getFirst().getFrequency());
					boolean areFrequenciesEqual = frequencyOrdering == 0;
					if (areFrequenciesEqual) {
						int numberOfEventsOrdering = Integer.compare(o2.getFirst().getNumEvents(),
								o1.getFirst().getNumEvents());
						return numberOfEventsOrdering;
					} else {
						return frequencyOrdering;
					}
				} else {
					return valueOrdering;
				}
			}
		});
		return res;
	}
}