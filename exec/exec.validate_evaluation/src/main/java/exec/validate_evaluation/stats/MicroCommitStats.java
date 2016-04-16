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
package exec.validate_evaluation.stats;

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
import exec.csharp.utils.QueryUtils;
import exec.validate_evaluation.microcommits.MicroCommit;
import exec.validate_evaluation.microcommits.MicroCommitIo;

public class MicroCommitStats {

	private MicroCommitIo io;
	private Map<ICoReTypeName, Map<ICoReMethodName, List<MicroCommit>>> allCommits = Maps.newHashMap();

	public MicroCommitStats(MicroCommitIo io) {
		this.io = io;
	}

	public void run() {
		for (String zip : io.findZips()) {

			System.out.println();
			System.out.printf("#####\n");
			System.out.printf("##### %s\n", zip);
			System.out.printf("#####\n");
			System.out.println();

			List<MicroCommit> mcs = io.read(zip);
			System.out.printf("%d commits\n", mcs.size());

			for (MicroCommit mc : mcs) {
				sort(mc);
			}
		}

		print();

		System.out.println();
		System.out.println();

		System.out.printf("--- overall stats ---\n");
		System.out.printf("numTypes: %d\n", usedTypes.size());
		System.out.printf("numLocations: %d\n", usedLocations.size());
		System.out.printf("numCommits: %d\n", numCommits);

		System.out.println();

		System.out.println("diff stats:");
		int numDiffs = 0;
		for (String diff : diffCounts.keySet()) {
			int diffCount = diffCounts.get(diff);
			numDiffs += diffCount;
			System.out.printf("%s\t%d\n", diff, diffCount);
		}
		System.out.printf("---\ntotal\t%d\n", numDiffs);
	}

	private void sort(MicroCommit mc) {

		validate(mc);

		ICoReTypeName type = mc.getType();
		ICoReMethodName context = mc.getMethodContext();

		Map<ICoReMethodName, List<MicroCommit>> ctxs = allCommits.get(type);
		if (ctxs == null) {
			ctxs = Maps.newHashMap();
			allCommits.put(type, ctxs);
		}

		List<MicroCommit> mcs = ctxs.get(context);
		if (mcs == null) {
			mcs = Lists.newLinkedList();
			ctxs.put(context, mcs);
		}

		Asserts.assertTrue(mcs.add(mc));
	}

	private void validate(MicroCommit mc) {
		Usage start = mc.getStart();
		Usage end = mc.getEnd();
		if (!(start instanceof NoUsage || end instanceof NoUsage)) {
			Asserts.assertEquals(start.getType(), end.getType(), "expected equal type");
			Asserts.assertEquals(start.getMethodContext(), end.getMethodContext(), "expected equal ctx");
		}
	}

	private Set<ICoReTypeName> usedTypes = Sets.newHashSet();
	private Set<Tuple<ICoReTypeName, ICoReMethodName>> usedLocations = Sets.newHashSet();
	private int numCommits = 0;
	private Map<String, Integer> diffCounts = Maps.newHashMap();

	private void print() {

		for (ICoReTypeName t : allCommits.keySet()) {
			Map<ICoReMethodName, List<MicroCommit>> ctxs = allCommits.get(t);

			usedTypes.add(t);
			System.out.println();
			System.out.printf("#### commits of '%s' ####\n", t);

			boolean isFirst = true;
			for (ICoReMethodName m : ctxs.keySet()) {
				List<MicroCommit> mcs = ctxs.get(m);

				if (isFirst) {
					Tuple<ICoReTypeName, ICoReMethodName> loc = Tuple.newTuple(t, m);
					if (!usedLocations.add(loc)) {
						System.out.println();
					}
					isFirst = false;
				}

				System.out.println();
				System.out.printf("in: %s\n", m);

				numCommits += mcs.size();

				for (MicroCommit mc : mcs) {
					String diff = QueryUtils.toDiffString(mc);
					System.out.printf("%s, ", diff);

					Integer i = diffCounts.get(diff);
					if (i == null) {
						diffCounts.put(diff, 1);
					} else {
						diffCounts.put(diff, i + 1);
					}
				}

				System.out.println();
			}
		}
	}
}