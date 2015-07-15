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

package cc.kave.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.csharp.CsMethodName;

public class CsMethodNameTest {

	private MethodName name = CsMethodName
			.newMethodName("[org.eclipse.jdt.core.dom.CompilationUnit, org.eclipse.jdt.core.dom, VersionPlaceholder]"
					+ " [de.vogella.jdt.astsimple.handler.GetInfo, de.vogella.jdt.astsimple.handler, VersionPlaceholder]"
					+ ".findClass([org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core, VersionPlaceholder] packages, "
					+ "[java.lang.String, java.lang, VersionPlaceholder] className)");

	@Test
	public void getSignature() {
		String actual = name.getSignature();
		String expected = "findClass([org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core, VersionPlaceholder] packages, "
				+ "[java.lang.String, java.lang, VersionPlaceholder] className)";

		assertEquals(expected, actual);
	}

	@Test
	public void getParameterFirst() {
		String actual = name.getParameters().get(0).getIdentifier();
		String expected = "[org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core, VersionPlaceholder] packages";

		assertEquals(expected, actual);
	}

	@Test
	public void getParameterSecond() {
		String actual = name.getParameters().get(1).getIdentifier();
		String expected = "[java.lang.String, java.lang, VersionPlaceholder] className";

		assertEquals(expected, actual);
	}

	@Test
	public void getParameters() {
		String actual = "";

		for (ParameterName p : name.getParameters()) {
			actual += p.getIdentifier() + ", ";
		}
		actual = actual.substring(0, actual.length() - 2);
		String expected = "[org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core, VersionPlaceholder] packages, "
				+ "[java.lang.String, java.lang, VersionPlaceholder] className";

		assertEquals(expected, actual);
	}

	@Test
	public void hasParameters() {
		boolean actual = name.hasParameters();
		boolean expected = true;

		assertEquals(expected, actual);
	}

	@Test
	public void hasParametersFalse() {
		MethodName methodName = CsMethodName
				.newMethodName("[org.eclipse.jdt.core.dom.CompilationUnit, org.eclipse.jdt.core.dom, VersionPlaceholder] "
						+ "[de.vogella.jdt.astsimple.handler.GetInfo, de.vogella.jdt.astsimple.handler, VersionPlaceholder]"
						+ ".findClass()");
		boolean actual = methodName.hasParameters();
		boolean expected = false;

		assertEquals(expected, actual);
	}

	@Test
	public void isConstructor() {
		boolean actual = name.isConstructor();
		boolean expected = false;

		assertEquals(expected, actual);
	}

	@Test
	public void isConstructorFalse() {
		MethodName methodName = CsMethodName.newMethodName("[void, void, VersionPlaceholder] "
				+ "[de.vogella.jdt.astsimple.handler.GetInfo, de.vogella.jdt.astsimple.handler, VersionPlaceholder]"
				+ ".GetInfo()");
		boolean actual = name.isConstructor();
		boolean expected = false;

		assertEquals(expected, actual);
	}

	@Test
	public void getReturnType() {
		String actual = name.getReturnType().getIdentifier();
		String expected = "org.eclipse.jdt.core.dom.CompilationUnit, org.eclipse.jdt.core.dom, VersionPlaceholder";

		assertEquals(expected, actual);
	}
}