/**
 * Copyright 2014 Technische Universität Darmstadt
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.ToStringUtils;

public class Event {

	// capital letters are necessary for JSON compatibility!
	private EventKind Kind = EventKind.METHOD_DECLARATION;
	private ITypeName Type;
	private IMethodName Method;

	public EventKind getKind() {
		return Kind;
	}

	public ITypeName getType() {
		return Type;
	}

	public IMethodName getMethod() {
		return Method;
	}

	public void setKind(EventKind invocation) {
		this.Kind = invocation;
	}

	public void setType(ITypeName typeName) {
		this.Type = typeName;
	}

	public void setMethod(IMethodName methodName) {
		this.Method = methodName;
	}

	public Event createDummyEvent() {
		String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
		IMethodName DUMMY_METHOD = Names.newMethod(DUMMY_METHOD_NAME);

		this.Method = DUMMY_METHOD;

		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
}