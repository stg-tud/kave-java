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
package exec.validate_evaluation.queryhistory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.gson.reflect.TypeToken;

import cc.recommenders.io.Directory;
import cc.recommenders.usages.Usage;

public class QueryHistoryIo {

	private String dir;

	public QueryHistoryIo(String dir) {
		this.dir = dir;
	}

	public Set<String> findQueryHistoryZips() {
		Directory dir = new Directory(this.dir);
		return dir.findFiles(s -> s.endsWith(".json"));
	}

	public void storeQueryHistories(Collection<List<Usage>> collection, String zip) {

		if (zip.endsWith(".zip")) {
			zip = zip.substring(0, zip.length() - 3) + "json";
		}

		try {
			Directory dir = new Directory(this.dir);
			dir.write(collection, zip);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<List<Usage>> readQueryHistories(String zip) {

		Type type = new TypeToken<Collection<List<Usage>>>() {
		}.getType();

		try {
			Directory dir = new Directory(this.dir);
			Collection<List<Usage>> out = dir.read(zip, type);
			return out;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}