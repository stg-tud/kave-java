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
package exec.validate_evaluation.io;

import java.util.Set;
import java.util.stream.Stream;

import cc.kave.commons.model.events.IDEEvent;

public class IDEEventIo {

	private String root;

	public IDEEventIo(String root) {
		this.root = root;
	}

	public Stream<IDEEvent> readAll(String zip) {
		return null;
	}

	public Set<String> findZips() {
		// TODO Auto-generated method stub
		return null;
	}
}