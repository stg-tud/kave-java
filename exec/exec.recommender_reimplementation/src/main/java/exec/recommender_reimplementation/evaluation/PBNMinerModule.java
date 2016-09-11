/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.evaluation;

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

public class PBNMinerModule extends AbstractModule {

	@Override
	protected void configure() {
		configueOptions();
	}

	private void configueOptions() {
		QueryOptions qOpts = new QueryOptions();
		qOpts.queryType = QueryType.ZERO;
		qOpts.minProbability = 0.3;
		qOpts.useClassContext = false;
		qOpts.useMethodContext = true;
		qOpts.useDefinition = true;
		qOpts.useParameterSites = false;
		qOpts.isIgnoringAfterFullRecall = false;
		bind(QueryOptions.class).toInstance(qOpts);

		MiningOptions mOpts = new MiningOptions();
		mOpts.setDistanceMeasure(DistanceMeasure.COSINE);
		mOpts.setAlgorithm(Algorithm.CANOPY);
		mOpts.setT1(0.151);
		mOpts.setT2(0.15);

		mOpts.setFeatureDropping(false);
		bind(MiningOptions.class).toInstance(mOpts);
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
