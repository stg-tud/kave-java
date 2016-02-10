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

import cc.kave.commons.mining.episodes.EpisodeRecommender;
import cc.kave.episodes.analyzer.outputs.EpisodeGraphGeneratorTrainingData;
import cc.kave.episodes.analyzer.outputs.EpisodeGraphGeneratorValidationData;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.EventStreamReader;
import cc.kave.episodes.mining.reader.FileReader;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.recommenders.io.Directory;

public class Module extends AbstractModule {

	private final String rootFolder;

	public Module(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	@Override
	protected void configure() {
		File episodeFile = new File(rootFolder + "configurations/");
		Directory episodeDir = new Directory(episodeFile.getAbsolutePath());
		File eventStreamData = new File(rootFolder + "EpisodeMining/EventStreamForEpisodeMining/");
		Directory eventStreamDir = new Directory(eventStreamData.getAbsolutePath());
		File validationContexts = new File(rootFolder + "EpisodeMining/Contexts-Validation/");
		Directory validationCtxtDir = new Directory(validationContexts.getAbsolutePath());
		File graphFile = new File(rootFolder);
		Directory graphDir = new Directory(graphFile.getAbsolutePath());

		Map<String, Directory> dirs = Maps.newHashMap();
		dirs.put("episode", episodeDir);
		dirs.put("events", eventStreamDir);
		dirs.put("contexts", validationCtxtDir);
		dirs.put("graph", graphDir);
		bindInstances(dirs);

		bind(File.class).annotatedWith(Names.named("episode")).toInstance(episodeFile);
		bind(File.class).annotatedWith(Names.named("events")).toInstance(eventStreamData);
		bind(File.class).annotatedWith(Names.named("contexts")).toInstance(validationContexts);
		bind(File.class).annotatedWith(Names.named("graph")).toInstance(graphFile);

		File episodeRoot = episodeFile;
		FileReader reader = new FileReader();
		bind(EpisodeParser.class).toInstance(new EpisodeParser(episodeRoot, reader));
		
		File eventStreamRoot = eventStreamData;
		bind(EventMappingParser.class).toInstance(new EventMappingParser(eventStreamRoot));
		
		EventMappingParser mappingParser = new EventMappingParser(eventStreamRoot);
		bind(EventStreamReader.class).toInstance(new EventStreamReader(eventStreamRoot, reader, mappingParser));
		File graphRoot = graphFile;
		
		File validationContextsRoot = validationContexts;
		bind(ValidationContextsParser.class).toInstance(new ValidationContextsParser(validationContextsRoot));
		
		EpisodeParser episodeParser = new EpisodeParser(episodeRoot, reader);
		MaximalEpisodes episodeLearned = new MaximalEpisodes();
		EpisodeToGraphConverter graphConverter = new EpisodeToGraphConverter();
		EpisodeAsGraphWriter graphWriter = new EpisodeAsGraphWriter();
		TransitivelyClosedEpisodes transitivityClosure = new TransitivelyClosedEpisodes();
		
		ValidationContextsParser validationParser = new ValidationContextsParser(validationContextsRoot);
//		bind(EpisodeGraphGenerator.class).toInstance(new EpisodeGraphGenerator(graphRoot, validationParser, episodeLearned, mappingParser, transitivityClosure, writer, graphConverter));
//		bind(EpisodeGraphGenerator.class).toInstance(new EpisodeGraphGenerator(graphRoot, episodeParser, episodeLearned, mappingParser, transitivityClosure, graphWriter, graphConverter));
		EpisodeRecommender recommender = new EpisodeRecommender();
//		bind(Suggestions.class).toInstance(new Suggestions(graphRoot, episodeParser, episodeLearned, transitivityClosure, query, mappingParser, recommender, graphConverter, graphWriter));
		bind(EpisodeGraphGeneratorValidationData.class).toInstance(new EpisodeGraphGeneratorValidationData(graphRoot, validationParser, mappingParser, transitivityClosure, graphWriter, graphConverter));
		bind(EpisodeGraphGeneratorTrainingData.class).toInstance(new EpisodeGraphGeneratorTrainingData(graphRoot, episodeParser, episodeLearned, mappingParser, transitivityClosure, graphWriter, graphConverter));
	}

	private void bindInstances(Map<String, Directory> dirs) {
		for (String name : dirs.keySet()) {
			Directory dir = dirs.get(name);
			bind(Directory.class).annotatedWith(Names.named(name)).toInstance(dir);
		}
	}
}