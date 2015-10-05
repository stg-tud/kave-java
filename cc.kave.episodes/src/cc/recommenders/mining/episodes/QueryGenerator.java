/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 */
package cc.recommenders.mining.episodes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Episode;
import cc.recommenders.datastructures.Tuple;

public class QueryGenerator {

	public Tuple<Episode, Episode> oneEventSkipped(Map<Integer, List<Episode>> episodesLearned)
			throws Exception {
		Episode episode = getRandomEpisode(1, episodesLearned);
		Episode query = new Episode();
		query.getFacts() = new LinkedList<String>();
		query.numEvents = episode.numEvents;
		Random randomGenerator = new Random();

		int randomEvent = randomGenerator.nextInt(episode.numEvents);
		String event = episode.getFacts().get(randomEvent);
		for (String fact : episode.getFacts()) {
			if (fact.equalsIgnoreCase(event) || fact.contains(event)) {
				continue;
			} else {
				query.getFacts().add(fact);
			}
		}
		Tuple<Episode, Episode> tuple = Tuple.newTuple(query, episode);

		return tuple;
	}

	public Map<Episode, Episode> twoEventSkipped(Map<Integer, List<Episode>> episodesLearned)
			throws Exception {
		Map<Episode, Episode> mapping = new HashMap<Episode, Episode>();
		Episode episode = getRandomEpisode(2, episodesLearned);
		Episode query = new Episode();
		Random randomGenerator = new Random();

		int randomEvent1 = randomGenerator.nextInt(episode.numEvents);
		int randomEvent2 = randomGenerator.nextInt(episode.numEvents);
		if (randomEvent1 == randomEvent2) {
			while (randomEvent2 == randomEvent1) {
				randomEvent2 = randomGenerator.nextInt(episode.numEvents);
			}
		}
		String event1 = episode.getFacts().get(randomEvent1);
		String event2 = episode.getFacts().get(randomEvent2);
		for (String fact : episode.getFacts()) {
			if (fact.equalsIgnoreCase(event1) || fact.contains(event1) || fact.equalsIgnoreCase(event2)
					|| fact.contains(event2)) {
				continue;
			} else {
				query.getFacts().add(fact);
			}
		}
		query.numEvents = episode.numEvents - 2;
		mapping.put(query, episode);

		return mapping;
	}

	public Map<Episode, Episode> threeEventSkipped(Map<Integer, List<Episode>> episodesLearned)
			throws Exception {
		Map<Episode, Episode> mapping = new HashMap<Episode, Episode>();
		Episode episode = getRandomEpisode(3, episodesLearned);
		Episode query = new Episode();
		Random randomGenerator = new Random();

		int randomEvent1 = randomGenerator.nextInt(episode.numEvents);
		int randomEvent2 = randomGenerator.nextInt(episode.numEvents);
		if (randomEvent1 == randomEvent2) {
			while (randomEvent2 == randomEvent1) {
				randomEvent2 = randomGenerator.nextInt(episode.numEvents);
			}
		}
		int randomEvent3 = randomGenerator.nextInt(episode.numEvents);
		if (randomEvent3 == randomEvent1 || randomEvent3 == randomEvent2) {
			while (randomEvent3 == randomEvent1 || randomEvent3 == randomEvent2) {
				randomEvent3 = randomGenerator.nextInt(episode.numEvents);
			}
		}
		String event1 = episode.getFacts().get(randomEvent1);
		String event2 = episode.getFacts().get(randomEvent2);
		String event3 = episode.getFacts().get(randomEvent3);
		for (String fact : episode.getFacts()) {
			if (fact.equalsIgnoreCase(event1) || fact.contains(event1) || fact.equalsIgnoreCase(event2)
					|| fact.contains(event2) || fact.equalsIgnoreCase(event3) || fact.contains(event3)) {
				continue;
			} else {
				query.getFacts().add(fact);
			}
		}
		query.numEvents = episode.numEvents - 3;
		mapping.put(query, episode);

		return mapping;
	}

	private Episode getRandomEpisode(int numEventsSkipped, Map<Integer, List<Episode>> episodesLearned)
			throws Exception {
		Episode episode = new Episode();
		Random randomGenerator = new Random();
		int randomNode;
		int numEpisodes;
		int randomEpisode;

		int[] eventNodes = getNumberOfEvents(episodesLearned);
		int numNodes = eventNodes[eventNodes.length - 1];
		if (numNodes > numEventsSkipped) {
			int rnd = new Random().nextInt(eventNodes.length);
			if (numEventsSkipped > 1) {
				randomNode = eventNodes[rnd];
				while (randomNode <= numEventsSkipped) {
					rnd = new Random().nextInt(eventNodes.length);
					randomNode = eventNodes[rnd];
				}
				int range = numNodes - numEventsSkipped;
				randomNode = randomGenerator.nextInt(range) + numEventsSkipped;
			} else {
				randomNode = eventNodes[rnd];
			}
		} else {
			throw new Exception("Can not remove more events that the learned episode has!");
		}
		numEpisodes = episodesLearned.get(randomNode).size();
		randomEpisode = randomGenerator.nextInt(numEpisodes);

		episode.numEvents = randomNode;
		episode.setFacts() = episodesLearned.get(randomNode).get(randomEpisode).getFacts();
		return episode;
	}
	
	private int[] getNumberOfEvents( Map<Integer, List<Episode>> episodes) {
		Set<Integer> nodes = Sets.newTreeSet(episodes.keySet());
		Iterator<Integer> itr = nodes.iterator(); 
		int[] numEvents = new int[episodes.size()];
		int idx = 0;
		while (itr.hasNext()) {
			numEvents[idx] = itr.next();
			idx++;
		}
		return numEvents;
	}
}
