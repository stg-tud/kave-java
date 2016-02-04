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

import static cc.recommenders.evaluation.optimization.Vector.extract;
import static cc.recommenders.evaluation.optimization.Vector.v;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.recommenders.evaluation.optimization.BoundsMatcher;
import cc.recommenders.evaluation.optimization.EvaluationOptions;
import cc.recommenders.evaluation.optimization.Vector;

@Ignore
public class RasterAroundCandidateSelectorTest {
	private RasterAroundCandidateSelector uut;

	@Before
	public void beforeEach() {
		uut = new RasterAroundCandidateSelector(new Rasterizer(),
				new BoundsMatcher());
	}

	@Test
	public void testTwoDim() {
		Vector stepSize = v(0.1, 0.1);
		Set<EvaluationOptions> result = uut.selectNextCandidates(stepSize,
				extract(v(0.5, 0.5)));
		Vector[] expectedVectors = { v(0.4, 0.4), v(0.5, 0.4), v(0.6, 0.4),
				v(0.4, 0.5), v(0.6, 0.5), v(0.4, 0.6), v(0.5, 0.6), v(0.6, 0.6) };
		Set<EvaluationOptions> expected = extract(expectedVectors);
		assertThat(result, equalTo(expected));
	}

	@Test
	public void testFilterLowerOutOfBounds() {
		Vector stepSize = v(0.2, 0.2);
		Set<EvaluationOptions> result = uut.selectNextCandidates(stepSize,
				extract(v(0.1, 0.1)));
		Vector[] expectedVectors = { v(0.3, 0.1), v(0.1, 0.3), v(0.3, 0.3) };
		Set<EvaluationOptions> expected = extract(expectedVectors);
		assertThat(result, equalTo(expected));
	}

	@Test
	public void testFilterUpperOutOfBounds() {
		Vector stepSize = v(0.2, 0.2);
		Set<EvaluationOptions> result = uut.selectNextCandidates(stepSize,
				extract(v(0.9, 0.9)));
		Vector[] expectedVectors = { v(0.7, 0.7), v(0.7, 0.9), v(0.9, 0.7) };
		Set<EvaluationOptions> expected = extract(expectedVectors);
		assertThat(result, equalTo(expected));
	}

	@Test
	public void testTwoDimMultiple() {
		Vector stepSize = v(0.01, 0.01);
		Set<EvaluationOptions> result = uut.selectNextCandidates(stepSize,
				extract(v(0.1, 0.1)), extract(v(0.9, 0.9)));
		Vector[] expectedVectors = { v(0.09, 0.09), v(0.1, 0.09), v(0.11, 0.09),
				v(0.09, 0.1), v(0.11, 0.1),
				v(0.09, 0.11), v(0.1, 0.11), v(0.11, 0.11),
				v(0.89, 0.89), v(0.9, 0.89), v(0.91, 0.89),
				v(0.89, 0.9), v(0.91, 0.9),
				v(0.89, 0.91), v(0.9, 0.91), v(0.91, 0.91)};
		Set<EvaluationOptions> expected = extract(expectedVectors);
		assertThat(result, equalTo(expected));
	}
}
