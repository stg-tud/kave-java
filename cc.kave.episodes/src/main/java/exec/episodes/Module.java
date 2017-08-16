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

import java.io.File;
import java.util.Map;

import cc.kave.episodes.GraphGenerator.TrainingDataGraphGenerator;
import cc.kave.episodes.GraphGenerator.ValidationDataGraphGenerator;
import cc.kave.episodes.evaluation.queries.QueryStrategy;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.FileReader;
import cc.kave.episodes.io.ValidationContextsParser;
import cc.kave.episodes.mining.evaluation.EpisodeRecommender;
import cc.kave.episodes.mining.evaluation.RecommenderEvaluation;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.TargetsCategorization;
import cc.kave.episodes.postprocessor.MaximalEpisodes;
import cc.kave.episodes.postprocessor.ThresholdsFrequency;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.kave.episodes.statistics.EpisodesStatistics;
import cc.recommenders.io.Directory;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class Module extends AbstractModule {

	private final String rootFolder;

	public Module(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	@Override
	protected void configure() {
		File rootFile = new File(rootFolder + "/");
		Directory rootDir = new Directory(rootFile.getAbsolutePath());
		File eventsData = new File(rootFolder + "dataSet/events/");
		Directory eventsDir = new Directory(eventsData.getAbsolutePath());
		File contexts = new File(rootFolder + "dataSet/SSTs/");
		Directory ctxtDir = new Directory(contexts.getAbsolutePath());
		File evaluationFile = new File(rootFolder + "Evaluations/");
		Directory evaluationDir = new Directory(evaluationFile.getAbsolutePath());
		File statFile = new File(rootFolder + "statistics/");
		Directory statDir = new Directory(statFile.getAbsolutePath());
		File patternsFile = new File(rootFolder + "dataSet/patterns/");
		Directory patternsDir = new Directory(patternsFile.getAbsolutePath());

		Map<String, Directory> dirs = Maps.newHashMap();
		dirs.put("root", rootDir);
		dirs.put("statistics", statDir);
		dirs.put("events", eventsDir);
		dirs.put("contexts", ctxtDir);
		dirs.put("evaluation", evaluationDir);
		dirs.put("patterns", patternsDir);
		bindInstances(dirs);

		bind(File.class).annotatedWith(Names.named("root")).toInstance(rootFile);
		bind(File.class).annotatedWith(Names.named("events")).toInstance(eventsData);
		bind(File.class).annotatedWith(Names.named("contexts")).toInstance(contexts);
		bind(File.class).annotatedWith(Names.named("evaluation")).toInstance(evaluationFile);
		bind(File.class).annotatedWith(Names.named("statistics")).toInstance(statFile);
		bind(File.class).annotatedWith(Names.named("patterns")).toInstance(patternsFile);

		File eventStreamRoot = eventsData;
		File graphRoot = rootFile;
		EventStreamIo streamParser = new EventStreamIo(eventStreamRoot);

		Directory vcr = new Directory(contexts.getAbsolutePath());
		bind(ValidationContextsParser.class).toInstance(new ValidationContextsParser(vcr));

		FileReader reader = new FileReader();
		EpisodeParser episodeParser = new EpisodeParser(eventsData, reader);
		MaximalEpisodes episodeLearned = new MaximalEpisodes();
		EpisodeToGraphConverter graphConverter = new EpisodeToGraphConverter();
		EpisodeAsGraphWriter graphWriter = new EpisodeAsGraphWriter();
		TransClosedEpisodes transitivityClosure = new TransClosedEpisodes();

		File patternsRoot = patternsFile;
		EpisodesStatistics stats = new EpisodesStatistics();
		bind(ThresholdsFrequency.class).toInstance(new ThresholdsFrequency(patternsRoot, episodeParser, stats));

		ValidationContextsParser validationParser = new ValidationContextsParser(vcr);
		EpisodeRecommender recommender = new EpisodeRecommender();
		bind(ValidationDataGraphGenerator.class).toInstance(new ValidationDataGraphGenerator(graphRoot,
				validationParser, streamParser, transitivityClosure, graphWriter, graphConverter));
		bind(TrainingDataGraphGenerator.class).toInstance(new TrainingDataGraphGenerator(graphRoot, episodeParser,
				episodeLearned, streamParser, transitivityClosure, graphWriter, graphConverter));

		File evaluationRoot = evaluationFile;
		QueryStrategy queryGenerator = new QueryStrategy();
		TargetsCategorization categorizer = new TargetsCategorization();
		bind(RecommenderEvaluation.class).toInstance(new RecommenderEvaluation(evaluationRoot, validationParser, streamParser,
				queryGenerator, recommender, episodeParser, episodeLearned, categorizer));
	}

	private void bindInstances(Map<String, Directory> dirs) {
		for (String name : dirs.keySet()) {
			Directory dir = dirs.get(name);
			bind(Directory.class).annotatedWith(Names.named(name)).toInstance(dir);
		}
	}
}