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

import java.util.List;

import com.google.common.collect.Lists;

import cc.recommenders.usages.Usage;
import exec.validate_evaluation.queryhistory.QueryHistoryIo;

public class MicroCommitGenerationRunner {

	private QueryHistoryIo qhIo;
	private MicroCommitIo mcIo;
	private MicroCommitGenerationLogger log;

	public MicroCommitGenerationRunner(QueryHistoryIo qhIo, MicroCommitIo mcIo, MicroCommitGenerationLogger log) {
		this.qhIo = qhIo;
		this.mcIo = mcIo;
		this.log = log;
	}

	public void run() {
		for (String zip : qhIo.findQueryHistoryZips()) {
			for (List<Usage> qh : qhIo.readQueryHistories(zip)) {
				List<MicroCommit> mc = createCommits(qh);
			}
		}
	}

	private List<MicroCommit> createCommits(List<Usage> qh) {
		List<MicroCommit> commits = Lists.newLinkedList();
		for (int i = 1; i < qh.size(); i++) {
			for (int j = 0; j < i; j++) {

			}
		}
		return commits;
	}
}