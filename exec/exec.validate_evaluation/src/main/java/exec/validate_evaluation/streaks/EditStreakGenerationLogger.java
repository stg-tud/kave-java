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

import java.util.Collection;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.recommenders.io.Logger;

public class EditStreakGenerationLogger {

	private int totalZips = 0;
	private int currentZip = 0;

	public void foundZips(Set<String> zips) {
		totalZips = zips.size();
		Logger.log("found %d zips", totalZips);
		for (String zip : zips) {
			Logger.log("\t- %s", zip);
		}
	}

	public void startingZip(String zip) {
		currentZip++;
		Logger.log("");
		Logger.log("### processing %s (%d/%d - %.1f%%) ###", zip, currentZip, totalZips,
				100 * (currentZip - 1) / (double) totalZips);

	}

	public void foundEvents(Set<ICompletionEvent> events) {
		Logger.log("");
		Logger.log("found %d events:", events.size());
		Logger.log("");
	}

	public void processingEvent(ICompletionEvent e) {
		Logger.append(".");
	}

	public void startRemoveSingleEdits() {
		Logger.log("");
		Logger.log("starting to remove single edits: ");
		Logger.log("");
	}

	public void removeSingleEdit() {
		Logger.append(".");
	}

	public void endZip(Collection<EditStreak> streaks) {
		Logger.log("");
		Logger.log("found streaks for %d files for this user", streaks.size());
	}

	public void finish() {
		Logger.log("");
		Logger.log("### done (%d/%d - 100.0%%) ###", totalZips, totalZips);
	}
}