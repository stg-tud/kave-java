/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.evaluation.events;

import java.util.function.Predicate;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.TerminationState;
import cc.kave.commons.model.naming.codeelements.IMethodName;

public class CompletionEventFilter implements Predicate<ICompletionEvent> {

	@Override
	public boolean test(ICompletionEvent event) {
		if (event.getTerminatedState() != TerminationState.Applied) {
			return false;
		}

		IProposal proposal = event.getLastSelectedProposal();
		if (proposal == null) {
			return false;
		} else if (!(proposal.getName() instanceof IMethodName)) {
			return false;
		} else if (((IMethodName) proposal.getName()).isStatic()) {
			return false;
		}

		return true;
	}
}