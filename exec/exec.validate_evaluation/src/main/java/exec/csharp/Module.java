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

import java.io.File;
import java.nio.file.Paths;

import org.eclipse.recommenders.commons.bayesnet.BayesianNetwork;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import cc.recommenders.assertions.Asserts;
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
import exec.csharp.evaluation.Evaluation;
import exec.csharp.evaluation.IEvaluation;
import exec.csharp.queries.RandomQueryBuilder;
import exec.csharp.utils.StorageHelper;
import exec.validate_evaluation.microcommits.MicroCommitIo;

public class Module extends AbstractModule {

	private static final String ROOT_PATH = "/Volumes/Data/";

	@Override
	protected void configure() {
		bindMiningAndQueryOptions();

		
		Asserts.assertTrue(new File(ROOT_PATH).exists(), "\n\n###\n### ROOT_PATH does not exist. Evaluation disc not mounted?\n###\n\n");
		
		// TODO read ROOT_PATH from settings file
		bind(StorageHelper.class).toInstance(new StorageHelper(ROOT_PATH));
		bind(IEvaluation.class).to(Evaluation.class);
	}

	private void bindMiningAndQueryOptions() {
		@SuppressWarnings("deprecation")
		String opts = OptionsUtils.pbn(10).c(false).d(true).p(false).useFloat().ignore(false).dropRareFeatures(false)
				.min(30).get();
		bind(QueryOptions.class).toInstance(newQueryOptions(opts));
		bind(MiningOptions.class).toInstance(newMiningOptions(opts));
	}

	@Provides
	public RandomQueryBuilder provideRandomQueryBuilder() {
		return new RandomQueryBuilder(6);
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
	public MicroCommitIo provideMicroCommitIo() {
		return new MicroCommitIo(path("MicroCommits"));
	}

	private String path(String... relSubFolders) {
		return Paths.get(ROOT_PATH, relSubFolders).toString();
	}
}