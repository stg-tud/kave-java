/**
 * Copyright (c) 2011-2014 Darmstadt University of Technology. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Sebastian Proksch - initial API and implementation
 */
package exec.csharp;

import static cc.recommenders.mining.calls.MiningOptions.newMiningOptions;
import static cc.recommenders.mining.calls.QueryOptions.newQueryOptions;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import cc.recommenders.evaluation.OptionsUtils;
import cc.recommenders.evaluation.queries.QueryBuilder;
import cc.recommenders.evaluation.queries.ZeroCallQueryBuilder;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.ModelBuilder;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.mining.calls.clustering.FeatureWeighter;
import cc.recommenders.mining.calls.pbn.PBNModelBuilder;
import cc.recommenders.mining.features.FeatureExtractor;
import cc.recommenders.mining.features.UsageFeatureExtractor;
import cc.recommenders.mining.features.UsageFeatureWeighter;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.UsageFeature;
import exec.csharp.evaluation.Evaluation;
import exec.csharp.evaluation.IEvaluation;
import exec.csharp.queries.RandomQueryBuilder;
import exec.csharp.utils.StorageHelper;

public class Module extends AbstractModule {

	private static final String ROOT_PATH = "/Volumes/Data/";

	@Override
	protected void configure() {
		bindMiningAndQueryOptions();

		// TODO read ROOT_PATH from settings file
		bind(StorageHelper.class).toInstance(new StorageHelper(ROOT_PATH));
		bind(IEvaluation.class).to(Evaluation.class);
	}

	private void bindMiningAndQueryOptions() {
		String opts = OptionsUtils.pbn(10).c(false).d(true).p(false).useFloat().ignore(false).min(30).get();
		bind(QueryOptions.class).toInstance(newQueryOptions(opts));
		bind(MiningOptions.class).toInstance(newMiningOptions(opts));
	}

	@Provides
	public RandomQueryBuilder provideRandomQueryBuilder() {
		return new RandomQueryBuilder(10);
	}

	@Provides
	public FeatureWeighter<UsageFeature> provideFeatureWeighter(MiningOptions options) {
		return new UsageFeatureWeighter(options);
	}

	@Provides
	public FeatureExtractor<Usage, UsageFeature> provideFeatureExtractor(MiningOptions options) {
		return new UsageFeatureExtractor(options);
	}

	@Provides
	public ModelBuilder<UsageFeature, BayesianNetwork> provideModelBuilder() {
		return new PBNModelBuilder();
	}

	@Provides
	public QueryBuilder<Usage, Query> provideQueryBuilder() {
		return new ZeroCallQueryBuilder();
		// PartialUsageQueryBuilder qb = new PartialUsageQueryBuilder();
		// qb.setNumOfQueries(3);
		// return qb;
	}
}