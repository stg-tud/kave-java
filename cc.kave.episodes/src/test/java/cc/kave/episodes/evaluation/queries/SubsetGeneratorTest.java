package cc.kave.episodes.evaluation.queries;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.recommenders.exceptions.AssertionException;

public class SubsetGeneratorTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Set<Fact> originalSet = Sets.newHashSet();
	private Set<Fact> subSets;
	private Map<Integer, Set<Set<Fact>>> expected = new HashMap<>();
	private Map<Integer, Set<Set<Fact>>> actuals;

	private SubsetsGenerator sut;

	@Before
	public void setup() {
		originalSet.add(new Fact(12));
		originalSet.add(new Fact(13));
		originalSet.add(new Fact(14));

		sut = new SubsetsGenerator();
	}

	@Test
	public void emptySet() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Cannot subselect from less then one method invocation!");
		sut.generateSubsets(Sets.newHashSet(), Sets.newHashSet(1, 2));
	}

	@Test
	public void oneEntry() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Cannot subselect from less then one method invocation!");
		sut.generateSubsets(Sets.newHashSet(new Fact(15)), Sets.newHashSet(1, 2));
	}

	@Test
	public void subselectAllEntries() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Please subselect less than the total number of Facts!");
		sut.generateSubsets(originalSet, Sets.newHashSet(1, 3));
	}

	@Test
	public void subselectMoreEntries() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Please subselect less than the total number of Facts!");
		sut.generateSubsets(originalSet, Sets.newHashSet(2, 5));
	}

	@Test
	public void oneEvent() {
		expected.put(1, Sets.newHashSet());
		subSets = Sets.newHashSet(new Fact(12));
		expected.get(1).add(subSets);

		subSets = Sets.newHashSet(new Fact(13));
		expected.get(1).add(subSets);

		subSets = Sets.newHashSet(new Fact(14));
		expected.get(1).add(subSets);

		actuals = sut.generateSubsets(originalSet, Sets.newHashSet(1));

		assertEquals(expected, actuals);
	}

	@Test
	public void twoNodes() {
		expected.put(2, Sets.newHashSet());
		subSets = Sets.newHashSet(new Fact(12), new Fact(13));
		expected.get(2).add(subSets);

		subSets = Sets.newHashSet(new Fact(12), new Fact(14));
		expected.get(2).add(subSets);

		subSets = Sets.newHashSet(new Fact(13), new Fact(14));
		expected.get(2).add(subSets);

		actuals = sut.generateSubsets(originalSet, Sets.newHashSet(2));

		assertEquals(expected, actuals);
	}
	
	@Test
	public void multipleNodes() {
		expected.put(1, Sets.newHashSet());
		expected.put(2, Sets.newHashSet());
		
		subSets = Sets.newHashSet(new Fact(12));
		expected.get(1).add(subSets);

		subSets = Sets.newHashSet(new Fact(13));
		expected.get(1).add(subSets);

		subSets = Sets.newHashSet(new Fact(14));
		expected.get(1).add(subSets);
		
		subSets = Sets.newHashSet(new Fact(12), new Fact(13));
		expected.get(2).add(subSets);

		subSets = Sets.newHashSet(new Fact(12), new Fact(14));
		expected.get(2).add(subSets);

		subSets = Sets.newHashSet(new Fact(13), new Fact(14));
		expected.get(2).add(subSets);

		actuals = sut.generateSubsets(originalSet, Sets.newHashSet(1, 2));

		assertEquals(expected, actuals);
	}
}
