package exec.episode.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.utils.io.Logger;
import cc.kave.episodes.model.events.Fact;

public class SubsetsGenerator {

	private static Set<Set<Fact>> powerSet(Set<Fact> originalSet) {
		int iter = 0;
		
		Set<Set<Fact>> sets = new HashSet<Set<Fact>>(); 
		if (originalSet.isEmpty()) { 
			sets.add(new HashSet<Fact>()); 
			
			iter++;
			Logger.log("Iteration %d", iter);
			Logger.log("%s", sets.toString());
			
			return sets; 
		} 
		List<Fact> list = new ArrayList<Fact>(originalSet); 
		Fact head = list.get(0); 
		Set<Fact> rest = new HashSet<Fact>(list.subList(1, list.size())); 
		for (Set<Fact> set : powerSet(rest)) { 
			iter++;
			Set<Fact> newSet = new HashSet<Fact>(); 
			newSet.add(head); 
			newSet.addAll(set); 
			sets.add(newSet); 
			sets.add(set); 
		} 
		Logger.log("Iteration %d", iter);
		Logger.log("%s", sets.toString());
		
		return sets; 
	}
	
	public static void main(String[] args) {
		Logger.setPrinting(true);
		
		Set<Set<Fact>> subsets = powerSet(Sets.newHashSet(new Fact(11), new Fact(12), 
										new Fact(13), new Fact(14), new Fact(15)));
		
		Logger.log("Final subsets %s", subsets.toString());
	}
}
