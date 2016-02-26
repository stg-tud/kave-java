package cc.kave.episodes.evaluation.queries;

/// **
// * Copyright 2016 Technische Universität Darmstadt
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
// package cc.kave.commons.evaluation.queries;
//
// import java.util.HashMap;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Map;
//
// import org.junit.Assert;
// import org.junit.Before;
// import org.junit.Test;
//
// import c.kave.commons.evaluation.queries.QueryGenerator;
// import c.kave.commons.evaluation.queries.SubsetsGenerator;
// import cc.kave.commons.model.episodes.Episode;
// import cc.kave.commons.model.episodes.QueryConfigurations;
//
// public class QueryGeneratorTest {
//
// private List<Episode> allMethods = new LinkedList<Episode>();
// private SubsetsGenerator subsets;
// private QueryGenerator sut;
//
// @Before
// public void setup() {
// allMethods.add(createEpisodeFromStrings("a", "b"));
// allMethods.add(createEpisodeFromStrings("a"));
// allMethods.add(createEpisodeFromStrings("a", "b", "c", "d"));
//
// sut = new QueryGenerator();
// }
//
// @Test
// public void configuration1Test() {
// Map<Episode, Map<Integer, List<Episode>>> expected = new HashMap<Episode,
/// Map<Integer, List<Episode>>>();
//
// Map<Integer, List<Episode>> episodeQueries = new HashMap<Integer,
/// List<Episode>>();
// List<Episode> queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a"));
// episodeQueries.put(1, queryLevel);
// expected.put(createEpisodeFromStrings("a", "b"), episodeQueries);
//
// episodeQueries = new HashMap<Integer, List<Episode>>();
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a", "b", "c"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "d"));
// queryLevel.add(createEpisodeFromStrings("a", "c", "d"));
// episodeQueries.put(1, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a", "b"));
// queryLevel.add(createEpisodeFromStrings("a", "c"));
// queryLevel.add(createEpisodeFromStrings("a", "d"));
// episodeQueries.put(2, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a"));
// episodeQueries.put(3, queryLevel);
//
// expected.put(createEpisodeFromStrings("a", "b", "c", "d"), episodeQueries);
//
// Map<Episode, Map<Integer, List<Episode>>> actuals =
/// sut.createQuery(allMethods, QueryConfigurations.INCLUDEMD_REMOVEONEBYONE);
//
// Assert.assertEquals(expected, actuals);
// }
//
// @Test
// public void configuration3Test() {
// Map<Episode, Map<Integer, List<Episode>>> expected = new HashMap<Episode,
/// Map<Integer, List<Episode>>>();
//
// Map<Integer, List<Episode>> episodeQueries = new HashMap<Integer,
/// List<Episode>>();
// List<Episode> queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("b", "c"));
// queryLevel.add(createEpisodeFromStrings("b", "d"));
// queryLevel.add(createEpisodeFromStrings("c", "d"));
// episodeQueries.put(1, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("b"));
// queryLevel.add(createEpisodeFromStrings("c"));
// queryLevel.add(createEpisodeFromStrings("d"));
// episodeQueries.put(2, queryLevel);
//
// expected.put(createEpisodeFromStrings("a", "b", "c", "d"), episodeQueries);
//
// Map<Episode, Map<Integer, List<Episode>>> actuals =
/// sut.createQuery(allMethods, QueryConfigurations.REMOVEMD_REMOVEONEBYONE);
//
// Assert.assertEquals(expected, actuals);
// }
//
// @Test
// public void configuration2Test() {
// allMethods.add(createEpisodeFromStrings("a", "b", "c", "d", "e", "f"));
//
// Map<Episode, Map<Integer, List<Episode>>> expected = new HashMap<Episode,
/// Map<Integer, List<Episode>>>();
//
// Map<Integer, List<Episode>> episodeQueries = new HashMap<Integer,
/// List<Episode>>();
// List<Episode> queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a"));
// episodeQueries.put(1, queryLevel);
// expected.put(createEpisodeFromStrings("a", "b"), episodeQueries);
//
// episodeQueries = new HashMap<Integer, List<Episode>>();
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a", "b", "c"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "d"));
// queryLevel.add(createEpisodeFromStrings("a", "c", "d"));
// episodeQueries.put(1, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a", "b"));
// queryLevel.add(createEpisodeFromStrings("a", "c"));
// queryLevel.add(createEpisodeFromStrings("a", "d"));
// episodeQueries.put(2, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a"));
// episodeQueries.put(3, queryLevel);
//
// expected.put(createEpisodeFromStrings("a", "b", "c", "d"), episodeQueries);
//
// episodeQueries = new HashMap<Integer, List<Episode>>();
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a", "b", "c", "d"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "c", "e"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "d", "e"));
// queryLevel.add(createEpisodeFromStrings("a", "c", "d", "e"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "c", "f"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "d", "f"));
// queryLevel.add(createEpisodeFromStrings("a", "c", "d", "f"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "e", "f"));
// queryLevel.add(createEpisodeFromStrings("a", "c", "e", "f"));
// queryLevel.add(createEpisodeFromStrings("a", "d", "e", "f"));
// episodeQueries.put(2, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a", "b", "c"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "d"));
// queryLevel.add(createEpisodeFromStrings("a", "c", "d"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "e"));
// queryLevel.add(createEpisodeFromStrings("a", "c", "e"));
// queryLevel.add(createEpisodeFromStrings("a", "d", "e"));
// queryLevel.add(createEpisodeFromStrings("a", "b", "f"));
// queryLevel.add(createEpisodeFromStrings("a", "c", "f"));
// queryLevel.add(createEpisodeFromStrings("a", "d", "f"));
// queryLevel.add(createEpisodeFromStrings("a", "e", "f"));
// episodeQueries.put(3, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a", "b"));
// queryLevel.add(createEpisodeFromStrings("a", "c"));
// queryLevel.add(createEpisodeFromStrings("a", "d"));
// queryLevel.add(createEpisodeFromStrings("a", "e"));
// queryLevel.add(createEpisodeFromStrings("a", "f"));
// episodeQueries.put(4, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("a"));
// episodeQueries.put(5, queryLevel);
//
// expected.put(createEpisodeFromStrings("a", "b", "c", "d", "e", "f"),
/// episodeQueries);
//
// Map<Episode, Map<Integer, List<Episode>>> actuals =
/// sut.createQuery(allMethods,
/// QueryConfigurations.INCLUDEMD_REMOVEBYPERCENTAGE);
//
// Assert.assertEquals(expected, actuals);
// }
//
// @Test
// public void configuration4Test() {
// allMethods.add(createEpisodeFromStrings("a", "b", "c", "d", "e", "f"));
// allMethods.add(createEpisodeFromStrings("a", "b", "c", "d", "e", "f", "g",
/// "h", "i"));
//
// Map<Episode, Map<Integer, List<Episode>>> expected = new HashMap<Episode,
/// Map<Integer, List<Episode>>>();
// Map<Integer, List<Episode>> episodeQueries = new HashMap<Integer,
/// List<Episode>>();
// List<Episode> queryLevel = new LinkedList<Episode>();
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("b", "c"));
// queryLevel.add(createEpisodeFromStrings("b", "d"));
// queryLevel.add(createEpisodeFromStrings("c", "d"));
// episodeQueries.put(1, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("b"));
// queryLevel.add(createEpisodeFromStrings("c"));
// queryLevel.add(createEpisodeFromStrings("d"));
// episodeQueries.put(2, queryLevel);
//
// expected.put(createEpisodeFromStrings("a", "b", "c", "d"), episodeQueries);
//
// episodeQueries = new HashMap<Integer, List<Episode>>();
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("b", "c", "d"));
// queryLevel.add(createEpisodeFromStrings("b", "c", "e"));
// queryLevel.add(createEpisodeFromStrings("b", "d", "e"));
// queryLevel.add(createEpisodeFromStrings("c", "d", "e"));
// queryLevel.add(createEpisodeFromStrings("b", "c", "f"));
// queryLevel.add(createEpisodeFromStrings("b", "d", "f"));
// queryLevel.add(createEpisodeFromStrings("c", "d", "f"));
// queryLevel.add(createEpisodeFromStrings("b", "e", "f"));
// queryLevel.add(createEpisodeFromStrings("c", "e", "f"));
// queryLevel.add(createEpisodeFromStrings("d", "e", "f"));
// episodeQueries.put(2, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("b", "c"));
// queryLevel.add(createEpisodeFromStrings("b", "d"));
// queryLevel.add(createEpisodeFromStrings("c", "d"));
// queryLevel.add(createEpisodeFromStrings("b", "e"));
// queryLevel.add(createEpisodeFromStrings("c", "e"));
// queryLevel.add(createEpisodeFromStrings("d", "e"));
// queryLevel.add(createEpisodeFromStrings("b", "f"));
// queryLevel.add(createEpisodeFromStrings("c", "f"));
// queryLevel.add(createEpisodeFromStrings("d", "f"));
// queryLevel.add(createEpisodeFromStrings("e", "f"));
// episodeQueries.put(3, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queryLevel.add(createEpisodeFromStrings("b"));
// queryLevel.add(createEpisodeFromStrings("c"));
// queryLevel.add(createEpisodeFromStrings("d"));
// queryLevel.add(createEpisodeFromStrings("e"));
// queryLevel.add(createEpisodeFromStrings("f"));
// episodeQueries.put(4, queryLevel);
//
// expected.put(createEpisodeFromStrings("a", "b", "c", "d", "e", "f"),
/// episodeQueries);
//
// List<String> episodeAsList = createList("b", "c", "d", "e", "f", "g", "h",
/// "i");
// episodeQueries = new HashMap<Integer, List<Episode>>();
// queryLevel = new LinkedList<Episode>();
// List<List<String>> queries = subsets.generateSubsets(episodeAsList, 6);
// for (List<String> q : queries) {
// queryLevel.add(createEpisodeFromList(q));
// }
// episodeQueries.put(2, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queries = subsets.generateSubsets(episodeAsList, 4);
// for (List<String> q : queries) {
// queryLevel.add(createEpisodeFromList(q));
// }
// episodeQueries.put(4, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queries = subsets.generateSubsets(episodeAsList, 2);
// for (List<String> q : queries) {
// queryLevel.add(createEpisodeFromList(q));
// }
// episodeQueries.put(6, queryLevel);
//
// queryLevel = new LinkedList<Episode>();
// queries = subsets.generateSubsets(episodeAsList, 1);
// for (List<String> q : queries) {
// queryLevel.add(createEpisodeFromList(q));
// }
// episodeQueries.put(7, queryLevel);
//
// expected.put(createEpisodeFromStrings("a", "b", "c", "d", "e", "f", "g", "h",
/// "i"), episodeQueries);
//
// Map<Episode, Map<Integer, List<Episode>>> actuals =
/// sut.createQuery(allMethods,
/// QueryConfigurations.REMOVEMD_REMOVEBYPERCENTAGE);
//
// Assert.assertEquals(expected, actuals);
// }
//
// private List<String> createList(String ...strings) {
// List<String> result = new LinkedList<String>();
// for (String s : strings) {
// result.add(s);
// }
// return result;
// }
//
// private Episode createEpisodeFromList(List<String> query) {
// Episode episode = new Episode();
// episode.setFrequency(1);
// episode.setNumEvents(query.size());
// episode.addListOfFacts(query);
//
// String previousEvent = "";
//
// for (String event : query) {
// if (previousEvent.isEmpty()) {
// previousEvent = event;
// } else {
// episode.addFact(previousEvent + ">" + event);
// previousEvent = event;
// }
// }
// return episode;
// }
//
// private Episode createEpisodeFromStrings(String ... strings) {
// Episode episode = new Episode();
// episode.setFrequency(1);
// episode.setNumEvents(strings.length);
// episode.addStringsOfFacts(strings);
//
// String previousEvent = "";
//
// for (String event : strings) {
// if (previousEvent.isEmpty()) {
// previousEvent = event;
// } else {
// episode.addFact(previousEvent + ">" + event);
// previousEvent = event;
// }
// }
// return episode;
// }
// }
