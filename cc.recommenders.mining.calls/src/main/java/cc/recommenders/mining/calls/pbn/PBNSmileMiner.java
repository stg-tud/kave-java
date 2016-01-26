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
package cc.recommenders.mining.calls.pbn;

import java.util.List;

import com.google.inject.Inject;

import cc.recommenders.mining.calls.DictionaryBuilder;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.Pattern;
import cc.recommenders.mining.calls.PatternFinderFactory;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.mining.features.FeatureExtractor;
import cc.recommenders.mining.features.OptionAwareFeaturePredicate;
import cc.recommenders.mining.features.RareFeatureDropper;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.UsageFeature;
import cc.recommenders.utils.dictionary.Dictionary;
import smile.Network;

public class PBNSmileMiner extends AbstractPBNMiner<Network> {

	private PBNSmileModelBuilder modelBuilder;

	@Inject
	public PBNSmileMiner(FeatureExtractor<Usage, UsageFeature> featureExtractor,
			DictionaryBuilder<Usage, UsageFeature> dictionaryBuilder,
			PatternFinderFactory<UsageFeature> patternFinderFactory, PBNSmileModelBuilder modelBuilder,
			QueryOptions qOpts, MiningOptions mOpts, RareFeatureDropper<UsageFeature> dropper,
			OptionAwareFeaturePredicate featurePred) {

		super(featureExtractor, dictionaryBuilder, patternFinderFactory, qOpts, mOpts, dropper, featurePred);
		this.modelBuilder = modelBuilder;
	}

	@Override
	protected Network buildModel(List<Pattern<UsageFeature>> patterns, Dictionary<UsageFeature> dictionary) {
		return modelBuilder.build(patterns, dictionary);
	}

	@Override
	public ICallsRecommender<Query> createRecommender(List<Usage> in) {
		throw new RuntimeException("not implemented");
	}
}