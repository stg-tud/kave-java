package cc.kave.episodes.preprocessing;

import java.util.List;
import java.util.Map;

import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.kave.episodes.eventstream.EventsFilter;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.RepositoriesParser;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class PreprocessingFolded {

	private RepositoriesParser reposParser;

	private EventStreamIo eventStreamIo;
	private ValidationDataIO validationIO;

	private static final int NUM_FOLDS = 10;

	@Inject
	public PreprocessingFolded(RepositoriesParser repositories,
			EventStreamIo trainingData, ValidationDataIO valIo) {
		this.reposParser = repositories;
		this.eventStreamIo = trainingData;
		this.validationIO = valIo;
	}

	public void runPreparation(int frequency) throws Exception {

		Map<String, EventStreamGenerator> repos = reposParser
				.generateReposEvents();

		Logger.log("Generating event stream data for freq = %d ...", frequency);

		for (int curFold = 0; curFold < NUM_FOLDS; curFold++) {
			Logger.log("Generating foldNum %d/%d", (curFold + 1), NUM_FOLDS);

			List<Event> trainingData = createTrainingData(curFold, NUM_FOLDS,
					repos);
			EventStream trainingStream = EventsFilter.filterStream(
					trainingData, frequency);
			eventStreamIo.write(trainingStream, frequency);
			trainingData.clear();
			trainingStream.delete();

			List<Event> validationData = createValidationData(curFold,
					NUM_FOLDS, repos);
			validationIO.write(validationData, curFold);
			validationData.clear();
		}
	}

	public List<Event> createTrainingData(int curFold, int numFolds,
			Map<String, EventStreamGenerator> in) {

		List<Event> outs = Lists.newLinkedList();

		int numRepos = 0;
		int i = 0;
		for (Map.Entry<String, EventStreamGenerator> entry : in.entrySet()) {
			if (i != curFold) {
				outs.addAll(entry.getValue().getEventStream());
				numRepos++;
			}
			i = (i + 1) % numFolds;
		}
		Logger.log("\tWritting event stream ... (training: %d repositories)",
				numRepos);
		return outs;
	}

	public List<Event> createValidationData(int curFold, int numFolds,
			Map<String, EventStreamGenerator> in) {
		List<Event> outs = Lists.newLinkedList();

		int numRepos = 0;
		int i = 0;
		for (Map.Entry<String, EventStreamGenerator> entry : in.entrySet()) {
			if (i == curFold) {
				outs.addAll(entry.getValue().getEventStream());
				numRepos++;
			}
			i = (i + 1) % numFolds;
		}
		Logger.log("\tWritting event stream ... (validation: %d repositories)",
				numRepos);
		return outs;
	}
}
