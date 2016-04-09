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
package exec.validate_evaluation.queryhistory;

import java.util.Iterator;
import java.util.List;

import cc.recommenders.usages.Usage;

public class QueryHistoryFixer {

	public void fix(List<Usage> qh, RemovalCallback onRemove) {
		Usage last = null;
		int diff = 0;

		for (Iterator<Usage> it = qh.iterator(); it.hasNext();) {
			Usage u = it.next();

			if (u.equals(last)) {
				it.remove();
				diff++;
			}

			last = u;
		}

		if (diff > 0) {
			onRemove.action(diff);
		}
	}

	public static interface RemovalCallback {
		public void action(int diff);
	}
}