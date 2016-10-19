package cc.kave.episodes.preprocessing;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cc.kave.episodes.io.IndivReposParser;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PreprocessingFolded {

	private Directory reposDir;

	private IndivReposParser reposParser;

	private static final int NUM_FOLDS = 10;

	@Inject
	public PreprocessingFolded(@Named("repositories") Directory directory,
			IndivReposParser repositories) {
		this.reposDir = directory;
		this.reposParser = repositories;
	}

	public void runPreparation() throws IOException {

		Map<String, List<Event>> repos = reposParser.generateReposEvents();

		for (int curFold = 0; curFold < NUM_FOLDS; curFold++) {
			Logger.log("Generating foldNum %d/%d", (curFold + 1), NUM_FOLDS);

			List<Event> trainingData = createTrainingData(curFold, NUM_FOLDS,
					repos);
		}
	}

	private List<Event> createTrainingData(int curFold, int numFolds,
			Map<String, List<Event>> in) {

		List<Event> outs = Lists.newLinkedList();

		int i = 0;
		for (Map.Entry<String, List<Event>> entry : in.entrySet()) {
			if (i != curFold) {
				outs.addAll(entry.getValue());
			}
			i = (i + 1) % numFolds;
		}
		return outs;
	}
}
