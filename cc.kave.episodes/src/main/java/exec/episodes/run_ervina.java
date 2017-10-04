/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.episodes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import cc.kave.episodes.preprocessing.Preprocessing;
import cc.recommenders.io.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class run_ervina {

	private static final String PROPERTY_NAME = "episodeFolder";
	private static final String PROPERTY_FILE = "episode.properties";

	private static final int FREQUENCY = 10;

	private static final int THF = 700;
	private static final double THE = 0.6 ;

	private static final int METHODSIZE = 5000;

	private static Injector injector;

	public static void main(String[] args) throws Exception {
		initLogger();
		printAvailableMemory();

		String rootFolder = readPropertyFromFile(PROPERTY_NAME);
		injector = Guice.createInjector(new Module(rootFolder));

		Logger.append("\n");
		Logger.log("started: %s\n", new Date());
		
//		load(EventStreamChecker.class).duplicates(FREQUENCY);
		
		 load(Preprocessing.class).run(FREQUENCY);
		// load(PartitionStream.class).partition(FREQUENCY);
		// load(EventsStatistics.class).histogram(FREQUENCY);
//		load(EventsStatistics.class).printEventFreqs(FREQUENCY);
		
//		 load(PatternsStatistics.class).numPatterns(FREQUENCY, THF, THE);
//		load(ThresholdAnalyzer.class).EntDim(FREQUENCY);
//		load(ThresholdAnalyzer.class).createHistogram(EpisodeType.PARALLEL, FREQUENCY, THE);
//		load(Generalizability.class).validate(FREQUENCY, THF, THE);
//		load(APIUsages.class).categorise(EpisodeType.GENERAL, FREQUENCY, THF, THE);
//		load(APIUsages.class).orderApis(FREQUENCY, THF, THE);
//		load(APIUsages.class).freqOrderApis(FREQUENCY, THF, THE);
//		load(APIUsages.class).freqGensApis(FREQUENCY, THF, THE);
//		load(APIUsages.class).specificPatterns(FREQUENCY, THF, THE);
//		load(APIUsages.class).apiUsages(EpisodeType.GENERAL, FREQUENCY, THF, THE);
//		load(APIUsages.class).patternEvents(FREQUENCY, THF, THE);
//		load(APIUsages.class).getRepoOccStrictPatt(FREQUENCY, THF, THE);
//		load(APIUsages.class).getRepoOccSpecPatt(FREQUENCY, THF, THE);
//		load(APIUsages.class).getApisComp(FREQUENCY, THF, THE);
//		load(APIUsages.class).getOrderInfo(FREQUENCY, THF, THE);
//		load(APIUsages.class).partialSequentials(FREQUENCY, THF, THE);
		
		// load(Evaluations.class).patternsOutput(EpisodeType.PARALLEL,
		// FREQUENCY,
		// FOLDNUM, THF, THE);

		// load(PatternsComparison.class).extractConcreteCode(FREQUENCY,
		// FOLDNUM);
		// load(PatternsComparison.class).coverage(EpisodeType.SEQUENTIAL,
		// EpisodeType.PARALLEL, FOLDNUM, FREQUENCY);
		// load(PatternsComparison.class).compStats(EpisodeType.PARALLEL,
		// EpisodeType.SEQUENTIAL, FOLDNUM, FREQUENCY);

		// load(SpecificPatterns.class).patternsInfo(EpisodeType.GENERAL,
		// FREQUENCY, FOLDNUM, FREQUENCY, ENTROPY);
		// load(PatternsEvents.class).getEventsType(EpisodeType.PARALLEL,
		// FREQUENCY, ENTROPY, FOLDNUM);
		// load(EpisodeWriter.class).writeNewEpisodes(FREQ);

		// load(PatternsIdentifier.class).trainingCode(FREQ,
		// EpisodeType.GENERAL);

		// load(EvaluationsPaper.class).diff(FREQ);
		// load(EvaluationsPaper.class).part2(FREQ);
		// load(EvaluationsPaper.class).part1(FREQ);

		// load(EventStreamSize.class).printNumberOfEvents(NUM_FOLDS);
		// EpisodeKind.SEQUENTIAL,
		// EpisodeKind.PARALLEL, FREQ, ENTROPY);
		// load(PartialOrderAnalyzer.class).analyze(FOLDNUM, FREQ, ENTROPY);

		// load(PatternsIdentifier.class).inRepos(NUMBREPOS, FREQ, ENTROPY);

		// load(FrameworksDistribution.class).getDistribution(NUMBREPOS);
		// load(Preprocessing.class).generate(NUMBREPOS, FREQ);

		// load(MethodSize.class).statistics(NUMBREPOS, METHODSIZE);

		// load(ThresholdsFrequency.class).writer(NUMBREPOS);
		// load(ThresholdsBidirection.class).writer(NUMBREPOS, FREQTHRESH);

		// load(PatternsOutput.class).write(NUMBREPOS, FREQTHRESH,
		// BIDIRECTTHRESH);
		// load(PatternsIdentifier.class).trainingCode(NUMBREPOS, FREQTHRESH,
		// BIDIRECTTHRESH);
		// load(FrequenciesAnalyzer.class).analyzeSuperEpisodes(NUMBREPOS,
		// FREQTHRESH, BIDIRECTTHRESH);

		// load(SimilarityMetrics.class).validate();

		// load(FrameworksData.class).getFrameworksDistribution();
		// load(StreamFrequencies.class).frequencies();
		// load(Preprocessing.class).generate();
		// load(StreamPartition.class).partition();
		// load(SampleCodeMatcher.class).generateGraphs();

		// load(EpisodeGraphGeneratorValidationData.class).generateGraphs();
		// load(QueriesGraphGenerator.class).generateGraphs();
		// load(EpisodeGraphGeneratorTrainingData.class).generateGraphs(5,
		// 0.01);
		// load(RecommenderGraphGenerator.class).generateGraphs();
		// load(ProposalStrategyProvider.class).evaluate();
		// load(Evaluation.class).evaluate();
		// load(TargetsCategorization.class).categorize();

		// load(Suggestions.class).run();
		// load(PatternAnalyzer.class).readPatterns();

		Logger.log("done");
	}

	private static void initLogger() {
		Logger.setPrinting(true);
		Logger.setDebugging(false);
		Logger.setCapturing(false);
	}

	private static void printAvailableMemory() {
		long maxMem = Runtime.getRuntime().maxMemory();
		float maxMemInMb = Math.round(maxMem * 1.0d / (1024 * 1024 * 1.0f));
		Logger.log("maximum memory (-Xmx): %.0f MB", maxMemInMb);
	}

	private static String readPropertyFromFile(String propertyName) {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(PROPERTY_FILE));
			String property = properties.getProperty(propertyName);
			if (property == null) {
				throw new RuntimeException("property '" + propertyName
						+ "' not found in properties file");
			}
			Logger.log("%s: %s", propertyName, property);

			return property;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T load(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

}