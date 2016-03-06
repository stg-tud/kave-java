/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.csharp.statistics;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

import cc.recommenders.io.NestedZipFolders;
import cc.recommenders.names.ITypeName;
import cc.recommenders.usages.Usage;
import exec.csharp.utils.MapSorter;
import exec.csharp.utils.MicroCommit;
import exec.csharp.utils.StorageCase;
import exec.csharp.utils.StorageHelper;

public class UsageToMicroCommitRatioCalculator {

	private final NestedZipFolders<ITypeName> dirMicroCommits;
	private final NestedZipFolders<ITypeName> dirUsages;

	@Inject
	public UsageToMicroCommitRatioCalculator(StorageHelper storageHelper) {
		dirMicroCommits = storageHelper.getNestedZipFolder(StorageCase.MICRO_COMMITS);
		dirUsages = storageHelper.getNestedZipFolder(StorageCase.USAGES);
	}

	public void run() throws IOException {

		Map<String, Double> usageToHistoryRatio = Maps.newLinkedHashMap();

		int numTypesTotal = 0;
		int numTuplesTotal = 0;
		int numUsagesTotal = 0;

		int numTypesDATEV = 0;
		int numTuplesDATEV = 0;
		int numUsagesDATEV = 0;

		int numTypesWith = 0;
		int numTuplesWith = 0;
		int numUsagesWith = 0;
		int numTypesWithout = 0;
		int numTuplesWithout = 0;
		int numUsagesWithout = 0;

		Set<ITypeName> keys = dirMicroCommits.findKeys();
		for (ITypeName t : keys) {
			System.out.printf("reading %s... ", t);

			List<MicroCommit> histories = dirMicroCommits.readAllZips(t, MicroCommit.class);
			List<Usage> usages = dirUsages.readAllZips(t, Usage.class);

			int numTuples = histories.size();
			int numUsages = usages.size();
			System.out.printf("%d tuples, %d usages\n", numTuples, numUsages);

			// if (numUsages > 0 && !isDatev(t)) {
			if (!isDatev(t)) {
				double ratio = (0.000001 + numUsages) / (1.0 * numTuples);
				String key = String.format("%s (%d/%d)", t, numUsages, numTuples);
				usageToHistoryRatio.put(key, ratio);
			}

			numTypesTotal++;
			numTuplesTotal += numTuples;
			numUsagesTotal += numUsages;

			if (numTuples > 0 && numUsages > 0) {
				numTypesWith++;
				numTuplesWith += numTuples;
				numUsagesWith += numUsages;
			} else {
				numTypesWithout++;
				numTuplesWithout += numTuples;
				numUsagesWithout += numUsages;

				if (isDatev(t)) {
					numTypesDATEV++;
					numTuplesDATEV += numTuples;
					numUsagesDATEV += numUsages;
				}
			}
		}

		System.out.printf("\n\nsummary:\n");
		System.out.printf("we have a total of %d start/end tuples and %d usages for %d different types\n",
				numTuplesTotal, numUsagesTotal, numTypesTotal);
		System.out.printf("currently, we have both tuples and usages for %d types (%d queries, %d usages)\n",
				numTypesWith, numTuplesWith, numUsagesWith);
		System.out.printf("we have tuples, but no usages for %d types (%d queries, %d usages)\n", numTypesWithout,
				numTuplesWithout, numUsagesWithout);
		System.out.printf("out of these, %d types (%d queries, %d usages) are related to DATEV\n", numTypesDATEV,
				numTuplesDATEV, numUsagesDATEV);

		System.out.printf("\n\nratios (usages/histories):\n");
		Map<String, Double> sortedRatios = MapSorter.sort(usageToHistoryRatio);
		for (String key : sortedRatios.keySet()) {
			double ratio = sortedRatios.get(key);
			System.out.printf("%3.2f - %s\n", ratio, key);
		}

	}

	private static boolean isDatev(ITypeName t) {
		return StringUtils.containsIgnoreCase(t.toString(), "datev");
	}
}