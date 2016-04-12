/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.utils.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class UsageSerializationTest {

	@Test
	public void querySerialization_json() {
		Query q = createQuery();

		String json = JsonUtils.toJson(q);
		assertEquals(
				"{\"type\":\"LT\",\"classCtx\":\"LS\",\"methodCtx\":\"LF.m()V\",\"definition\":{\"kind\":\"CONSTANT\"},\"sites\":[{\"kind\":\"RECEIVER\",\"method\":\"LT.m()V\"}]}",
				json);
	}

	@Test
	public void queryDeserialization_asQuery() {
		Query q = createQuery();

		String json = JsonUtils.toJson(q);
		Query actual = JsonUtils.fromJson(json, Query.class);

		assertEquals(q, actual);
	}

	@Test
	public void queryDeserialization_asUsage() {
		Query q = createQuery();

		String json = JsonUtils.toJson(q);
		Usage actual = JsonUtils.fromJson(json, Usage.class);

		assertEquals(q, actual);
	}

	private Query createQuery() {
		Query q = new Query();
		q.setType(CoReTypeName.get("LT"));
		q.setClassContext(CoReTypeName.get("LS"));
		q.setMethodContext(CoReMethodName.get("LF.m()V"));
		q.setDefinition(DefinitionSites.createDefinitionByConstant());
		q.addCallSite(CallSites.createReceiverCallSite("LT.m()V"));
		return q;
	}

	@Test
	public void noUsageSerialization_json() {
		NoUsage q = new NoUsage();

		String json = JsonUtils.toJson(q);
		assertEquals("\"NoUsage\"", json);
	}

	@Test
	public void noUsageDeserialization_asNoUsage() {
		NoUsage q = new NoUsage();

		String json = JsonUtils.toJson(q);
		NoUsage actual = JsonUtils.fromJson(json, NoUsage.class);

		assertEquals(q, actual);
	}

	@Test
	public void noUsageDeserialization_asUsage() {
		NoUsage q = new NoUsage();

		String json = JsonUtils.toJson(q);
		Usage actual = JsonUtils.fromJson(json, Usage.class);

		assertEquals(q, actual);
	}
}