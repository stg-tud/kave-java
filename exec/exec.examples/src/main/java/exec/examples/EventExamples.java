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
package exec.examples;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.userprofiles.UserProfileEvent;
import cc.recommenders.io.ReadingArchive;

/**
 * This class contains several code examples that explain how to read enriched
 * event streams with the CARET platform. It cannot be run, the code snippets
 * serve as documentation.
 */
public class EventExamples {

	/**
	 * this variable should point to a folder that contains a bunch of .zip
	 * files that may be nested in subfolders. If you have downloaded the event
	 * dataset from our website, please unzip the archive and point to the
	 * containing folder here.
	 */
	private static final String DIR_USERDATA = "/Users/seb/Downloads/All-Clean/";

	public static void main(String[] args) {

		System.out.println(new Date());

		List<String> userZips = findAllUsers(DIR_USERDATA);

		for (String user : userZips) {
			System.out.println("### " + user);
			List<IDEEvent> events = read(user);
			for (IDEEvent event : events) {
				// if (event instanceof CommandEvent) {
				// CommandEvent ce = (CommandEvent) event;
				// System.out.printf("[%s]", ce.CommandId);
				// } else
				if (event instanceof UserProfileEvent) {
					UserProfileEvent upe = (UserProfileEvent) event;
					System.out.printf("[PID:%s]", upe.ProfileId);
				} else {
					System.out.printf(".");
				}
			}
			System.out.println();
		}

		System.out.println(new Date());
	}

	/**
	 * finds all users in a given folder. Remember that events are grouped by
	 * user, so each .zip file corresponds to one user.
	 */
	public static List<String> findAllUsers(String dir) {
		List<String> zips = Lists.newLinkedList();
		for (File f : FileUtils.listFiles(new File(dir), new String[] { "zip" }, true)) {
			zips.add(f.getAbsolutePath());
		}
		return zips;
	}

	/**
	 * reads all captured events for a given user (= .zip file)
	 */
	public static List<IDEEvent> read(String zipFile) {
		LinkedList<IDEEvent> res = Lists.newLinkedList();
		try {
			ReadingArchive ra = new ReadingArchive(new File(zipFile));
			while (ra.hasNext()) {
				res.add(ra.getNext(IIDEEvent.class));
			}
			ra.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}