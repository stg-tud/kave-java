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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.impl.SST;

public class ContextSerializationTest {

	@Test
	public void testSerializeToJson() {
		String sut = JsonUtils.toJson(getExampleContext(), Context.class);
		assertThat(sut, equalTo(getExampleJson_Current()));
	}

	@Test
	public void testDeserializeToContext() {
		Context sut = JsonUtils.fromJson(getExampleJson_Current(), Context.class);
		assertThat(sut, equalTo(getExampleContext()));
	}

	private static String getExampleJson_Current() {
		return "{\"$type\":\"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\",\"TypeShape\":{\"$type\":\"KaVE.Commons.Model.TypeShapes.TypeShape, KaVE.Commons\",\"TypeHierarchy\":{\"$type\":\"KaVE.Commons.Model.TypeShapes.TypeHierarchy, KaVE.Commons\",\"Element\":\"CSharp.UnknownTypeName:?\",\"Implements\":[]},\"MethodHierarchies\":[]},\"SST\":{\"$type\":\"[SST:SST]\",\"EnclosingType\":\"CSharp.TypeName:T,P\",\"PartialClassIdentifier\":\"\",\"Fields\":[],\"Properties\":[],\"Methods\":[],\"Events\":[],\"Delegates\":[]}}";
	}

	private static Context getExampleContext() {
		Context context = new Context();
		SST sst = new SST();
		sst.setEnclosingType(Names.newType("T,P"));
		context.setSST(sst);
		return context;
	}
}