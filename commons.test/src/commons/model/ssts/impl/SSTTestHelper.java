package commons.model.ssts.impl;

import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class SSTTestHelper {

	public static VisitorAssertion accept(ISSTNode node, int context) {
		return new VisitorAssertion(node, context);
	}

}