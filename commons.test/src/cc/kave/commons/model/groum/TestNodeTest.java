package cc.kave.commons.model.groum;

public class TestNodeTest extends NodeContractTest {

	@Override
	protected INode createNode(String id) {
		return new TestNode(id);
	}

}
