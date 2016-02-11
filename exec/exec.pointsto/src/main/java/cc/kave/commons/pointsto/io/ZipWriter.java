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

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWriter<T> implements Closeable {

	private AtomicInteger count = new AtomicInteger();
	private ZipOutputStream output;

	private Function<T, String> serializer;

	public ZipWriter(File file, Function<T, String> serializer) throws FileNotFoundException {
		output = new ZipOutputStream(new FileOutputStream(file));
		this.serializer = serializer;
	}

	public void add(T entry) throws IOException {
		String filename = Integer.toString(count.getAndIncrement()) + ".json";
		add(entry, filename);
	}

	public void add(T entry, String filename) throws IOException {
		byte[] data = serializer.apply(entry).getBytes(StandardCharsets.UTF_8);

		synchronized (output) {
			output.putNextEntry(new ZipEntry(filename));
			output.write(data);
			output.closeEntry();
		}
	}

	@Override
	public void close() throws IOException {
		synchronized (output) {
			output.close();
		}
	}

}
