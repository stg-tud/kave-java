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
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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
import java.util.stream.Stream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.reflect.TypeToken;

import cc.kave.commons.pointsto.io.IOHelper;
import cc.kave.commons.pointsto.io.ZipArchive;
import cc.kave.commons.utils.json.legacy.GsonUtil;
import cc.recommenders.names.ITypeName;
import cc.recommenders.names.Names;
import cc.recommenders.usages.Usage;

public class ProjectUsageStore implements UsageStore {

	private static final String CACHE_FILENAME = "cache.dat";

	private Path baseDir;

	private Map<Path, ProjectStore> projectDirToStore = Collections.synchronizedMap(new HashMap<>());

	private boolean cacheInvalidated;

	public ProjectUsageStore(Path baseDir) throws IOException {
		this.baseDir = baseDir;
		Files.createDirectories(baseDir);

		if (!loadCache()) {
			// manually load projects
			Set<Path> projectDirs = ProjectStore.getProjectDirs(baseDir);
			for (Path projectDir : projectDirs) {
				projectDirToStore.put(projectDir, new ProjectStore(projectDir));
			}

			invalidateCache();
		}

	}

	private boolean loadCache() throws IOException {
		Path cacheFile = baseDir.resolve(CACHE_FILENAME);
		if (!Files.exists(cacheFile)) {
			return false;
		}

		try (Stream<String> lines = Files.lines(cacheFile, StandardCharsets.UTF_8)) {
			projectDirToStore.putAll(lines.parallel().map(line -> Paths.get(line))
					.collect(Collectors.toMap(p -> baseDir.resolve(p), p -> {
						try {
							return new ProjectStore(baseDir.resolve(p));
						} catch (IOException e) {
							throw new UncheckedIOException(e);
						}
					})));

			return true;
		} catch (UncheckedIOException e) {
			throw e.getCause();
		}
	}

	private void storeCache() throws IOException {
		Path cacheFile = baseDir.resolve(CACHE_FILENAME);

		try (Stream<String> lines = projectDirToStore.keySet().stream().map(p -> baseDir.relativize(p).toString())) {
			Files.write(cacheFile, (Iterable<String>) lines::iterator, StandardCharsets.UTF_8);
		}
		cacheInvalidated = false;
	}

	private void invalidateCache() throws IOException {
		cacheInvalidated = true;
		Path cacheFile = baseDir.resolve(CACHE_FILENAME);
		if (Files.exists(cacheFile)) {
			Files.delete(cacheFile);
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

	public String getName() {
		return baseDir.getFileName().toString();
	}

	public void store(Collection<Usage> usages, ProjectIdentifier project) throws IOException {
		store(usages, project.getProjectDirectory());
	}

	@Override
	public void store(Collection<Usage> usages, Path relativeInput) throws IOException {
		invalidateCache();

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

	public Set<ProjectIdentifier> getProjects(ITypeName type) {
		Set<ProjectIdentifier> projects = new HashSet<>();

		synchronized (projectDirToStore) {
			for (Map.Entry<Path, ProjectStore> entry : projectDirToStore.entrySet()) {
				if (entry.getValue().getAllTypes().contains(type)) {
					Path relativeProjectDir = baseDir.relativize(entry.getKey());
					projects.add(new ProjectIdentifier(relativeProjectDir));
				}
			}
		}

		return projects;
	}

	public Map<ProjectIdentifier, Integer> getNumberOfUsagesPerProject(ITypeName type) throws IOException {
		Map<ProjectIdentifier, Integer> projectUsageCounts = new HashMap<>(projectDirToStore.size());

		synchronized (projectDirToStore) {
			for (Map.Entry<Path, ProjectStore> entry : projectDirToStore.entrySet()) {
				Path relativeProjectDir = baseDir.relativize(entry.getKey());
				ProjectIdentifier identifier = new ProjectIdentifier(relativeProjectDir);
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
				Path relativeProjectDir = baseDir.relativize(projectEntry.getKey());
				ProjectIdentifier identifier = new ProjectIdentifier(relativeProjectDir);
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

			if (cacheInvalidated) {
				storeCache();
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

		private boolean cacheInvalidated;

		public ProjectStore(Path projectDir) throws IOException {
			this.projectDir = projectDir;
			if (!Files.exists(projectDir)) {
				Files.createDirectories(projectDir);
			}

			if (!loadCache()) {
				loadTypes();
				invalidateCache();
			}
		}

		private boolean loadCache() throws IOException {
			Path cacheFile = projectDir.resolve(CACHE_FILENAME);
			if (!Files.exists(cacheFile)) {
				return false;
			}

			Map<ITypeName, Collection<String>> typeFiles = GsonUtil.deserialize(cacheFile.toFile(),
					new TypeToken<Map<ITypeName, Collection<String>>>() {
						private static final long serialVersionUID = 1L;
					}.getType());
			for (Map.Entry<ITypeName, Collection<String>> typeFile : typeFiles.entrySet()) {
				for (String file : typeFile.getValue()) {
					typeToZipFiles.put(typeFile.getKey(), projectDir.resolve(file));
				}
			}

			return true;
		}

		private void storeCache() throws IOException {
			Multimap<ITypeName, String> typeFiles = HashMultimap.create(typeToZipFiles.keys().size(), 1);
			for (Map.Entry<ITypeName, Path> typeFile : typeToZipFiles.entries()) {
				typeFiles.put(typeFile.getKey(), projectDir.relativize(typeFile.getValue()).toString());
			}
			GsonUtil.serialize(typeFiles.asMap(), projectDir.resolve(CACHE_FILENAME).toFile());
			cacheInvalidated = false;
		}

		private void invalidateCache() throws IOException {
			cacheInvalidated = true;
			Path cacheFile = projectDir.resolve(CACHE_FILENAME);
			if (Files.exists(cacheFile)) {
				Files.delete(cacheFile);
			}
		}

		private static Set<Path> getProjectDirs(Path basePath) throws IOException {
			Set<Path> projectDirs = new HashSet<>();
			Files.walkFileTree(basePath, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.getFileName().toString().matches(TYPE_FILE_PATTERN)) {
						projectDirs.add(file.getParent().getParent());
						return FileVisitResult.SKIP_SIBLINGS;
					} else {
						return FileVisitResult.CONTINUE;
					}
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					if (projectDirs.contains(dir.getParent())) {
						return FileVisitResult.SKIP_SIBLINGS;
					} else {
						return FileVisitResult.CONTINUE;
					}
				}
			});

			return projectDirs;
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
			invalidateCache();
			Multimap<ITypeName, Usage> typeToUsages = ArrayListMultimap.create(); // keep
																					// duplicate
																					// key-value
																					// pairs

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

			if (cacheInvalidated) {
				storeCache();
			}

			typeToZipFiles.clear();
		}

	}

}
