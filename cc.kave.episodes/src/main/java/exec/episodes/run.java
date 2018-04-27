/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.episodes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class run {

	private static final String PROPERTY_NAME = "episodeFolder";
	private static final String PROPERTY_FILE = "episode.properties";

	private static final int FREQTHRESH = 300;
	private static final double BIDIRECTTHRESH = 0.6;

	private static final int METHODSIZE = 500;

	private static Injector injector;

	private static EventStreamIo eventStream;

	public run(EventStreamIo streamIo) {
		this.eventStream = streamIo;
	}

	public static void main(String[] args) throws Exception {
		initLogger();
		printAvailableMemory();

		String rootFolder = readPropertyFromFile(PROPERTY_NAME);
		injector = Guice.createInjector(new Module(rootFolder));

		Logger.append("\n");
		Logger.log("started: %s\n", new Date());

		List<Event> events = load(EventStreamIo.class).readMapping(FREQTHRESH);

		int i = 0;
		for (Event e : events) {
			if (i < 1) {
				i++;
				continue;
			}
			IMethodName methodName = e.getMethod();

			Logger.log("Event: %s", e.toString());
			Logger.log("Method: %s", methodName.getIdentifier());
			Logger.log("");

			ITypeName returnType = methodName.getReturnType();
			Logger.log("Return type identifier: %s", returnType.getIdentifier());
			Logger.log("Return type full name: %s", returnType.getFullName());

			INamespaceName namespace = returnType.getNamespace();
			Logger.log("Namespace: %s", namespace.getIdentifier());
//			Logger.log("Namespace name: %s", namespace.getName());
			Logger.log("Return type name: %s", returnType.getName());

			Logger.log("");

			IAssemblyName assembly = returnType.getAssembly();
			Logger.log("Assembly identifier: %s", assembly.getIdentifier());
			Logger.log("Assembly name: %s", assembly.getName());
			Logger.log("Assembly version identifier: %s", assembly.getVersion()
					.getIdentifier());
//			Logger.log("Assembly version major: %d", assembly.getVersion()
//					.getMajor());
//			Logger.log("Assembly version minor: %d", assembly.getVersion()
//					.getMinor());
//			Logger.log("Assembly version revision: %d", assembly.getVersion()
//					.getRevision());
//			Logger.log("Assembly version build: %d", assembly.getVersion()
//					.getBuild());
			Logger.log("");

			ITypeName declaringType = methodName.getDeclaringType();
			Logger.log("Declaring type: %s", declaringType.getIdentifier());
			Logger.log("");

			Logger.log("Method name: %s", methodName.getFullName());
			List<IParameterName> parameters = methodName.getParameters();
			for (IParameterName param : parameters) {
				Logger.log("Parameter: %s", param.getIdentifier());
				Logger.log("Parameter type: %s", param.getValueType().getIdentifier());
				Logger.log("Parameter name: %s", param.getName());
			}

			break;
		}

		Logger.log("done");
	}

	private static void initLogger() {
		Logger.setPrinting(true);
		Logger.setDebugging(false);
		Logger.setCapturing(false);
	}

	private static void printAvailableMemory() {
		long maxMem = Runtime.getRuntime().maxMemory();
		float maxMemInMb = Math.round(maxMem * 1.0d / (1024 * 1024 * 1.0f));
		Logger.log("maximum memory (-Xmx): %.0f MB", maxMemInMb);
	}

	private static String readPropertyFromFile(String propertyName) {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(PROPERTY_FILE));
			String property = properties.getProperty(propertyName);
			if (property == null) {
				throw new RuntimeException("property '" + propertyName
						+ "' not found in properties file");
			}
			Logger.log("%s: %s", propertyName, property);

			return property;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T load(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

}