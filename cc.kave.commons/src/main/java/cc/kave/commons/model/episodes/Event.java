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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;

public class Event {

	// capital letters are necessary for JSON compatibility!
	private EventKind Kind = EventKind.METHOD_DECLARATION;
	private TypeName Type;
	private MethodName Method;

	public EventKind getKind() {
		return Kind;
	}

	public TypeName getType() {
		return Type;
	}

	public MethodName getMethod() {
		return Method;
	}

	public void setKind(EventKind invocation) {
		this.Kind = invocation;
	}

	public void setType(TypeName typeName) {
		this.Type = typeName;
	}

	public void setMethod(MethodName methodName) {
		this.Method = methodName;
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
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}