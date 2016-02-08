///**
// * Copyright 2016 Technische Universität Darmstadt
// * 
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * 
// *     http://www.apache.org/licenses/LICENSE-2.0
// * 
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package c.kave.commons.evaluation.queries;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import cc.kave.commons.model.episodes.Episode;
//import cc.kave.commons.model.episodes.QueryConfigurations;
//
//public class QueryGenerator {
//	
//	private SubsetsGenerator generator = new SubsetsGenerator();
//
//	public Map<Episode, Map<Integer, List<Episode>>> createQuery(List<Episode> allMethods,
//			QueryConfigurations configuration) {
//		Map<Episode, Map<Integer, List<Episode>>> generatedQueries = new HashMap<Episode, Map<Integer, List<Episode>>>();
//		
//		for (Episode episode : allMethods) {
//			Map<Integer, List<Episode>> queries = new HashMap<Integer, List<Episode>>();
//			List<Integer> sublistLengths = new LinkedList<Integer>();
//			boolean md = false;
//
//			if (episode.getNumEvents() > 1) {
//				if (configuration.equals(QueryConfigurations.INCLUDEMD_REMOVEONEBYONE)) {
//					md = true;
//					sublistLengths = removeOneByOne(episode.getNumEvents() - 1);
//					queries = removeInvocations(episode, sublistLengths, md);
//					generatedQueries.put(episode, queries);
//					continue;
//				}
//				if (configuration.equals(QueryConfigurations.INCLUDEMD_REMOVEBYPERCENTAGE)) {
//					md = true;
//					sublistLengths = removeByPercentage(episode.getNumEvents() - 1);
//					queries = removeInvocations(episode, sublistLengths, md);
//					generatedQueries.put(episode, queries);
//					continue;
//				}
//			}
//			if (episode.getNumEvents() > 2) {
//				if (configuration.equals(QueryConfigurations.REMOVEMD_REMOVEONEBYONE)) {
//					sublistLengths = removeOneByOne(episode.getNumEvents() - 1);
//					queries = removeInvocations(episode, sublistLengths, md);
//					generatedQueries.put(episode, queries);
//					continue;
//				}
//				if (configuration.equals(QueryConfigurations.REMOVEMD_REMOVEBYPERCENTAGE)) {
//					sublistLengths = removeByPercentage(episode.getNumEvents() - 1);
//					queries = removeInvocations(episode, sublistLengths, md);
//					generatedQueries.put(episode, queries);
//				}
//			}
//		}
//		return generatedQueries;
//	}
//
//	private Episode createQuery(Episode episode, List<String> list, boolean md) {
//		Episode query = new Episode();
//		query.setFrequency(1);
//		if (md) {
//			query.addFact(episode.get(0).getRawFact());
//			query.setNumEvents(list.size() + 1);
//		} else {
//			query.setNumEvents(list.size());
//		}
//		query.addListOfFacts(list);
//		for (int idx = 0; idx < query.getNumEvents() - 1; idx++) {
//			query.addFact(query.get(idx).getRawFact() + ">" + query.get(idx + 1).getRawFact());
//		}
//		return query;
//	}
//
//	private List<String> getInvocations(Episode episode) {
//		List<String> invocations = new LinkedList<String>();
//		for (int idx = 1; idx < episode.getNumEvents(); idx++) {
//			invocations.add(episode.get(idx).getRawFact());
//		}
//		return invocations;
//	}
//
//	private Map<Integer, List<Episode>> removeInvocations(Episode episode, List<Integer> subsets, boolean md) {
//		Map<Integer, List<Episode>> results = new HashMap<Integer, List<Episode>>();
//		List<String> invocations = getInvocations(episode);
//
//		for (int remove : subsets) {
//			List<List<String>> allQueries = generator.generateSubsets(invocations, invocations.size() - remove);
//			List<Episode> queryLevel = new LinkedList<Episode>();
//			for (List<String> list : allQueries) {
//				Episode query = createQuery(episode, list, md);
//				queryLevel.add(query);
//			}
//			results.put(remove, queryLevel);
//		}
//		if (md) {
//			List<Episode> removeAllInvocations = new LinkedList<Episode>();
//			Episode minimalQuery = new Episode();
//			minimalQuery.setFrequency(1);
//			minimalQuery.setNumEvents(1);
//			minimalQuery.addFact(episode.get(0).getRawFact());
//			removeAllInvocations.add(minimalQuery);
//			results.put(invocations.size(), removeAllInvocations);
//		}
//		if (!md && (subsets.get(subsets.size() - 1) != episode.getNumEvents() - 2)) {
//			List<Episode> removeMost = new LinkedList<Episode>();
//			for (int idx = 1; idx < episode.getNumEvents(); idx++) {
//				Episode minimalQuery = new Episode();
//				minimalQuery.setFrequency(1);
//				minimalQuery.setNumEvents(1);
//				minimalQuery.addFact(episode.get(idx).getRawFact());
//				removeMost.add(minimalQuery);
//			}
//			results.put(episode.getNumEvents() - 2, removeMost);
//		}
//		return results;
//	}
//
//	private List<Integer> removeOneByOne(int NumberOfInvocations) {
//		List<Integer> result = new LinkedList<Integer>();
//		for (int remove = 1; remove < NumberOfInvocations; remove++) {
//			result.add(remove);
//		}
//		return result;
//	}
//
//	private List<Integer> removeByPercentage(int numberInvocations) {
//		List<Integer> removeInvocations = new LinkedList<Integer>();
//		List<Double> percentages = new LinkedList<Double>();
//		percentages.add(0.01);
//		percentages.add(0.25);
//		percentages.add(0.50);
//		percentages.add(0.75);
//		for (double p : percentages) {
//			int remove = (int) Math.ceil(p * (double) numberInvocations);
//			if (!removeInvocations.contains(remove) && remove != numberInvocations) {
//				removeInvocations.add(remove);
//			}
//		}
//		return removeInvocations;
//	}
//}
