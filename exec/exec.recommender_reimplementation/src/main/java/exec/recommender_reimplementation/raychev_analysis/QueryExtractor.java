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

import com.google.common.collect.Iterables;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.TerminationState;
import cc.kave.commons.model.names.IName;
import cc.kave.commons.model.ssts.ISST;
import exec.recommender_reimplementation.java_printer.JavaPrintingVisitor.InvalidJavaCodeException;
import exec.recommender_reimplementation.java_printer.printer.RaychevQueryPrinter;

public class QueryExtractor {

	public String createJavaCodeForQuery(CompletionEvent completionEvent) {
		if (isValidCompletionEvent(completionEvent)) {
			return createJavaCodeForQuery(completionEvent.getContext());
		}
		return "";
	}

	public static boolean isValidCompletionEvent(CompletionEvent completionEvent) {
		return completionEvent.terminatedState == TerminationState.Applied && !completionEvent.selections.isEmpty()
				&& IsNonStaticSelection(Iterables.getLast(completionEvent.selections).getProposal());
	}

	public static boolean IsNonStaticSelection(IProposal proposal) {
		IName name = proposal.getName();
		return !name.getIdentifier().contains("static");
	}

	public String createJavaCodeForQuery(Context context) {
		try {
			return new RaychevQueryPrinter().print(context);
		} catch (InvalidJavaCodeException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String createJavaCodeForQuery(ISST sst) {
		try {
			return new RaychevQueryPrinter().print(sst);
		} catch (InvalidJavaCodeException e) {
			e.printStackTrace();
			return "";
		}
	}
}
