/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.raychev_analysis;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cc.kave.commons.utils.StringUtils;

public class RaychevRecommender {

	private static final String SUDO_PASSWORD = "mangos";

	public static String DEFAULT_PATH = "/home/markus/Documents/SLANG";

	public String path;

	public RaychevRecommender(String path) {
		if (!path.isEmpty()) {
			this.path = path;
		} else {
			path = DEFAULT_PATH;
		}
	}

	public RaychevRecommender() {
		path = DEFAULT_PATH;
	}
	
	public void executeRecommender(String queryName, String analysisSet, boolean verbose) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder("sudo", "-S", MessageFormat.format("{0}/fill_ngram.sh", path), analysisSet, queryName);
		pb.directory(new File(path));
		if(verbose) {
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);			
		}
		Process process = pb.start();
		writeSudoPassword(process);
		process.waitFor();
	}

	private void writeSudoPassword(Process process) throws IOException {
		OutputStream outputStream = process.getOutputStream();
		String password = SUDO_PASSWORD + System.lineSeparator();
		outputStream.write(password.getBytes());
		outputStream.flush();
	}

	public int parseOutputAndReturnRank(String expectedMethod) throws IOException {
		File file = Paths.get(path, "tst1.eval").toFile();
		String outputString = FileUtils.readFileToString(file);
		if (Strings.isNullOrEmpty(outputString)) {
			return 0;
		}
		String[] solutionArray = outputString.trim().split("> Solution [0-9]* : ");
		for (int i = 0; i < solutionArray.length; i++) {
			String solution = solutionArray[i];
			if (solution.isEmpty()) {
				continue;
			}
			String methodName = getMethodName(solution);
			if (methodName.equals(expectedMethod)) {
				return i;
			}
		}
		return 0;
	}

	public List<String> getProposals() throws IOException {
		List<String> proposals = Lists.newLinkedList();
		
		File file = Paths.get(path, "tst1.eval").toFile();
		String outputString = FileUtils.readFileToString(file);
		if (Strings.isNullOrEmpty(outputString)) {
			return proposals;
		}
		String[] solutionArray = outputString.trim().split("> Solution [0-9]* : ");
		for (int i = 0; i < solutionArray.length; i++) {
			String solution = solutionArray[i];
			if (solution.isEmpty()) {
				continue;
			}
			String methodName = getMethodName(solution);
			proposals.add(methodName);
		}
		return proposals;
	}

	public String getMethodName(String solution) {
		if(solution.contains("%") || !solution.contains(">")) {
			return "";
		}
		int start = StringUtils.FindNext(solution, 0, '>') + 2;
		int bracketBelongingToMethodName = StringUtils.FindNext(solution, start, '(');
		int end = StringUtils.FindNext(solution, bracketBelongingToMethodName + 1, '(');
		return solution.substring(start, end);
	}
}
