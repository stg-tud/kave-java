package exec.episodes;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.Sets;

public class TestClass {

	public static void main(String[] args) {
		Map<Integer, Set<Double>> map = new TreeMap<Integer, Set<Double>>(Collections.reverseOrder());
		map.put(5, Sets.newHashSet(0.1, 0.3, 0.2));
		map.put(3, Sets.newHashSet(0.8));
		map.put(4, Sets.newHashSet(0.5));
		
		for (Map.Entry<Integer, Set<Double>> entry : map.entrySet()) {
			System.out.println("Frequency = " + entry.getKey());
			System.out.println("Entropy = " + entry.getValue().toString());
			System.out.println();
		}
	}
}
