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

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import cc.kave.episodes.analyzer.TrainingDataGraphGenerator;
import cc.kave.episodes.analyzer.ValidationDataGraphGenerator;
import cc.kave.episodes.evaluation.queries.QueryStrategy;
import cc.kave.episodes.mining.evaluation.EpisodeRecommender;
import cc.kave.episodes.mining.evaluation.Evaluation;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.EventStreamReader;
import cc.kave.episodes.mining.reader.FileReader;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.kave.episodes.model.TargetsCategorization;
import cc.recommenders.io.Directory;

public class Module extends AbstractModule {

	private final String rootFolder;

	public Module(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	@Override
	protected void configure() {
		File episodeFile = new File(rootFolder + "configurations/");
//		Directory episodeDir = new Directory(episodeFile.getAbsolutePath());
		File eventStreamData = new File(rootFolder + "EpisodeMining/dataSet/SST/");
		Directory eventStreamDir = new Directory(eventStreamData.getAbsolutePath());
		File contexts = new File(rootFolder + "dataSet/SST/");
		Directory ctxtDir = new Directory(contexts.getAbsolutePath());
		File episodeRootFile = new File(rootFolder + "dataSet/");
		Directory episodeRootDir = new Directory(episodeRootFile.getAbsolutePath());
		File evaluationFile = new File(rootFolder + "Evaluations/");
		Directory evaluationDir = new Directory(evaluationFile.getAbsolutePath());

		Map<String, Directory> dirs = Maps.newHashMap();
//		dirs.put("episode", episodeDir);
		dirs.put("events", eventStreamDir);
		dirs.put("contexts", ctxtDir);
		dirs.put("rootDir", episodeRootDir);
		dirs.put("evaluation", evaluationDir);
		bindInstances(dirs);

		bind(File.class).annotatedWith(Names.named("episode")).toInstance(episodeFile);
		bind(File.class).annotatedWith(Names.named("events")).toInstance(eventStreamData);
		bind(File.class).annotatedWith(Names.named("contexts")).toInstance(contexts);
		bind(File.class).annotatedWith(Names.named("rootDir")).toInstance(episodeRootFile);
		bind(File.class).annotatedWith(Names.named("evaluation")).toInstance(evaluationFile);
		

		File episodeRoot = episodeFile;
		FileReader reader = new FileReader();
		bind(EpisodeParser.class).toInstance(new EpisodeParser(episodeRoot, reader));

		File eventStreamRoot = eventStreamData;
		bind(EventMappingParser.class).toInstance(new EventMappingParser(eventStreamRoot));

		EventMappingParser mappingParser = new EventMappingParser(eventStreamRoot);
		bind(EventStreamReader.class).toInstance(new EventStreamReader(eventStreamRoot, reader, mappingParser));
		File graphRoot = episodeRootFile;

		Directory vcr = new Directory(contexts.getAbsolutePath());
		bind(ValidationContextsParser.class).toInstance(new ValidationContextsParser(vcr));

		EpisodeParser episodeParser = new EpisodeParser(episodeRoot, reader);
		MaximalEpisodes episodeLearned = new MaximalEpisodes();
		EpisodeToGraphConverter graphConverter = new EpisodeToGraphConverter();
		EpisodeAsGraphWriter graphWriter = new EpisodeAsGraphWriter();
		TransitivelyClosedEpisodes transitivityClosure = new TransitivelyClosedEpisodes();

		ValidationContextsParser validationParser = new ValidationContextsParser(vcr);
		EpisodeRecommender recommender = new EpisodeRecommender();
		bind(ValidationDataGraphGenerator.class).toInstance(new ValidationDataGraphGenerator(graphRoot, validationParser, mappingParser, transitivityClosure, graphWriter, graphConverter));
		bind(TrainingDataGraphGenerator.class).toInstance(new TrainingDataGraphGenerator(graphRoot, episodeParser, episodeLearned, mappingParser, transitivityClosure, graphWriter, graphConverter));

		File evaluationRoot = evaluationFile;
		QueryStrategy queryGenerator = new QueryStrategy();
		TargetsCategorization categorizer = new TargetsCategorization();
		bind(Evaluation.class).toInstance(new Evaluation(evaluationRoot, validationParser, mappingParser, queryGenerator, recommender, episodeParser, episodeLearned, categorizer));
	}

	private void bindInstances(Map<String, Directory> dirs) {
		for (String name : dirs.keySet()) {
			Directory dir = dirs.get(name);
			bind(Directory.class).annotatedWith(Names.named(name)).toInstance(dir);
		}
	}
}