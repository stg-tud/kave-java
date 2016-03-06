/**
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.utils.json;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;

import cc.recommenders.names.VmMethodName;
import cc.recommenders.names.VmTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;

public class JsonUtilsTest {

	private File tmpFile;

	@Before
	public void setup() throws IOException {
		tmpFile = File.createTempFile("test-", ".json");
	}

	@Test
	public void fromJson_file() throws IOException {
		writeToTemp("{ \"A\": 1}");
		TestType actual = JsonUtils.fromJson(tmpFile, TestType.class);
		TestType expected = TestType.create(1);
		assertEquals(expected, actual);
	}

	private void writeToTemp(String json) throws IOException {
		FileUtils.writeStringToFile(tmpFile, json);
	}

	@Test
	public void fromJson_stream() throws IOException {
		writeToTemp("{ \"A\": 2}");
		FileInputStream inputStream = new FileInputStream(tmpFile);
		TestType actual = JsonUtils.fromJson(inputStream, TestType.class);
		TestType expected = TestType.create(2);
		assertEquals(expected, actual);
	}

	@Test
	public void toJson_file() throws IOException {
		TestType data = TestType.create(3);
		JsonUtils.toJson(data, tmpFile);
		String actual = FileUtils.readFileToString(tmpFile);
		String expected = "{\"A\":3}";
		assertEquals(expected, actual);
	}

	@Test
	// @Ignore("currently fails with stack overflow")
	public void integrationTestWithUsage() {
		Query u = new Query();
		u.setDefinition(DefinitionSites.createDefinitionByConstant());
		u.setMethodContext(VmMethodName.get("LT.m()V"));
		u.setClassContext(VmTypeName.get("LSuperType"));
		u.addCallSite(CallSites.createReceiverCallSite("LContext.m1()V"));

		String actual = JsonUtils.toJson(u);
		String expected = "{\"classCtx\":\"LSuperType\",\"methodCtx\":\"LT.m()V\",\"definition\":{\"kind\":\"CONSTANT\"},\"sites\":[{\"kind\":\"RECEIVER\",\"method\":\"LContext.m1()V\"}]}";
		assertEquals(expected, actual);
	}

	@Test
	// @Ignore("currently fails with stack overflow")
	public void integrationTestWithCallSite() {
		CallSite c = CallSites.createReceiverCallSite("LContext.m1()V");
		String actual = JsonUtils.toJson(c);
		String expected = "{\"Kind\":\"RECEIVER\",\"Method\":\"LContext.m1()V\",\"ArgIndex\":0}";
		assertEquals(expected, actual);
	}

	private static class TestType {

		@SuppressWarnings("unused")
		public int a;

		public static TestType create(int a) {
			TestType t = new TestType();
			t.a = a;
			return t;
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	}
}