package cc.kave.episodes.evaluation.queries;

import java.io.File;
import java.util.Set;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;

public class QueryTemp {

	public static void main(String[] args) throws Exception {
		Episode pattern = new Episode();
		pattern.addFact(new Fact(3035));
		pattern.addFact(new Fact(2936));
		pattern.addFact(new Fact(20171));
		pattern.addFact(new Fact(3062));
		pattern.addFact(new Fact("3035>2936"));
		pattern.addFact(new Fact("3035>20171"));
		pattern.addFact(new Fact("3035>3062"));
		pattern.addFact(new Fact("2936>20171"));
		pattern.addFact(new Fact("2936>3062"));
		pattern.addFact(new Fact("20171>3062"));
		
		QueryGeneration generator = new QueryGeneration(new File("/Users/ervinacergani/Documents/EpisodeMining/dataSet/events/"));
		
		Set<Episode> queries = generator.generate(pattern);
		
		System.out.println(queries.toString());
	}
}
