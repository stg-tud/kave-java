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
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.ActivityEvent;
import cc.kave.commons.model.events.CommandEvent;
import cc.kave.commons.model.events.ErrorEvent;
import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.InfoEvent;
import cc.kave.commons.model.events.NavigationEvent;
import cc.kave.commons.model.events.SystemEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.testrunevents.TestRunEvent;
import cc.kave.commons.model.events.userprofiles.UserProfileEvent;
import cc.kave.commons.model.events.versioncontrolevents.VersionControlEvent;
import cc.kave.commons.model.events.visualstudio.BuildEvent;
import cc.kave.commons.model.events.visualstudio.DebuggerEvent;
import cc.kave.commons.model.events.visualstudio.DocumentEvent;
import cc.kave.commons.model.events.visualstudio.EditEvent;
import cc.kave.commons.model.events.visualstudio.FindEvent;
import cc.kave.commons.model.events.visualstudio.IDEStateEvent;
import cc.kave.commons.model.events.visualstudio.InstallEvent;
import cc.kave.commons.model.events.visualstudio.SolutionEvent;
import cc.kave.commons.model.events.visualstudio.UpdateEvent;
import cc.kave.commons.model.events.visualstudio.WindowEvent;
import cc.kave.commons.utils.json.JsonUtils;
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
			List<String> eventStrings = read(user);
			for (String eventString : eventStrings) {

				IDEEvent event = JsonUtils.fromJson(eventString, IIDEEvent.class);

				if (event instanceof ActivityEvent) {
					System.out.printf(".");
				} else if (event instanceof CommandEvent) {
					System.out.printf(".");
				} else if (event instanceof ErrorEvent) {
					System.out.printf(".");
				} else if (event instanceof InfoEvent) {
					System.out.printf(".");
				} else if (event instanceof NavigationEvent) {
					System.out.printf(".");
				} else if (event instanceof SystemEvent) {
					System.out.printf(".");
					// ---------
				} else if (event instanceof CompletionEvent) {
					System.out.printf(".");
					// ---------
				} else if (event instanceof TestRunEvent) {
					System.out.printf(".");
					// ---------
				} else if (event instanceof UserProfileEvent) {
					System.out.printf(".");
					// ---------
				} else if (event instanceof VersionControlEvent) {
					System.out.printf(".");
					// ---------
				} else if (event instanceof BuildEvent) {
					System.out.printf(".");
				} else if (event instanceof DebuggerEvent) {
					System.out.printf(".");
				} else if (event instanceof DocumentEvent) {
					System.out.printf(".");
				} else if (event instanceof EditEvent) {
					System.out.printf(".");
				} else if (event instanceof FindEvent) {
					System.out.printf(".");
				} else if (event instanceof IDEStateEvent) {
					System.out.printf(".");
				} else if (event instanceof InstallEvent) {
					System.out.printf(".");
				} else if (event instanceof SolutionEvent) {
					System.out.printf(".");
				} else if (event instanceof UpdateEvent) {
					System.out.printf(".");
				} else if (event instanceof WindowEvent) {
					System.out.printf(".");
				} else {
					throw new RuntimeException("??");
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
	public static List<String> read(String zipFile) {
		List<String> res = Lists.newLinkedList();
		try {
			ReadingArchive ra = new ReadingArchive(new File(zipFile));
			while (ra.hasNext()) {
				res.add(ra.getNextPlain());
			}
			ra.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}