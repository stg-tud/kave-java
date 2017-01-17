package cc.kave.episodes.eventstream;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.inject.name.Named;

/* Stream length can be less or equal to the method file length
 * We include empty methods along the stream in our parsing, 
 * but not the empty methods in the end of the stream.  
 */


public class StreamAndMethodChecker {

	private File directory;
	private EventStreamIo streamIo;
	
	@Inject
	public StreamAndMethodChecker(@Named("events") File folder, EventStreamIo streamIo) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.directory = folder;
		this.streamIo = streamIo;
	}
	
	public void checkLengths() {
		List<List<Fact>> stream = streamIo.parseStream(getStreamPath());
		List<Event> methods = streamIo.readMapping(getMethodsPath());
		
		if (stream.size() == methods.size()) {
			Logger.log("Perfect match between stream and methods files");
		} else {
			Logger.log("There is a missmatch between stream and methods sizes");
			Logger.log("Number of methods body in the stream file is %d", stream.size());
			Logger.log("Number of element methods in methods file is %d", methods.size());
		}
	}

	private String getPath() {
		String pathName = directory.getAbsolutePath() + "/TrainingData/fold0/";
		return pathName;
	}
	
	private String getStreamPath() {
		String streamFile = getPath() + "stream.txt";
		return streamFile;
	}
	
	private String getMethodsPath() {
		String streamFile = getPath() + "methods.txt";
		return streamFile;
	}
}
