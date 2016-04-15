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
package cc.kave.episodes.preprocessing;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.inject.name.Named;

import cc.recommenders.io.Directory;

public class EventStreamMappingGenerator {

	private Directory rootDir;
	
	public EventStreamMappingGenerator(@Named("contexts") Directory directory) {
		this.rootDir = directory;
	}
	
	public void readAllContexts() {
		
	}
	
	private List<File> findAllZips(String dir) {
		List<File> zips = Lists.newLinkedList();
		for (File f : FileUtils.listFiles(new File(dir), new String[] { "zip" }, true)) {
			zips.add(f);
		}
		return zips;
	}
	
	private String getContextsPath() {
		String path = "";
		return path;
	}
}
