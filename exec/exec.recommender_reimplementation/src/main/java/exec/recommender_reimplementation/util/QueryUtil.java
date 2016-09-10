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
package exec.recommender_reimplementation.util;

import java.util.Iterator;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.TerminationState;
import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;

public class QueryUtil {

	public static boolean isValidCompletionEvent(CompletionEvent completionEvent) {
		return completionEvent.terminatedState == TerminationState.Applied && !completionEvent.selections.isEmpty()
				&& isValidSelection(completionEvent.getLastSelectedProposal());
	}

	public static boolean isValidSelection(IProposal proposal) {
		IName name = proposal.getName();
		return name instanceof IMethodName && !name.getIdentifier().contains("static");
	}

	public static IMethodName getExpectedMethodName(CompletionEvent completionEvent) {
		IProposal lastSelectedProposal = completionEvent.getLastSelectedProposal();
		IName name = lastSelectedProposal.getName();
		if (name instanceof IMethodName) {
			return (IMethodName) name;
		}
		return Names.getUnknownMethod();
	}

	public static double calculateReciprocalRank(ICoReMethodName coReMethodName, Set<Tuple<ICoReMethodName, Double>> proposals) {
		Iterator<Tuple<ICoReMethodName, Double>> iterator = proposals.iterator();
		int i = 1;
		while (iterator.hasNext()) {
			Tuple<ICoReMethodName, Double> proposal = iterator.next();
			if (coReMethodName.equals(proposal.getFirst())) {
				return 1 / (double) i;
			}
			i++;
		}
		return 0;
	}
}
