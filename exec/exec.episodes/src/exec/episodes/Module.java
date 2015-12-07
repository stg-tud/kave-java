package exec.episodes;

/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */

import java.io.File;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import cc.kave.commons.mining.episodes.EpisodeGraphGenerator;
import cc.kave.commons.mining.episodes.EpisodeRecommender;
import cc.kave.commons.mining.episodes.EpisodeToGraphConverter;
import cc.kave.commons.mining.episodes.MaximalFrequentEpisodes;
import cc.kave.commons.mining.episodes.NoTransitivelyClosedEpisodes;
import cc.kave.commons.mining.reader.EpisodeParser;
import cc.kave.commons.mining.reader.EventMappingParser;
import cc.kave.commons.mining.reader.EventStreamAsListOfMethodsParser;
import cc.kave.commons.mining.reader.EventStreamParser;
import cc.kave.commons.mining.reader.FileReader;
import cc.kave.commons.model.persistence.EpisodeAsGraphWriter;
import cc.kave.commons.model.persistence.EventStreamModifier;
import cc.recommenders.io.Directory;

public class Module extends AbstractModule {

	private final String rootFolder;

	public Module(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	@Override
	protected void configure() {
		File episodeFile = new File(rootFolder + "n-graph-miner/");
		Directory episodeDir = new Directory(episodeFile.getAbsolutePath());
		File eventStreamFile = new File(rootFolder + "EpisodeMining/EventStreamForEpisodeMining/");
		Directory eventStreamDir = new Directory(eventStreamFile.getAbsolutePath());
		File mappingFile = new File(rootFolder + "EpisodeMining/EventStreamForEpisodeMining/");
		Directory mappingDir = new Directory(mappingFile.getAbsolutePath());
		File graphFile = new File(rootFolder);
		Directory graphDir = new Directory(graphFile.getAbsolutePath());

		Map<String, Directory> dirs = Maps.newHashMap();
		dirs.put("episode", episodeDir);
		dirs.put("events", eventStreamDir);
		dirs.put("mapping", mappingDir);
		dirs.put("graph", graphDir);
		bindInstances(dirs);

		bind(File.class).annotatedWith(Names.named("episode")).toInstance(episodeFile);
		bind(File.class).annotatedWith(Names.named("events")).toInstance(eventStreamFile);
		bind(File.class).annotatedWith(Names.named("mapping")).toInstance(mappingFile);
		bind(File.class).annotatedWith(Names.named("graph")).toInstance(graphFile);

		File episodeRoot = episodeFile;
		FileReader reader = new FileReader();
		bind(EpisodeParser.class).toInstance(new EpisodeParser(episodeRoot, reader));
		
		File eventStreamRoot = eventStreamFile;
		bind(EventStreamAsListOfMethodsParser.class).toInstance(new EventStreamAsListOfMethodsParser(eventStreamRoot, reader));
		
		File mappingRoot = mappingFile;
		bind(EventMappingParser.class).toInstance(new EventMappingParser(mappingRoot));
		EventMappingParser mappingParser = new EventMappingParser(mappingRoot);
		bind(EventStreamParser.class).toInstance(new EventStreamParser(eventStreamRoot, reader, mappingParser));
		bind(EventStreamModifier.class).toInstance(new EventStreamModifier(eventStreamRoot, reader));
		File graphRoot = graphFile;
		
		EpisodeParser episodeParser = new EpisodeParser(episodeRoot, reader);
		MaximalFrequentEpisodes episodeLearned = new MaximalFrequentEpisodes();
		EpisodeToGraphConverter graphConverter = new EpisodeToGraphConverter();
		EpisodeAsGraphWriter graphWriter = new EpisodeAsGraphWriter();
		NoTransitivelyClosedEpisodes transitivityClosure = new NoTransitivelyClosedEpisodes();
		bind(EpisodeGraphGenerator.class).toInstance(new EpisodeGraphGenerator(graphRoot, episodeParser, episodeLearned, mappingParser, transitivityClosure, graphWriter, graphConverter));
		EventStreamAsListOfMethodsParser query = new EventStreamAsListOfMethodsParser(eventStreamRoot, reader);
		EpisodeRecommender recommender = new EpisodeRecommender();
		bind(Suggestions.class).toInstance(new Suggestions(graphRoot, episodeParser, episodeLearned, transitivityClosure, query, mappingParser, recommender, graphConverter, graphWriter));
	}

	private void bindInstances(Map<String, Directory> dirs) {
		for (String name : dirs.keySet()) {
			Directory dir = dirs.get(name);
			bind(Directory.class).annotatedWith(Names.named(name)).toInstance(dir);
		}
	}
}