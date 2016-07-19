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

}
