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

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Iterables;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.ssts.ISST;
import exec.recommender_reimplementation.java_printer.JavaClassPathGenerator;
import exec.recommender_reimplementation.java_printer.printer.JavaPrinter;
import exec.recommender_reimplementation.raychev_analysis.NestedCompletionExpressionEliminationVisitor.EliminationStrategy;

public class QueryGenerator {

	public enum QueryStrategy {
		RANDOM, COMPLETION
	}

	private Path queryPath;
	private QueryExtractor queryExtractor;
	
	private Map<Context, List<IName>> expectedCompletionsMap;

	public QueryGenerator(Path queryPath) {
		this.queryPath = queryPath;
		queryExtractor = new QueryExtractor();
		expectedCompletionsMap = new HashMap<>();
	}

	public boolean generateQuery(Context context, QueryStrategy queryStrategy) throws IOException {
		switch (queryStrategy) {
		case RANDOM:
			return generateQueryWithRandomHoles(context);
		case COMPLETION:
			return generateQueryFromCompletionExpression(context);
		default:
			return false;
		}
	}

	private boolean generateQueryWithRandomHoles(Context context) throws IOException {
		RandomHoleInsertionVisitor randomHoleVisitor = new RandomHoleInsertionVisitor();
		ISST sst = context.getSST();
		sst = (ISST) sst.accept(randomHoleVisitor, null);
		if(randomHoleVisitor.hasGeneratedRandomHole()) {
			context.setSST(sst);
			expectedCompletionsMap.put(context, randomHoleVisitor.getExpectedProposals());
			return generateQueryFromCompletionExpression(context);
		}
		return false;
	}

	public boolean generateQueryFromCompletionExpression(Context context) throws IOException {
		ISST sst = context.getSST();
		Context transformedQueryContext = new RaychevQueryTransformer().transfromIntoQuery(sst);
		if (transformedQueryContext == null) {
			return false;
		}
		sst.accept(new NestedCompletionExpressionEliminationVisitor(EliminationStrategy.DELETE), null);
		String javaCode = new JavaPrinter().print(context);
		String transformedQueryJavaCode = queryExtractor.createJavaCodeForQuery(transformedQueryContext);
		if (!transformedQueryJavaCode.isEmpty() && !sst.getEnclosingType().isUnknown()) {
			File file = JavaClassPathGenerator.generateClassPath(sst, queryPath.toString());
			writeJavaFile(javaCode, file);
			writeQueryFile(transformedQueryJavaCode, transformedQueryContext.getSST());
			return true;
		}
		return false;
	}

	public boolean generateQuery(CompletionEvent completionEvent) throws IOException {
		if (QueryExtractor.isValidCompletionEvent(completionEvent)) {
			IName selection = Iterables.getLast(completionEvent.selections).getProposal().getName();
			Context context = completionEvent.getContext();
			boolean successful = generateQueryFromCompletionExpression(context);
			if (successful) {
				expectedCompletionsMap.put(completionEvent.getContext(), newArrayList(selection));
			}
			return successful;

		}
		return false;
	}

	private void writeJavaFile(String javaCode, File file) throws IOException {
		FileUtils.writeStringToFile(file, javaCode);
	}

	private void writeQueryFile(String javaCode, ISST sst) throws IOException {
		FileUtils.writeStringToFile(new File(queryPath.toString() + "\\" + sst.getEnclosingType().getName() + ".java"),
				javaCode);
	}

	public Map<Context, List<IName>> getExpectedCompletionsMap() {
		return expectedCompletionsMap;
	}

}
