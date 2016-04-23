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

import cc.recommenders.assertions.Asserts;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Usage;
import exec.csharp.evaluation.impl.F1ByCategory.QueryContent;
import exec.validate_evaluation.microcommits.MicroCommit;

public class QueryJudge {

	private final Usage start;
	private final Usage end;

	private NoiseMode noiseMode;
	private int numAdditions;
	private int numRemovals;

	public QueryJudge(MicroCommit mc) {
		this(mc.getStart(), mc.getEnd());
	}

	public QueryJudge(Usage start, Usage end) {
		this.start = start;
		this.end = end;

		numAdditions = QueryUtils.countAdditions(start, end);
		numRemovals = QueryUtils.countRemovals(start, end);

		noiseMode = calcNoiseMode();
	}

	private NoiseMode calcNoiseMode() {
		if (start instanceof NoUsage) {
			return NoiseMode.NO_NOISE;
		}

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
		if (start instanceof NoUsage || end instanceof NoUsage) {
			return true;
		}
		return !start.getDefinitionSite().equals(end.getDefinitionSite());
	}

	public QueryContent getQueryContentCategorization() {
		Asserts.assertFalse(end instanceof NoUsage);

		if (start instanceof NoUsage) {
			return QueryContent.ZERO;
		}

		int numStart = start.getReceiverCallsites().size();
		int numAdded = QueryUtils.countAdditions(start, end);
		int numRemoved = QueryUtils.countRemovals(start, end);
		int numStartWithoutNoise = numStart - numRemoved;
		Asserts.assertGreaterOrEqual(numStartWithoutNoise, 0);
		int numEnd = end.getReceiverCallsites().size();
		Asserts.assertEquals(numStartWithoutNoise + numAdded, numEnd);

		if (numStartWithoutNoise == 0) {
			return QueryContent.ZERO;
		}
		if (numStartWithoutNoise == 1 && numEnd == 2) {
			return QueryContent.NM;
		}
		if (numStartWithoutNoise == numEnd - 1) {
			return QueryContent.MINUS1;
		}
		return QueryContent.NM;
	}
}