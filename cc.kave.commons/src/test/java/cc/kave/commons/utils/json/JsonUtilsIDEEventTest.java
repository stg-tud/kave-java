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
package cc.kave.commons.utils.json;

import static cc.kave.commons.utils.StringUtils.f;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

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
import cc.kave.testcommons.ParameterData;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;

@RunWith(JUnitParamsRunner.class)
public class JsonUtilsIDEEventTest {

	/*
	 * this string is automatically generated, do not edit it manually! In case
	 * you encounter a bug, fix it on C#, regenerate the string and only then
	 * fix it on Java.
	 */
	private static final String jsonIn = "[\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.CompletionEvent, KaVE.Commons\",\n" + //
			"        \"Context2\": {\n" + //
			"            \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\",\n" + //
			"            \"TypeShape\": {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeShape, KaVE.Commons\",\n" + //
			"                \"TypeHierarchy\": {\n" + //
			"                    \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeHierarchy, KaVE.Commons\",\n" + //
			"                    \"Element\": \"0T:?\",\n" + //
			"                    \"Implements\": []\n" + //
			"                },\n" + //
			"                \"MethodHierarchies\": []\n" + //
			"            },\n" + //
			"            \"SST\": {\n" + //
			"                \"$type\": \"[SST:SST]\",\n" + //
			"                \"EnclosingType\": \"0T:T,P\",\n" + //
			"                \"Fields\": [],\n" + //
			"                \"Properties\": [],\n" + //
			"                \"Methods\": [],\n" + //
			"                \"Events\": [],\n" + //
			"                \"Delegates\": []\n" + //
			"            }\n" + //
			"        },\n" + //
			"        \"ProposalCollection\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Proposal, KaVE.Commons\",\n" + //
			"                \"Name\": \"0General:y\",\n" + //
			"                \"Relevance\": 2\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"Selections\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.ProposalSelection, KaVE.Commons\",\n"
			+ //
			"                \"Proposal\": {\n" + //
			"                    \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Proposal, KaVE.Commons\",\n" + //
			"                    \"Name\": \"0General:z\",\n" + //
			"                    \"Relevance\": 4\n" + //
			"                },\n" + //
			"                \"SelectedAfter\": \"00:00:01\",\n" + //
			"                \"Index\": -1\n" + //
			"            },\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.ProposalSelection, KaVE.Commons\",\n"
			+ //
			"                \"Proposal\": {\n" + //
			"                    \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Proposal, KaVE.Commons\"\n" + //
			"                },\n" + //
			"                \"SelectedAfter\": \"429.04:05:06.0070008\",\n" + //
			"                \"Index\": -1\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"TerminatedBy\": 2,\n" + //
			"        \"TerminatedState\": 1,\n" + //
			"        \"ProposalCount\": 3,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5699311+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.TestRunEvents.TestRunEvent, KaVE.Commons\",\n" + //
			"        \"WasAborted\": true,\n" + //
			"        \"Tests\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.TestRunEvents.TestCaseResult, KaVE.Commons\",\n" + //
			"                \"TestMethod\": \"0M:[?] [?].M()\",\n" + //
			"                \"Parameters\": \"..\",\n" + //
			"                \"Duration\": \"00:00:05\",\n" + //
			"                \"Result\": 1\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5830062+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.UserProfiles.UserProfileEvent, KaVE.Commons\",\n" + //
			"        \"ProfileId\": \"p\",\n" + //
			"        \"Education\": 2,\n" + //
			"        \"Position\": 3,\n" + //
			"        \"ProjectsCourses\": true,\n" + //
			"        \"ProjectsPersonal\": true,\n" + //
			"        \"ProjectsSharedSmall\": true,\n" + //
			"        \"ProjectsSharedMedium\": true,\n" + //
			"        \"ProjectsSharedLarge\": true,\n" + //
			"        \"TeamsSolo\": true,\n" + //
			"        \"TeamsSmall\": true,\n" + //
			"        \"TeamsMedium\": true,\n" + //
			"        \"TeamsLarge\": true,\n" + //
			"        \"CodeReviews\": 1,\n" + //
			"        \"ProgrammingGeneral\": 5,\n" + //
			"        \"ProgrammingCSharp\": 3,\n" + //
			"        \"Comment\": \"c\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5842105+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VersionControlEvents.VersionControlEvent, KaVE.Commons\",\n"
			+ //
			"        \"Actions\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.VersionControlEvents.VersionControlAction, KaVE.Commons\",\n"
			+ //
			"                \"ExecutedAt\": \"2016-08-15T04:28:30.5873885+02:00\",\n" + //
			"                \"ActionType\": 4\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"Solution\": \"0Sln:s\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5873885+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"Duration\": \"00:00:02\",\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.BuildEvent, KaVE.Commons\",\n" + //
			"        \"Scope\": \"s\",\n" + //
			"        \"Action\": \"a\",\n" + //
			"        \"Targets\": [\n" + //
			"            {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.BuildTarget, KaVE.Commons\",\n" + //
			"                \"Project\": \"p\",\n" + //
			"                \"ProjectConfiguration\": \"pcfg\",\n" + //
			"                \"Platform\": \"plt\",\n" + //
			"                \"SolutionConfiguration\": \"scfg\",\n" + //
			"                \"StartedAt\": \"2016-08-15T04:28:30.5898889+02:00\",\n" + //
			"                \"Duration\": \"00:00:12\",\n" + //
			"                \"Successful\": true\n" + //
			"            }\n" + //
			"        ],\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5898889+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.DebuggerEvent, KaVE.Commons\",\n" + //
			"        \"Mode\": 0,\n" + //
			"        \"Reason\": \"r\",\n" + //
			"        \"Action\": \"a\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5922042+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.DocumentEvent, KaVE.Commons\",\n" + //
			"        \"Document\": \"0Doc:type path\",\n" + //
			"        \"Action\": 0,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5931112+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.EditEvent, KaVE.Commons\",\n" + //
			"        \"Context2\": {\n" + //
			"            \"$type\": \"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\",\n" + //
			"            \"TypeShape\": {\n" + //
			"                \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeShape, KaVE.Commons\",\n" + //
			"                \"TypeHierarchy\": {\n" + //
			"                    \"$type\": \"KaVE.Commons.Model.TypeShapes.TypeHierarchy, KaVE.Commons\",\n" + //
			"                    \"Element\": \"0T:?\",\n" + //
			"                    \"Implements\": []\n" + //
			"                },\n" + //
			"                \"MethodHierarchies\": []\n" + //
			"            },\n" + //
			"            \"SST\": {\n" + //
			"                \"$type\": \"[SST:SST]\",\n" + //
			"                \"EnclosingType\": \"0T:Edit, P\",\n" + //
			"                \"Fields\": [],\n" + //
			"                \"Properties\": [],\n" + //
			"                \"Methods\": [],\n" + //
			"                \"Events\": [],\n" + //
			"                \"Delegates\": []\n" + //
			"            }\n" + //
			"        },\n" + //
			"        \"NumberOfChanges\": 1,\n" + //
			"        \"SizeOfChanges\": 2,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5941597+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.FindEvent, KaVE.Commons\",\n" + //
			"        \"Cancelled\": true,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5950652+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.IDEStateEvent, KaVE.Commons\",\n" + //
			"        \"IDELifecyclePhase\": 1,\n" + //
			"        \"OpenWindows\": [\n" + //
			"            \"0Win:w w\"\n" + //
			"        ],\n" + //
			"        \"OpenDocuments\": [\n" + //
			"            \"0Doc:d d\"\n" + //
			"        ],\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5954339+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.InstallEvent, KaVE.Commons\",\n" + //
			"        \"PluginVersion\": \"pv\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5968869+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.SolutionEvent, KaVE.Commons\",\n" + //
			"        \"Action\": 3,\n" + //
			"        \"Target\": \"0Doc:d d\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5978611+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.UpdateEvent, KaVE.Commons\",\n" + //
			"        \"OldPluginVersion\": \"o\",\n" + //
			"        \"NewPluginVersion\": \"n\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5986452+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.VisualStudio.WindowEvent, KaVE.Commons\",\n" + //
			"        \"Window\": \"0Win:w w\",\n" + //
			"        \"Action\": 3,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5986452+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.ActivityEvent, KaVE.Commons\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5996484+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.CommandEvent, KaVE.Commons\",\n" + //
			"        \"CommandId\": \"cid\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.5996484+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.ErrorEvent, KaVE.Commons\",\n" + //
			"        \"Content\": \"c\",\n" + //
			"        \"StackTrace\": [\n" + //
			"            \"s1\",\n" + //
			"            \"s2\"\n" + //
			"        ],\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.6005692+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.InfoEvent, KaVE.Commons\",\n" + //
			"        \"Info\": \"info\",\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.6005692+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.NavigationEvent, KaVE.Commons\",\n" + //
			"        \"Target\": \"0General:t\",\n" + //
			"        \"Location\": \"0General:l\",\n" + //
			"        \"TypeOfNavigation\": 1,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.6020957+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    },\n" + //
			"    {\n" + //
			"        \"$type\": \"KaVE.Commons.Model.Events.SystemEvent, KaVE.Commons\",\n" + //
			"        \"Type\": 6,\n" + //
			"        \"IDESessionUUID\": \"sid\",\n" + //
			"        \"KaVEVersion\": \"vX\",\n" + //
			"        \"TriggeredAt\": \"2016-08-15T04:28:30.6029072+02:00\",\n" + //
			"        \"TriggeredBy\": 3,\n" + //
			"        \"ActiveWindow\": \"0Win:w w\",\n" + //
			"        \"ActiveDocument\": \"0Doc:d d\"\n" + //
			"    }\n" + //
			"]";

	@Test
	public void shouldParseAndSerializeCSharpCompatibleString() {
		@SuppressWarnings("serial")
		Type type = new TypeToken<Set<IDEEvent>>() {
		}.getType();
		Set<IDEEvent> events = JsonUtils.fromJson(jsonIn, type);

		String jsonOut = JsonUtils.toJsonFormatted(events);
		assertEquals(jsonIn, jsonOut);
	}

	@Test
	@Parameters(method = "provideEvents")
	@TestCaseName("{2}")
	public void shouldDeSerialize(IIDEEvent in, Class<?> type, String tcn) {
		String json = JsonUtils.toJson(in, type);
		IIDEEvent out = JsonUtils.fromJson(json, type);
		assertEquals(in, out);
	}

	@Test
	@Parameters(method = "provideEvents")
	@TestCaseName("{2}")
	public void shouldIncludeTypeAnnotation(IIDEEvent in, Class<?> type, String tcn) {
		String json = JsonUtils.toJson(in, type);
		assertTrue(json.contains("$type"));
	}

	private static final IIDEEvent[] events = new IIDEEvent[] {
			// specific
			new CompletionEvent(), new TestRunEvent(), new UserProfileEvent(), new VersionControlEvent(),
			// visual studio
			new BuildEvent(), new DebuggerEvent(), new DocumentEvent(), new EditEvent(), new FindEvent(),
			new IDEStateEvent(), new InstallEvent(), new SolutionEvent(), new UpdateEvent(), new WindowEvent(),
			// general
			new ActivityEvent(), new CommandEvent(), new ErrorEvent(), new InfoEvent(), new NavigationEvent(),
			new SystemEvent() };

	public static Object[][] provideEvents() {
		ParameterData d = new ParameterData();

		for (IIDEEvent event : events) {
			for (Class<?> type : getHierarchy(event.getClass())) {
				String testCaseName = f("%s as %s", event.getClass().getSimpleName(), type.getSimpleName());
				d.add(event, type, testCaseName);
			}
		}
		return d.toArray();
	}

	private static Set<Class<?>> getHierarchy(Class<?> elem) {
		Set<Class<?>> hierarchy = Sets.newHashSet();
		if (elem == null || elem.equals(Object.class)) {
			return hierarchy;
		}

		hierarchy.add(elem);

		for (Class<?> i : elem.getInterfaces()) {
			hierarchy.addAll(getHierarchy(i));
		}
		hierarchy.addAll(getHierarchy(elem.getSuperclass()));

		return hierarchy;
	}
}