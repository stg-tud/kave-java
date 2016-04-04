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

import java.io.IOException;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.recommenders.io.Directory;
import cc.recommenders.io.ReadingArchive;
import cc.recommenders.io.WritingArchive;

public class EditStreakGenerationIo {

	private String dirIn;
	private String dirOut;

	public EditStreakGenerationIo(String dirIn, String dirOut) {
		this.dirIn = dirIn;
		this.dirOut = dirOut;
	}

	public Set<String> findZips() {
		Directory dir = new Directory(dirIn);
		return dir.findFiles(s -> s.endsWith(".zip"));
	}

	public Set<ICompletionEvent> read(String zip) {
		Set<ICompletionEvent> es = Sets.newLinkedHashSet();
		Directory dir = new Directory(dirIn);
		try (ReadingArchive ra = dir.getReadingArchive(zip)) {
			while (ra.hasNext()) {
				es.add(ra.getNext(ICompletionEvent.class));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return es;
	}

	public void store(Iterable<EditStreak> streaks, String zip) {
		Directory dir = new Directory(dirOut);
		try (WritingArchive wa = dir.getWritingArchive(zip)) {
			for (EditStreak es : streaks) {
				wa.add(es);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}