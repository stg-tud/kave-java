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

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.ToFactsVisitor;
import cc.recommenders.io.ReadingArchive;

public class ValidationContextsParser {

	private File rootFolder;
	
	public ValidationContextsParser(@Named("contexts")File directory) {
		assertTrue(directory.exists(), "Frequent episode folder does not exist");
		assertTrue(directory.isDirectory(), "Frequent episode folder is not a folder, but a file");
		this.rootFolder = directory;
	}
	
	public List<Episode> parse(List<Event> eventsList) throws ZipException, IOException {
		List<Episode> validationEpisodes = new LinkedList<Episode>();
		
		for (File zip : findAllZips(rootFolder.getAbsolutePath())) {
			ReadingArchive ra = new ReadingArchive(zip);
			
			int i = 0;
			
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				
				ToFactsVisitor tfv = new ToFactsVisitor(eventsList);
				
				ISST sst = ctx.getSST();
				
				int counter = 0;
				
				for(IMethodDeclaration md : sst.getMethods()) {
					
					Set<Fact> facts = Sets.newHashSet();
					
//					tfv.visit(md, facts);
					
					md.accept(tfv, facts);
					
					System.out.println("Creating episode" + ++counter);
					Episode ep = createEpisode(facts);
					validationEpisodes.add(ep);
					
//					System.out.println("New episode");
//					for (Fact f : ep.getFacts()) {
//						if (!f.isRelation())
//							System.out.println(ep.toString());
//					}
				}
				
				if (i++ > 10) {
					System.out.println("\t... (skipping the rest)");
					ra.close();
					return validationEpisodes;
				}
			}
			ra.close();
		}
		return validationEpisodes;
	}
	
	private Episode createEpisode(Set<Fact> facts) {
		Episode episode = new Episode();
		episode.setFrequency(1);
		episode.setNumEvents(facts.size());
		
		for(Fact f : facts){
			episode.addFact(f);
		} 
		for (int first = 0; first < facts.size() - 1; first++) {
			for (int second = first + 1; second < facts.size(); second++) {
				episode.addFact(new Fact(episode.get(first), episode.get(second)));
			}
		}
		return episode;
	}

	private List<File> findAllZips(String dir) {
		List<File> zips = Lists.newLinkedList();
		for (File f : FileUtils.listFiles(new File(dir), new String[] { "zip" }, true)) {
			zips.add(f);
		}
		return zips;
	}
}
