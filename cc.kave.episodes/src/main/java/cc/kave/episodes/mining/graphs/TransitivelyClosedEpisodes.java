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
package cc.kave.episodes.mining.graphs;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;

public class TransitivelyClosedEpisodes {

	public List<Episode> removeTransitivelyClosure(List<Episode> allEpisodes)
			throws Exception {

		if (allEpisodes.isEmpty()) {
			throw new Exception("You passed an empty list of maximal episode!");
		}

		List<Episode> updatedEpisodes = new LinkedList<Episode>();
		for (Episode episode : allEpisodes) {
			if (episode.getNumFacts() != episode.getNumEvents()) {
				Episode newEpisode = reduceRelations(episode);
					updatedEpisodes.add(newEpisode);
				} else {
					updatedEpisodes.add(episode);
				}
			}
		return updatedEpisodes;
	}

	private Episode reduceRelations(Episode episode) {
		List<List<Fact>> allPaths = new LinkedList<List<Fact>>();
		Episode episodeResult = new Episode(){};
		for (Fact fact : episode.getFacts()) {
			if (fact.isRelation()) {
				addFactPath(fact, allPaths);
			} else {
				episodeResult.addFact(fact);
			}
		}
		List<Fact> simplifiedRelations = removeClosureRelations(allPaths);
		List<Fact> finalRelations = doubleCheckClosureRemoval(simplifiedRelations);
		for (int numItr = 0; numItr < episode.getNumEvents() - 3; numItr++) {
			finalRelations = doubleCheckClosureRemoval(finalRelations);
		}
		episodeResult.addListOfFacts(finalRelations);
		return episodeResult;
	}

	private List<Fact> doubleCheckClosureRemoval(List<Fact> simplifiedRelations) {
		List<Fact> positiveRelations = new LinkedList<Fact>();
		List<Fact> negativeRelations = new LinkedList<Fact>();
		for (int idStart = 0; idStart < simplifiedRelations.size() - 1; idStart++) {
			Fact relation = simplifiedRelations.get(idStart);
			if (!(positiveRelations.contains(relation) || negativeRelations.contains(relation))) {
				positiveRelations.add(relation);
			}
			Tuple<Fact, Fact> eventsStart = relation.getRelationFacts();
			for (int idContinue = idStart + 1; idContinue < simplifiedRelations.size(); idContinue++) {
				Tuple<Fact, Fact> eventsContinue = simplifiedRelations.get(idContinue).getRelationFacts();
				if (eventsStart.getSecond().equals(eventsContinue.getFirst())) {
					negativeRelations.add(new Fact(eventsStart.getFirst(), eventsContinue.getSecond()));
				}
				if (eventsStart.getFirst().equals(eventsContinue.getSecond())) {
					negativeRelations.add(new Fact(eventsContinue.getFirst(), eventsStart.getSecond()));
				}
			}
		}
		Fact lastRelation = simplifiedRelations.get(simplifiedRelations.size() - 1);
		if (!(negativeRelations.contains(lastRelation) || positiveRelations.contains(lastRelation))) {
			positiveRelations.add(lastRelation);
		}
		for (Fact relation : negativeRelations) {
			if (positiveRelations.contains(relation)) {
				positiveRelations.remove(relation);
			}
		}
		return positiveRelations;
	}

	private List<Fact> removeClosureRelations(List<List<Fact>> allPaths) {
		List<Fact> positiveRelations = new LinkedList<Fact>();
		List<Fact> negativeRelations = new LinkedList<Fact>();
		for (List<Fact> list : allPaths) {
			for (int idx = 0; idx < list.size() - 1; idx++) {
				Fact relation = new Fact(list.get(idx), list.get(idx + 1));
				if (!(positiveRelations.contains(relation) || negativeRelations.contains(relation))) {
					positiveRelations.add(relation);
				}
			}
			if (list.size() > 2) {
				for (int idx = 0; idx < list.size() - 2; idx++) {
					Fact relation = new Fact(list.get(idx), list.get(idx + 2));
					if (!negativeRelations.contains(relation)) {
						negativeRelations.add(relation);
					}
					if (positiveRelations.contains(relation)) {
						positiveRelations.remove(relation);
					}
				}
			}
		}
		return positiveRelations;
	}

	private void addFactPath(Fact fact, List<List<Fact>> allPaths) {
		Tuple<Fact, Fact> events = fact.getRelationFacts();
		boolean pathFound = false;
		if (!allPaths.isEmpty()) {
			for (List<Fact> list : allPaths) {
				if (list.get(list.size() - 1).equals(events.getFirst())) {
					list.add(events.getSecond());
					pathFound = true;
				} else if (list.get(0).equals(events.getSecond())) {
					list.add(0, events.getFirst());
					pathFound = true;
				}
			}
		}
		if (!pathFound) {
			List<Fact> factPaths = new LinkedList<Fact>();
			factPaths.add(events.getFirst());
			factPaths.add(events.getSecond());
			allPaths.add(factPaths);
		}
	}
}
