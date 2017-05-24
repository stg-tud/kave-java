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
package cc.kave.episodes.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.episodes.eventstream.EventStreamRepository;
import cc.kave.episodes.eventstream.Filters;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ContextsParser {

	private Directory contextsDir;
	private Filters filters;

	@Inject
	public ContextsParser(@Named("contexts") Directory directory,
			Filters filters) {
		this.contextsDir = directory;
		this.filters = filters;
	}

	private Map<String, Set<IMethodName>> repoDecls = Maps.newLinkedHashMap();
	private Set<IMethodName> allDecls = Sets.newLinkedHashSet();

	private Set<ITypeName> typeDecls = Sets.newHashSet();
	private Set<IMethodName> ctxFirst = Sets.newHashSet();
	private Set<IMethodName> ctxSuper = Sets.newHashSet();
	private Set<IMethodName> ctxElement = Sets.newHashSet();
	private Set<IMethodName> invExpr = Sets.newHashSet();

	int numbFirst = 0;
	int numbSuper = 0;
	int numbElement = 0;
	int numbInv = 0;

	int numGenerated = 0;

	public void parse() throws Exception {
		EventStreamRepository eventStream = new EventStreamRepository();
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if (repoName.isEmpty() || !zip.startsWith(repoName)) {
				if (!eventStream.getEventStream().isEmpty()) {
					processStream(repoName, eventStream.getEventStream());
					numGenerated += eventStream.getNumbGeneratedMethods();
					eventStream = new EventStreamRepository();
				}
				repoName = getRepoName(zip);
				repoDecls.put(repoName, Sets.newHashSet());
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				repoDecls.get(repoName).addAll(getElementMethod(ctx));
				eventStream.add(ctx);
			}
			ra.close();
		}
		processStream(repoName, eventStream.getEventStream());
		numGenerated += eventStream.getNumbGeneratedMethods();
		printStats();
		checkForEmptyRepos();
	}

	private void checkForEmptyRepos() {
		int numbEmptyRepos = 0;

		for (Map.Entry<String, Set<IMethodName>> entry : repoDecls.entrySet()) {
			if (entry.getValue().isEmpty()) {
				Logger.log("Empty repository: %s", entry.getKey());
				numbEmptyRepos++;
			}
		}
		Logger.log("------");
		Logger.log("Empty repositories counted: %d", numbEmptyRepos);
	}

	private void processStream(String repoName, List<Event> eventStream) {
		Map<Event, List<Event>> streamFilters = filters.apply(
				repoDecls.get(repoName), eventStream);
		addStats(streamFilters);
	}

	private void addStats(Map<Event, List<Event>> eventStream) {
		for (Map.Entry<Event, List<Event>> entry : eventStream.entrySet()) {
			IMethodName decl = entry.getKey().getMethod();
			ctxElement.add(decl);
			numbElement++;

			typeDecls.add(decl.getDeclaringType());

			for (Event event : entry.getValue()) {
				switch (event.getKind()) {
				case FIRST_DECLARATION:
					ctxFirst.add(event.getMethod());
					numbFirst++;
					break;
				case SUPER_DECLARATION:
					ctxSuper.add(event.getMethod());
					numbSuper++;
					break;
				case INVOCATION:
					invExpr.add(event.getMethod());
					numbInv++;
					break;
				default:
					System.err.println("should not happen");
				}
			}
		}
	}

	private void printStats() {
		Logger.log("Number of repositories: %d", repoDecls.size());
		
		Logger.log("------");
		Logger.log("typeDecls:\t\t(%d unique)", typeDecls.size());
		Logger.log("ctxFirst:\t%d (%d unique)", numbFirst, ctxFirst.size());
		Logger.log("ctxSuper:\t%d (%d unique)", numbSuper, ctxSuper.size());
		Logger.log("ctxElement:\t%d (%d unique)", numbElement,
				ctxElement.size());
		Logger.log("invExpr:\t%d (%d unique)", numbInv, invExpr.size());
		Logger.log("------");
		int length = numbFirst + numbSuper + numbElement + numbInv;
		Logger.log("full stream:\t%d events (excl. types)", length);
		
		Logger.log("-----");
		Logger.log("Generated methods:\t%d", numGenerated);
		Logger.log("Overlapping methods:\t%d", filters.getNumOverlaps());
		Logger.log("Unknown events:\t%d", filters.getNumUnknowns());
		Logger.log("Local events:\t%d", filters.getNumLocals());
		Logger.log("Removed events:\t%d", filters.getNumRemovals());
	}

	private Set<IMethodName> getElementMethod(Context ctx) {
		Set<IMethodName> methods = Sets.newHashSet();

		ISST sst = ctx.getSST();
		Set<IMethodDeclaration> decls = sst.getMethods();

		for (IMethodDeclaration d : decls) {
			IMethodName name = d.getName();
			if (allDecls.add(name)) {
				methods.add(name);
			}
		}
		return methods;
	}

	private String getRepoName(String zipName) {
		int index = zipName.indexOf("/",
				zipName.indexOf("/", zipName.indexOf("/") + 1) + 1);
		String startPrefix = zipName.substring(0, index);

		return startPrefix;
	}

	private Set<String> findZips(Directory contextsDir) {
		Set<String> zips = contextsDir.findFiles(new Predicate<String>() {

			@Override
			public boolean apply(String arg0) {
				return arg0.endsWith(".zip");
			}
		});
		return zips;
	}
}
