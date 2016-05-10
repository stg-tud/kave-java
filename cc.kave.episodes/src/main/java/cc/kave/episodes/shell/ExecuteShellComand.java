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
package cc.kave.episodes.shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteShellComand {

	public static final int TOTAL = 357;
	public static final int THREADS = 8;

	public static void main(String[] args) {

		int partition = 0;

		ExecuteShellComand obj = new ExecuteShellComand();

		String command = "cd Documents/PhD_work/episode-miner/n-graph-miner/";

		obj.executeCommand(command);

		while (partition < TOTAL) {
			partition++;
			ExecuteShellComand session = new ExecuteShellComand();

			command = "script eventStream" + partition + ".txt"; 

			String output = session.executeCommand(command);

			System.out.println(output);

		}
	}

	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}
}
