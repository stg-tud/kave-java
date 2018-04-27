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
package cc.kave.episodes.model.events;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;

public class Events {
	public static Event newType(ITypeName type) {
		Event event = new Event();
		event.setKind(EventKind.TYPE);
		event.setType(type);
		return event;
	}
	
	public static Event newElementContext(IMethodName ctx) {
		Event event = new Event();
		event.setKind(EventKind.METHOD_DECLARATION);
		event.setMethod(ctx);
		return event;
	}
	
	public static Event newElementContext(String ctx) {
		Event event = new Event();
		event.setKind(EventKind.METHOD_DECLARATION);
		event.setMethod(Names.newMethod(ctx));
		return event;
	}

	public static Event newFirstContext(IMethodName ctx) {
		Event event = new Event();
		event.setKind(EventKind.FIRST_DECLARATION);
		event.setMethod(ctx);
		return event;
	}
	
	public static Event newFirstContext(String ctx) {
		Event event = new Event();
		event.setKind(EventKind.FIRST_DECLARATION);
		event.setMethod(Names.newMethod(ctx));
		return event;
	}

	public static Event newSuperContext(IMethodName ctx) {
		Event event = new Event();
		event.setKind(EventKind.SUPER_DECLARATION);
		event.setMethod(ctx);
		return event;
	}
	
	public static Event newSuperContext(String ctx) {
		Event event = new Event();
		event.setKind(EventKind.SUPER_DECLARATION);
		event.setMethod(Names.newMethod(ctx));
		return event;
	}

	public static Event newInvocation(IMethodName m) {
		Event event = new Event();
		event.setKind(EventKind.INVOCATION);
		event.setMethod(m);
		return event;
	}
	
	public static Event newInvocation(String m) {
		Event event = new Event();
		event.setKind(EventKind.INVOCATION);
		event.setMethod(Names.newMethod(m));
		return event;
	}

	public static Event newDummyEvent() {
		String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
		IMethodName DUMMY_METHOD = Names.newMethod(DUMMY_METHOD_NAME);
		Event DUMMY_EVENT = Events.newElementContext(DUMMY_METHOD);
		return DUMMY_EVENT;
	}
}