package commons.utils.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.utils.json.JsonUtils;

public class GsonClassnameFormatTest {

	@Test
	public void replacedLongNamespace() {
		assertEquals("SST:CompletionExpression",
				JsonUtils
						.removeNamespace("cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression"));
	}

	@Test
	public void replacedShortNamespace() {
		assertEquals(
				"SST:LoopHeaderBlockExpressionTest",
				JsonUtils
						.removeNamespace("commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpressionTest"));
	}

	@Test
	public void uppedFirstLetter() {
		assertEquals("SST:ISST", JsonUtils.removeNamespace("ccc.kave.commons.model.ssts.iSST"));
	}

	@Test
	public void formattedShortClassname() {
		assertEquals("SST:ISST", JsonUtils.removeNamespace("iSST"));
	}

}
