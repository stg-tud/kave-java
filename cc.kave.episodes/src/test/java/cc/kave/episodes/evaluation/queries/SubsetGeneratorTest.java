package cc.kave.episodes.evaluation.queries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	
	private static final int LIMIT = 5;

	private Set<Fact> originalSet = Sets.newHashSet();
	private Set<Set<Fact>> expected;
	private Set<Set<Fact>> actuals;

	private SubsetsGenerator sut;

	@Before
	public void setup() {
		originalSet.add(new Fact(11));
		originalSet.add(new Fact(12));
		originalSet.add(new Fact(13));
		originalSet.add(new Fact(14));

		expected = Sets.newHashSet();
		actuals = Sets.newHashSet();
		
		sut = new SubsetsGenerator();
	}

	@Test
	public void emptySet() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Cannot subselect from less then one method invocation!");
		sut.generateSubsets(Sets.newHashSet(), 2, LIMIT);
	}

	@Test
	public void oneEntry() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Cannot subselect from less then one method invocation!");
		sut.generateSubsets(Sets.newHashSet(new Fact(15)), 2, LIMIT);
	}

	@Test
	public void subselectAllEntries() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Please subselect less than the total number of Facts!");
		sut.generateSubsets(originalSet, 4, LIMIT);
	}

	@Test
	public void subselectMoreEntries() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Please subselect less than the total number of Facts!");
		sut.generateSubsets(originalSet, 5, LIMIT);
	}

	@Test
	public void oneEvent() {
		expected.add(Sets.newHashSet(new Fact(11)));
		expected.add(Sets.newHashSet(new Fact(12)));
		expected.add(Sets.newHashSet(new Fact(13)));
		expected.add(Sets.newHashSet(new Fact(14)));
		
		actuals = sut.generateSubsets(originalSet, 1, LIMIT);

		assertEquals(expected, actuals);
	}

	@Test
	public void twoNodes() {
		actuals = sut.generateSubsets(originalSet, 2, LIMIT);
		
		assertTrue(actuals.size() == LIMIT);
	}
	
	@Test
	public void threeNodes() {
		expected.add(Sets.newHashSet(new Fact(11), new Fact(12), new Fact(13)));
		expected.add(Sets.newHashSet(new Fact(11), new Fact(12), new Fact(14)));
		expected.add(Sets.newHashSet(new Fact(11), new Fact(13), new Fact(14)));
		expected.add(Sets.newHashSet(new Fact(12), new Fact(13), new Fact(14)));
		
		actuals = sut.generateSubsets(originalSet, 3, LIMIT);
		
		assertEquals(expected, actuals);
	}
}
