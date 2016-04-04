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
package exec.validate_evaluation.streaks.usages;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.completionevents.TerminationState;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.Usage;
import exec.validate_evaluation.streaks.EditStreak;
import exec.validate_evaluation.utils.CoReNameUtils;

public class UsageStreakGenerationRunner {

	private final UsageStreakGenerationIo io;
	private final UsageStreakGenerationLogger log;

	private Map<Tuple<ICoReMethodName, ICoReTypeName>, EditStreak> editStreaks;
	private IUsageExtractor usageExtractor;

	public UsageStreakGenerationRunner(UsageStreakGenerationIo io, UsageStreakGenerationLogger log) {
		this.io = io;
		this.log = log;
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

			removeSingleEdits();

			log.endZip(editStreaks);
			io.store(editStreaks.values(), zip);
		}

		log.finish();
	}

	private void removeSingleEdits() {
		log.startRemoveSingleEdits();
		Iterator<Entry<Tuple<ICoReMethodName, ICoReTypeName>, EditStreak>> entries = editStreaks.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<Tuple<ICoReMethodName, ICoReTypeName>, EditStreak> entry = entries.next();
			if (entry.getValue().isEmptyOrSingleEdit()) {
				entries.remove();
				log.removeSingleEdit();
			}
		}
	}

	private void extractEdits(ICompletionEvent e) {
		Date date = e.getTriggeredAt();

		IAnalysisResult result = usageExtractor.analyse(e.getContext());
		List<Usage> usages = result.getUsages();
		Usage query = result.getFirstQuery();

		if (e.getTerminatedState() == TerminationState.Applied) {
			usages.remove(query);
			ICoReMethodName selection = getSelection(e.getLastSelectedProposal());
			register(date, query, selection);
		}

		for (Usage u : usages) {
			register(date, u, null);
		}
	}

	private void register(Date d, Usage u, ICoReMethodName selection) {
		// Snapshot se = Snapshot.create(d, u, selection);
		// getEdits(u).add(se);
	}

	private ICoReMethodName getSelection(IProposal p) {
		boolean isMethodName = p.getName() instanceof cc.kave.commons.model.names.IMethodName;
		if (!isMethodName) {
			return null;
		}
		cc.kave.commons.model.names.IMethodName m = (cc.kave.commons.model.names.IMethodName) p.getName();
		return CoReNameUtils.toCoReName(m);
	}

	private EditStreak getEdits(Usage u) {
		Tuple<ICoReMethodName, ICoReTypeName> key = getKey(u);
		EditStreak streak = editStreaks.get(key);
		if (streak == null) {
			streak = new EditStreak();
			editStreaks.put(key, streak);
		}
		return streak;
	}

	private Tuple<ICoReMethodName, ICoReTypeName> getKey(Usage u) {
		return Tuple.newTuple(u.getMethodContext(), u.getType());
	}
}