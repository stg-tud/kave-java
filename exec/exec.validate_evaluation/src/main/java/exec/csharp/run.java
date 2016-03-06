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
package exec.csharp;

import java.io.IOException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.NestedZipFolders;
import cc.recommenders.mining.calls.pbn.BatchPBNSmileMiner;
import cc.recommenders.names.ITypeName;
import exec.csharp.utils.StorageCase;
import exec.csharp.utils.StorageHelper;

public class run {

	private static Injector injector = Guice.createInjector(new Module());
	private static StorageHelper storageHelper;

	private static void init() {
		storageHelper = load(StorageHelper.class);

		Logger.setPrinting(true);
		Logger.setDebugging(false);
		Logger.setCapturing(false);

		long maxMem = Runtime.getRuntime().maxMemory();
		float maxMemInMb = Math.round(maxMem * 1.0d / (1024 * 1024 * 1.0f));
		Logger.log("maximum memory (-Xmx): %.0f MB", maxMemInMb);
	}

	public static void main(String[] args) throws IOException {
		init();

		/* data preparation */
		runBatchPBNSmileMiner();
		// storageHelper.setModifier("inlined");
		// runBatchPBNSmileMiner();
		// storageHelper.clearModifier();

		/* evaluations */
		// load(UsageToMicroCommitRatioCalculator.class).run();
		// load(MicroCommitStatisticsRunner.class).run();
		// slow: load(UsagesStatisticsRunner.class).run(); // takes *very* long

		/* evaluations */
		// storageHelper.setModifier("inlined"); // TODO remove faked call in
		// evaluator
		// load(F1ByQueryMode.class).run();
		// load(F1ByQueryType.class).run();
		// load(F1ByCategory.class).run();
		// load(F1Details.class).run();
		// load(AnalysisOfNoise.class).run();
	}

	private static void runBatchPBNSmileMiner() {
		NestedZipFolders<ITypeName> zipsUsages = storageHelper.getNestedZipFolder(StorageCase.USAGES);
		Directory dirNetworks = storageHelper.getDirectory(StorageCase.NETWORKS);
		load(BatchPBNSmileMiner.class).run(zipsUsages, dirNetworks);
	}

	private static <T> T load(Class<T> c) {
		return injector.getInstance(c);
	}
}