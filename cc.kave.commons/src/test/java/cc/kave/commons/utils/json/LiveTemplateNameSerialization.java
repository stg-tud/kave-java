package cc.kave.commons.utils.json;

import org.junit.Assert;
import org.junit.Test;
import cc.kave.commons.model.names.resharper.LiveTemplateName;
import cc.kave.commons.utils.json.JsonUtils;
import static org.hamcrest.CoreMatchers.equalTo;

public class LiveTemplateNameSerialization {
	
	
	@Test
	public void testLiveTemplateNameFromJson(){
		String json = "\"ReSharper.LiveTemplateName:test\"";
		LiveTemplateName actual = JsonUtils.fromJson(json, LiveTemplateName.class);
		LiveTemplateName expected = LiveTemplateName.newLiveTemplateName("test");
		Assert.assertThat(actual, equalTo(expected));
	}
	
	@Test
	public void testLiveTemplateNameToJson(){
		LiveTemplateName name = LiveTemplateName.newLiveTemplateName("test");
		String actual = JsonUtils.toJson(name);
		String expected = "\"ReSharper.LiveTemplateName:test\"";
		Assert.assertThat(actual, equalTo(expected));
	}
}
