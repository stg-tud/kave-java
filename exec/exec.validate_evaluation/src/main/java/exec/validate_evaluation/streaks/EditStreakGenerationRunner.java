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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.events.completionevents.TerminationState;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IName;
import cc.kave.commons.model.names.ITypeName;

public class EditStreakGenerationRunner {

	private final EditStreakGenerationLogger log;
	private final EditStreakGenerationIo io;

	private Map<ITypeName, EditStreak> editStreaks;

	public EditStreakGenerationRunner(EditStreakGenerationIo io, EditStreakGenerationLogger log) {
		this.io = io;
		this.log = log;
	}

	public void run() {
		Set<String> zips = io.findZips();
		log.foundZips(zips);

		for (String zip : zips) {
			log.startingZip(zip);
			editStreaks = Maps.newLinkedHashMap();

			Set<ICompletionEvent> events = io.read(zip);
			log.foundEvents(events);

			for (ICompletionEvent e : events) {
				log.processingEvent(e);
				extractEdits(e);
			}

			removeSingleEdits();

			Set<EditStreak> streaks = Sets.newLinkedHashSet();
			streaks.addAll(editStreaks.values());
			log.endZip(streaks);
			io.storeStreaks(streaks, zip);
		}

		log.finish();
	}

	private void extractEdits(ICompletionEvent e) {
		Date date = e.getTriggeredAt();
		Context ctx = e.getContext();

		IMethodName selection = null;
		if (e.getTerminatedState() == TerminationState.Applied) {
			selection = getSelection(e);
		}
		register(date, ctx, selection);
	}

	private void register(Date d, Context ctx, IMethodName selection) {
		ITypeName encType = ctx.getSST().getEnclosingType();
		if (!encType.isUnknown()) {
			Snapshot se = Snapshot.create(d, ctx, selection);
			getEdits(encType).add(se);
		}
	}

	private IMethodName getSelection(ICompletionEvent e) {
		IName proposalName = e.getLastSelectedProposal().getName();
		boolean isMethodName = proposalName instanceof IMethodName;
		if (isMethodName) {
			return (IMethodName) proposalName;
		}
		return null;
	}

	private void removeSingleEdits() {
		log.startRemoveSingleEdits();
		Iterator<Entry<ITypeName, EditStreak>> entries = editStreaks.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<ITypeName, EditStreak> entry = entries.next();
			if (entry.getValue().isEmptyOrSingleEdit()) {
				entries.remove();
				log.removeSingleEdit();
			}
		}
	}

	private EditStreak getEdits(ITypeName type) {
		EditStreak streak = editStreaks.get(type);
		if (streak == null) {
			streak = new EditStreak();
			editStreaks.put(type, streak);
		}
		return streak;
	}
}