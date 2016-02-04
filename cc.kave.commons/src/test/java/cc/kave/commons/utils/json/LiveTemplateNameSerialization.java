/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
