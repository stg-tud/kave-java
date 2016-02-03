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

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.Trigger;

public class CompletionEvent extends IDEEvent implements ICompletionEvent {

	@SerializedName("Context2")
	public Context context;

	public List<IProposal> proposalCollection;

	public List<IProposalSelection> selections;

	public Trigger terminatedBy;

	public TerminationState terminatedState;
	
	private int proposalCount;
	
	public CompletionEvent(){
		this.selections = Lists.newArrayList();
		this.proposalCollection = Lists.newArrayList();
		this.context = new Context();
		this.terminatedState = terminatedState.Unknown;
		this.proposalCount = 0;
	}
	
	@Override
	public Context getContext() {
		return context;
	}

	@Override
	public List<IProposal> getProposalCollection() {
		return proposalCollection;
	}

	@Override
	public List<IProposalSelection> getSelections() {
		return selections;
	}

	@Override
	public Trigger getTerminatedBy() {
		return terminatedBy;
	}

	@Override
	public TerminationState getTerminatedState() {
		return terminatedState;
	}

	public int getProposalCount() {
		return proposalCount;
	}

	public void setProposalCount(int proposalCount) {
		this.proposalCount = proposalCount;
	}
}