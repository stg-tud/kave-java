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

import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.getTransformedType;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.json.JsonUtils;

public class RaychevEvaluation {
	public static final String ANALYSIS_SET = "dotnet";

	public static String DEFAULT_PATH = "/home/markus/Documents/SLANG";

	public static String QUERY_PATH = "/home/markus/Documents/SLANG/tests/src/com/example/fill";

	public static void main(String[] args) {
		runEvaluation(QUERY_PATH);
	}

	public static void runEvaluation(String path) {
		RaychevRecommender raychevRecommender = new RaychevRecommender(DEFAULT_PATH);
		List<String> queryNames = getQueryNames(path);
		double reciprocalRank = 0.0d;
		int invocationCount = 0;
		for (String queryName : queryNames) {
			try {
				raychevRecommender.executeRecommender(queryName, ANALYSIS_SET, false);
				IMethodName methodName = getExpectedMethod(queryName);
				int rank = raychevRecommender.parseOutputAndReturnRank(getRaychevMethodName(methodName));
				System.out.println(queryName + " " + rank);
				if(rank == 0) {
					reciprocalRank += 0;
				}
				else {
					reciprocalRank += 1 / (double) rank;
				}
				invocationCount++;
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		double mrr = reciprocalRank / invocationCount;
		System.out.println("Mean reciprocal rank:" + mrr);
	}

	public static String getRaychevMethodName(IMethodName methodName) {
		StringBuilder sb = new StringBuilder();
		sb.append(getDeclaringType(methodName));
		sb.append(".");
		sb.append(methodName.getName());
		sb.append("(");
		sb.append(getParameterString(methodName));
		sb.append(")");
		sb.append(getReturnString(methodName));
		return sb.toString();
	}

	public static String getDeclaringType(IMethodName methodName) {
		return getTransformedType(methodName.getDeclaringType()).getFullName().replace(" ", "");
	}

	private static char getReturnString(IMethodName methodName) {
		ITypeName returnType = methodName.getReturnType();
		if (returnType.isVoidType())
			return 'v';
		char firstChar = returnType.getName().charAt(0);
		if (returnType.isValueType())
			firstChar = Character.toLowerCase(firstChar);
		return firstChar;
	}

	private static String getParameterString(IMethodName methodName) {
		List<IParameterName> parameters = methodName.getParameters();
		StringBuilder sb = new StringBuilder();
		for (IParameterName parameterName : parameters) {
			char firstChar = parameterName.getValueType().getName().charAt(0);
			if (parameterName.getValueType().isValueType())
				firstChar = Character.toLowerCase(firstChar);
			sb.append(firstChar);
		}
		return sb.toString();
	}

	private static IMethodName getExpectedMethod(String queryName) throws IOException {
		String pathToCompletionEvent = MessageFormat.format("{0}/{1}/{1}.json", QUERY_PATH, queryName);
		CompletionEvent completionEvent = JsonUtils.fromJson(new File(pathToCompletionEvent), CompletionEvent.class);
		IName name = completionEvent.getLastSelectedProposal().getName();
		if (name instanceof IMethodName) {
			return (IMethodName) name;
		}
		else {
			return Names.getUnknownMethod();
		}
	}

	public static List<String> getQueryNames(String path) {
		File[] folderPaths = new File(path).listFiles(File::isDirectory);
		List<String> queryNames = new ArrayList<>();
		for (File file : folderPaths) {
			queryNames.add(file.getName());
		}
		return queryNames;
	}

}
