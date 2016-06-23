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
package exec.recommender_reimplementation.heinemann_analysis;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.stringType;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class QueryExtractorTest extends PBNAnalysisBaseTest {

	@Test
	public void extractsIdentifiers() {
		setupDefaultEnclosingMethod(				
				varDecl("foo", stringType),
				assign("foo", referenceExpr(fieldReference("this", field(stringType, DefaultClassContext, "someField")))),
				SSTUtil.expr(SSTUtil.completionExpr("c")));
		
		CompletionEvent completionEvent = new CompletionEvent();
		completionEvent.context = context;
		
		QueryExtractor queryExtractor = new QueryExtractor();
		
		Set<String> identifiers = queryExtractor.extractQueryFromCompletion(completionEvent, 5, false, false);
		
		assertThat(identifiers, Matchers.containsInAnyOrder("string","foo","this", "some","field"));
	}

}
