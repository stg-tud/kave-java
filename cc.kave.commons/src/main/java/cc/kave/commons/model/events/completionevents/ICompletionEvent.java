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

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.events.Trigger;

public interface ICompletionEvent {

	@NonNull
	public Context getContext();

	@NonNull
	public List<IProposal> getProposalCollection();

	@NonNull
	public String getPrefix();

	@NonNull
	public List<IProposalSelection> getSelections();

	@NonNull
	public Trigger getTerminatedBy();

	@NonNull
	public TerminationState getTerminatedState();
}