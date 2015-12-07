package cc.kave.commons.mining.reader;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.episodes.Method;

public class EventStreamAsListOfMethodsParser {

	private File rootFolder;
	private FileReader reader;

	@Inject
	public EventStreamAsListOfMethodsParser(@Named("events") File directory, FileReader reader) {
		assertTrue(directory.exists(), "Event stream folder does not exist!");
		assertTrue(directory.isDirectory(), "Event stream folder is not a folder, but a file!");
		this.rootFolder = directory;
		this.reader = reader;
	}

	public List<Method> parse() {
		List<String> eventStream = reader.readFile(getFilePath());
		List<Method> methodsStream = new LinkedList<Method>();
		Method method = new Method();
		double previousEventTimestamp = 0.000;

		for (String line : eventStream) {
			String[] rowValues = line.split(",");
			double currentEventTimestamp = Double.parseDouble(rowValues[1]);
			if (currentEventTimestamp == 0.000) {
				method.setMethodName(rowValues[0]);
			} else if ((currentEventTimestamp - previousEventTimestamp) >= 0.5) {
				method.setNumberOfInvocations(method.getNumberOfInvocations());
				methodsStream.add(method);
				method = new Method();
				method.setMethodName(rowValues[0]);
			} else {
				if (!method.containsInvocations(new Fact(rowValues[0]))) {
					method.addFact(rowValues[0]);
				}
			}
			previousEventTimestamp = currentEventTimestamp;
		}
		method.setNumberOfInvocations(method.getNumberOfInvocations());
		methodsStream.add(method);
		return methodsStream;
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventstreamModified.txt";
		File file = new File(fileName);
		return file;
	}
}
