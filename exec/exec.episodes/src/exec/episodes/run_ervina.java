package exec.episodes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cc.kave.commons.mining.episodes.EpisodeGraphGenerator;
import cc.recommenders.io.Logger;

public class run_ervina {

	private static final String PROPERTY_NAME = "episodeFolder";
	private static final String PROPERTY_FILE = "episode.properties";

	private static Injector injector;

	public static void main(String[] args) throws Exception {
		initLogger();
		printAvailableMemory();
		
		String rootFolder = readPropertyFromFile(PROPERTY_NAME);
		injector = Guice.createInjector(new Module(rootFolder));

		Logger.append("\n");
		Logger.log("started: %s\n", new Date());

		load(EpisodeGraphGenerator.class).generate();
//		load(EventStreamModifier.class).modify();
		
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
				throw new RuntimeException("property '" + propertyName + "' not found in properties file");
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