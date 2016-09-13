package exec.recommender_reimplementation.evaluation;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class MRRCalculatorTest extends PBNAnalysisBaseTest {

	@Test
	public void returnsCorrectReciprocalRankForRank1() {
		ICoReMethodName expectedMethod= CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m1 = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));
		ICoReMethodName m3 = CoReNameConverter.convert(method(voidType, type("A"), "m3"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m1, 0.8));
		proposals.add(Tuple.newTuple(m2, 0.8));
		proposals.add(Tuple.newTuple(m3, 0.8));

		assertEquals(1.0, testMeasure(expectedMethod, proposals), 0.0);
	}

	@Test
	public void returnsCorrectReciprocalRankForRank2() {
		ICoReMethodName expectedMethod = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m1 = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));
		ICoReMethodName m3 = CoReNameConverter.convert(method(voidType, type("A"), "m3"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m2, 0.8));
		proposals.add(Tuple.newTuple(m1, 0.8));
		proposals.add(Tuple.newTuple(m3, 0.8));

		assertEquals(1 / (double) 2, testMeasure(expectedMethod, proposals), 0.0);
	}

	@Test
	public void returnsCorrectReciprocalRankForRank3() {
		ICoReMethodName expectedMethod = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m1 = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));
		ICoReMethodName m3 = CoReNameConverter.convert(method(voidType, type("A"), "m3"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m2, 0.8));
		proposals.add(Tuple.newTuple(m3, 0.8));
		proposals.add(Tuple.newTuple(m1, 0.8));

		assertEquals(1 / (double) 3, testMeasure(expectedMethod, proposals), 0.0);
	}

	@Test
	public void returnsCorrectReciprocalRankIfNotInProposals() {
		ICoReMethodName expectedMethod = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));
		ICoReMethodName m3 = CoReNameConverter.convert(method(voidType, type("A"), "m3"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m2, 0.8));
		proposals.add(Tuple.newTuple(m3, 0.8));

		assertEquals(0.0, testMeasure(expectedMethod, proposals), 0.0);
	}

	@Test
	public void calculatesMeanCorrectly() {
		ICoReMethodName expectedMethod = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m1 = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));

		Set<Tuple<ICoReMethodName, Double>> proposals1 = Sets.newLinkedHashSet();
		proposals1.add(Tuple.newTuple(m1, 0.8));
		proposals1.add(Tuple.newTuple(m2, 0.7));

		Set<Tuple<ICoReMethodName, Double>> proposals2 = Sets.newLinkedHashSet();

		MRRCalculator mrrCalc = new MRRCalculator();
		mrrCalc.addValue(expectedMethod, proposals1);
		mrrCalc.addValue(expectedMethod, proposals2);

		assertEquals(0.5, mrrCalc.getMean(), 0.0);
	}

	private double testMeasure(ICoReMethodName expectedMethod, Set<Tuple<ICoReMethodName, Double>> proposals) {
		MRRCalculator mrrCalc = new MRRCalculator();
		mrrCalc.addValue(expectedMethod, proposals);
		return mrrCalc.getMean();
	}
}
