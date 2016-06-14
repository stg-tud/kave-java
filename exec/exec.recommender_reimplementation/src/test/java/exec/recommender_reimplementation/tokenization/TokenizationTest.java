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
package exec.recommender_reimplementation.tokenization;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class TokenizationTest extends PBNAnalysisBaseTest {

	@Test
	public void integrationTest() {
		SST sst = sst(
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

		sst.getFields().add(fieldDecl(field(type("B"), type("A"), "b")));

		PointsToContext context = getContextFor(sst, typeHierarchy(type("A"), type("S")),
				methodHierarchy(method(voidType, type("A"), "entry1"), method(voidType, type("S"), "entry1")));
		
		List<String> tokenStream = TokenExtractor.extractTokenStream(context);
		
		assertFalse(tokenStream.isEmpty());
		
		assertThat(tokenStream, Matchers.contains(
				"class", "A", ":", "S", 
				"{",
					"B", "b", ";",
				
					"Void", "helper", "(", ")", 
						"{", 
							"B", "tmpB", ";",
							"tmpB", "=", "this", ".", "b", ";", 
							"tmpB", ".", "m2", "(", ")", ";",
						"}",
					
					"Void", "entry1", "(", ")", 
						"{",
							"B", "tmpB", ";",  
							"tmpB", "=", "this", ".", "b", ";", 
							"tmpB", ".", "m1", "(", ")", ";", 
							"this", ".", "helper", "(" , ")",  ";",
							"C", "c", ";",
							"c", "=", "this", ".", "fromS", "(", ")", ";", 
							"c", ".", "entry2", "(", "this",".", "b", ")", ";",  
						"}",
									
				"}"));
	}
	
}
