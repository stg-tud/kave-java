package c.kave.commons.evaluation.queries;

import java.util.LinkedList;
import java.util.List;

public class QueryGenerator {

	// public Map<Method, Map<Integer, List<Query>>> createQuery(List<Method>
	// allMethods, String configuration) {
	// Map<Episode, Map<Integer, List<Episode>>> generatedQueries = new
	// HashMap<Episode, Map<Integer, List<Episode>>>();
	// Map<Integer, List<Episode>> skipedEventsAndQueryPair = new
	// HashMap<Integer, List<Episode>>();
	// List<Episode> currentueries = new LinkedList<Episode>();
	// for (Method method : allMethods) {
	// if (method.getNumberOfInvocations() > invocationsRemoved) {
	// Query query = new Query();
	// query.addFact(method.getMethodName());
	// query.setNumberOfFacts(method.getNumberOfInvocations() + 1 -
	// invocationsRemoved);
	// }
	// }
	//
	// return generatedQueries;
	// }

	private List<int[]> subsetsGenerator(int[] array, int subsetLength) {
		int N = array.length;
		List<int[]> results = new LinkedList<int[]>();

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
				int subsetIdx = 0;
				int[] subset = new int[subsetLength];
				for (int j = 0; j < N; j++) {
					if (binary[i] % 10 == 1) {
						subset[subsetIdx] = array[j];
						subsetIdx++;
					}
					binary[i] /= 10;
				}
				results.add(subset);
			}
		}
		return results;
	}
}
