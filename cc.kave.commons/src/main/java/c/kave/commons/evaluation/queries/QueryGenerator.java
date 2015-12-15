package c.kave.commons.evaluation.queries;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.QueryConfigurations;

public class QueryGenerator {

	public Map<Episode, Map<Integer, List<Episode>>> createQuery(List<Episode> allMethods,
			QueryConfigurations configuration) throws Exception {
		Map<Episode, Map<Integer, List<Episode>>> generatedQueries = new HashMap<Episode, Map<Integer, List<Episode>>>();

		for (Episode episode : allMethods) {
			Map<Integer, List<Episode>> queries = new HashMap<Integer, List<Episode>>();
			List<Integer> sublistLengths = new LinkedList<Integer>();
			boolean md = false;

			if (episode.getNumberOfFacts() > 1) {
				if (configuration.equals(QueryConfigurations.INCLUDEMD_REMOVEONEBYONE)) {
					md = true;
					sublistLengths = removeOneByOne(episode.getNumEvents() - 1);
					queries = removeInvocations(episode, sublistLengths, md);
				} else if (configuration.equals(QueryConfigurations.INCLUDEMD_REMOVEBYPERCENTAGE)) {
					md = true;
					sublistLengths = removeByPercentage(episode.getNumEvents() - 1);
					queries = removeInvocations(episode, sublistLengths, md);
				} else if (configuration.equals(QueryConfigurations.REMOVEMD_REMOVEONEBYONE)) {
					sublistLengths = removeOneByOne(episode.getNumEvents() - 1);
					queries = removeInvocations(episode, sublistLengths, md);
				} else if (configuration.equals(QueryConfigurations.REMOVEMD_REMOVEBYPERCENTAGE)) {
					sublistLengths = removeByPercentage(episode.getNumEvents() - 1);
					queries = removeInvocations(episode, sublistLengths, md);
				} else {
					throw new Exception("Specify valid query configuration porameter!");
				}
				generatedQueries.put(episode, queries);
			}
		}
		return generatedQueries;
	}

	private Episode createQuery(Episode episode, List<String> list, boolean md) {
		Episode query = new Episode();
		query.setNumEvents(list.size() + 1);
		query.setFrequency(1);
		if (md) {
			query.addFact(episode.get(0).getRawFact());
			if (list.size() > 0) {
				query.addListOfFacts(list);
				query.addFact(query.get(0) + ">" + list.get(0));
			}
			if (list.size() > 1) {
				for (int idx = 0; idx < list.size() - 1; idx++) {
					query.addFact(list.get(idx) + ">" + list.get(idx + 1));
				}
			}
		}
		for (int idx1 = 0; idx1 < list.size(); idx1++) {
			for (int idx2 = idx1; idx2 < list.size() + 1; idx2++) {
				query.addFact(list.get(idx1) + ">" + list.get(idx2));
			}
		}
		return query;
	}

	private List<String> getInvocations(Episode episode) {
		List<String> invocations = new LinkedList<String>();
		for (int idx = 1; idx < episode.getNumEvents(); idx++) {
			invocations.add(episode.get(idx).getRawFact());
		}
		return invocations;
	}

	private Map<Integer, List<Episode>> removeInvocations(Episode episode, List<Integer> subsets, boolean md) {
		Map<Integer, List<Episode>> results = new HashMap<Integer, List<Episode>>();
		List<String> invocations = getInvocations(episode);
		for (int remove : subsets) {
			List<List<String>> allQueries = subsetsGenerator(invocations, invocations.size() - remove);
			List<Episode> queryLevel = new LinkedList<Episode>();
			for (List<String> list : allQueries) {
				Episode query = createQuery(episode, list, md);
				queryLevel.add(query);
			}
			results.put(remove, queryLevel);
		}
		if (md) {
			List<Episode> removeAllInvocations = new LinkedList<Episode>();
			Episode lastQuery = new Episode();
			lastQuery.setFrequency(1);
			lastQuery.setNumEvents(1);
			lastQuery.addFact(episode.get(0).getRawFact());
			removeAllInvocations.add(lastQuery);
			results.put(invocations.size(), removeAllInvocations);
		}
		return results;
	}

	private List<Integer> removeOneByOne(int NumberOfInvocations) {
		List<Integer> result = new LinkedList<Integer>();
		for (int remove = 1; remove < NumberOfInvocations; remove++) {
			result.add(remove);
		}
		return result;
	}

	private List<Integer> removeByPercentage(int numberInvocations) {
		List<Integer> removeInvocations = new LinkedList<Integer>();
		List<Double> percentages = new LinkedList<Double>();
		percentages.add(0.25);
		percentages.add(0.50);
		percentages.add(0.75);
		for (double p : percentages) {
			int remove = (int) Math.ceil(p * numberInvocations);
			if (!removeInvocations.contains(remove)) {
				removeInvocations.add(remove);
			}
		}
		return removeInvocations;
	}

	private static List<List<String>> subsetsGenerator(List<String> array, int subsetLength) {
		int N = array.size();
		List<List<String>> results = new LinkedList<List<String>>();

		int[] binary = new int[(int) Math.pow(2, N)];
		for (int i = 0; i < Math.pow(2, N); i++) {
			int b = 1;
			binary[i] = 0;
			int num = i, count = 0;
			while (num > 0) {
				if (num % 2 == 1)
					count++;
				binary[i] += (num % 2) * b;
				num /= 2;
				b = b * 10;
			}

			if (count == subsetLength) {
				List<String> subset = new LinkedList<>();
				for (int j = 0; j < N; j++) {
					if (binary[i] % 10 == 1) {
						subset.add(array.get(j));
					}
					binary[i] /= 10;
				}
				results.add(subset);
			}
		}
		return results;
	}
}
