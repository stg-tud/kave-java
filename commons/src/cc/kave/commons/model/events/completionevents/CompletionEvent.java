/**
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

package cc.kave.commons.model.events.completionevents;

import java.util.List;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.Trigger;

public class CompletionEvent extends IDEEvent implements ICompletionEvent {

	public Context Context;

	public List<IProposal> ProposalCollection;

	public String Prefix;

	public List<IProposalSelection> Selections;

	public Trigger TerminatedBy;

	public TerminationState TerminatedState;

	@Override
	public Context getContext() {
		return Context;
	}

	@Override
	public List<IProposal> getProposalCollection() {
		return ProposalCollection;
	}

	@Override
	public String getPrefix() {
		return Prefix;
	}

	@Override
	public List<IProposalSelection> getSelections() {
		return Selections;
	}

	@Override
	public Trigger getTerminatedBy() {
		return TerminatedBy;
	}

	@Override
	public TerminationState getTerminatedState() {
		return TerminatedState;
	}
}