/*******************************************************************************
 * Copyright (c) 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sebastian Proksch - initial API and implementation
 ******************************************************************************/
package cc.recommenders.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.utils.json.JsonUtils;

public class ReadingArchive implements Closeable {

	private ZipFile zipFile;
	private Enumeration<? extends ZipEntry> entries;

	public ReadingArchive(File file) throws ZipException, IOException {
		zipFile = new ZipFile(file);
		entries = zipFile.entries();
	}

	public boolean hasNext() throws IOException {
		return entries.hasMoreElements();
	}

	public <T> T getNext(Type classOfT) throws IOException {

		ZipEntry next = entries.nextElement();
		InputStream in = zipFile.getInputStream(next);
		T obj = JsonUtils.fromJson(in, classOfT);
		in.close();

		return obj;
	}

	public String getNextPlain() throws IOException {

		ZipEntry next = entries.nextElement();
		InputStream in = zipFile.getInputStream(next);
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer, Charset.defaultCharset().toString());
		String str = writer.toString();
		in.close();

		return str;
	}

	public <T> List<T> getAll(Class<T> c) {
		try {
			List<T> out = Lists.newLinkedList();
			while (hasNext()) {
				out.add(getNext(c));
			}
			return out;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int numberOfEntries() {
		return zipFile.size();
	}

	@Override
	public void close() throws IOException {
		zipFile.close();
	}
}