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
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;

public class QueryExtractionTest {

	@Test
	public void extractionOfQuery() {
		CompletionEvent completionEvent = new CompletionEvent();
		
		TestSSTBuilder builder = new TestSSTBuilder();
		
		completionEvent.context = builder.createPaperTest().get(0);
		
		IMethodDeclaration methodDecl = completionEvent.context.getSST().getMethods().stream().findFirst().get();
		methodDecl.getBody().add(SSTUtil.expr(SSTUtil.completionExpr("c")));
		
		ITypeName sType = TypeName.newTypeName("Test.PaperTest.S, Test");
		ITypeName cType = TypeName.newTypeName("Test.PaperTest.C, Test");
		ITypeName voidType = TypeName.newTypeName("System.Void, mscorlib");

		IMethodName sEntry1 = PbnPaperExample.createMethodName(voidType, sType, "entry1", "");

		Query cS = new Query();
		cS.setClassContext(convert(sType));
		cS.setMethodContext(convert(sEntry1));
		cS.setType(convert(cType));
		cS.setDefinition(DefinitionSites.createDefinitionByReturn(convert(PbnPaperExample.createMethodName(cType, sType, "fromS", ""))));
		
	}

}
