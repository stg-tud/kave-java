/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.csharp.evaluation;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.inject.Inject;

import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.BoxplotData;
import cc.recommenders.evaluation.data.Measure;
import cc.recommenders.io.Logger;
import cc.recommenders.io.NestedZipFolders;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.names.IMethodName;
import cc.recommenders.names.ITypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.csharp.queries.IQueryBuilder;
import exec.csharp.queries.QueryBuilderFactory;
import exec.csharp.queries.QueryMode;
import exec.csharp.utils.MicroCommit;
import exec.csharp.utils.ModelHelper;
import exec.csharp.utils.QueryUtils;
import exec.csharp.utils.StorageCase;
import exec.csharp.utils.StorageHelper;

public class Evaluation implements IEvaluation {

	private ModelHelper models;
	private QueryBuilderFactory queryBuilderFactory;
	private NestedZipFolders<ITypeName> historyDir;
	private NestedZipFolders<ITypeName> usages;
	private MiningOptions mOpts;
	private QueryOptions qOpts;
	private ICallsRecommender<Query> rec;
	private IQueryBuilder<Usage, Query> queryBuilder;
	private IEvaluationConsumer consumer;

	@Inject
	public Evaluation(ModelHelper models, QueryBuilderFactory queryBuilderFactory, StorageHelper storageHelper,
			MiningOptions mOpts, QueryOptions qOpts) {
		this.models = models;
		this.queryBuilderFactory = queryBuilderFactory;
		this.usages = storageHelper.getNestedZipFolder(StorageCase.USAGES);
		this.historyDir = storageHelper.getNestedZipFolder(StorageCase.MICRO_COMMITS);
		this.mOpts = mOpts;
		this.qOpts = qOpts;
	}

	@Override
	public void run(IEvaluationConsumer consumer) {
		this.consumer = consumer;
		Logger.log("");
		Logger.log("Running: %s -- %s", getClass().getSimpleName(), consumer.getClass().getSimpleName());
		Logger.log("options: %s%s", mOpts, qOpts);
		Logger.log("");

		for (ITypeName type : historyDir.findKeys()) {
			List<MicroCommit> histories = historyDir.readAllZips(type, MicroCommit.class);
			List<Usage> us = usages.readAllZips(type, Usage.class);
			if (us.size() < 1) {
				consumer.skippingType(type, us, histories);
				continue;
			}

			consumer.startingType(type, us, histories);

			rec = models.get(type);

			for (QueryMode mode : QueryMode.values()) {
				boolean shouldLog = mode == QueryMode.LINEAR;

				consumer.startingQueryMode(mode);
				queryBuilder = queryBuilderFactory.get(mode);

				for (MicroCommit t : histories) {

					Query start = t.getStart();
					Query end = t.getEnd();

					if (shouldSkip(start, end, shouldLog, mode)) {
						continue;
					}

					double f1 = measurePredictionQuality(start, end);
					consumer.addResult(start, end, mode, f1);
				}
			}
		}

		consumer.finish();
	}

	private boolean shouldSkip(Query start, Query end, boolean shouldLog, QueryMode mode) {
		int numAdditions = QueryUtils.countAdditions(start, end);
		int numRemovals = QueryUtils.countRemovals(start, end);
		if (0 == numAdditions) {
			if (shouldLog) {
				if (0 == numRemovals) {
					consumer.skipCommit_NoChange(mode);
				} else {
					consumer.skipCommit_NoAddition(mode);
				}
			}
			return true;
		}
		return false;
	}

	private double measurePredictionQuality(Query start, Query end) {
		List<Query> queries = queryBuilder.createQueries(start, end);
		BoxplotData res = new BoxplotData();
		for (Query q : queries) {
			Set<IMethodName> proposals = getProposals(rec, q);
			Set<IMethodName> expectation = getExpectation(q, end);
			Measure measure = Measure.newMeasure(expectation, proposals);
			res.add(measure.getF1());
		}
		return res.getMean();
	}

	private Set<IMethodName> getExpectation(Query q, Query end) {

		Set<IMethodName> expectation = Sets.newLinkedHashSet();
		for (CallSite cs : end.getReceiverCallsites()) {
			if (!q.getAllCallsites().contains(cs)) {
				expectation.add(cs.getMethod());
			}
		}
		return expectation;
	}

	private Set<IMethodName> getProposals(ICallsRecommender<Query> rec, Query query) {
		Set<IMethodName> proposals = Sets.newHashSet();
		for (Tuple<IMethodName, Double> p : rec.query(query)) {
			proposals.add(p.getFirst());
		}
		return proposals;
	}
}