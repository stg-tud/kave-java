/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.evaluation.cv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.pointsto.stores.ProjectIdentifier;
import cc.recommenders.usages.Usage;

public class ProjectTrainValidateSetProvider extends AbstractSetProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectTrainValidateSetProvider.class);

	private final List<List<ProjectIdentifier>> projectFolds;
	private final Map<ProjectIdentifier, List<Usage>> trainingUsages;
	private final Map<ProjectIdentifier, List<Usage>> validationUsages;

	public ProjectTrainValidateSetProvider(List<List<ProjectIdentifier>> projectFolds,
			Map<ProjectIdentifier, List<Usage>> trainingUsages, Map<ProjectIdentifier, List<Usage>> validationUsages) {
		super(projectFolds.size());
		this.projectFolds = projectFolds;
		this.trainingUsages = trainingUsages;
		this.validationUsages = validationUsages;
	}

	@Override
	public List<Usage> getTrainingSet(int validationFoldIndex) {
		List<Usage> usages = new ArrayList<>();
		for (int i = 0; i < projectFolds.size(); ++i) {
			if (i == validationFoldIndex) {
				continue;
			}

			for (ProjectIdentifier project : projectFolds.get(i)) {
				List<Usage> projectUsages = trainingUsages.get(project);
				if (projectUsages != null) {
					usages.addAll(projectUsages);
				} else {
					LOGGER.warn("Project {} does not have any usages", project.toString());
				}
			}
		}

		updateAvgTrainingFoldSize(usages.size());
		return usages;
	}

	@Override
	public List<Usage> getValidationSet(int validationFoldIndex) {
		List<Usage> usages = new ArrayList<>();
		for (ProjectIdentifier project : projectFolds.get(validationFoldIndex)) {
			List<Usage> projectUsages = validationUsages.get(project);
			if (projectUsages != null) {
				usages.addAll(validationUsages.get(project));
			} else {
				LOGGER.warn("Project {} does not have any usages", project.toString());
			}
		}

		updateAvgValidationFoldSize(usages.size());
		return usages;
	}
}
