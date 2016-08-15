package exec.episode.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.kave.episodes.model.events.Fact;

public class SubsetsV1 {

	private static void getSubsets(List<Fact> superSet, int k, int idx, Set<Fact> current,List<Set<Fact>> solution) {
	    //successful stop clause
	    if (current.size() == k) {
	        solution.add(new HashSet<>(current));
	        return;
	    }
	    //unseccessful stop clause
	    if (idx == superSet.size()) return;
	    Fact x = superSet.get(idx);
	    current.add(x);
	    //"guess" x is in the subset
	    getSubsets(superSet, k, idx+1, current, solution);
	    current.remove(x);
	    //"guess" x is not in the subset
	    getSubsets(superSet, k, idx+1, current, solution);
	}

	public static List<Set<Fact>> getSubsets(List<Fact> superSet, int k) {
	    List<Set<Fact>> res = new ArrayList<>();
	    getSubsets(superSet, k, 0, new HashSet<Fact>(), res);
	    return res;
	}
	
	public static void main(String[] args) {
//		Logger.setPrinting(true);
//		
//		Set<Set<Fact>> subsets = getSubsets(Sets.newHashSet(new Fact(11), new Fact(12), 
//										new Fact(13), new Fact(14), new Fact(15)), 2);
//		
//		Logger.log("Final subsets %s", subsets.toString());
		
		List<Fact> superSet = new ArrayList<>();
		superSet.add(new Fact(11));
		superSet.add(new Fact(12));
		superSet.add(new Fact(13));
		superSet.add(new Fact(14));
		superSet.add(new Fact(15));
		superSet.add(new Fact(16));
		superSet.add(new Fact(17));
		superSet.add(new Fact(18));
		superSet.add(new Fact(19));
		superSet.add(new Fact(20));
		superSet.add(new Fact(21));
		superSet.add(new Fact(22));
		superSet.add(new Fact(23));
		superSet.add(new Fact(24));
		superSet.add(new Fact(25));
		superSet.add(new Fact(26));
		superSet.add(new Fact(27));
		superSet.add(new Fact(28));
		superSet.add(new Fact(29));
		superSet.add(new Fact(30));
		System.out.println(getSubsets(superSet,10));
	}
}
