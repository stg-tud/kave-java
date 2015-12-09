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
package cc.kave.commons.pointsto.extraction;

import java.util.Collections;
import java.util.List;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.pointsto.dummies.DummyUsage;

public class PointsToUsageExtractor {

	private UsageStatisticsCollector collector;

	public PointsToUsageExtractor() {
		this.collector = new UsageStatisticsCollector() {

			@Override
			public void onUsageExtracted(DummyUsage usage) {

			}

			@Override
			public void onProcessContext(Context context) {

			}

			@Override
			public void merge(UsageStatisticsCollector other) {

			}
		};
	}

	public PointsToUsageExtractor(UsageStatisticsCollector collector) {
		this.collector = collector;
	}

	public UsageStatisticsCollector getStatisticsCollector() {
		return collector;
	}

	public List<DummyUsage> extract(Context context) {
		collector.onProcessContext(context);

		return Collections.emptyList();
	}
}
