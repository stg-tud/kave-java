/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dennis Albrecht
 */
package cc.kave.commons.utils.json.legacy;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import cc.kave.commons.utils.json.legacy.GsonUtil;
import cc.recommenders.names.CoReFieldName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;

import com.google.common.collect.Sets;

public class GsonUtilQueryTest {

	@Test
	public void checkDeSerializationOfQuery() {
		Query expected = new Query();
		expected.setType(CoReTypeName.get("Lusages/Query"));
		expected.setDefinition(getDefinition());
		expected.setClassContext(CoReTypeName.get("LContext"));
		expected.setMethodContext(CoReMethodName.get("LReceiver.equals(LArgument;)LResult;"));
		expected.setAllCallsites(getCallSites());

		String json = GsonUtil.serialize(expected);
		Query actual = GsonUtil.deserialize(json, Query.class);
		assertEquals(expected, actual);
	}

	private Set<CallSite> getCallSites() {
		Set<CallSite> callSites = Sets.newHashSet();
		callSites.add(getParamCallSite());
		callSites.add(getReceiverCallSite());
		return callSites;
	}

	private CallSite getParamCallSite() {
		return CallSites.createParameterCallSite("LCallSite.param(LParam;)LReturn;", 23);
	}

	private CallSite getReceiverCallSite() {
		return CallSites.createReceiverCallSite("LCallSite.param(LParam;)LReturn;");
	}

	private DefinitionSite getDefinition() {
		DefinitionSite site = DefinitionSites.createDefinitionByThis();
		site.setMethod(CoReMethodName.get("LDefiner.define(LScheme;)LPattern;"));
		site.setField(CoReFieldName.get("LField.field;LType"));
		site.setArgIndex(42);
		return site;
	}
}