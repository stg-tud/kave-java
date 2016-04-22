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
package exec.validate_evaluation.microcommits;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Usage;
import exec.validate_evaluation.queryhistory.QueryHistoryIo;

public class MicroCommitGenerationRunner {

	private static final int MAX_HISTORY_DEPTH = 10;

	private QueryHistoryIo qhIo;
	private MicroCommitIo mcIo;
	private MicroCommitGenerationLogger log;

	public MicroCommitGenerationRunner(QueryHistoryIo qhIo, MicroCommitIo mcIo, MicroCommitGenerationLogger log) {
		this.qhIo = qhIo;
		this.mcIo = mcIo;
		this.log = log;
	}

	public void run() {
		Set<String> zips = qhIo.findQueryHistoryZips();
		log.foundZips(zips);
		for (String zip : zips) {
			log.processingZip(zip);
			List<MicroCommit> mcForUser = Lists.newLinkedList();

			Collection<List<Usage>> histories = qhIo.readQueryHistories(zip);
			log.foundHistories(histories.size());

			for (List<Usage> qh : histories) {
				List<MicroCommit> mcs = createCommits(qh);
				log.convertedToCommits(qh.size(), mcs.size());
				mcForUser.addAll(mcs);
			}
			mcIo.store(mcForUser, zip);
		}

		log.done();
	}

	private List<MicroCommit> createCommits(List<Usage> qh) {

		List<MicroCommit> commits = Lists.newLinkedList();
		// TODO fix/test
		for (int i = 1; i < qh.size(); i++) {

			int first = Math.max(0, i - MAX_HISTORY_DEPTH);

			for (int j = first; j < i; j++) {
				Usage end = qh.get(i);
				Usage start = qh.get(j);

				// TODO write test for this check
				boolean atLeastOneIsRealUsage = !(start instanceof NoUsage) || !(end instanceof NoUsage);
				if (atLeastOneIsRealUsage) {
					commits.add(MicroCommit.create(start, end));
				}
			}
		}
		return commits;
	}
}