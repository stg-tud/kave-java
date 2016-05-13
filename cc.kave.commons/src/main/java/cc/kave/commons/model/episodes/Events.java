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

import cc.kave.commons.model.naming.codeelements.IMethodName;

public class Events {
	public static Event newContext(IMethodName ctx) {
		Event event = new Event();
		event.setKind(EventKind.METHOD_DECLARATION);
		event.setMethod(ctx);
		return event;
	}

	public static Event newInvocation(IMethodName m) {
		Event event = new Event();
		event.setKind(EventKind.INVOCATION);
		event.setMethod(m);
		return event;
	}
	
	public static Event newHolder() {
		Event event = new Event();
		event.setKind(EventKind.CONTEXT_HOLDER);
		return event;
	}
	
	public static Event newDummyEvent() {
		String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
		IMethodName DUMMY_METHOD = MethodName.newMethodName(DUMMY_METHOD_NAME);
		Event DUMMY_EVENT = Events.newContext(DUMMY_METHOD);
		return DUMMY_EVENT;
	}
	
	public static Event newUnknownEvent() {
		String UNKOWN_METHOD_NAME = "[?] [?].???()";
		IMethodName UNKOWN_METHOD = MethodName.newMethodName(UNKOWN_METHOD_NAME);
		Event UNKNOWN_EVENT = Events.newContext(UNKOWN_METHOD);
		UNKNOWN_EVENT.setKind(EventKind.METHOD_DECLARATION);
		return UNKNOWN_EVENT;
	}
}