package exec.episodes;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import cc.kave.episodes.model.Threshold;

import com.google.common.collect.Sets;

public class TestClass {

	public static void main(String[] args) {
		SortedMap<Integer, Set<Double>> sortedMap = new TreeMap<Integer, Set<Double>>();
		
		SortedSet<Double> sortedSet = new TreeSet<Double>();
		sortedSet.add(0.1);
		sortedSet.add(0.3);
		sortedSet.add(0.2);
		
		sortedMap.put(5, sortedSet);
		sortedMap.put(3, Sets.newHashSet(0.8));
		sortedMap.put(4, Sets.newHashSet(0.5));
		
//		for (Map.Entry<Integer, Set<Double>> entry : sortedMap.entrySet()) {
//			System.out.println("Frequency = " + entry.getKey());
//			System.out.println("Entropy = " + entry.getValue().toString());
//			System.out.println();
//		}
		
		Set<Threshold> thresholds = Sets.newLinkedHashSet();
		thresholds.add(new Threshold(5, 0.5));
		
		Threshold th2 = new Threshold(5, 0.5);
		th2.addGenPattern();
		th2.addGenPattern();
		th2.addSpecPattern();
		th2.addGenPattern();
		
		boolean test = thresholds.contains(th2);
//		System.out.println(test);
		
		double d = 0.45372;
		System.out.println(Math.floor(d *1000) / 1000);
	}
}
