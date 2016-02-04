/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.evaluation.optimization;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.QueryOptions;

public class EvaluationOptions {
	public MiningOptions miningOptions;
	public QueryOptions queryOptions;

	public EvaluationOptions(MiningOptions miningOptions, QueryOptions queryOptions) {
		this.miningOptions = miningOptions;
		this.queryOptions = queryOptions;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public String toString() {
		return String.format("%s%s", miningOptions, queryOptions);
	}

	public static EvaluationOptions copy(EvaluationOptions other) {
		MiningOptions mOpts = new MiningOptions().setFrom(other.miningOptions);
		QueryOptions qOpts = new QueryOptions().setFrom(other.queryOptions);
		return new EvaluationOptions(mOpts, qOpts);
	}
}