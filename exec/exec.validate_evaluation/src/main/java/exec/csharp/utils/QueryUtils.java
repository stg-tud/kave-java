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

import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.Usage;

public class QueryUtils {

	public static String toDiffString(Usage a, Usage b) {
		StringBuilder sb = new StringBuilder();
		sb.append(a.getReceiverCallsites().size());
		int numRemovals = countRemovals(a, b);
		if (numRemovals > 0) {
			sb.append('-').append(numRemovals);
		}
		int numAdditions = countAdditions(a, b);
		if (numAdditions > 0) {
			sb.append('+').append(numAdditions);
		}
		boolean hasSameType = a.getType().equals(b.getType());
		boolean hasSameClassContext = a.getClassContext().equals(b.getClassContext());
		boolean hasSameMethodContext = a.getMethodContext().equals(b.getMethodContext());
		boolean hasSameDefinition = a.getDefinitionSite().equals(b.getDefinitionSite());

		if (!(hasSameType && hasSameClassContext && hasSameMethodContext && hasSameDefinition)) {
			sb.append('~');
		}
		if (!hasSameType) {
			sb.append('T');
		}
		if (!hasSameClassContext) {
			sb.append('C');
		}
		if (!hasSameMethodContext) {
			sb.append('M');
		}
		if (!hasSameDefinition) {
			sb.append('D');
		}

		return sb.toString();
	}

	public static int countAdditions(Usage a, Usage b) {
		int additions = 0;
		for (CallSite cs : b.getReceiverCallsites()) {
			if (!a.getReceiverCallsites().contains(cs)) {
				additions++;
			}
		}
		return additions;
	}

	public static int countRemovals(Usage a, Usage b) {
		int removals = 0;
		for (CallSite cs : a.getReceiverCallsites()) {
			if (!b.getReceiverCallsites().contains(cs)) {
				removals++;
			}
		}
		return removals;
	}
}