/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.pbn;

import static cc.kave.commons.pointsto.extraction.CoReNameConverter.convert;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.objectType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Usage;

public class PbnPaperExample extends PBNAnalysisBaseTest {

	public List<Context> contexts;

	private UsageExtractor uut = new UsageExtractor();

	@Before
	public void LoadPaperExample() {
		SST sstA = sst(
				type("A"),
				methodDecl(
						method(voidType, type("A"), "entry1"),
						true,
						varDecl("tmpB", type("B")),
						assign("tmpB", referenceExpr(fieldReference("this", field(type("B"), type("A"), "b")))),
						invokeStmt("tmpB", method(voidType, type("B"), "m1")),
						invokeStmt("this", method(voidType, type("A"), "helper")),
						varDecl("c", type("C")),
						assign("c", invoke("this", method(type("C"), type("S"), "fromS"))),
						invokeStmt(invokeWithParameters("c",
								method(voidType, type("C"), "entry2", parameter(type("B"), "b")),
								referenceExpr(fieldReference("this", field(type("B"), type("A"), "b")))))),
				methodDecl(method(voidType, type("A"), "helper"), false, varDecl("tmpB", type("B")),
						assign("tmpB", referenceExpr(fieldReference("this", field(type("B"), type("A"), "b")))),
						invokeStmt("tmpB", method(voidType, type("B"), "m2"))));

		sstA.getFields().add(fieldDecl(field(type("B"), type("A"), "b")));

		PointsToContext ctxA = getContextFor(sstA, typeHierarchy(type("A"), type("S")),
				methodHierarchy(method(voidType, type("A"), "entry1"), method(voidType, type("S"), "entry1")));

		SST sstC = sst(
				type("C"),
				methodDecl(method(voidType, type("C"), "entry2", parameter(type("B"), "b")), true,
						invokeStmt("b", method(voidType, type("B"), "m3")),
						invokeStmt("this", method(voidType, type("C"), "entry3"))),
				methodDecl(
						method(voidType, type("C"), "entry3"),
						true,
						varDecl("d", type("D")),
						assign("d", constructor(type("D"))),
						tryBlock(Lists.newArrayList(invokeStmt("d", method(voidType, type("D"), "m4"))),
								Lists.newArrayList(invokeStmt("d", method(voidType, type("D"), "m5"))))));

		PointsToContext ctxB = getContextFor(sstC, typeHierarchy(type("A")),
				methodHierarchy(method(voidType, type("C"), "entry2", parameter(type("B"), "b"))),
				methodHierarchy(method(voidType, type("C"), "entry3")));

		contexts = Lists.newArrayList(ctxA, ctxB);
	}

	@Test
	public void CheckPaperExample() {
		List<Usage> usageList = new ArrayList<>();
		for (Context ctx : contexts) {
			uut.extractUsageFromContext(ctx, usageList);
		}
		ITypeName sType = type("S");
		ITypeName aType = type("A");
		ITypeName bType = type("B");
		ITypeName cType = type("C");
		ITypeName dType = type("D");

		IMethodName sEntry1 = method(voidType, sType, "entry1");
		IMethodName cEntry2 = method(voidType, cType, "entry2", parameter(bType, "b"));
		IMethodName cEntry3 = method(voidType, cType, "entry3");

		IFieldName bField = field(bType, aType, "b");

		Usage bS = query(bType, sType, sEntry1, DefinitionSites.createDefinitionByField(convert(bField)),
				CallSites.createReceiverCallSite(convert(method(voidType, bType, "m1"))),
				CallSites.createReceiverCallSite(convert(method(voidType, bType, "m2"))),
				CallSites.createParameterCallSite(convert(cEntry2), 0));

		Usage cS = query(cType, sType, sEntry1,
				DefinitionSites.createDefinitionByReturn(convert(method(cType, sType, "fromS"))),
				CallSites.createReceiverCallSite(convert(cEntry2)));

		Usage sS = query(sType, sType, sEntry1, DefinitionSites.createDefinitionByThis(),
				CallSites.createReceiverCallSite(convert(method(cType, sType, "fromS"))));

		Usage bC = query(bType, objectType, cEntry2, DefinitionSites.createDefinitionByParam(convert(cEntry2), 0),
				CallSites.createReceiverCallSite(convert(method(voidType, bType, "m3"))));

		Usage dC = query(dType, objectType, cEntry3, DefinitionSites.createDefinitionByConstructor(convert(method(voidType, dType, ".ctor"))), 
				CallSites.createReceiverCallSite(convert(method(voidType, dType, "m4"))));
		
		assertQueriesWithoutSettingContexts(usageList, bS,cS,sS,bC,dC);;
	}

}
