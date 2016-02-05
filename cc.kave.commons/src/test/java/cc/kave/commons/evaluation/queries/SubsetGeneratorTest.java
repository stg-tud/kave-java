package cc.kave.commons.evaluation.queries;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import c.kave.commons.evaluation.queries.SubsetsGenerator;

public class SubsetGeneratorTest {

	private List<String> originalList = new LinkedList<String>();
	
	private List<List<String>> expected = new LinkedList<List<String>>();
	private List<String> subSets = new LinkedList<String>();
	
	private List<List<String>> actuals;
	
	private SubsetsGenerator sut;
	
	@Before
	public void setup() {
		originalList.add("a");
		originalList.add("b");
		originalList.add("c");
		
		sut = new SubsetsGenerator();
	}
	
	@Test
	public void oneEvents() {
		subSets.add("a");
		expected.add(subSets);
		
		subSets = new LinkedList<String>();
		subSets.add("b");
		expected.add(subSets);
		
		subSets = new LinkedList<String>();
		subSets.add("c");
		expected.add(subSets);
		
		actuals = sut.generateSubsets(originalList, 1);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void twoNodes() {
		subSets.add("a");
		subSets.add("b");
		expected.add(subSets);
		
		subSets = new LinkedList<String>();
		subSets.add("a");
		subSets.add("c");
		expected.add(subSets);
		
		subSets = new LinkedList<String>();
		subSets.add("b");
		subSets.add("c");
		expected.add(subSets);
		
		actuals = sut.generateSubsets(originalList, 2);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void threeNodes() {
		
		actuals = sut.generateSubsets(originalList, 3);
		
		assertEquals(expected, actuals);
	}
}
