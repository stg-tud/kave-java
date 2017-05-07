package cc.kave.episodes.statistics;

import java.util.concurrent.ExecutionException;

public class RunMe {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// String path = "/Users/seb/some-contexts/";
		String path = "/Documents/EpisodeMining/dataSet/SST/Github/";
		EventStreamGenerator2 gen = new EventStreamGenerator2(path);
		gen.run();
	}
}
