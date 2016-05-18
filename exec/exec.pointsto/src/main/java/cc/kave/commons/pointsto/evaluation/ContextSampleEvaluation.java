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
package cc.kave.commons.pointsto.evaluation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.google.inject.Inject;

import cc.kave.commons.model.events.completionevents.Context;

public abstract class ContextSampleEvaluation extends AbstractEvaluation {

	private final ContextSampler sampler;

	@Inject
	public ContextSampleEvaluation(ContextSampler sampler) {
		this.sampler = sampler;
	}

	protected int getSampleSize() {
		return 8000;
	}

	protected List<Context> getSamples(Path contextsDir) throws IOException {
		return sampler.sample(contextsDir, getSampleSize());
	}

}
