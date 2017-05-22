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

import org.apache.mahout.classifier.df.mapreduce.inmem.InMemBuilder;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.episodes.eventstream.EventStreamRepository;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
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

	@Inject
	public ContextsParser(@Named("contexts") Directory directory) {
		this.contextsDir = directory;
	}

	private Map<String, Set<IMethodName>> repoDecls = Maps.newLinkedHashMap();
	private Set<ITypeName> types = Sets.newLinkedHashSet();
	private Set<IMethodDeclaration> allDecls = Sets.newLinkedHashSet();

	public void parse() throws Exception {
		EventStreamRepository eventStream = new EventStreamRepository();
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if (repoName.isEmpty() || !zip.startsWith(repoName)) {
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
		calcStats(eventStream.getEventStream());
	}

	private void calcStats(List<Event> eventStream) {
		Map<ITypeName, Integer> typeDecls = Maps.newLinkedHashMap();
		Map<IMethodName, Integer> ctxFirst = Maps.newLinkedHashMap();
		Map<IMethodName, Integer> ctxSuper = Maps.newLinkedHashMap();
		Map<IMethodName, Integer> ctxElement = Maps.newLinkedHashMap();
		Map<IMethodName, Integer> invExpr = Maps.newLinkedHashMap();

		for (Event event : eventStream) {
			if (event.getKind() == EventKind.TYPE) {
				ITypeName type = event.getType();
				if (typeDecls.containsKey(type)) {
					int counter = typeDecls.get(type);
					typeDecls.put(type, counter + 1);
				} else {
					typeDecls.put(type, 1);
				}
				continue;
			}
			IMethodName name = event.getMethod();
			if (event.getKind() == EventKind.FIRST_DECLARATION) {
				if (ctxFirst.containsKey(name)) {
					int counter = ctxFirst.get(name);
					ctxFirst.put(name, counter + 1);
				} else {
					ctxFirst.put(name, 1);
				}
				continue;
			}
			if (event.getKind() == EventKind.SUPER_DECLARATION) {
				if (ctxSuper.containsKey(name)) {
					int counter = ctxSuper.get(name);
					ctxSuper.put(name, counter + 1);
				} else {
					ctxSuper.put(name, 1);
				}
				continue;
			}
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				if (ctxElement.containsKey(name)) {
					int counter = ctxElement.get(name);
					ctxElement.put(name, counter + 1);
				} else {
					ctxElement.put(name, 1);
				}
				continue;
			}
			if (event.getKind() == EventKind.INVOCATION) {
				
			}
		}
	}

	private Set<IMethodName> getElementMethod(Context ctx) {
		Set<IMethodName> methods = Sets.newHashSet();

		ISST sst = ctx.getSST();
		ITypeName typeName = sst.getEnclosingType();
		types.add(typeName);

		Set<IMethodDeclaration> decls = sst.getMethods();

		for (IMethodDeclaration d : decls) {
			if (!allDecls.contains(d)) {
				allDecls.add(d);
				methods.add(d.getName());
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

	private void printStatistics() {
		Logger.log("Number of repositories: %d", repoDecls.size());
		Logger.log("Number of visited classes: %d", types.size());
	}
}
