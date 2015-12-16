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

package cc.kave.commons.model.names;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.names.IEventName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.TypeName;

public class EventNameTest {

	@Test
	public void shouldImplementIsUnknown() {
		assertTrue(EventName.UNKNOWN_NAME.isUnknown());
	}

	@Test
	public void shouldBeSimpleEvent() {
		IEventName eventName = EventName
				.newEventName("[ChangedEventHandler, IO, 1.2.3.4] [TextBox, GUI, 0.8.7.6].Changed");

		assertEquals("ChangedEventHandler", eventName.getHandlerType().getFullName());
		assertEquals("TextBox", eventName.getDeclaringType().getFullName());
		assertEquals("Changed", eventName.getName());
	}

	@Test
	public void shouldBeUnknownEvent() {
		assertEquals(TypeName.UNKNOWN_NAME, EventName.UNKNOWN_NAME.getHandlerType());
		assertEquals(TypeName.UNKNOWN_NAME, EventName.UNKNOWN_NAME.getDeclaringType());
		assertEquals("???", EventName.UNKNOWN_NAME.getName());
	}
}
