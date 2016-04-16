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
package exec.validate_evaluation.basicexcel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Boxplot;
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
import exec.csharp.queries.IQueryBuilder;
import exec.csharp.queries.QueryBuilderFactory;
import exec.csharp.queries.QueryMode;
import exec.csharp.utils.ModelHelper;
import exec.csharp.utils.QueryUtils;
import exec.csharp.utils.StorageCase;
import exec.csharp.utils.StorageHelper;
import exec.validate_evaluation.microcommits.MicroCommit;
import exec.validate_evaluation.microcommits.MicroCommitIo;

public class BasicExcelEvaluation {

	private MicroCommitIo mcIo;
	private ModelHelper mh;
	private QueryBuilderFactory qbf;
	private NestedZipFolders<ICoReTypeName> usages;
	private IQueryBuilder<Usage, Query> qb;
	private ICallsRecommender<Query> rec;

	@Inject
	public BasicExcelEvaluation(StorageHelper storageHelper, MicroCommitIo mcIo, ModelHelper mh,
			QueryBuilderFactory qb) {
		this.mcIo = mcIo;
		this.mh = mh;
		this.qbf = qb;
		this.usages = storageHelper.getNestedZipFolder(StorageCase.USAGES);
	}

	public void run() {

		// String = QueryMode\tDiffString
		Map<String, BoxplotData> res2 = Maps.newHashMap();
		Map<QueryMode, BoxplotData> res = Maps.newHashMap();
		Map<Tuple<QueryMode, ICoReTypeName>, BoxplotData> res3 = Maps.newHashMap();
		Map<ICoReTypeName, Integer> typeUsageCounts = Maps.newHashMap();

		for (QueryMode mode : QueryMode.values()) {
			res.put(mode, new BoxplotData());
		}

		System.out.printf("\ninitializing nested .zip folder for types/usages... (%s)\n", new Date());
		Set<ICoReTypeName> types = usages.findKeys();
		for (ICoReTypeName type : types) {
			List<Usage> us = usages.readAllZips(type, Usage.class);
			if (us.size() < 1) {
				continue;
			}
			typeUsageCounts.put(type, us.size());
			rec = mh.get(type);

			for (String zip : mcIo.findZips()) {

				// System.out.println("mode\tcategory\tf1");

				List<MicroCommit> readCommits = readCommits(zip, type);
				if (readCommits.isEmpty()) {
					continue;
				}

				System.out.printf("\n### found %d commits ###\nfor %s\nin %s\n\n", readCommits.size(), type, zip);

				for (QueryMode mode : QueryMode.values()) {
					System.out.println(mode);

					BoxplotData boxplotByType = new BoxplotData();
					Tuple<QueryMode, ICoReTypeName> typeKey = Tuple.newTuple(mode, type);
					res3.put(typeKey, boxplotByType);

					qb = qbf.get(mode);

					for (MicroCommit mc : readCommits) {
						Usage start = mc.getStart();
						Usage end = mc.getEnd();

						if (shouldSkip(start, end)) {
							continue;
						}

						double f1 = measurePredictionQuality(start, end);
						String diff = QueryUtils.toDiffString(mc);

						res.get(mode).add(f1);
						boxplotByType.add(f1);

						String k2 = mode + "\t" + diff;
						BoxplotData bp = res2.get(k2);
						if (bp == null) {
							bp = new BoxplotData();
							res2.put(k2, bp);
						}
						bp.add(f1);

						// StringBuilder sb = new StringBuilder();
						//
						// sb.append(mode);
						// sb.append('\t');
						// sb.append(diff);
						// sb.append('\t');
						// sb.append(f1);
						// sb.append('\n');
						//
						// System.out.printf(sb.toString());
						System.out.printf(".");
					}
					System.out.println();
				}
			}
		}

		System.out.println("done at " + new Date());

		System.out.println();
		System.out.println();
		System.out.println("### RES1 -- grouped by (mode) ###");
		System.out.println("mode -> f1 (boxplot)");
		for (QueryMode m : res.keySet()) {
			Boxplot bp = res.get(m).getBoxplot();
			System.out.printf("%s -> %.3f (%s)\n", m, bp.getMean(), bp);
		}

		System.out.println();
		System.out.println();
		System.out.println("### RES2 -- grouped by (mode+diff) ###");
		System.out.println("mode\tdiff\tcount\tf1\tbp");
		for (String k : res2.keySet()) {
			Boxplot bp = res2.get(k).getBoxplot();
			System.out.printf("%s\t%d\t%.3f\t%s\n", k, bp.getNumValues(), bp.getMean(), bp);
		}

		System.out.println();
		System.out.println();
		System.out.println("### RES3 -- grouped by type ###");
		System.out.println("type\tnumUsages\tcount\tf1\tbp");
		for (Tuple<QueryMode, ICoReTypeName> modeAndType : res3.keySet()) {
			Boxplot bp = res3.get(modeAndType).getBoxplot();

			QueryMode mode = modeAndType.getFirst();
			ICoReTypeName type = modeAndType.getSecond();

			System.out.printf("%s\t%s\t%d\t%d\t%.3f\t%s\n", mode, type, typeUsageCounts.get(type), bp.getNumValues(),
					bp.getMean(), bp);
		}
	}

	private Map<String, List<MicroCommit>> allCommits = Maps.newHashMap();

	private List<MicroCommit> readCommits(String zip, ICoReTypeName type) {
		List<MicroCommit> commits = allCommits.get(zip);
		if (commits == null) {
			commits = mcIo.read(zip);
			allCommits.put(zip, commits);
		}

		List<MicroCommit> byType = Lists.newLinkedList();
		for (MicroCommit c : commits) {
			if (type.equals(c.getType())) {
				byType.add(c);
			}
		}
		// return commits.stream().filter(mc ->
		// type.equals(mc.getType())).collect(Collectors.toList());
		return byType;
	}

	private boolean shouldSkip(Usage start, Usage end) {
		int numAdditions = QueryUtils.countAdditions(start, end);
		if (0 == numAdditions) {
			return true;
		}
		return false;
	}

	private double measurePredictionQuality(Usage start, Usage end) {
		List<Query> queries = qb.createQueries(safe(start, end), safe(end, start));
		BoxplotData res = new BoxplotData();
		for (Query q : queries) {
			Set<ICoReMethodName> proposals = getProposals(rec, q);
			Set<ICoReMethodName> expectation = getExpectation(q, end);
			Measure measure = Measure.newMeasure(expectation, proposals);
			res.add(measure.getF1());
		}
		return res.getMean();
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
}