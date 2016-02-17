package cc.kave.episodes.evaluation.queries;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;

public class SubsetGeneratorTest {

	private Set<Fact> originalList = Sets.newHashSet();
	
	private List<List<Fact>> expected = new LinkedList<List<Fact>>();
	private List<Fact> subSets = new LinkedList<Fact>();
	
	private List<List<Fact>> actuals;
	
	private SubsetsGenerator sut;
	
	@Before
	public void setup() {
		originalList.add(new Fact("a"));
		originalList.add(new Fact("b"));
		originalList.add(new Fact("c"));
		
		sut = new SubsetsGenerator();
	}
	
	@Test
	public void oneEvents() {
		subSets.add(new Fact("a"));
		expected.add(subSets);
		
		subSets = new LinkedList<Fact>();
		subSets.add(new Fact("b"));
		expected.add(subSets);
		
		subSets = new LinkedList<Fact>();
		subSets.add(new Fact("c"));
		expected.add(subSets);
		
		actuals = sut.generateSubsets(originalList, 1);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void twoNodes() {
		subSets.add(new Fact("a"));
		subSets.add(new Fact("b"));
		expected.add(subSets);
		
		subSets = new LinkedList<Fact>();
		subSets.add(new Fact("a"));
		subSets.add(new Fact("c"));
		expected.add(subSets);
		
		subSets = new LinkedList<Fact>();
		subSets.add(new Fact("b"));
		subSets.add(new Fact("c"));
		expected.add(subSets);
		
		actuals = sut.generateSubsets(originalList, 2);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void threeNodes() {
		
		actuals = sut.generateSubsets(originalList, 3);
		
		assertEquals(expected, actuals);
	}
	
	@Ignore
	@Test
	public void wrongNumberOfSelection() {
		
		actuals = sut.generateSubsets(originalList, 5);
		
		assertEquals(expected, actuals);
	}
}
