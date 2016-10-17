package cc.kave.episodes.preprocessing;

import java.io.IOException;
import java.util.List;

import cc.kave.episodes.export.EventStreamGenerator;
import cc.kave.episodes.io.ReposFoldedParser;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import static cc.recommenders.assertions.Asserts.assertTrue;

public class PreprocessingFolded {

	private Directory reposDir;

	private ReposFoldedParser reposParser;

	private static final int NUM_FOLDS = 10;

	@Inject
	public PreprocessingFolded(@Named("repositories") Directory directory,
			ReposFoldedParser repositories) {
		this.reposDir = directory;
		this.reposParser = repositories;
	}

	public void runPreparation() throws IOException {

		List<List<EventStreamGenerator>> repositories = reposParser
				.generateFoldedEvents(NUM_FOLDS);

		assertTrue(repositories.size() == NUM_FOLDS,
				"The repositories are not divided into n-folds!");

		for (int curFold = 0; curFold < NUM_FOLDS; curFold++) {
			Logger.log("Generating foldNum %d/%d", (curFold + 1), NUM_FOLDS);

			List<EventStream> trainingData = createTrainingData(curFold,
					NUM_FOLDS, repositories);
		}
	}

	private List<EventStream> createTrainingData(int curFold, int numFolds,
			List<List<EventStreamGenerator>> repositories) {

		List<List<EventStreamGenerator>> outs = Lists.newLinkedList();

		int i = 0;
		for (List<EventStreamGenerator> list : repositories) {
			if (i != curFold) {
				outs.add(list);
			}
			i = (i + 1) % numFolds;
		}
		List<EventStream> trainingRepos = preprocescTraining(outs);
		return trainingRepos;
	}

	private List<EventStream> preprocescTraining(
			List<List<EventStreamGenerator>> outs) {
		assertTrue(outs.size() == (NUM_FOLDS - 1),
				"Training data does not contain the right number of folds!");
		
		int numRepos = getNumRepos(outs);
		int partition = numRepos / 2;
		int curRepo = 0;
		List<EventStream> partitions = Lists.newLinkedList();
		List<Event> events = Lists.newLinkedList();
		EventStream stream = new EventStream();

		for (List<EventStreamGenerator> fold : outs) {
			for (EventStreamGenerator repo : fold) {
				if (curRepo < partition) {
					events.addAll(repo.getEventStream());
				}
			}

		}

		return partitions;
	}

	private int getNumRepos(List<List<EventStreamGenerator>> outs) {
		int numRepos = 0;

		for (List<EventStreamGenerator> fold : outs) {
			numRepos += numRepos + fold.size();
		}
		return numRepos;
	}
}
