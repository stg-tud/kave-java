/**
 * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.commons.model.episodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import cc.kave.commons.model.naming.codeelements.IMethodName;

public class EventsTest {

	@Test
	public void methodDecl() {
		IMethodName m = mock(IMethodName.class);
		Event actual = Events.newContext(m);

		assertEquals(EventKind.METHOD_DECLARATION, actual.getKind());
		assertSame(m, actual.getMethod());
		assertNull(actual.getType());
	}

	@Test
	public void invocations() {
		IMethodName m = mock(IMethodName.class);
		Event actual = Events.newInvocation(m);

		assertEquals(EventKind.INVOCATION, actual.getKind());
		assertSame(m, actual.getMethod());
		assertNull(actual.getType());
	}
	
	@Test
	public void holder() {
		Event actual = Events.newHolder();
		
		assertEquals(EventKind.CONTEXT_HOLDER, actual.getKind());
		assertNull(actual.getType());
	}
	
	@Test
	public void dummy() {
		Event actual = Events.newDummyEvent();
		
		String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
		IMethodName DUMMY_METHOD = MethodName.newMethodName(DUMMY_METHOD_NAME);
		
		assertEquals(EventKind.METHOD_DECLARATION, actual.getKind());
		assertSame(DUMMY_METHOD, actual.getMethod());
		assertNull(actual.getType());
	}
}