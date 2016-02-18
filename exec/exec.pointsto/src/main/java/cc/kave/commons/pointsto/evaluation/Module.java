/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.evaluation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.MiningOptions.Algorithm;
import cc.recommenders.mining.calls.MiningOptions.DistanceMeasure;
import cc.recommenders.mining.calls.ModelBuilder;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.mining.calls.QueryOptions.QueryType;
import cc.recommenders.mining.calls.clustering.FeatureWeighter;
import cc.recommenders.mining.calls.pbn.PBNModelBuilder;
import cc.recommenders.mining.features.FeatureExtractor;
import cc.recommenders.mining.features.UsageFeatureExtractor;
import cc.recommenders.mining.features.UsageFeatureWeighter;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.UsageFeature;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		configOptions();

		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		// ExecutorService executorService = Executors.newSingleThreadExecutor();
		bind(ExecutorService.class).toInstance(executorService);
	}

	private void configOptions() {
		QueryOptions qOpts = new QueryOptions();
		qOpts.queryType = QueryType.NM;
		qOpts.minProbability = 0.3;
		qOpts.useClassContext = true;
		qOpts.useMethodContext = true;
		qOpts.useDefinition = true;
		qOpts.useParameterSites = true;
		qOpts.isIgnoringAfterFullRecall = false;
		bind(QueryOptions.class).toInstance(qOpts);

		MiningOptions mOpts = new MiningOptions();
		mOpts.setDistanceMeasure(DistanceMeasure.COSINE);
		mOpts.setAlgorithm(Algorithm.CANOPY);
		mOpts.setT1(0.101);
		mOpts.setT2(0.1);
		bind(MiningOptions.class).toInstance(mOpts);
	}

	@Provides
	public RandomGenerator provideRandomGenerator() {
		return new Well19937c(1457288501271L);
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

}
