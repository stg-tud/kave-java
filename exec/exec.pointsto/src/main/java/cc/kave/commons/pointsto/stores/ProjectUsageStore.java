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
package cc.kave.commons.pointsto.stores;

import static com.google.common.io.Files.getNameWithoutExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import cc.kave.commons.pointsto.io.IOHelper;
import cc.kave.commons.pointsto.io.ZipArchive;
import cc.recommenders.names.ITypeName;
import cc.recommenders.names.Names;
import cc.recommenders.usages.Usage;
import cc.recommenders.utils.gson.GsonUtil;

public class ProjectUsageStore implements UsageStore {

	private Path baseDir;

	private Map<Path, ProjectStore> projectDirToStore = Collections.synchronizedMap(new HashMap<>());

	public ProjectUsageStore(Path baseDir) throws IOException {
		this.baseDir = baseDir;

		Files.createDirectories(baseDir);
		Set<Path> projectDirs = ProjectStore.getProjectDirs(baseDir);
		for (Path projectDir : projectDirs) {
			projectDirToStore.put(projectDir, new ProjectStore(projectDir));
		}
	}

	private Path getProjectDir(Path relativeInput) {
		String baseName = getNameWithoutExtension(relativeInput.getFileName().toString());
		Path relativeProjectDir = relativeInput.getParent().resolve(baseName);
		return baseDir.resolve(relativeProjectDir);
	}

	private ProjectStore getProjectStore(Path projectDir) throws IOException {
		ProjectStore store;
		synchronized (projectDirToStore) {
			store = projectDirToStore.get(projectDir);
			if (store == null) {
				store = new ProjectStore(projectDir);
				projectDirToStore.put(projectDir, store);
			}
		}

		return store;
	}

	@Override
	public void store(Collection<Usage> usages, Path relativeInput) throws IOException {
		Path projectDir = getProjectDir(relativeInput);
		ProjectStore store = getProjectStore(projectDir);

		store.store(usages, relativeInput);
	}

	@Override
	public Set<ITypeName> getAllTypes() {
		Set<ITypeName> types = new HashSet<>();
		synchronized (projectDirToStore) {
			for (ProjectStore store : projectDirToStore.values()) {
				types.addAll(store.getAllTypes());
			}
		}
		return types;
	}

	public int getNumberOfProjects() {
		return projectDirToStore.size();
	}

	public Map<ProjectIdentifier, Integer> getNumberOfUsagesPerProject(ITypeName type) throws IOException {
		Map<ProjectIdentifier, Integer> projectUsageCounts = new HashMap<>(projectDirToStore.size());

		synchronized (projectDirToStore) {
			for (Map.Entry<Path, ProjectStore> entry : projectDirToStore.entrySet()) {
				ProjectIdentifier identifier = new ProjectIdentifier(entry.getKey());
				int count = entry.getValue().getNumberOfUsages(type);
				projectUsageCounts.put(identifier, count);
			}
		}

		return projectUsageCounts;
	}

	public Map<ProjectIdentifier, List<Usage>> loadUsagesPerProject(ITypeName type) throws IOException {
		return loadUsagesPerProject(type, x -> true);
	}

	public Map<ProjectIdentifier, List<Usage>> loadUsagesPerProject(ITypeName type, Predicate<Usage> filter)
			throws IOException {
		Map<ProjectIdentifier, List<Usage>> projectUsages = new HashMap<>(getNumberOfProjects());

		synchronized (projectDirToStore) {
			for (Map.Entry<Path, ProjectStore> projectEntry : projectDirToStore.entrySet()) {
				ProjectIdentifier identifier = new ProjectIdentifier(projectEntry.getKey());
				projectUsages.put(identifier, projectEntry.getValue().load(type, filter));
			}
		}

		return projectUsages;
	}

	@Override
	public List<Usage> load(ITypeName type) throws IOException {
		return load(type, x -> true);
	}

	@Override
	public List<Usage> load(ITypeName type, Predicate<Usage> filter) throws IOException {
		List<Usage> usages = new ArrayList<>();
		synchronized (projectDirToStore) {
			for (ProjectStore store : projectDirToStore.values()) {
				usages.addAll(store.load(type, filter));
			}
		}
		return usages;
	}

	@Override
	public void flush() throws IOException {
		synchronized (projectDirToStore) {
			for (ProjectStore store : projectDirToStore.values()) {
				store.flush();
			}
		}
	}

	@Override
	public void close() throws IOException {
		synchronized (projectDirToStore) {
			for (ProjectStore store : projectDirToStore.values()) {
				store.close();
			}
			projectDirToStore.clear();
		}
	}

	private static class ProjectStore implements UsageStore {

		private static final String TYPE_FILE_NAME = "type.json";
		private static final String TYPE_FILE_PATTERN = "type\\.json";

		private Path projectDir;

		private Multimap<ITypeName, Path> typeToZipFiles = Multimaps.synchronizedMultimap(HashMultimap.create());
		private Map<Path, ZipArchive> openArchives = Collections.synchronizedMap(new HashMap<>());

		public ProjectStore(Path projectDir) throws IOException {
			this.projectDir = projectDir;
			if (!Files.exists(projectDir)) {
				Files.createDirectories(projectDir);
			}

			loadTypes();
		}

		private static Set<Path> getProjectDirs(Path basePath) throws IOException {
			return IOHelper.streamFiles(TYPE_FILE_PATTERN, basePath).map(typeFile -> typeFile.getParent().getParent())
					.collect(Collectors.toSet());
		}

		private Path getTypePath(ITypeName type) {
			String typeName = Names.vm2srcQualifiedType(type).replace("[]", "__").replace("$", "_");
			return projectDir.resolve(typeName);
		}

		private ZipArchive getArchive(Path zipFile) throws IOException {
			ZipArchive archive;
			synchronized (openArchives) {
				archive = openArchives.get(zipFile);

				if (archive == null) {
					archive = new ZipArchive(zipFile);
					openArchives.put(zipFile, archive);
				}
			}

			return archive;
		}

		private void loadTypes() throws IOException {
			List<Path> typeFiles = IOHelper.getFiles(TYPE_FILE_PATTERN, projectDir);

			synchronized (typeToZipFiles) {
				typeToZipFiles.clear();

				for (Path typeFile : typeFiles) {
					ITypeName type = GsonUtil.deserialize(typeFile.toFile(), ITypeName.class);
					Path typeDir = typeFile.getParent();

					for (Path zipFile : IOHelper.getZipFiles(typeDir)) {
						typeToZipFiles.put(type, zipFile);
					}
				}
			}
		}

		private Path initZipFile(ITypeName type) throws IOException {
			Path zipFile = getTypePath(type).resolve("usages.zip");
			typeToZipFiles.put(type, zipFile);

			IOHelper.createParentDirs(zipFile);
			Path parentDir = zipFile.getParent();
			Files.createDirectories(parentDir);
			GsonUtil.serialize(type, parentDir.resolve(TYPE_FILE_NAME).toFile());

			return zipFile;
		}

		private Path getZipFile(ITypeName type) throws IOException {
			Path zipFile;
			synchronized (typeToZipFiles) {
				Collection<Path> zipFiles = typeToZipFiles.get(type);
				if (zipFiles.isEmpty()) {
					zipFile = initZipFile(type);
				} else {
					zipFile = zipFiles.iterator().next();
				}
			}

			return zipFile;
		}

		@Override
		public void store(Collection<Usage> usages, Path relativeInput) throws IOException {
			Multimap<ITypeName, Usage> typeToUsages = ArrayListMultimap.create(); // keep duplicate key-value pairs

			for (Usage usage : usages) {
				typeToUsages.put(usage.getType(), usage);
			}

			for (ITypeName type : typeToUsages.keySet()) {
				Path zipFile = getZipFile(type);

				ZipArchive archive = getArchive(zipFile);
				for (Usage usage : typeToUsages.get(type)) {
					archive.store(usage, GsonUtil::serialize);
				}
			}
		}

		@Override
		public Set<ITypeName> getAllTypes() {
			return Collections.unmodifiableSet(typeToZipFiles.keySet());
		}

		public int getNumberOfUsages(ITypeName type) throws IOException {
			int count = 0;

			synchronized (typeToZipFiles) {
				for (Path zipFile : typeToZipFiles.get(type)) {
					ZipArchive archive = getArchive(zipFile);
					count += archive.countFiles();
				}
			}

			return count;
		}

		public Map<ITypeName, Integer> getNumberOfUsages() throws IOException {
			Map<ITypeName, Integer> counts = new HashMap<>(typeToZipFiles.keySet().size());

			synchronized (typeToZipFiles) {
				for (Map.Entry<ITypeName, Path> entry : typeToZipFiles.entries()) {
					ITypeName type = entry.getKey();
					ZipArchive archive = getArchive(entry.getValue());
					Integer count = counts.getOrDefault(type, 0) + archive.countFiles();
					counts.put(type, count);
				}
			}

			return counts;
		}

		@Override
		public List<Usage> load(ITypeName type) throws IOException {
			return load(type, x -> true);
		}

		@Override
		public List<Usage> load(ITypeName type, Predicate<Usage> filter) throws IOException {
			List<Usage> usages = new ArrayList<>();

			synchronized (typeToZipFiles) {
				for (Path zipFile : typeToZipFiles.get(type)) {
					ZipArchive archive = getArchive(zipFile);
					usages.addAll(archive.stream(Usage.class, GsonUtil::deserialize).filter(filter)
							.collect(Collectors.toList()));
				}
			}

			return usages;
		}

		@Override
		public void flush() throws IOException {
			synchronized (openArchives) {
				for (ZipArchive archive : openArchives.values()) {
					archive.close();
				}
				openArchives.clear();
			}
		}

		@Override
		public void close() throws IOException {
			flush();
			typeToZipFiles.clear();
		}

	}

}
