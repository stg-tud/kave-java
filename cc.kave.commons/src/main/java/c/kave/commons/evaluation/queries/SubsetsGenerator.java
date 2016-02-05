package c.kave.commons.evaluation.queries;

import java.util.LinkedList;
import java.util.List;

public class SubsetsGenerator {

	public List<List<String>> generateSubsets(List<String> array, int subsetLength) {
		int N = array.size();
		List<List<String>> results = new LinkedList<List<String>>();
		
		if (N == subsetLength) {
			return results;
		}

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
