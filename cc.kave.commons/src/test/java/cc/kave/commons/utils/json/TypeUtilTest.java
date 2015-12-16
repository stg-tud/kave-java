package cc.kave.commons.utils.json;

import org.junit.Assert;
import org.junit.Test;

public class TypeUtilTest {

	@Test
	public void SSTTypeToJaveTypeName() {
		String type = "[SST:Expressions.Simple.UnknownExpression]";
		String expected = "cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression";
		String actual = TypeUtil.toJavaTypeNames(type);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void contextToJavaTypeName() {
		String type = "\"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\"";
		String expected = "\"cc.kave.commons.model.events.completionevents.Context\"";
		String actual = TypeUtil.toJavaTypeNames(type);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void sstType() {
		String type = "[SST:Declarations.FieldDeclaration]";
		String expected = "cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration";
		String actual = TypeUtil.toJavaTypeNames(type);
		Assert.assertEquals(expected, actual);
	}

}
