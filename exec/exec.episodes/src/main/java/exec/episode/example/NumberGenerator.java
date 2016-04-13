package exec.episode.example;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

public class NumberGenerator {

	public static void main(String[] args) {
		Set<Integer> set = Sets.newHashSet();
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
			int number = random.nextInt(100);
			set.add(number);
			if (set.size() == 10) {
				break;
			}
		}
		for (int value : set) {
			System.out.println(value + "\t");
		}
	} 
}
