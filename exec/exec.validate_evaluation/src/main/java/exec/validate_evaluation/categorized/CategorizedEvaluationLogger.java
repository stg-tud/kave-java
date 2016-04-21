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
package exec.validate_evaluation.categorized;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.recommenders.evaluation.data.Boxplot;
import cc.recommenders.names.ICoReTypeName;
import exec.csharp.queries.QueryMode;

public class CategorizedEvaluationLogger<Category> {

	private ICoReTypeName currentType;

	private boolean isFirstUser;

	public void type(ICoReTypeName type) {
		this.currentType = type;
		isFirstUser = true;
	}

	private boolean isFirstHistory;

	public void user(String user) {
		if (isFirstUser) {
			System.out.println();
			System.out.printf("### %s #############################\n", currentType);
			isFirstUser = false;
		}
		System.out.println();
		System.out.printf("[ %s ]\n", user);
		isFirstHistory = true;
	}

	public void history() {
		if (!isFirstHistory) {
			System.out.println("---");
		}
		isFirstHistory = false;
	}

	public void queryMode(QueryMode mode) {
		System.out.printf("%s: ", mode);
	}

	public void microCommit() {
		System.out.printf(".");
	}

	public void finishedMicroCommits() {
		System.out.println();
	}

	public void done(Map<QueryMode, List<CategorizedResults<Category>>> allRes) {
		System.out.println(new Date());
		System.out.println();

		Set<Category> categories = collectCategories(allRes.values());

		System.out.printf("mode");
		for (Category c : categories) {
			System.out.printf("\t%s", c);
		}
		System.out.println();

		Map<Category, Integer> counts = Maps.newHashMap();

		boolean shouldCount = true;
		for (QueryMode mode : allRes.keySet()) {
			List<CategorizedResults<Category>> byMode = allRes.get(mode);
			CategorizedResults<Category> mergedRes = CategorizedResults.merge(byMode);

			System.out.printf("%s", mode);
			for (Category c : categories) {
				Boxplot bp = mergedRes.get(c);
				System.out.printf("\t%.5f", bp.getMean());

				if (shouldCount) {
					int num = bp.getNumValues();
					Integer i = counts.get(c);
					if (i == null) {
						counts.put(c, num);
					} else {
						counts.put(c, i + num);
					}
				}
			}
			System.out.println();
			shouldCount = false;
		}

		System.out.printf("count");
		for (Category c : categories) {
			System.out.printf("\t%d", counts.get(c));
		}
		System.out.println();
	}

	private Set<Category> collectCategories(Collection<List<CategorizedResults<Category>>> values) {
		Set<Category> catgories = Sets.newLinkedHashSet();
		for (List<CategorizedResults<Category>> results : values) {
			for (CategorizedResults<Category> result : results) {
				catgories.addAll(result.getCategories());
			}
		}
		return catgories;
	}

	public static <T> CategorizedEvaluationLogger<T> create() {
		return new CategorizedEvaluationLogger<T>();
	}
}