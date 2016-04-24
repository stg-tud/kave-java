/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package exec.validate_evaluation.stats;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import cc.recommenders.assertions.Asserts;
import cc.recommenders.evaluation.data.Boxplot;
import cc.recommenders.evaluation.data.BoxplotData;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Usage;
import exec.validate_evaluation.queryhistory.QueryHistoryIo;

public class QueryHistoryStats {

	private QueryHistoryIo io;
	private Map<ICoReTypeName, Multimap<ICoReMethodName, Usage>> sortedHists;

	public QueryHistoryStats(QueryHistoryIo io) {
		this.io = io;
	}

	public void run() {

		BoxplotData uhLen = new BoxplotData();

		for (String zip : io.findQueryHistoryZips()) {

			System.out.println();
			System.out.printf("#####\n");
			System.out.printf("##### %s\n", zip);
			System.out.printf("#####\n");
			System.out.println();

			Collection<List<Usage>> hists = io.readQueryHistories(zip);
			System.out.printf("%d histories:\n", hists.size());

			sortedHists = Maps.newHashMap();

			for (List<Usage> hist : hists) {
				uhLen.add((double) hist.size());
				sort(hist);
			}

			print();
		}

		System.out.println();
		System.out.println();

		System.out.printf("--- overall stats ---\n");
		System.out.printf("uhLen stats: %s\n", uhLen.getBoxplot());
		System.out.println("percentiles for len(uh):");
		for (int percentile = 80; percentile < 101; percentile += 1) {
			double len = uhLen.getPercentil(percentile);
			System.out.printf("\t%d covered with a uh len of %.1f\n", percentile, len);
		}
		System.out.println();
		System.out.printf("numHistories: %d (%d collisions)\n", numLocations, collisions);
		Boxplot bp = avgHistLength.getBoxplot();
		System.out.printf("avgLength: %.1f usages %s\n", bp.getMean(), bp);
	}

	private int numLocations = 0;
	private BoxplotData avgHistLength = new BoxplotData();

	private void print() {

		for (ICoReTypeName t : sortedHists.keySet()) {
			System.out.println();
			System.out.printf("#### usages of '%s' ####\n", t);
			Multimap<ICoReMethodName, Usage> hists = sortedHists.get(t);
			for (ICoReMethodName m : hists.keySet()) {
				System.out.println();
				System.out.printf("in: %s\n", m);

				Collection<Usage> hist = hists.get(m);
				numLocations++;
				avgHistLength.add((double) hist.size());

				for (Usage usage : hist) {
					if (usage instanceof NoUsage) {
						System.out.printf("_, ");
					} else {
						System.out.printf("%d, ", usage.getReceiverCallsites().size());
					}
				}

				System.out.println();
			}
		}
	}

	private int collisions = 0;

	private void sort(List<Usage> hist) {

		Usage first = findFirstRealUsage(hist);
		ICoReTypeName type = first.getType();
		ICoReMethodName context = first.getMethodContext();

		Multimap<ICoReMethodName, Usage> hists = sortedHists.get(type);
		if (hists == null) {
			hists = LinkedHashMultimap.create();
			sortedHists.put(type, hists);
		}

		if (hists.containsKey(context)) {
			collisions++;
			System.out.flush();
			System.err.printf("collision for context '%s'\n", context);
			System.err.flush();
		}

		for (Usage u : hist) {
			if (!(u instanceof NoUsage)) {
				Asserts.assertTrue(type.equals(u.getType()));
				Asserts.assertTrue(context.equals(u.getMethodContext()));
			}
			hists.put(context, u);
		}
	}

	private Usage findFirstRealUsage(List<Usage> hist) {
		Iterator<Usage> it = hist.iterator();
		while (it.hasNext()) {
			Usage usage = it.next();
			if (!(usage instanceof NoUsage)) {
				return usage;
			}
		}
		throw new RuntimeException("at least one real usage should always exist!");
	}
}