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
package exec.recommender_reimplementation.heinemann_analysis;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.intType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.stringType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;

public class HeinemannRecommenderTest {

	private HeinemannRecommender uut;
	
	@Before
	public void setup() {
		uut = new HeinemannRecommender(createSampleModel(), 0.5);
	}
	
	@Test
	public void jaccardIndexCalculatesCorrectly() {
		Set<String> t1 = Sets.newHashSet("file", "input", "read");
		Set<String> t2 = Sets.newHashSet("file", "write");
		
		double similarity = uut.calculateJaccardSimilarity(t1, t2);
		assertEquals(0.25, similarity, 0.0);
	}
	
	@Test
	public void sizeOfStringCalculatedCorrectly() {
		String s = "Hello World";
		
		int size = uut.getSizeForString(s);
		
		assertEquals(38, size);
	}
	
	@Test
	public void returnsCorrectSizeForSampleModel() {
		assertEquals(1054, uut.getSize());
	}
	
	@Test
	public void queryRecommenderReturnsCorrectResult() {
		HeinemannQuery query = createQuery(type("C"), "String", "some", "Str", "=");
		
		Set<Tuple<ICoReMethodName, Double>> result = uut.query(query);
		
		Set<Tuple<ICoReMethodName, Double>> expectedResult = Sets.newLinkedHashSet();
		expectedResult.add(Tuple.newTuple(CoReNameConverter.convert(method(stringType, type("C"), "m4")), 1.0));
		expectedResult.add(Tuple.newTuple(CoReNameConverter.convert(method(stringType, type("C"), "m5")), 0.6));
		
		assertEqualSet(result, expectedResult);
	}
	
	private HeinemannQuery createQuery(ITypeName declaringType, String... lookback) {
		return new HeinemannQuery(Sets.newHashSet(lookback), declaringType);
	}
	
	private Map<ITypeName, Set<Entry>> createSampleModel() {
		Map<ITypeName, Set<Entry>> model = new HashMap<>();
		
		model.put(type("A"), Sets.newHashSet(
				createEntry(method(voidType, type("A"), "foo"), "String", "str", "Int", "num"),
				createEntry(method(voidType, type("A"), "bar"), "String", "str", "=", "???")));
		model.put(type("B"), Sets.newHashSet(
				createEntry(method(voidType, type("B"), "m1"), "Int", "number", "=", "42"),
				createEntry(method(intType, type("B"), "m2"), "<if>", "true", "Int", "number")));
		model.put(type("C"), Sets.newHashSet(
				createEntry(method(voidType, type("C"), "m3"), "String", "str", "Int", "num2"),
				createEntry(method(stringType, type("C"), "m4"), "String", "some","Str", "="),			
				createEntry(method(stringType, type("C"), "m5"), "String", "other", "Str", "=")			
				));
		
		return model;
	}
	
	private static <T> void assertEqualSet(Set<Tuple<T, Double>> a, Set<Tuple<T, Double>> b) {
		assertTrue(a.size() == b.size());
		Iterator<Tuple<T, Double>> itA = a.iterator();
		Iterator<Tuple<T, Double>> itB = b.iterator();

		while (itA.hasNext()) {
			Tuple<T, Double> tA = itA.next();
			Tuple<T, Double> tB = itB.next();

			assertEquals(tA.getFirst(), tB.getFirst());
			assertEquals(tA.getSecond(), tB.getSecond(), 0.001);
		}
	}
	
	private Entry createEntry(IMethodName method, String... strings) {
		return new Entry(Sets.newHashSet(strings), method);
	}
	
	protected static ITypeName type(String simpleName) {
		return Names.newType(simpleName + ",P1");
	}

	protected static IMethodName method(ITypeName returnType, ITypeName declType, String simpleName,
			IParameterName... parameters) {
		String parameterStr = Joiner.on(", ").join(
				Arrays.asList(parameters).stream().map(p -> p.getIdentifier()).toArray());
		String methodIdentifier = String.format("[%1$s] [%2$s].%3$s(%4$s)", returnType.getIdentifier(),
				declType.getIdentifier(), simpleName,
				parameterStr);
		return Names.newMethod(methodIdentifier);
	}

}
