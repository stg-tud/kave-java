/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package exec.validate_evaluation.streaks;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.TerminationState;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.IMethodName;
import cc.recommenders.names.ITypeName;
import cc.recommenders.usages.Usage;

public class StreakGeneration {

	private final StreakIo io;
	private final StreakLogger log;

	private Map<Tuple<IMethodName, ITypeName>, List<Edit>> editStreaks;
	private IUsageExtractor usageExtractor;

	public StreakGeneration(StreakIo io, StreakLogger log, IUsageExtractor usageExtractor) {
		this.io = io;
		this.log = log;
		this.usageExtractor = usageExtractor;
	}

	public void run() {
		Set<String> zips = io.findZips();
		log.foundZips(zips);

		for (String zip : zips) {
			editStreaks = Maps.newLinkedHashMap();

			Set<ICompletionEvent> events = io.read(zip);
			log.foundEvents(events);

			for (ICompletionEvent e : events) {
				log.processingEvent(e);
				extractEdits(e);
			}

			log.endZip(editStreaks);
			io.store(editStreaks.values(), zip);
		}

		log.finish();
	}

	private void extractEdits(ICompletionEvent e) {
		Date date = e.getTriggeredAt();

		IAnalysisResult result = usageExtractor.analyse(e.getContext());
		List<Usage> usages = result.getUsages();
		Usage query = result.getFirstQuery();

		if (e.getTerminatedState() == TerminationState.Applied) {
			usages.remove(query);
			IMethodName selection = getSelection(e.getLastSelectedProposal());
			register(date, query, selection);
		}

		for (Usage u : usages) {
			register(date, u, null);
		}
	}

	private void register(Date d, Usage u, IMethodName selection) {
		Edit se = Edit.create(d, u, selection);
		getEdits(u).add(se);
	}

	private IMethodName getSelection(IProposal p) {
		boolean isMethodName = p.getName() instanceof cc.kave.commons.model.names.IMethodName;
		if (!isMethodName) {
			return null;
		}
		cc.kave.commons.model.names.IMethodName m = (cc.kave.commons.model.names.IMethodName) p.getName();
		return NameUtils.toCoReName(m);
	}

	private List<Edit> getEdits(Usage u) {
		Tuple<IMethodName, ITypeName> key = getKey(u);
		List<Edit> edits = editStreaks.get(key);
		if (edits == null) {
			edits = Lists.newLinkedList();
			editStreaks.put(key, edits);
		}
		return edits;
	}

	private Tuple<IMethodName, ITypeName> getKey(Usage u) {
		return Tuple.newTuple(u.getMethodContext(), u.getType());
	}
}