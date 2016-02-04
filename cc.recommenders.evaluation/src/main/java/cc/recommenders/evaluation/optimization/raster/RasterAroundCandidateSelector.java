/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.evaluation.optimization.raster;

import static cc.recommenders.evaluation.optimization.Vector.add;
import static cc.recommenders.evaluation.optimization.Vector.extract;
import static cc.recommenders.evaluation.optimization.Vector.sub;
import static cc.recommenders.evaluation.optimization.Vector.v;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import cc.recommenders.evaluation.optimization.CandidateSelector;
import cc.recommenders.evaluation.optimization.EvaluationOptions;
import cc.recommenders.evaluation.optimization.Vector;
import cc.recommenders.evaluation.optimization.BoundsMatcher;

public class RasterAroundCandidateSelector implements CandidateSelector {

	private Rasterizer rasterizer;
	private BoundsMatcher boundsMatcher;

	@Inject
	public RasterAroundCandidateSelector(Rasterizer rasterizer,
			BoundsMatcher boundsMatcher) {
		this.rasterizer = rasterizer;
		this.boundsMatcher = boundsMatcher;
	}

	@Override
	public Set<EvaluationOptions> selectNextCandidates(Vector stepSize,
			EvaluationOptions... evaluationOptions) {
		Set<Vector> resultVectors = new HashSet<Vector>();
		for (EvaluationOptions opts : evaluationOptions) {
			Vector[] stepped = step(v(opts), stepSize);
			Set<Vector> rasterized = rasterizer.rasterize(stepped);
			rasterized.remove(v(opts));
			resultVectors.addAll(rasterized);
		}
		return extract(filterOutOfBounds(resultVectors));
	}

	private Set<Vector> filterOutOfBounds(Set<Vector> toFilter) {
		Set<Vector> filtered = new HashSet<Vector>();
		for (Vector v: toFilter) {
			if (boundsMatcher.matches(v)) {
				filtered.add(v);
			}
		}
		return filtered;
	}

	@Override
	public Set<EvaluationOptions> selectNextCandidates(Vector stepSize,
			Collection<EvaluationOptions> evaluationOptions) {
		EvaluationOptions[] a = new EvaluationOptions[evaluationOptions.size()];
		return selectNextCandidates(stepSize, evaluationOptions.toArray(a));
	}

	private Vector[] step(Vector origin, Vector stepSize) {
		Vector[] result = new Vector[3];
		result[0] = origin;
		result[1] = sub(origin, stepSize);
		result[2] = add(origin, stepSize);
		return result;
	}
}
