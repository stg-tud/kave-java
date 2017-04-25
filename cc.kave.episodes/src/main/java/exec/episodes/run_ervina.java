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

import cc.kave.episodes.postprocessor.PostChecking;
import cc.kave.episodes.preprocessing.EventOccurrences;
import cc.recommenders.io.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class run_ervina {

	private static final String PROPERTY_NAME = "episodeFolder";
	private static final String PROPERTY_FILE = "episode.properties";

	private static final int FOLDNUM = 0;
	private static final int FREQUENCY = 1;
	private static final double ENTROPY = 0.6;

	private static final int METHODSIZE = 5000;
	private static final int NUM_FOLDS = 10;

	private static Injector injector;

	public static void main(String[] args) throws Exception {
		initLogger();
		printAvailableMemory();

		String rootFolder = readPropertyFromFile(PROPERTY_NAME);
		injector = Guice.createInjector(new Module(rootFolder));

		Logger.append("\n");
		Logger.log("started: %s\n", new Date());

//		 load(PreChecking.class).reposInfo();
		load(EventOccurrences.class).generate(FREQUENCY);
//		 load(PreprocessingFolded.class).allRepos(FREQUENCY);;

//		 load(PostChecking.class).streamData(FREQUENCY);

//		load(ThresholdsAnalyzer.class).analyze(EpisodeType.GENERAL, FREQUENCY,
//				FOLDNUM, 0, ENTROPY);
		// load(Evaluations.class).patternsOutput(EpisodeType.GENERAL,
		// FREQUENCY,
		// FOLDNUM, FREQUENCY, ENTROPY);
		// load(SpecificPatterns.class).patternEvents(EpisodeType.GENERAL,
		// FREQUENCY, FOLDNUM);
//		load(PatternsComparison.class).nonoverlappings(EpisodeType.GENERAL,
//				EpisodeType.PARALLEL, FREQUENCY, FOLDNUM, FREQUENCY, ENTROPY);
		// load(SpecificPatterns.class).patternsInfo(EpisodeType.GENERAL,
		// FREQUENCY, FOLDNUM, FREQUENCY, ENTROPY);
//		load(PatternsEvents.class).getEventsType(EpisodeType.PARALLEL, FREQUENCY, ENTROPY, FOLDNUM);
		// load(EpisodeWriter.class).writeNewEpisodes(FREQ);
		// load(PatternsComparison.class).printCommonPatterns(FREQ,
		// EpisodeType.SEQUENTIAL, EpisodeType.PARALLEL);
		// load(PatternsComparison.class).extractConcreteCode(FREQ);

		// load(PatternsIdentifier.class).trainingCode(FREQ,
		// EpisodeType.GENERAL);

		// load(EvaluationsPaper.class).diff(FREQ);
		// load(EvaluationsPaper.class).part2(FREQ);
		// load(EvaluationsPaper.class).part1(FREQ);

		// load(EventStreamSize.class).printNumberOfEvents(NUM_FOLDS);
		// load(PatternsComparisons.class).compare(FOLDNUM,
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
		// load(PatternsIdentifier.class).validationCode(NUMBREPOS, FREQTHRESH,
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
		// load(EventStreamModifier.class).modify();
		// load(EventStreamReader.class).read();
		// load(MappingReader.class).read();

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