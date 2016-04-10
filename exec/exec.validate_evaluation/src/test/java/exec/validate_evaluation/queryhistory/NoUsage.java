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
package exec.validate_evaluation.queryhistory;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.collect.Sets;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.AbstractUsage;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;

public class NoUsage extends AbstractUsage {

	@Override
	public ICoReTypeName getType() {
		return null;
	}

	@Override
	public ICoReTypeName getClassContext() {
		return null;
	}

	@Override
	public ICoReMethodName getMethodContext() {
		return null;
	}

	@Override
	public DefinitionSite getDefinitionSite() {
		return DefinitionSites.createUnknownDefinitionSite();
	}

	@Override
	public Set<CallSite> getAllCallsites() {
		return Sets.newHashSet();
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}