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
package exec.csharp.utils;

import cc.recommenders.usages.Query;

public class QueryJudge {

	private final Query start;
	private final Query end;

	private NoiseMode noiseMode;
	private int numAdditions;
	private int numRemovals;

	public QueryJudge(Query start, Query end) {
		this.start = start;
		this.end = end;

		numAdditions = QueryUtils.countAdditions(start, end);
		numRemovals = QueryUtils.countRemovals(start, end);

		noiseMode = calcNoiseMode();
	}

	private NoiseMode calcNoiseMode() {
		if (hasRemovals() && hasDefChange()) {
			if (hasAdditions()) {
				return NoiseMode.DEF_AND_REMOVAL;
			} else {
				return NoiseMode.PURE_REMOVAL;
			}
		}

		if (hasRemovals()) {
			return NoiseMode.REMOVAL;
		}

		if (hasDefChange()) {
			return NoiseMode.DEF;
		}

		return NoiseMode.NO_NOISE;
	}

	public NoiseMode getNoiseMode() {
		return noiseMode;
	}

	public boolean hasAdditions() {
		return getNumAdditions() > 0;
	}

	public int getNumAdditions() {
		return numAdditions;
	}

	public int getNumRemovals() {
		return numRemovals;
	}

	public boolean hasRemovals() {
		return getNumRemovals() > 0;
	}

	public boolean hasDefChange() {
		return !start.getDefinitionSite().equals(end.getDefinitionSite());
	}
}