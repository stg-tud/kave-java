/**
 * Copyright 2014 Technische Universität Darmstadt
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
package cc.kave.episodes.model.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;

public class EventTest {

	private static final String DUMMY_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";

	@Test
	public void defaultValues() {
		Event sut = new Event();
		assertEquals(EventKind.METHOD_DECLARATION, sut.getKind());
		assertEquals(Names.getUnknownType(), sut.getType());
		assertEquals(Names.getUnknownMethod(), sut.getMethod());
	}

	@Test
	public void settingValues() {
		ITypeName typeName = mock(ITypeName.class);
		IMethodName methodName = mock(IMethodName.class);

		Event sut = new Event();
		sut.setKind(EventKind.INVOCATION);
		sut.setType(typeName);
		sut.setMethod(methodName);

		assertEquals(EventKind.INVOCATION, sut.getKind());
		assertEquals(typeName, sut.getType());
		assertEquals(methodName, sut.getMethod());
	}

	@Test
	public void dummyEvent() {
		Event sut = new Event();
		sut.createDummyEvent();
		
		assertEquals(EventKind.METHOD_DECLARATION, sut.getKind());
		assertEquals(Names.getUnknownType(), sut.getType());
		assertEquals(Names.newMethod(DUMMY_NAME), sut.getMethod());
	}

	@Test
	public void equalityDefault() {
		Event a = new Event();
		Event b = new Event();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equalityReallyTheSame() {
		ITypeName typeName = mock(ITypeName.class);
		IMethodName methodName = mock(IMethodName.class);

		Event a = new Event();
		a.setKind(EventKind.INVOCATION);
		a.setType(typeName);
		a.setMethod(methodName);

		Event b = new Event();
		b.setKind(EventKind.INVOCATION);
		b.setType(typeName);
		b.setMethod(methodName);

		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equalityDifferentKind() {
		Event a = new Event();
		a.setKind(EventKind.INVOCATION);
		Event b = new Event();
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equalityDifferentType() {
		Event a = new Event();
		a.setType(mock(ITypeName.class));
		Event b = new Event();
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equalityDifferentMethod() {
		Event a = new Event();
		a.setMethod(mock(IMethodName.class));
		Event b = new Event();
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}
}