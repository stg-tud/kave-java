/**
 * Copyright 2014 Technische Universität Darmstadt
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
package exec.episodes;

import java.io.IOException;

public class run_me {

//	private static String root = "/Volumes/Data/EpisodeMining/";
	private static String root = "/Users/ervinacergani/Documents/PhD_work/episode-miner/";
	private static String dirContexts = root + "Contexts/";
	private static String fileEventStream = root + "eventStream.txt";
	private static String fileEventMapping = root + "EpisodeMining/EventStreamForEpisodeMining/eventMapping.txt";

	public static void main(String[] args) throws IOException {
		new DoSomething().run(fileEventStream, fileEventMapping, dirContexts);
	}
}