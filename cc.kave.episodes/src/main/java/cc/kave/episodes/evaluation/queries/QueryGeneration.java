package cc.kave.episodes.evaluation.queries;

import static cc.kave.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.evaluation.ProposalHelper;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class QueryGeneration {

	private File rootFolder;
	private EventStreamIo eventStreamIo;

	@Inject
	public QueryGeneration(@Named("events") File folder) {
		assertTrue(folder.exists(), "Events folder does not exist!");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file!");
		this.rootFolder = folder;
	}

	public Set<Episode> generate(Episode pattern) throws Exception {
		assertTrue(pattern.getNumEvents() > 1,
				"Pattern is non-valid, few events!");

		List<Event> mapping = eventStreamIo.readMapping(0);

		Tuple<Set<Fact>, Set<Fact>> mdmi = separateEvents(pattern, mapping);

		if (mdmi.getSecond().size() == 0) {
			return Sets.newHashSet(pattern);
		}
		Set<Fact> MIRanking = rankMI(pattern, mdmi.getSecond());

		Set<Episode> queries = Sets.newLinkedHashSet();
		queries.add(createQuery(pattern, MIRanking, QStrategies.TOPBOTTOM));
		return queries;
	}

	private Episode createQuery(Episode pattern, Set<Fact> mis,
			QStrategies strategy) {
		Episode query = new Episode();
		Set<Fact> removals = Sets.newHashSet();

		if (mis.size() == 1) {
			removals.addAll(mis);
		} else if (mis.size() > 1) {
			if (strategy == QStrategies.TOPBOTTOM) {
				removals = topBottom(mis);
			}
		}

		for (Fact fact : pattern.getFacts()) {
			if (removals.contains(fact)) {
				continue;
			}
			if (fact.isRelation()) {
				Tuple<Fact, Fact> relFacts = fact.getRelationFacts();
				Fact fact1 = relFacts.getFirst();
				Fact fact2 = relFacts.getSecond();

				if (removals.contains(fact1) || removals.contains(fact2)) {
					continue;
				}
			}
			query.addFact(fact);
		}
		return query;
	}

	private Set<Fact> topBottom(Set<Fact> mis) {
		int numInvs = mis.size();
		int numRemovals = numInvs / 2;
		Set<Fact> events = Sets.newHashSet();

		for (Fact fact : mis) {
			if (numRemovals > 0) {
				numRemovals--;
			} else {
				events.add(fact);
			}
		}
		return events;
	}

	private Set<Fact> rankMI(Episode pattern, Set<Fact> mis) {
		Map<Fact, Integer> factScores = initMap(mis);

		for (Fact relation : pattern.getRelations()) {
			Tuple<Fact, Fact> relFacts = relation.getRelationFacts();
			Fact fact = relFacts.getFirst();
			if (mis.contains(fact)) {
				int score = factScores.get(fact);
				factScores.put(fact, score + 1);
			}
		}
		Map<Integer, Set<Fact>> groupFacts = Maps.newHashMap();

		for (Map.Entry<Fact, Integer> entry : factScores.entrySet()) {
			Fact fact = entry.getKey();
			int score = entry.getValue();

			if (groupFacts.containsKey(score)) {
				Set<Fact> currFacts = groupFacts.get(score);
				currFacts.add(fact);
				groupFacts.put(score, currFacts);
			} else {
				groupFacts.put(score, Sets.newHashSet(fact));
			}
		}
		Set<Tuple<Integer, Set<Fact>>> treeSet = ProposalHelper
				.createFactsSortedSet();

		for (Map.Entry<Integer, Set<Fact>> entry : groupFacts.entrySet()) {
			int rank = entry.getKey();
			Set<Fact> facts = entry.getValue();
			treeSet.add(Tuple.newTuple(rank, facts));
		}
		Set<Fact> invs = Sets.newLinkedHashSet();

		for (Tuple<Integer, Set<Fact>> tuple : treeSet) {
			invs.addAll(tuple.getSecond());
		}
		return invs;
	}

	private Map<Fact, Integer> initMap(Set<Fact> mis) {
		Map<Fact, Integer> init = Maps.newLinkedHashMap();

		for (Fact fact : mis) {
			init.put(fact, 0);
		}
		return init;
	}

	private Tuple<Set<Fact>, Set<Fact>> separateEvents(Episode pattern,
			List<Event> mapping) throws Exception {
		Set<Fact> md = Sets.newLinkedHashSet();
		Set<Fact> mi = Sets.newLinkedHashSet();

		for (Fact fact : pattern.getEvents()) {
			int factId = fact.getFactID();
			Event event = mapping.get(factId);

			if ((event.getKind() == EventKind.FIRST_DECLARATION)
					|| (event.getKind() == EventKind.SUPER_DECLARATION)) {
				md.add(fact);
			} else if (event.getKind() == EventKind.INVOCATION) {
				mi.add(fact);
			} else {
				throw new Exception("Undefined event type!");
			}
		}
		return Tuple.newTuple(md, mi);
	}

	private String getMappingPath() {
		String pathName = rootFolder.getAbsolutePath()
				+ "/200Repos/mapping.txt";
		return pathName;
	}
}
