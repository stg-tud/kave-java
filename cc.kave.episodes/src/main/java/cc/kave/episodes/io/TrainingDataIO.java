package cc.kave.episodes.io;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;

import javax.inject.Named;

import cc.kave.episodes.model.EventStream;

import com.google.inject.Inject;

public class TrainingDataIO {

	private File repoDir;
	private EventStreamIo eventStreamIo;

	@Inject
	public TrainingDataIO(@Named("repositories") File folder) {
		assertTrue(folder.exists(), "Repositories folder does not exist");
		assertTrue(folder.isDirectory(),
				"Repositories is not a folder, but a file");
		this.repoDir = folder;
	}

	public void write(EventStream eventStream, int fold) {
		eventStreamIo.write(eventStream, getTrainPath(fold).streamPath,
				getTrainPath(fold).mappingPath, getTrainPath(fold).methodsPath);
	}

	private class TrainingPath {
		String streamPath = "";
		String mappingPath = "";
		String methodsPath = "";
	}

	private TrainingPath getTrainPath(int fold) {
		File path = new File(repoDir.getAbsolutePath() + "/TrainingData/fold"
				+ fold);
		if (!path.isDirectory()) {
			path.mkdirs();
		}
		TrainingPath trainPath = new TrainingPath();
		trainPath.streamPath = path.getAbsolutePath() + "/stream.txt";
		trainPath.mappingPath = path.getAbsolutePath() + "/mapping.txt";
		trainPath.methodsPath = path.getAbsolutePath() + "/methods.txt";

		return trainPath;
	}
}
