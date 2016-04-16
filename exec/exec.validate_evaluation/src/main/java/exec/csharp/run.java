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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cc.recommenders.assertions.Asserts;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.NestedZipFolders;
import cc.recommenders.mining.calls.pbn.BatchPBNSmileMiner;
import cc.recommenders.names.ICoReTypeName;
import exec.csharp.utils.StorageCase;
import exec.csharp.utils.StorageHelper;
import exec.validate_evaluation.basicexcel.BasicExcelEvaluation;
import exec.validate_evaluation.microcommits.MicroCommitGenerationLogger;
import exec.validate_evaluation.microcommits.MicroCommitGenerationRunner;
import exec.validate_evaluation.microcommits.MicroCommitIo;
import exec.validate_evaluation.queryhistory.QueryHistoryCollector;
import exec.validate_evaluation.queryhistory.QueryHistoryGenerationLogger;
import exec.validate_evaluation.queryhistory.QueryHistoryGenerationRunner;
import exec.validate_evaluation.queryhistory.QueryHistoryIo;
import exec.validate_evaluation.queryhistory.UsageExtractor;
import exec.validate_evaluation.stats.UsageToMicroCommitRatioCalculator;
import exec.validate_evaluation.streaks.EditStreakGenerationIo;
import exec.validate_evaluation.streaks.EditStreakGenerationLogger;
import exec.validate_evaluation.streaks.EditStreakGenerationRunner;

public class run {

	private static Injector injector = Guice.createInjector(new Module());
	private static StorageHelper storageHelper;
	private static String dirRoot = "/Volumes/Data/";
	private static String dirCE = dirRoot + "Events/OnlyCompletion";
	private static String dirES = dirRoot + "EditStreaks";
	private static String dirQH = dirRoot + "QueryHistories";
	private static String dirMC = dirRoot + "MicroCommits";

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
		// runBatchPBNSmileMiner();
		// storageHelper.setModifier("inlined");
		// runBatchPBNSmileMiner();
		// runMicroCommitTransformation();
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

		/* new evals */
		load(BasicExcelEvaluation.class).run();
		// runStats();
	}

	private static void runStats() throws IOException {
		MicroCommitIo mcIo = new MicroCommitIo(dirMC);
		QueryHistoryIo qhIo = new QueryHistoryIo(dirQH);
		StorageHelper sh = new StorageHelper(dirRoot);

		// new MicroCommitStats(mcIo).run();
		// new QueryHistoryStats(qhIo).run();
		new UsageToMicroCommitRatioCalculator(sh, mcIo).run();
	}

	private static void runMicroCommitTransformation() {

		EditStreakGenerationIo esIo = new EditStreakGenerationIo(dirCE, dirES);
		QueryHistoryIo qhIo = new QueryHistoryIo(dirQH);
		MicroCommitIo mcIo = new MicroCommitIo(dirMC);

		EditStreakGenerationLogger esLog = new EditStreakGenerationLogger();
		QueryHistoryGenerationLogger qhLog = new QueryHistoryGenerationLogger();
		MicroCommitGenerationLogger mcLog = new MicroCommitGenerationLogger();

		EditStreakGenerationRunner esGen = new EditStreakGenerationRunner(esIo, esLog);
		QueryHistoryGenerationRunner qhGen = new QueryHistoryGenerationRunner(esIo, qhIo, qhLog,
				new QueryHistoryCollector(qhLog), new UsageExtractor());
		MicroCommitGenerationRunner mcGen = new MicroCommitGenerationRunner(qhIo, mcIo, mcLog);

		// clean(dirES);
		// esGen.run();

		clean(dirQH);
		qhGen.run();

		clean(dirMC);
		mcGen.run();
	}

	private static void clean(String dir) {
		try {
			File f = new File(dir);
			FileUtils.deleteDirectory(f);
			Asserts.assertFalse(f.exists());
			f.mkdirs();
			Asserts.assertTrue(f.exists());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void runBatchPBNSmileMiner() {
		NestedZipFolders<ICoReTypeName> zipsUsages = storageHelper.getNestedZipFolder(StorageCase.USAGES);
		Directory dirNetworks = storageHelper.getDirectory(StorageCase.NETWORKS);
		load(BatchPBNSmileMiner.class).run(zipsUsages, dirNetworks);
	}

	private static <T> T load(Class<T> c) {
		return injector.getInstance(c);
	}
}