/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.recommenders.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.assertions.Asserts;

public class ZipFolderLRUCache<T> implements AutoCloseable {

	private final String _root;
	private final int _capacity;

	private final List<T> _accessOrderList = Lists.newLinkedList();
	private final Map<T, WritingArchive> _openArchives = Maps.newLinkedHashMap();
	private final Map<T, ZipFolder> _folders = Maps.newLinkedHashMap();

	public ZipFolderLRUCache(String root, int capacity) {
		Asserts.assertTrue(new File(root).exists());
		Asserts.assertTrue(capacity > 0);

		_root = root;
		_capacity = capacity;
	}

	public WritingArchive getArchive(T key) {
		RefreshAccess(key);

		if (_openArchives.containsKey(key)) {
			return _openArchives.get(key);
		}

		ZipFolder folder = _folders.containsKey(key) ? _folders.get(key) : GetFolder(key);

		WritingArchive wa = folder.createNewArchive();
		_openArchives.put(key, wa);

		EnsureCapacityIsRespected();

		return wa;
	}

	private void EnsureCapacityIsRespected() {
		if (_accessOrderList.size() > _capacity) {
			T oldest = _accessOrderList.iterator().next();
			_accessOrderList.remove(oldest);
			try {
				_openArchives.get(oldest).close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			_openArchives.remove(oldest);
		}
	}

	private void RefreshAccess(T key) {
		_accessOrderList.remove(key);
		_accessOrderList.add(key);
	}

	private ZipFolder GetFolder(T key) {
		if (_folders.containsKey(key)) {
			return _folders.get(key);
		}

		String folderName = GetTargetFolder(key);
		if (!new File(folderName).exists()) {
			new File(folderName).mkdirs();
		}
		ZipFolder folderUtil = new ZipFolder(folderName, JsonUtils.toJson(key));

		_folders.put(key, folderUtil);

		return folderUtil;
	}

	private String GetTargetFolder(T key) {
		// TODO regex magic
		// var regex = new Regex(@"[^a-zA-Z0-9,\\-_/+$(){}[\\]]");

		String relName = JsonUtils.toJson(key);
		relName = relName.replace('.', '/');
		relName = relName.replace("\\\"", "\""); // quotes inside json
		relName = relName.replace("\"", ""); // surrounding quotes
		relName = relName.replace('\\', '/');
		// relName = regex.Replace(relName, "_");

		return new File(_root, relName).getAbsolutePath();
	}

	public boolean isCached(T key) {
		return _openArchives.containsKey(key);
	}

	public int size() {
		return _openArchives.size();
	}

	public void close() {
		for (WritingArchive wa : _openArchives.values()) {
			try {
				wa.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		_accessOrderList.clear();
		_openArchives.clear();
		_folders.clear();
	}
}