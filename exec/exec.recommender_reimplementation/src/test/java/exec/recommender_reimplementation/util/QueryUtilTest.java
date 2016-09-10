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
package exec.recommender_reimplementation.util;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static exec.recommender_reimplementation.util.QueryUtil.calculateReciprocalRank;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class QueryUtilTest extends PBNAnalysisBaseTest {
	@Test
	public void returnsCorrectReciprocalRankForRank1() {
		ICoReMethodName expected = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m1 = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));
		ICoReMethodName m3 = CoReNameConverter.convert(method(voidType, type("A"), "m3"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m1, 0.8));
		proposals.add(Tuple.newTuple(m2, 0.8));
		proposals.add(Tuple.newTuple(m3, 0.8));

		assertEquals(1, calculateReciprocalRank(expected, proposals), 0.0);
	}

	@Test
	public void returnsCorrectReciprocalRankForRank2() {
		ICoReMethodName expected = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m1 = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));
		ICoReMethodName m3 = CoReNameConverter.convert(method(voidType, type("A"), "m3"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m2, 0.8));
		proposals.add(Tuple.newTuple(m1, 0.8));
		proposals.add(Tuple.newTuple(m3, 0.8));

		assertEquals(1 / (double) 2, calculateReciprocalRank(expected, proposals), 0.0);
	}

	@Test
	public void returnsCorrectReciprocalRankForRank3() {
		ICoReMethodName expected = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m1 = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));
		ICoReMethodName m3 = CoReNameConverter.convert(method(voidType, type("A"), "m3"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m2, 0.8));
		proposals.add(Tuple.newTuple(m3, 0.8));
		proposals.add(Tuple.newTuple(m1, 0.8));

		assertEquals(1 / (double) 3, calculateReciprocalRank(expected, proposals), 0.0);
	}

	@Test
	public void returnsCorrectReciprocalRankIfNotInProposals() {
		ICoReMethodName expected = CoReNameConverter.convert(method(voidType, type("A"), "m1"));
		ICoReMethodName m2 = CoReNameConverter.convert(method(voidType, type("A"), "m2"));
		ICoReMethodName m3 = CoReNameConverter.convert(method(voidType, type("A"), "m3"));

		Set<Tuple<ICoReMethodName, Double>> proposals = Sets.newLinkedHashSet();
		proposals.add(Tuple.newTuple(m2, 0.8));
		proposals.add(Tuple.newTuple(m3, 0.8));

		assertEquals(0, calculateReciprocalRank(expected, proposals), 0.0);
	}

	@Test
	public void ShouldParseDeclaringTypes() {
		ITypeName sut = Names.newType("s:n.T1+S, P");
		Assert.assertTrue(sut.isNestedType());
		Assert.assertEquals(Names.newType("n.T1, P"), sut.getDeclaringType());
	}


}
