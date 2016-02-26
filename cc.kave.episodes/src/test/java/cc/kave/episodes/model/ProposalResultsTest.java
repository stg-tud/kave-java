/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.kave.episodes.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProposalResultsTest {

	private ProposalResults sut;
	
	@Before
	public void setup() {
		sut = new ProposalResults();
	}
	
	@Test
	public void defaultValues() {
		assertTrue(sut.getTargetNo() == 0);
		assertTrue(sut.getAllResults().isEmpty());
	}
	
	@Test
	public void valuesCanBeSet() {
		sut.setTargetNo(1);;
		assertEquals(1, sut.getTargetNo());
		
		sut.setResult(0.8);
		sut.setResult(0.5);
		sut.setResult(0.3);
		
		List<Double> expected = new LinkedList<Double>();
		expected.add(0.8);
		expected.add(0.5);
		expected.add(0.3);
		
		assertEquals(expected, sut.getAllResults());
	}
	
	@Test
	public void equality_default() {
		ProposalResults a = new ProposalResults();
		ProposalResults b = new ProposalResults();
		
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertTrue(a.equals(b));
	}
	
	@Test
	public void equality_reallyTheSame() {
		ProposalResults a = new ProposalResults();
		a.setTargetNo(2);
		a.setResult(0.7);
		a.setResult(0.4);
		a.setResult(0.2);
		
		ProposalResults b = new ProposalResults();
		b.setTargetNo(2);
		b.setResult(0.7);
		b.setResult(0.4);
		b.setResult(0.2);
		
		assertEquals(a, b);
	}
	
	@Test
	public void diffResults() {
		ProposalResults a = new ProposalResults();
		a.setTargetNo(2);
		a.setResult(0.8);
		a.setResult(0.5);
		a.setResult(0.3);
		
		ProposalResults b = new ProposalResults();
		b.setTargetNo(2);
		b.setResult(0.7);
		b.setResult(0.4);
		b.setResult(0.2);
		
		assertNotEquals(a, b);
		assertTrue(a.getTargetNo() == b.getTargetNo());
		assertNotEquals(a.getAllResults(), b.getAllResults());
	}
	
	@Test
	public void diffTargets() {
		ProposalResults a = new ProposalResults();
		a.setTargetNo(1);
		a.setResult(0.7);
		a.setResult(0.4);
		a.setResult(0.2);
		
		ProposalResults b = new ProposalResults();
		b.setTargetNo(2);
		b.setResult(0.7);
		b.setResult(0.4);
		b.setResult(0.2);
		
		assertNotEquals(a, b);
		assertFalse(a.getTargetNo() == b.getTargetNo());
		assertEquals(a.getAllResults(), b.getAllResults());
	}
}
