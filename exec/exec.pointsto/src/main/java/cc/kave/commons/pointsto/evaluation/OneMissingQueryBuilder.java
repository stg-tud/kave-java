/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.random.RandomGenerator;

import com.google.inject.Inject;

import cc.recommenders.evaluation.queries.QueryBuilder;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class OneMissingQueryBuilder implements QueryBuilder<Usage, Query> {

	private final RandomGenerator rndGenerator;
	private int maxNumQueries = 3;

	@Inject
	public OneMissingQueryBuilder(RandomGenerator rndGenerator) {
		this.rndGenerator = rndGenerator;
	}

	@Override
	public List<Query> createQueries(Usage in) {
		List<CallSite> receiverCallSites = new ArrayList<>(in.getReceiverCallsites());
		if (receiverCallSites.isEmpty()) {
			return Collections.emptyList();
		}

		int numQueries = Math.min(maxNumQueries, receiverCallSites.size());
		Set<Set<CallSite>> queriesCallSites = new HashSet<>(numQueries);
		int iters = 0;
		while (queriesCallSites.size() < numQueries && iters < 100) {
			int missingIndex = rndGenerator.nextInt(receiverCallSites.size());
			CallSite missingCallSite = receiverCallSites.get(missingIndex);
			Set<CallSite> newCallSites = new HashSet<>(in.getAllCallsites());
			newCallSites.remove(missingCallSite);
			queriesCallSites.add(newCallSites);

			++iters;
		}

		List<Query> queries = new ArrayList<>(maxNumQueries);
		for (Set<CallSite> callSites : queriesCallSites) {
			Query query = Query.createAsCopyFrom(in);
			query.setAllCallsites(callSites);
			queries.add(query);
		}
		return queries;
	}

}
