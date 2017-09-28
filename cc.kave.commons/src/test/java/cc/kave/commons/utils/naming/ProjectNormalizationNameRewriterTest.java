/**
 * Copyright 2017 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.utils.naming;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;

public class ProjectNormalizationNameRewriterTest {

	@Test
	public void type_localtype() {
		assertNormalization(Names.newType("a.B, myproject"),
				Names.newType("a.B, ???"));
	}

	@Test
	public void type_libtype() {
		assertNormalization(Names.newType("a.B, lib, 1.2.3.4"),
				Names.newType("a.B, lib, 1.2.3.4"));
	}

	@Test
	public void method_return() {
		assertNormalization(Names.newMethod("[T,P] [T,L,1.2.3.4].m()"),
				Names.newMethod("[T,???] [T,L,1.2.3.4].m()"));
	}

	@Test
	public void method_declaring() {
		assertNormalization(Names.newMethod("[T,L,1.2.3.4] [T,P].m()"),
				Names.newMethod("[T,L,1.2.3.4] [T,???].m()"));
	}

	@Test
	public void method_param() {
		assertNormalization(
				Names.newMethod("[T,L,1.2.3.4] [T,L,2.3.4.5].m([T,P] p)"),
				Names.newMethod("[T,L,1.2.3.4] [T,L,2.3.4.5].m([T,???] p)"));
	}

	@Test
	public void generics() {
		assertNormalization(
				Names.newMethod("[R,P] [T1`1[[T -> T3,P)]],P].m2`1[[T -> T3,P]]()"),
				Names.newMethod("[R,???] [T1`1[[T]],???].m2`1[[T]]()"));
	}
	
	// still to add: fields, properties, etc..

	private void assertNormalization(ITypeName input, ITypeName expected) {
		INameRewriter sut = new ProjectNormalizationNameRewriter();
		ITypeName actual = sut.rewrite(input);
		assertEquals(expected, actual);
	}

	private void assertNormalization(IMethodName input, IMethodName expected) {
		INameRewriter sut = new ProjectNormalizationNameRewriter();
		IMethodName actual = sut.rewrite(input);
		assertEquals(expected, actual);
	}
}