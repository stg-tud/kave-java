/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.statistics;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.recommenders.usages.Usage;

public interface UsageStatisticsCollector {

	/**
	 * Creates a new {@link UsageStatisticsCollector} instance of the implementing class.
	 */
	UsageStatisticsCollector create();

	/**
	 * Merges the state of the given {@link UsageStatisticsCollector} into this instance. Only guaranteed to work if
	 * this and the other instance are of the same class.
	 */
	void merge(UsageStatisticsCollector other);

	void onProcessContext(Context context);

	void onEntryPointUsagesExtracted(IMethodDeclaration entryPoint, List<? extends Usage> usages);

	void process(List<? extends Usage> usages);

	void onUsagesPruned(int numPrunedUsages);

	void output(Path file) throws IOException;

}
