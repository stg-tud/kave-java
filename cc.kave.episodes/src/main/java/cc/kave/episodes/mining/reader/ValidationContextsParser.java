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
package cc.kave.episodes.mining.reader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipException;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.ToFactsVisitor;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class ValidationContextsParser {

	private Directory rootDir;

	public ValidationContextsParser(@Named("contexts") Directory directory) {
		this.rootDir = directory;
	}

	public List<Episode> parse(List<Event> eventsList) throws ZipException, IOException {
		List<Episode> validationEpisodes = new LinkedList<Episode>();

		for (String zip : findZips()) {
			ReadingArchive ra = rootDir.getReadingArchive(zip);

			int i = 0;
			int counter = 0;

			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);

				ISSTNodeVisitor<Set<Fact>, Void> tfv = new ToFactsVisitor(eventsList);

				ISST sst = ctx.getSST();

				for (IMethodDeclaration md : sst.getMethods()) {

					Set<Fact> facts = Sets.newHashSet();

					md.accept(tfv, facts);

//					if (counter == 42) {
						Logger.log("Creating episode " + ++counter);
						Episode ep = createEpisode(facts);
						validationEpisodes.add(ep);
						
						Logger.log("Output strange episode:" + ep.toString());
//						return validationEpisodes;
//					} else {
//						counter++;
//					}
				}

				if (i++ > 10) {
					Logger.log("\t... (skipping the rest)");
					ra.close();
					return validationEpisodes;
				}
			}
			ra.close();
		}
		return validationEpisodes;
	}

	private Set<String> findZips() {
		Set<String> zips = rootDir.findFiles(new Predicate<String>() {

			@Override
			public boolean apply(String arg0) {
				return arg0.endsWith(".zip");
			}
		});
		return zips;
	}

	private Episode createEpisode(Set<Fact> facts) {
		int numberEvents = 0;

		Episode episode = new Episode();
		episode.setFrequency(1);

		for (Fact f : facts) {
			if (!f.isRelation()) {
				episode.addFact(f);
				numberEvents++;
			} else {
				Tuple<Fact, Fact> factsTuple = f.getRelationFacts();
				if (!factsTuple.getFirst().equals(factsTuple.getSecond())) {
					episode.addFact(f);
				}
			}
		}
		episode.setNumEvents(numberEvents);

		return episode;
	}
}