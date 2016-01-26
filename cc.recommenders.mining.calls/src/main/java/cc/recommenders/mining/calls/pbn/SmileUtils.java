/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.mining.calls.pbn;

import java.io.File;
import java.io.IOException;

import com.google.inject.Inject;

import cc.recommenders.io.Directory;
import cc.recommenders.io.IoUtils;
import smile.Network;

public class SmileUtils {

	private IoUtils io;

	@Inject
	public SmileUtils(IoUtils io) {
		this.io = io;
	}

	public String toString(Network network) {
		try {
			String randomFile = io.getRandomTempFile() + ".xdsl";
			network.writeFile(randomFile);

			Directory parent = io.getParentDirectory(randomFile);
			String name = new File(randomFile).getName();
			String xml = parent.readContent(name);
			parent.delete(name);
			return xml;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int getNumPatterns(Network network) {
		int handle = network.getNode(PBNModelConstants.PATTERN_TITLE);
		double[] def = network.getNodeDefinition(handle);
		return def.length;
	}
}