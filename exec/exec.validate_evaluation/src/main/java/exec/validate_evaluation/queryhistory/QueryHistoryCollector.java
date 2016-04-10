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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.Usage;

public class QueryHistoryCollector {

	private final QueryHistoryGenerationLogger log;

	private Map<Tuple<ICoReMethodName, ICoReTypeName>, List<Usage>> queryHistories;

	private Usage selectionResult;

	@Inject
	public QueryHistoryCollector(QueryHistoryGenerationLogger log) {
		this.log = log;
	}

	public void startUser() {
		// TODO Auto-generated method stub
		// queryHistories = Maps.newLinkedHashMap();
	}

	public void startEditStreak(int numSnapshots, Set<Tuple<ICoReTypeName, ICoReMethodName>> keys) {

	}

	public void startSnapshot() {
		// TODO Auto-generated method stub

	}

	public void register(Usage u) {
		List<Usage> qh = queryHistories.get(getKey(u));
		if (qh == null) {
			qh = Lists.newLinkedList();
			queryHistories.put(getKey(u), qh);
		}
		qh.add(u);
	}

	public void registerSelectionResult(Usage u2) {
		// TODO Auto-generated method stub

	}

	private Tuple<ICoReMethodName, ICoReTypeName> getKey(Usage u) {
		return Tuple.newTuple(u.getMethodContext(), u.getType());
	}

	public void endSnapshot() {
		// TODO Auto-generated method stub

	}

	public Collection<List<Usage>> getHistories() {
		// TODO Auto-generated method stub
		return null;
	}

	private void removeRepeatingUsages() {
		log.startFixingHistories();
		for (List<Usage> qh : queryHistories.values()) {
			removeRepeatingUsages(qh);
		}
	}

	public void removeRepeatingUsages(List<Usage> qh) {
		Usage last = null;
		int diff = 0;

		for (Iterator<Usage> it = qh.iterator(); it.hasNext();) {
			Usage u = it.next();

			if (u.equals(last)) {
				it.remove();
				diff++;
			}

			last = u;
		}

		if (diff > 0) {
			log.fixedQueryHistory(diff);
		}
	}

	private void removeEmptyOrSingleHistories() {
		log.startingRemoveEmptyHistories();
		queryHistories.entrySet().removeIf(e -> {
			if (e.getValue().size() < 2) {
				log.removedEmptyHistory();
				return true;
			}
			return false;
		});
	}
}