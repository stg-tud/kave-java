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

package eclipse.commons.analysis.sstanalysistestsuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import eclipse.commons.test.PluginAstParser;

public class BaseSSTAnalysisTest {

	protected SST context;

	protected MethodDeclaration newMethodDeclaration(String identifier) {
		MethodDeclaration decl = new MethodDeclaration();
		decl.setName(CsMethodName.newMethodName(identifier));
		decl.setEntryPoint(true);

		return decl;
	}

	protected void assertAllMethods(IMethodDeclaration... expectedDecls) {
		Set<IMethodDeclaration> actualDecls = context.getMethods();

		if (expectedDecls.length != actualDecls.size()) {
			System.out.format("\nexpected %d declarations, but was:\n", expectedDecls.length);

			for (IMethodDeclaration m : actualDecls) {
				System.out.println("-----");
				System.out.println(m);
			}

			assertEquals("incorrect number of method declarations", expectedDecls.length, actualDecls.size());
		}

		for (IMethodDeclaration expectedDecl : expectedDecls) {
			if (!actualDecls.contains(expectedDecl)) {
				System.out.println("expected:");
				System.out.println(expectedDecl);
				System.out.println("but was:");

				for (IMethodDeclaration m : actualDecls) {
					System.out.println("-----");
					System.out.println(m);
				}

				assertTrue("expected method not found in actual list of method declarations",
						actualDecls.contains(expectedDecl));
			}
		}
	}

	/*
	 * Has to be called for every new test class.
	 */
	protected void updateContext(String projectName, String qualifiedName) {
		PluginAstParser parser = new PluginAstParser(projectName, qualifiedName);
		context = parser.getContext();
	}
}
