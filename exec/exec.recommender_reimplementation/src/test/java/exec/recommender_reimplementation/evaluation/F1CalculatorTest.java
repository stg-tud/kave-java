/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

public class F1CalculatorTest extends PBNAnalysisBaseTest {
	@Test
	public void returnsCorrectF1OneHitButMultipleProposals() {
		ICoReMethodName expectedMethod = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m1 = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));
		ICoReMethodName m3 = CoReNameConverter.convert(method(voidType, type("A"), "m3"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m1, 0.8));
		proposals.add(Tuple.newTuple(m2, 0.8));
		proposals.add(Tuple.newTuple(m3, 0.8));

		assertEquals(0.5, testMeasure(expectedMethod, proposals), 0.0);
	}

	@Test
	public void returnsCorrectF1OneHitOneProposal() {
		ICoReMethodName expectedMethod = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m1 = CoReNameConverter.convert(method(voidType, type("A"), "m1"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m1, 0.8));

		assertEquals(1.0, testMeasure(expectedMethod, proposals), 0.0);
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

		Set<Tuple<ICoReMethodName, Double>> proposals1 = Sets.newLinkedHashSet();
		proposals1.add(Tuple.newTuple(m1, 0.8));

		Set<Tuple<ICoReMethodName, Double>> proposals2 = Sets.newLinkedHashSet();

		MRRCalculator mrrCalc = new MRRCalculator();
		mrrCalc.addValue(expectedMethod, proposals1);
		mrrCalc.addValue(expectedMethod, proposals2);

		assertEquals(0.5, mrrCalc.getMean(), 0.0);
	}

	private double testMeasure(ICoReMethodName expectedMethod, Set<Tuple<ICoReMethodName, Double>> proposals) {
		F1Calculator f1Calc = new F1Calculator();
		f1Calc.addValue(expectedMethod, proposals);
		return f1Calc.getMean();
	}
}
