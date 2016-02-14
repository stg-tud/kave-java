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

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import cc.recommenders.evaluation.OptionsUtils;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.ModelBuilder;
import cc.recommenders.mining.calls.QueryOptions;
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

		bind(RandomGenerator.class).toInstance(new Well19937c());
	}

	private void configOptions() {
		String opts = OptionsUtils.pbn(10).c(true).d(true).p(false).useFloat().ignore(false).min(30).get();
		QueryOptions qOpts = new QueryOptions();
		MiningOptions mOpts = new MiningOptions();
		qOpts.setFrom(opts);
		mOpts.setFrom(opts);
		bind(QueryOptions.class).toInstance(qOpts);
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
