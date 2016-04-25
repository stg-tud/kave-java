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
package exec.validate_evaluation.categorized;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
import exec.csharp.queries.IQueryBuilder;
import exec.csharp.queries.QueryBuilderFactory;
import exec.csharp.queries.QueryMode;
import exec.csharp.utils.ModelHelper;
import exec.validate_evaluation.microcommits.MicroCommit;

public abstract class CategorizedEvaluation<Category> {

	private NestedZipFolders<ICoReTypeName> usages;
	private MicroCommitIoExtension mcIo;
	private CategorizedEvaluationLogger<Category> log;
	private ModelHelper mh;
	private QueryBuilderFactory qbf;
	private IQueryBuilder<Usage, Query> qb;
	private ICallsRecommender<Query> rec;

	public CategorizedEvaluation(NestedZipFolders<ICoReTypeName> usages, ModelHelper mh, MicroCommitIoExtension mcIo,
			CategorizedEvaluationLogger<Category> log, QueryBuilderFactory qbf) {
		this.usages = usages;
		this.mh = mh;
		this.mcIo = mcIo;
		this.log = log;
		this.qbf = qbf;
	}

	public void run() {

		Map<QueryMode, CategorizedResults<Category>> allResUnmerged = Maps.newHashMap();
		Map<QueryMode, List<CategorizedResults<Category>>> allRes = Maps.newHashMap();

		Set<ICoReTypeName> keys = usages.findKeys();
		// ICoReTypeName[] keys = new ICoReTypeName[] {
		// CoReTypeName.get("LSystem/Enum"),
		// CoReTypeName.get("LSystem/Text/StringBuilder") };
		log.foundTypes(keys.size());
		for (ICoReTypeName type : keys) {
			log.type(type);
			rec = mh.get(type);

			for (String user : mcIo.findZipsWith(type)) {
				log.user(user);
				for (List<MicroCommit> mcs : mcIo.readZipAndSortByLocation(user, type)) {
					log.history();
					QueryMode[] queryModes = QueryMode.values();
					// QueryMode[] queryModes = new QueryMode[] { QueryMode.REAL
					// };
					for (QueryMode mode : queryModes) {
						log.queryMode(mode);
						qb = qbf.get(mode);
						evaluate(mcs, createNewResultsForQueryHistory(allRes, mode), getResult(allResUnmerged, mode));
					}
				}
			}
		}

		log.done(allRes);
		log.doneAllTogether(allResUnmerged);
	}

	private CategorizedResults<Category> getResult(Map<QueryMode, CategorizedResults<Category>> allRes,
			QueryMode mode) {
		CategorizedResults<Category> res = allRes.get(mode);
		if (res == null) {
			res = CategorizedResults.create();
			allRes.put(mode, res);
		}
		return res;
	}

	private CategorizedResults<Category> createNewResultsForQueryHistory(
			Map<QueryMode, List<CategorizedResults<Category>>> allRes, QueryMode mode) {

		List<CategorizedResults<Category>> resByMode = allRes.get(mode);
		if (resByMode == null) {
			resByMode = Lists.newLinkedList();
			allRes.put(mode, resByMode);
		}

		CategorizedResults<Category> newRes = CategorizedResults.create();
		resByMode.add(newRes);
		return newRes;
	}

	private void evaluate(List<MicroCommit> mcs, CategorizedResults<Category> res,
			CategorizedResults<Category> resUnmerged) {

		for (MicroCommit mc : mcs) {
			log.microCommit();
			Usage start = mc.getStart();
			Usage end = mc.getEnd();

			Category c = getCategory(mc);
			double f1 = shouldEvaluate(c) ? measurePredictionQuality(start, end) : 0;
			res.add(c, f1);
			resUnmerged.add(c, f1);
		}
		log.finishedMicroCommits();
	}

	protected abstract boolean shouldEvaluate(Category c);

	private double measurePredictionQuality(Usage start, Usage end) {
		Usage sstart = safe(start, end);
		Usage send = safe(end, start);
		List<Query> queries = qb.createQueries(sstart, send);
		BoxplotData res = new BoxplotData();
		for (Query q : queries) {
			Set<ICoReMethodName> proposals = getProposals(rec, q);
			Set<ICoReMethodName> expectation = getExpectation(q, end);
			double f1 = Measure.newMeasure(expectation, proposals).getF1();
			res.add(f1);
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

	protected abstract Category getCategory(MicroCommit mc);
}