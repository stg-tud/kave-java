/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.evaluation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.random.RandomGenerator;

import com.google.inject.Inject;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.pointsto.io.IOHelper;
import cc.kave.commons.pointsto.io.ZipArchive;
import cc.kave.commons.utils.json.JsonUtils;

public class ContextSampler {

	private final RandomGenerator rndGenerator;

	@Inject
	public ContextSampler(RandomGenerator rndGenerator) {
		this.rndGenerator = rndGenerator;
	}

	public List<Context> sample(Path contextsDirectory, int number) throws IOException {
		Map<ZipArchive, Integer> registry = buildRegistry(contextsDirectory);

		try {
			int totalNumContexts = registry.values().stream().mapToInt(Integer::intValue).sum();
			LinkedHashSet<Integer> contextIndices = new LinkedHashSet<>();
			if (totalNumContexts < number) {
				for (int i = 0; i < totalNumContexts; ++i) {
					contextIndices.add(i);
				}
			} else {
				for (int i = 0; i < number; ++i) {
					int index;
					do {
						index = rndGenerator.nextInt(totalNumContexts);
					} while (contextIndices.contains(index));
					contextIndices.add(index);
				}
			}

			List<Context> contexts = new ArrayList<>(number);
			for (Integer index : contextIndices) {
				Context ctxt = getContext(registry, index);
				if (ctxt == null) {
					ctxt = new Context();
				}
				contexts.add(ctxt);
			}

			return contexts;
		} finally {
			closeRegistry(registry);
		}
	}

	private Map<ZipArchive, Integer> buildRegistry(Path contextsDirectory) throws IOException {
		Map<ZipArchive, Integer> registry = new LinkedHashMap<>();
		for (Path zipFile : IOHelper.getZipFiles(contextsDirectory)) {
			ZipArchive archive = new ZipArchive(zipFile);
			registry.put(archive, archive.getFiles().size());
		}
		return registry;
	}

	private Context getContext(Map<ZipArchive, Integer> registry, int index) throws IOException {
		int startIndex = 0;
		for (Map.Entry<ZipArchive, Integer> entry : registry.entrySet()) {
			int nextStartIndex = startIndex + entry.getValue();
			if (index < nextStartIndex) {
				ZipArchive archive = entry.getKey();
				Path contextFile = archive.getFiles().get(index - startIndex);
				return archive.load(contextFile, Context.class, JsonUtils::fromJson);
			}
			startIndex = nextStartIndex;
		}

		throw new IllegalArgumentException();
	}

	private void closeRegistry(Map<ZipArchive, Integer> registry) throws IOException {
		for (ZipArchive archive : registry.keySet()) {
			archive.close();
		}
	}

}
