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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.recommenders.io.Directory;
import cc.recommenders.io.ReadingArchive;
import cc.recommenders.io.WritingArchive;

public class EditStreakGenerationIoTest {
	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	private File dirIn;
	private File dirOut;
	private EditStreakGenerationIo sut;

	@Before
	public void setup() throws IOException {
		dirIn = tmp.newFolder("in");
		dirOut = tmp.newFolder("out");
		sut = new EditStreakGenerationIo(dirIn.getAbsolutePath(), dirOut.getAbsolutePath());
	}

	@Test
	public void zipsAreFound() throws IOException {
		create(file(dirIn, "a.txt"));
		create(file(dirIn, "a.zip"));
		create(file(dirIn, "a", "b", "c.zip"));
		create(file(dirOut, "x.zip"));

		Set<String> actuals = sut.findZips();
		Set<String> expecteds = Sets.newHashSet("a.zip", Paths.get("a", "b", "c.zip").toString());
		assertEquals(expecteds, actuals);
	}

	@Test
	public void completionEventsAreRead() throws IOException {

		Set<ICompletionEvent> expecteds = Sets.newLinkedHashSet();
		expecteds.add(completionEvent(1));
		expecteds.add(completionEvent(2));
		expecteds.add(completionEvent(3));

		Directory dir = new Directory(dirIn.getAbsolutePath());
		try (WritingArchive wa = dir.getWritingArchive("a.zip");) {
			for (ICompletionEvent e : expecteds) {
				wa.add(e);
			}
		}

		Set<ICompletionEvent> actuals = sut.read("a.zip");
		assertEquals(expecteds, actuals);
	}

	@Test
	public void editStreaksAreStored() throws IOException {

		Set<EditStreak> expecteds = Sets.newLinkedHashSet();
		expecteds.add(editStreak(1));
		expecteds.add(editStreak(2));
		expecteds.add(editStreak(3));

		sut.storeStreaks(expecteds, "a.zip");

		Set<EditStreak> actuals = sut.readStreaks("a.zip");

		Directory dir = new Directory(dirOut.getAbsolutePath());
		try (ReadingArchive ra = dir.getReadingArchive("a.zip");) {

			while (ra.hasNext()) {
				EditStreak es = ra.getNext(EditStreak.class);
				actuals.add(es);
			}
		}

		assertEquals(expecteds, actuals);
	}

	@Test
	public void emptyEditStreaksAreNotStored() throws IOException {

		sut.storeStreaks(Sets.newLinkedHashSet(), "a.zip");

		Directory dir = new Directory(dirOut.getAbsolutePath());
		assertFalse(dir.exists("a.zip"));
	}

	private static EditStreak editStreak(int num) {
		EditStreak es = new EditStreak();
		for (int i = 0; i < num; i++) {
			Snapshot e = Snapshot.create(LocalDateTime.now(), new Context(), null);
			es.add(e);
		}
		return es;
	}

	private static ICompletionEvent completionEvent(int i) {
		CompletionEvent ce = new CompletionEvent();
		ce.IDESessionUUID = "sid:" + i;
		return ce;
	}

	private void create(String fileName) throws IOException {
		File f = new File(fileName);
		f.getParentFile().mkdirs();
		f.createNewFile();
	}

	private String file(File dir, String... parts) throws IOException {
		return Paths.get(dir.getAbsolutePath(), parts).toFile().getAbsolutePath();
	}
}