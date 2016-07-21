package exec.recommender_reimplementation.raychev_analysis;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class AbstractHistoryTest extends RaychevAnalysisBaseTest {

	@Test
	public void addsInteractionToAllConcreteHistories() {
		AbstractHistory abstractHistory = createAbstractHistory(Lists.newArrayList(
		//
				callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m2")), //
				Sets.newHashSet(
						createConcreteHistory(//
						callInDefaultContextAsReceiver("m1")),
						createConcreteHistory(callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"))));
		abstractHistory.addInteraction(callInDefaultContextAsReceiver("m3"));

		AbstractHistory expected = createAbstractHistory(Lists.newArrayList(
				//
				callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m2"),
				callInDefaultContextAsReceiver("m3")), //
				Sets.newHashSet(
						createConcreteHistory(//
								callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m3")), //
						createConcreteHistory(
								//
								callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m2"),
								callInDefaultContextAsReceiver("m3"))));

		assertEquals(expected, abstractHistory);
	}

	@Test
	public void mergeTest() {
		AbstractHistory abstractHistory = createAbstractHistory(Lists.newArrayList(
		//
				callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m2")), //
				Sets.newHashSet(
						createConcreteHistory(//
						callInDefaultContextAsReceiver("m1")),
						createConcreteHistory(callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"))));

		AbstractHistory abstractHistory2 = createAbstractHistory(Lists.newArrayList(
				//
				callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m2"),
				callInDefaultContextAsReceiver("m3")), //
				Sets.newHashSet(
						createConcreteHistory(//
								callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m3")),
						createConcreteHistory(callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"))));

		abstractHistory.mergeAbstractHistory(abstractHistory2);

		AbstractHistory expected = createAbstractHistory(Lists.newArrayList(
				//
				callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m2"),
				callInDefaultContextAsReceiver("m3")), //
				Sets.newHashSet(
						createConcreteHistory(//
								callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m3")),
						createConcreteHistory(//
								callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m3")),
						createConcreteHistory(callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"))));

		assertEquals(expected, abstractHistory);
	}
	
	@Test
	public void cloneTest() {
		AbstractHistory abstractHistory = createAbstractHistory(Lists.newArrayList(
		//
				callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m2")), //
				Sets.newHashSet(
						createConcreteHistory(//
						callInDefaultContextAsReceiver("m1")),
						createConcreteHistory(callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"))));

		
		AbstractHistory clone = abstractHistory.clone();
		
		assertEquals(abstractHistory, clone);
		assertFalse(abstractHistory == clone);
	}
	
	@Test
	public void mutableItemsInSet() {
		Set<AbstractHistory> someSet = Sets
				.newHashSet(
						createAbstractHistory(callInDefaultContextAsReceiver("m1")),
						createAbstractHistory(callInDefaultContextAsParameter(
								"m1", 1)));

		for (AbstractHistory history : someSet) {
			history.addInteraction(callInDefaultContextAsReceiver("m2"));
		}

		Set<AbstractHistory> expected = Sets.newHashSet(
				createAbstractHistory(callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m2")),
				createAbstractHistory(callInDefaultContextAsParameter("m1", 1),
						callInDefaultContextAsReceiver("m2")));

		assertEquals(expected, someSet);
	}

	@Test
	public void mutableItemsInList() {
		List<AbstractHistory> someSet = Lists.newArrayList(
				createAbstractHistory(callAtPosition(
						method(objectType, DefaultClassContext, "m1"), 0)),
				createAbstractHistory(callAtPosition(
						method(objectType, DefaultClassContext, "m1"), 1)));

		for (AbstractHistory history : someSet) {
			history.addInteraction(callAtPosition(
					method(objectType, DefaultClassContext, "m2"), 0));
		}

		List<AbstractHistory> expected = Lists.newArrayList(
				createAbstractHistory(
						callAtPosition(
								method(objectType, DefaultClassContext, "m1"),
								0),
						callAtPosition(
								method(objectType, DefaultClassContext, "m2"),
								0)),
				createAbstractHistory(
						callAtPosition(
								method(objectType, DefaultClassContext, "m1"),
								1),
						callAtPosition(
								method(objectType, DefaultClassContext, "m2"),
								0)));

		assertEquals(expected, someSet);
	}
}
