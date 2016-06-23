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

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class IndexExtractorTest extends PBNAnalysisBaseTest {

	@Test
	public void standardPaperTest() {
		Context context = getContextFor(sst(DefaultClassContext, createPaperTest()), defaultTypeHierarchy());
		
		Set<Entry> index = IndexExtractor.extractIndex(context, 5, false, false);
		
		Entry entry1 = new Entry(Sets.newHashSet("io","except","string","error","messag"), method(stringType, DefaultClassContext, "getMessage"));
		Entry entry2 = new Entry(Sets.newHashSet("string", "error", "messag", "get"), 
				method(voidType, type("JOptionPane"), "showMessageDialog", parameter(type("Component"), "component"),   
						parameter(objectType, "message")));
		Entry entry3 = new Entry(Sets.newHashSet(ExtractionUtil.EMPTY_TOKEN), method(voidType, DefaultClassContext, "readFile"));
		
		assertThat(index, Matchers.containsInAnyOrder(entry1,entry2, entry3));
	}
	
	@Test
	public void preserveKeywords() {
		IfElseBlock ifBlock = new IfElseBlock();
		ifBlock.setCondition(constant("true"));
		ifBlock.setThen(Lists.newArrayList(
				varDecl("someMessage",stringType),
				assign("someMessage", invoke("e", method(stringType, DefaultClassContext, "getMessage")))));
		setupDefaultEnclosingMethod(
				varDecl("foo", intType),
				ifBlock);
		
		Set<Entry> index = IndexExtractor.extractIndex(context, 5, false, false);
		
		Entry entry = new Entry(Sets.newHashSet("<if>","true","string","some","messag"), method(stringType, DefaultClassContext, "getMessage"));
		
		assertThat(index, Matchers.containsInAnyOrder(entry));
	}
	
	@Test
	public void removesKeywords() {
		IfElseBlock ifBlock = new IfElseBlock();
		ifBlock.setCondition(constant("true"));
		ifBlock.setThen(Lists.newArrayList(
				varDecl("someMessage",stringType),
				assign("someMessage", invoke("e", method(stringType, DefaultClassContext, "getMessage")))));
		setupDefaultEnclosingMethod(				
				varDecl("foo", intType),
				ifBlock);
		
		Set<Entry> index = IndexExtractor.extractIndex(context, 5, false, true);
		
		Entry entry = new Entry(Sets.newHashSet("foo","true","string","some","messag"), method(stringType, DefaultClassContext, "getMessage"));
		
		assertThat(index, Matchers.containsInAnyOrder(entry));
	}
	
	@Test
	public void preservesStopwords() {
		setupDefaultEnclosingMethod(				
				varDecl("foo", stringType),
				assign("foo", referenceExpr(fieldReference("this", field(stringType, DefaultClassContext, "someField")))),
				invokeStmt("this", method(voidType, DefaultClassContext, "someMethod")));
		
		Set<Entry> index = IndexExtractor.extractIndex(context, 5, false, false);
		
		Entry entry = new Entry(Sets.newHashSet("string","foo","this", "some","field"), method(voidType, DefaultClassContext, "someMethod"));
		
		assertThat(index, Matchers.is(Sets.newHashSet(entry)));
	}
	
	@Test
	public void removesStopwords() {
		setupDefaultEnclosingMethod(				
				varDecl("foo", stringType),
				assign("foo", referenceExpr(fieldReference("this", field(stringType, DefaultClassContext, "someField")))),
				invokeStmt("who", method(voidType, DefaultClassContext, "someMethod")));
		
		Set<Entry> index = IndexExtractor.extractIndex(context, 5, true, false);
		
		Entry entry = new Entry(Sets.newHashSet("string","foo", "this","some","field"), method(voidType, DefaultClassContext, "someMethod"));
		
		assertThat(index, Matchers.is(Sets.newHashSet(entry)));
	}

	public IMethodDeclaration createPaperTest() {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(Lists.newArrayList(invokeStmt("this", method(voidType, DefaultClassContext, "readFile"))));
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setKind(CatchBlockKind.Default);
		catchBlock.setParameter(parameter(type("IOException"), "e"));
		catchBlock.setBody(Lists.newArrayList(
				varDecl("errorMessage",stringType),
				assign("errorMessage", invoke("e", method(stringType, DefaultClassContext, "getMessage"))),
				invokeStmt(invokeWithParameters("JOptionPane", method(voidType, type("JOptionPane"), "showMessageDialog", parameter(type("Component"), "component"),   
						parameter(objectType, "message")), new NullExpression(), referenceExpr(varRef("errorMessage"))))));
		tryBlock.setCatchBlocks(Lists.newArrayList(catchBlock));
		
		return methodDecl(DefaultMethodContext, true, tryBlock);
	}
}
