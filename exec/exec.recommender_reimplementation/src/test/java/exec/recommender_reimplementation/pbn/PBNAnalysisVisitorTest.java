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
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.intType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.objectType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.stringType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Usage;

import com.google.common.collect.Lists;

public class PBNAnalysisVisitorTest extends PBNAnalysisBaseTest {

	@Test
	public void createsUsageListForExampleSST() {
		// also tests creation of Field and Parameter Definition Site
		setupEnclosingMethod(
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				varDecl("a", intType),
				assign("a", referenceExpr(fieldReference("this", field(intType, DefaultClassContext, "Apple")))),
				invokeStmt(invokeWithParameters("a",
						method(voidType, DefaultClassContext, "m1", parameter(objectType, "foo")),
						referenceExpr(varRef("b")))), invokeStmt("a", method(voidType, DefaultClassContext, "m2")));

		context.getSST().getFields().add(fieldDecl(field(intType, DefaultClassContext, "Apple")));

		List<Usage> usageList = new ArrayList<>();
		context.getSST().accept(new PBNAnalysisVisitor(context), usageList);

		Usage queryA = query(
				intType,
				objectType,
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				DefinitionSites.createDefinitionByField(convert(field(intType, DefaultClassContext, "Apple"))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m1",
						parameter(objectType, "foo")))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m2"))));

		Usage queryB = query(
				stringType,
				objectType,
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				DefinitionSites.createDefinitionByParam(
						convert(method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b"))), 0),
				CallSites.createParameterCallSite(
						convert(method(voidType, DefaultClassContext, "m1", parameter(objectType, "foo"))), 0));

		assertQueriesWithoutSettingContexts(usageList, queryA, queryB);
	}

	@Test
	public void ignoresStatementsInExceptionHandling() {
		InvocationExpression otherInvocation = Mockito.mock(InvocationExpression.class);

		ITryBlock tryBlock = tryBlock(Lists.newArrayList(), Lists.newArrayList(invokeStmt(otherInvocation)));
		
		PBNAnalysisVisitor uut = new PBNAnalysisVisitor(context);
		methodDecl(DefaultMethodContext,true, tryBlock).accept(uut, Lists.newArrayList());

		verify(otherInvocation, never()).accept(eq(uut), Mockito.any());
	}

}
