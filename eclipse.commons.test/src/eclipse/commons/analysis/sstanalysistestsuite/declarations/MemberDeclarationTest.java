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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;

import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;

public class MemberDeclarationTest extends BaseSSTAnalysisTest {

	private final String projectName = "testproject";
	private final String packageName = "memberdeclarationanalysistest;";

	@Test
	public void fieldDeclaration() {
		updateContext(projectName, packageName + "FieldDeclaration.java");

		Set<IFieldDeclaration> actual = context.getFields();
		Set<IFieldDeclaration> expected = new HashSet<>();
		expected.add(newFieldDeclaration(
				"[%int, rt.jar, 1.8] [memberdeclarationanalysistest.FieldDeclaration, ?]._f"));

		assertEquals(expected, actual);
	}

	@Test
	public void methodDeclaration() {
		updateContext(projectName, packageName + "MethodDeclaration.java");

		cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration m = newMethodDeclaration(
				"[%void, rt.jar, 1.8] [memberdeclarationanalysistest.MethodDeclaration, ?].M()");
		cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration n = newMethodDeclaration(
				"[%void, rt.jar, 1.8] [memberdeclarationanalysistest.MethodDeclaration, ?].N()");
		n.setEntryPoint(false);

		Set<IMethodDeclaration> expected = newSet(m, n);
		Set<IMethodDeclaration> actual = context.getMethods();

		assertEquals(expected, actual);
	}

	@Test
	public void nestedClass_Methods() {
		updateContext(projectName, packageName + "NestedClass_Methods.java");
		
		Set<IMethodDeclaration> actual = context.getMethods();
		Set<IMethodDeclaration> expected = new HashSet<>();

		assertEquals(expected, actual);
	}
	
	@Test
	public void nestedClass_Fields() {
		updateContext(projectName, packageName + "NestedClass_Fields.java");
		
		Set<IFieldDeclaration> actual = context.getFields();
		Set<IFieldDeclaration> expected = new HashSet<>();

		assertEquals(expected, actual);
	}

}
