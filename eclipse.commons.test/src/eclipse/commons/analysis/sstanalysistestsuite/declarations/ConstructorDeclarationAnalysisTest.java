/**
 * Copyright 2015 Waldemar Graf
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

package eclipse.commons.analysis.sstanalysistestsuite.declarations;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;

public class ConstructorDeclarationAnalysisTest extends BaseSSTAnalysisTest {

	public ConstructorDeclarationAnalysisTest() {
		packageName = getClass().getSimpleName();
	}
	
	@Test
	public void defaultConstructorIsNotCaptured() {
		updateContext();
		assertEquals(newSet(), context.getMethods());
	}

	@Test
	public void explicitDefinition() {
		updateContext();
		MethodDeclaration m = newMethodDeclaration("[constructordeclarationanalysistest.ExplicitDefinition, ?] [constructordeclarationanalysistest.ExplicitDefinition, ?]..ctor()");

		Set<MethodDeclaration> expected = newSet(m);
		Set<IMethodDeclaration> actual = context.getMethods();

		assertEquals(expected, actual);
	}

	@Test
	public void bodyIsAnalyzed() {
		updateContext();
		MethodDeclaration ctor = newMethodDeclaration("[constructordeclarationanalysistest.BodyIsAnalyzed, ?] [constructordeclarationanalysistest.BodyIsAnalyzed, ?]..ctor()");
		ReturnStatement returnStatement = new ReturnStatement();
		returnStatement.setIsVoid(true);
		ctor.getBody().add(returnStatement);

		Set<MethodDeclaration> expected = newSet(ctor);
		Set<IMethodDeclaration> actual = context.getMethods();

		assertEquals(expected, actual);
	}

	@Test
	public void Invalid() {
		// TODO: ask if to parse invalid syntax
	}

	@Test
	public void ConstructorsOfNestedClasses() {
		updateContext();
		MethodDeclaration m = newMethodDeclaration("[constructordeclarationanalysistest.ConstructorsOfNestedClasses, ?] [constructordeclarationanalysistest.ConstructorsOfNestedClasses, ?]..ctor()");

		Set<MethodDeclaration> expected = newSet(m);
		Set<IMethodDeclaration> actual = context.getMethods();

		assertEquals(expected, actual);
	}
}
