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
package exec.validate_evaluation.greedy_and_endgoal;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.BoxplotData;
import cc.recommenders.evaluation.data.Measure;
import cc.recommenders.io.NestedZipFolders;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.csharp.utils.ModelHelper;
import exec.validate_evaluation.queryhistory.QueryHistoryIo;

public class GreedyAndEndGoalEval {

	private QueryHistoryIo qhIo;
	private ModelHelper mh;
	private NestedZipFolders<ICoReTypeName> usages;

	public GreedyAndEndGoalEval(NestedZipFolders<ICoReTypeName> usages, ModelHelper mh, QueryHistoryIo qhIo) {
		this.usages = usages;
		this.mh = mh;
		this.qhIo = qhIo;

	}

	public void run() {

		BoxplotData bpdGreedy = new BoxplotData();
		BoxplotData bpdGoal = new BoxplotData();

		System.out.println();
		System.out.println("reading histories...");
		Map<ICoReTypeName, List<List<Usage>>> histories = readHistories();

		System.out.println("finding usages...");
		Set<ICoReTypeName> findKeys = usages.findKeys();

		System.out.printf("finding type: ");
		for (ICoReTypeName type : findKeys) {

			if (!histories.containsKey(type)) {
				System.out.printf(".");
				continue;
			}
			System.out.println();

			System.out.printf("### %s ###\n", type);

			System.out.println("learning recommender...");
			ICallsRecommender<Query> rec = mh.get(type);

			System.out.printf("eval: ");
			for (List<Usage> hist : histories.get(type)) {
				System.out.printf("|");
				int lastIdx = hist.size() - 1;
				for (int i = 0; i < lastIdx; i++) {
					System.out.printf(".");
					Usage query = hist.get(i);

					Usage greedyExpectation = hist.get(i + 1);
					Usage goalExpectation = hist.get(lastIdx);

					if (shouldProcess(query, greedyExpectation)) {
						bpdGreedy.add(measurePredictionQuality(rec, query, greedyExpectation));
					}

					if (shouldProcess(query, goalExpectation)) {
						bpdGoal.add(measurePredictionQuality(rec, query, goalExpectation));
					}
				}
			}
			System.out.println();
			System.out.printf("finding type: ");
		}
		System.out.println();
		System.out.println();

		System.out.println("results:");
		System.out.printf("greedy: %s\n", bpdGreedy.getBoxplot());
		System.out.printf("goal: %s\n", bpdGoal.getBoxplot());

		MannWhitneyUTest mut = new MannWhitneyUTest();
		System.out.printf("p-value: %.3f\n", mut.mannWhitneyUTest(bpdGreedy.getRawValues(), bpdGoal.getRawValues()));
	}

	private Map<ICoReTypeName, List<List<Usage>>> readHistories() {
		Map<ICoReTypeName, List<List<Usage>>> histories = Maps.newHashMap();

		for (String zip : qhIo.findQueryHistoryZips()) {

			for (List<Usage> hist : qhIo.readQueryHistories(zip)) {
				ICoReTypeName type = findType(hist);

				List<List<Usage>> allHists = histories.get(type);
				if (allHists == null) {
					allHists = Lists.newArrayList();
					histories.put(type, allHists);
				}

				allHists.add(hist);
			}
		}

		return histories;
	}

	private ICoReTypeName findType(List<Usage> hist) {
		for (Usage u : hist) {
			if (!(u instanceof NoUsage)) {
				return u.getType();
			}
		}
		throw new RuntimeException("only NoUsages found :/");
	}

	private boolean shouldProcess(Usage start, Usage end) {
		return atLeastOneIsRealUsage(start, end) && hasAddition(start, end);
	}

	private boolean atLeastOneIsRealUsage(Usage start, Usage end) {
		return !(start instanceof NoUsage && end instanceof NoUsage);
	}

	private boolean hasAddition(Usage start, Usage end) {
		int numCallsStart = safe(start, end).getReceiverCallsites().size();
		int numCallsEnd = safe(end, start).getReceiverCallsites().size();
		return numCallsStart < numCallsEnd;
	}

	private double measurePredictionQuality(ICallsRecommender<Query> rec, Usage start, Usage end) {
		Usage sstart = safe(start, end);
		Usage send = safe(end, start);
		Set<ICoReMethodName> proposals = getProposals(rec, (Query) sstart);
		Set<ICoReMethodName> expectation = getExpectation(sstart, send);
		double f1 = Measure.newMeasure(expectation, proposals).getF1();
		return f1;
	}

	private Usage safe(Usage u, Usage other) {
		if (u instanceof NoUsage) {
			Query q = new Query();
			q.setType(other.getType());
			q.setDefinition(DefinitionSites.createUnknownDefinitionSite());
			q.setClassContext(other.getClassContext());
			q.setMethodContext(other.getMethodContext());
			q.setAllCallsites(Sets.newHashSet());
			return q;
		} else {
			return u;
		}
	}

	private Set<ICoReMethodName> getExpectation(Usage q, Usage end) {

		Set<ICoReMethodName> expectation = Sets.newLinkedHashSet();
		for (CallSite cs : end.getReceiverCallsites()) {
			if (!q.getAllCallsites().contains(cs)) {
				expectation.add(cs.getMethod());
			}
		}
		return expectation;
	}

	private Set<ICoReMethodName> getProposals(ICallsRecommender<Query> rec, Query query) {
		Set<ICoReMethodName> proposals = Sets.newHashSet();
		for (Tuple<ICoReMethodName, Double> p : rec.query(query)) {
			proposals.add(p.getFirst());
		}
		return proposals;
	}

	public static void main(String[] args) {
		double[] a = new double[] { 1, 2, 3, 2, 2, 2, 2 };
		double[] b = new double[] { 2, 3.1, 4, 3, 3, 3, 3, 3 };

		MannWhitneyUTest test = new MannWhitneyUTest();
		double pVal = test.mannWhitneyUTest(a, b);
		double pVal2 = test.mannWhitneyU(a, b);
		System.out.println("pval  = " + pVal + ";\nwert2: " + pVal2);
	}
}