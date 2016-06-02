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
package cc.kave.commons.pointsto.io;

import static com.google.common.io.Files.getNameWithoutExtension;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

public class ZipArchive implements Closeable {

	private FileSystem fileSystem;

	private AtomicInteger count;

	public ZipArchive(Path zipFile) throws IOException {
		fileSystem = FileSystems.newFileSystem(URI.create("jar:" + zipFile.toUri()), createEnv());
		count = new AtomicInteger(getNextFreeCount());
	}

	protected Map<String, String> createEnv() {
		Map<String, String> env = new HashMap<>();
		env.put("create", "true");
		return env;
	}

	private Path getRoot() {
		// assume that the file system only has one root
		return fileSystem.getRootDirectories().iterator().next();
	}

	private int getNextFreeCount() throws IOException {
		int currentCount = Files.walk(getRoot())
				.filter(path -> Files.isRegularFile(path)
						&& getNameWithoutExtension(path.getFileName().toString()).matches("\\d+"))
				.map(path -> Integer.parseInt(getNameWithoutExtension(path.getFileName().toString()))).reduce(Math::max)
				.orElse(-1);

		return currentCount + 1;
	}

	private String getNextFilename() {
		return Integer.toString(count.getAndIncrement()) + ".json";
	}

	public <T> void store(T entry, Class<T> targetClass, BiFunction<T, Class<T>, String> serializer)
			throws IOException {
		String filename = getNextFilename();
		byte[] data = serializer.apply(entry, targetClass).getBytes(StandardCharsets.UTF_8);
		store(data, filename);
	}

	public <T> void store(T entry, Function<T, String> serializer) throws IOException {
		String filename = getNextFilename();
		byte[] data = serializer.apply(entry).getBytes(StandardCharsets.UTF_8);
		store(data, filename);
	}

	public void store(byte[] data, String filename) throws IOException {
		Files.write(fileSystem.getPath(filename), data);
	}

	/**
	 * Provides a {@link Stream} of the deserialized contents of this archive.
	 */
	public <T> Stream<T> stream(Class<T> targetClass, BiFunction<InputStream, Class<T>, T> deserializer)
			throws IOException {
		return Files.walk(getRoot())
				.filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
				.map((Path file) -> {
					try {
						return load(file, targetClass, deserializer);
					} catch (Exception e) {
						throw Throwables.propagate(e);
					}
				});
	}

	/**
	 * Provides a {@link Stream} of the deserialized contents of this archive that does not abort when a specific
	 * exception is encountered during loading of a context. Note that {@code null} values are discarded.
	 */
	public <T, E> Stream<T> stream(Class<T> targetClass, BiFunction<InputStream, Class<T>, T> deserializer,
			Class<E> exceptionToIgnore) throws IOException {
		return Files.walk(getRoot())
				.filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
				.map(file -> {
					try {
						return load(file, targetClass, deserializer);
					} catch (Exception e) {
						if (e.getClass().equals(exceptionToIgnore)) {
							return null;
						} else {
							throw Throwables.propagate(e);
						}
					}
				}).filter(Objects::nonNull);
	}

	public <T> T load(Path filename, Class<T> targetClass, BiFunction<InputStream, Class<T>, T> deserializer)
			throws IOException {
		try (InputStream inputStream = Files.newInputStream(filename, StandardOpenOption.READ)) {
			return deserializer.apply(inputStream, targetClass);
		}
	}

	public List<Path> getFiles() throws IOException {
		try (DirectoryStream<Path> files = Files.newDirectoryStream(getRoot())) {
			return Lists.newArrayList(files);
		}
	}

	public int countFiles() throws IOException {
		int count = 0;
		try (DirectoryStream<Path> files = Files.newDirectoryStream(getRoot())) {
			for (Path file : files) {
				if (Files.isRegularFile(file)) {
					++count;
				}
			}
		}

		return count;
	}

	@Override
	public void close() throws IOException {
		synchronized (fileSystem) {
			fileSystem.close();
		}
	}

}
