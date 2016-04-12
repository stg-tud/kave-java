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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.recommenders.assertions.Asserts;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Usage;

public class QueryHistoryCollector {

	private enum State {
		UNDEFINED, PROCESSING_USER, PROCESSING_STREAK, PROCESSING_SNAPSHOT
	}

	private State currentState = State.UNDEFINED;

	private final QueryHistoryGenerationLogger log;

	private Set<Tuple<ICoReTypeName, ICoReMethodName>> keys;
	private Map<Tuple<ICoReTypeName, ICoReMethodName>, List<Usage>> queryHistories;

	private Set<Tuple<ICoReTypeName, ICoReMethodName>> keysInThisSnapshot = Sets.newHashSet();

	private Usage selectionResult;

	public QueryHistoryCollector(QueryHistoryGenerationLogger log) {
		this.log = log;
	}

	private void transition(State from, State to) {
		Asserts.assertEquals(from, currentState,
				String.format("invalid state! expected %s, but was %s", from, currentState));
		currentState = to;
	}

	private void require(State s) {
		Asserts.assertEquals(s, currentState, String.format("invalid state! expected %s, but was %s", s, currentState));
	}

	public void startUser() {
		transition(State.UNDEFINED, State.PROCESSING_USER);
		queryHistories = Maps.newLinkedHashMap();
	}

	public void startEditStreak(Set<Tuple<ICoReTypeName, ICoReMethodName>> keys) {
		transition(State.PROCESSING_USER, State.PROCESSING_STREAK);
		this.keys = keys;
	}

	public void startSnapshot() {
		transition(State.PROCESSING_STREAK, State.PROCESSING_SNAPSHOT);
	}

	public void register(Usage u) {
		require(State.PROCESSING_SNAPSHOT);
		Tuple<ICoReTypeName, ICoReMethodName> key = getKey(u);
		Asserts.assertFalse(keysInThisSnapshot.contains(key),
				"you are trying to register the same key again for this snapshot");
		registerByKey(u, key);
	}

	public void registerSelectionResult(Usage u2) {
		require(State.PROCESSING_SNAPSHOT);
		Asserts.assertNull(selectionResult);
		selectionResult = u2;
	}

	private void registerByKey(Usage u, Tuple<ICoReTypeName, ICoReMethodName> key) {
		Asserts.assertTrue(keys.contains(key));

		keysInThisSnapshot.add(key);

		List<Usage> qh = queryHistories.get(key);
		if (qh == null) {
			qh = Lists.newLinkedList();
			queryHistories.put(key, qh);
		}
		qh.add(u);
	}

	private Tuple<ICoReTypeName, ICoReMethodName> getKey(Usage u) {
		return Tuple.newTuple(u.getType(), u.getMethodContext());
	}

	public void endSnapshot() {
		transition(State.PROCESSING_SNAPSHOT, State.PROCESSING_STREAK);

		if (selectionResult != null) {
			registerByKey(selectionResult, getKey(selectionResult));
			selectionResult = null;
		}

		for (Tuple<ICoReTypeName, ICoReMethodName> key : keys) {
			if (!keysInThisSnapshot.contains(key)) {
				registerByKey(new NoUsage(), key);
			}
		}

		keysInThisSnapshot.clear();
	}

	public Set<List<Usage>> getHistories() {
		transition(State.PROCESSING_STREAK, State.UNDEFINED);
		removeRepeatingUsages();
		removeSingleHistories();
		return Sets.newLinkedHashSet(queryHistories.values());
	}

	private void removeRepeatingUsages() {
		log.startFixingHistories();
		for (List<Usage> qh : queryHistories.values()) {
			removeRepeatingUsages(qh);
		}
	}

	private void removeRepeatingUsages(List<Usage> qh) {
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
			log.fixedQueryHistory(-diff);
		}
	}

	private void removeSingleHistories() {
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