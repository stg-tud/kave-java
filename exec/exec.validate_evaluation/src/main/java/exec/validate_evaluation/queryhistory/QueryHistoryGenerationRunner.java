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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.names.IMethodName;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.validate_evaluation.streaks.EditStreak;
import exec.validate_evaluation.streaks.EditStreakGenerationIo;
import exec.validate_evaluation.streaks.Snapshot;
import exec.validate_evaluation.utils.CoReNameUtils;

public class QueryHistoryGenerationRunner {

	private final QueryHistoryIo io;
	private final QueryHistoryGenerationLogger log;
	private final IUsageExtractor usageExtractor;
	private final QueryHistoryCollector histCollector;
	private final EditStreakGenerationIo esIo;

	private Set<EditStreak> editStreaks;

	public QueryHistoryGenerationRunner(EditStreakGenerationIo esIo, QueryHistoryIo io,
			QueryHistoryGenerationLogger log, QueryHistoryCollector histCollector, IUsageExtractor usageExtractor) {
		this.esIo = esIo;
		this.io = io;
		this.log = log;
		this.histCollector = histCollector;
		this.usageExtractor = usageExtractor;
	}

	public void run() {
		Set<String> zips = esIo.findEditStreakZips();
		log.foundZips(zips);

		for (String zip : zips) {
			histCollector.startUser();

			log.processingFile(zip);
			editStreaks = esIo.readEditStreaks(zip);
			log.foundEditStreaks(editStreaks.size());

			for (EditStreak es : editStreaks) {
				process(es);
			}

			Collection<List<Usage>> us = histCollector.getHistories();
			io.storeQueryHistories(us, zip);
		}

		log.finish();
	}

	private void process(EditStreak e) {

		log.processingEditStreak(e);

		Map<Snapshot, List<Usage>> allUsages = Maps.newHashMap();
		Map<Snapshot, Usage> allQueries = Maps.newHashMap();

		for (Snapshot s : e.getSnapshots()) {
			IAnalysisResult result = usageExtractor.analyse(s.getContext());
			allUsages.put(s, result.getUsages());
			allQueries.put(s, result.getFirstQuery());
		}

		histCollector.startEditStreak(getKeys(allUsages));

		for (Snapshot s : e.getSnapshots()) {
			log.startSnapshot();
			histCollector.startSnapshot();
			List<Usage> usages = allUsages.get(s);
			Usage query = allQueries.get(s);
			for (Usage u : usages) {
				histCollector.register(u);
				log.usage();

				boolean isQuery = u.equals(query);
				if (isQuery && s.hasSelection()) {
					Usage u2 = merge(u, s.getSelection());
					histCollector.registerSelectionResult(u2);
					log.usageMerged();
				}
			}
			histCollector.endSnapshot();
		}
	}

	private Usage merge(Usage u, IMethodName m) {
		CallSite call = CallSites.createReceiverCallSite(CoReNameUtils.toCoReName(m));
		Query q = Query.createAsCopyFrom(u);
		q.addCallSite(call);
		return q;

	}

	private static Set<Tuple<ICoReTypeName, ICoReMethodName>> getKeys(Map<Snapshot, List<Usage>> allUsages) {

		Set<Tuple<ICoReTypeName, ICoReMethodName>> keys = Sets.newHashSet();

		for (List<Usage> us : allUsages.values()) {
			for (Usage u : us) {
				Tuple<ICoReTypeName, ICoReMethodName> key = Tuple.newTuple(u.getType(), u.getMethodContext());
				keys.add(key);
			}
		}

		return keys;
	}
}