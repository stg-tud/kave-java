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
package cc.kave.commons.utils.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cc.kave.commons.utils.json.JsonUtils;

public class ZipWriter {
	private String path;
	private List<String> entries;

	public ZipWriter(String path) {
		// assertTrue(new File(path).exists() && new File(path).isDirectory());
		this.path = path;
		this.entries = new ArrayList<>();
	}

	public <T> void add(T obj) {
		add(obj, obj.getClass());
	}

	public <T> void addAll(List<T> entries) {
		for (T obj : entries) {
			String json = JsonUtils.toJson(obj, obj.getClass());
			this.entries.add(json);
		}
	}

	public <T> void add(T obj, Type targetType) {
		String json = JsonUtils.toJson(obj, targetType);
		entries.add(json);
	}

	public void dispose() {
		if (entries.isEmpty())
			return;
		int num = 0;
		try {
			File f = new File(path.substring(0, path.lastIndexOf("/") + 1));
			if (!f.exists())
				f.mkdirs();
			FileOutputStream fos = new FileOutputStream(path, false);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (String s : entries) {
				String fileName = num + ".json";
				num++;
				ZipEntry entry = new ZipEntry(fileName);
				zos.putNextEntry(entry);
				zos.write(s.getBytes(), 0, s.length());
				zos.closeEntry();
			}
			zos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		entries.clear();
	}
}