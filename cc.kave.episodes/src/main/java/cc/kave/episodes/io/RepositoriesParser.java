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
package cc.kave.episodes.io;

import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RepositoriesParser {

	private Directory contextsDir;

	@Inject
	public RepositoriesParser(@Named("contexts") Directory directory) {
		this.contextsDir = directory;
	}

	private Set<ITypeName> types = Sets.newLinkedHashSet();
	private Set<ITypeName> duplicateTypes = Sets.newLinkedHashSet();
	private Map<String, Set<ITypeName>> reposTypesMapper = Maps
			.newLinkedHashMap();

	public Map<String, EventStreamGenerator> generateReposEvents()
			throws Exception {
		EventStreamGenerator repoGen = new EventStreamGenerator();
		Map<String, EventStreamGenerator> results = Maps.newLinkedHashMap();
		Set<ITypeName> repoTypes = Sets.newLinkedHashSet();
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if ((repoName.equalsIgnoreCase("")) || (!zip.startsWith(repoName))) {
				if (!repoGen.getEventStream().isEmpty()) {
					results.put(repoName, repoGen);
					repoGen = new EventStreamGenerator();
				}
				types.addAll(repoTypes);
				repoTypes = Sets.newLinkedHashSet();
				repoName = getRepoName(zip);
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);

			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx != null) {
					repoGen.add(ctx);
					ITypeName typeName = ctx.getSST().getEnclosingType();
					if (!types.contains(typeName)) {
						repoTypes.add(typeName);
						addToMapper(repoName, typeName);
					} else {
						duplicateTypes.add(typeName);
					}
				}
			}
			ra.close();
		}
		types.addAll(repoTypes);
		if (!repoGen.getEventStream().isEmpty()) {
			results.put(repoName, repoGen);
		}
		return results;
	}
	
	public Tuple<Integer, Integer> getReposInfo() {
		Tuple<Integer, Integer> tuple = Tuple.newTuple(types.size(), duplicateTypes.size());
		return tuple;
	}
	
	public Map<String, Set<ITypeName>> getRepoTypesMapper() {
		return this.reposTypesMapper;
	}

	private void addToMapper(String repoName, ITypeName typeName) {

		if (reposTypesMapper.containsKey(repoName)) {
			Set<ITypeName> types = reposTypesMapper.get(repoName);
			types.add(typeName);
			reposTypesMapper.put(repoName, types);
		} else {
			reposTypesMapper.put(repoName, Sets.newHashSet(typeName));
		}
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
