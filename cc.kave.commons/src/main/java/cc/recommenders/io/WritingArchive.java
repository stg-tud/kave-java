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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.collect.Maps;

import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.assertions.Asserts;

public class WritingArchive implements IWritingArchive {

	private final File file;
	private final Map<String, String> content;

	public WritingArchive(File file) {
		Asserts.assertFalse(file.exists());
		File parent = file.getParentFile();
		Asserts.assertTrue(parent.exists());
		Asserts.assertTrue(parent.isDirectory());

		content = Maps.newLinkedHashMap();
		this.file = file;
	}

	@Override
	public <T> void addAll(Iterable<T> objs) {
		for (T obj : objs) {
			add(obj);
		}
	}

	@Override
	public <T> void add(T obj) {
		int count = content.size();
		String filename = count + ".json";
		add(obj, filename);
	}

	@Override
	public <T> void add(T obj, String fileName) {
		addPlain(JsonUtils.toJson(obj), fileName);
	}

	@Override
	public <T> void addPlain(String str) {
		int count = content.size();
		String filename = count + ".txt";
		addPlain(str, filename);
	}

	@Override
	public <T> void addPlain(String str, String fileName) {
		// TODO crash on fileName exists
		content.put(fileName, str);
	}

	@Override
	public void close() {
		try {
			if (!content.isEmpty()) {
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
				for (String fileName : content.keySet()) {
					out.putNextEntry(new ZipEntry(fileName));
					String json = content.get(fileName);
					out.write(json.getBytes());
					out.closeEntry();
				}
				out.close();
				content.clear();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}