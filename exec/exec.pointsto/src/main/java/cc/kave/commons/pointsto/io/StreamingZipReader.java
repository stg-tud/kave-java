/**
 * Copyright 2015 Simon Reuß
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cc.kave.commons.utils.json.JsonUtils;

public class StreamingZipReader {

	private final ZipFile zip;

	public StreamingZipReader(String filename) throws IOException {
		this(new File(filename));
	}

	public StreamingZipReader(File file) throws IOException {
		zip = new ZipFile(file);
	}

	public <T> Stream<T> stream(Class<T> targetType) {
		return zip.stream().map(new Function<ZipEntry, T>() {

			@Override
			public T apply(ZipEntry entry) {
				StringBuilder builder = new StringBuilder();
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)))) {
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to process zip entry sream", e);
				}

				return JsonUtils.fromJson(builder.toString(), targetType);
			}
		});
	}
}
