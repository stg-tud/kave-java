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

import cc.kave.commons.mining.reader.EpisodeParser;
import cc.kave.commons.mining.reader.FileReader;
import cc.kave.commons.mining.reader.QueryParser;
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

		Map<String, Directory> dirs = Maps.newHashMap();
		dirs.put("episode", episodeDir);
		dirs.put("events", eventStreamDir);
		bindInstances(dirs);

		bind(File.class).annotatedWith(Names.named("episode")).toInstance(episodeFile);
		bind(File.class).annotatedWith(Names.named("events")).toInstance(eventStreamFile);

		File episodeRoot = episodeFile;
		FileReader reader = new FileReader();
		bind(EpisodeParser.class).toInstance(new EpisodeParser(episodeRoot, reader));
		File eventStreamRoot = eventStreamFile;
		bind(QueryParser.class).toInstance(new QueryParser(eventStreamRoot, reader));
	}

	private void bindInstances(Map<String, Directory> dirs) {
		for (String name : dirs.keySet()) {
			Directory dir = dirs.get(name);
			bind(Directory.class).annotatedWith(Names.named(name)).toInstance(dir);
		}
	}
}