package cc.kave.episodes.preprocessing;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cc.kave.episodes.eventstream.EventsFilter;
import cc.kave.episodes.io.IndivReposParser;
import cc.kave.episodes.io.TrainingDataIO;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class PreprocessingFolded {

	private IndivReposParser reposParser;
	private TrainingDataIO trainingIO;
	private ValidationDataIO validationIO;

	private static final int NUM_FOLDS = 10;

	@Inject
	public PreprocessingFolded(IndivReposParser repositories,
			TrainingDataIO training, ValidationDataIO valIo) {
		this.reposParser = repositories;
		this.trainingIO = training;
		this.validationIO = valIo;
	}

	public void runPreparation(int freq) throws IOException {

		Map<String, List<Event>> repos = reposParser.generateReposEvents();

		for (int curFold = 0; curFold < NUM_FOLDS; curFold++) {
			Logger.log("Generating foldNum %d/%d", (curFold + 1), NUM_FOLDS);

			List<Event> trainingData = createTrainingData(curFold, NUM_FOLDS,
					repos);
			EventStream trainingStream = EventsFilter.filterStream(
					trainingData, freq);
			trainingIO.write(trainingStream, curFold);

			List<Event> validationData = createValidationData(curFold,
					NUM_FOLDS, repos);
			validationIO.write(validationData, curFold);
		}
	}

	private List<Event> createTrainingData(int curFold, int numFolds,
			Map<String, List<Event>> in) {

		List<Event> outs = Lists.newLinkedList();

		int numRepos = 0;
		int i = 0;
		for (Map.Entry<String, List<Event>> entry : in.entrySet()) {
			if (i != curFold) {
				outs.addAll(entry.getValue());
				numRepos++;
			}
			i = (i + 1) % numFolds;
		}
		Logger.log("\tWritting event stream ... (training: %d repositories)",
				numRepos);
		return outs;
	}

	private List<Event> createValidationData(int curFold, int numFolds,
			Map<String, List<Event>> in) {
		List<Event> outs = Lists.newLinkedList();

		int numRepos = 0;
		int i = 0;
		for (Map.Entry<String, List<Event>> entry : in.entrySet()) {
			if (i == curFold) {
				outs.addAll(entry.getValue());
				numRepos++;
			}
			i = (i + 1) % numFolds;
		}
		Logger.log("Writting event stream ... (validation: %d repositories)",
				numRepos);
		return outs;
	}
}
