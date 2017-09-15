package exec.episodes;

import java.util.Map;
import java.util.TreeSet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Examples {

	public static void main(String[] args) {
		Map<String, Integer> map = Maps.newLinkedHashMap();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getKey().equalsIgnoreCase("b")) {
				map.remove(entry.getKey());
			}
		}
		System.out.println(map.toString());
		
		TreeSet<String> treeStruct = Sets.newTreeSet();
		treeStruct.add("c");
		treeStruct.add("a");
		treeStruct.add("b");
		
		System.out.println(treeStruct.toString());
	}
}
