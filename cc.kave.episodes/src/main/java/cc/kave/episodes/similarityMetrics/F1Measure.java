package cc.kave.episodes.similarityMetrics;

import java.util.Set;

import cc.kave.episodes.model.events.Fact;
import cc.recommenders.evaluation.data.Measure;

public class F1Measure {

	public static double calcF1(Set<Fact> query, Set<Fact> pattern) {
		Measure m = Measure.newMeasure(query, pattern);
		double f1 = m.getF1();
		return f1;
	}
}
