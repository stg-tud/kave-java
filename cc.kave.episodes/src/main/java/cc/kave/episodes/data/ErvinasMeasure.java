/**
 * Copyright (c) 2010-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package cc.kave.episodes.data;

import static cc.recommenders.assertions.Asserts.assertLessOrEqual;
import static cc.recommenders.assertions.Asserts.assertNotNegative;
import static com.google.common.collect.Sets.newLinkedHashSet;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

public class ErvinasMeasure {

	private double precision = 0.0;
	private double recall = 0.0;

	private DecimalFormat df = new DecimalFormat("#.###");

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getF1() {
		return getF(1);
	}

	public double getF(double beta) {
		assertNotNegative(beta);
		assertLessOrEqual(beta, 1.0);

		if (isPrecisionOrRecallSet()) {
			return Double
					.valueOf(df.format((1.0 + beta * beta) * precision * recall / (beta * beta * precision + recall)));
		} else {
			return 0.0;
		}

	}

	private boolean isPrecisionOrRecallSet() {
		return precision != 0.0 || recall != 0.0;
	}

	public static <T> ErvinasMeasure newMeasure(Set<T> expected, Set<T> proposed) {

		double numExpected = expected.size();
		double numProposed = proposed.size();

		double numHits = 0;
		for (Object o : proposed) {
			if (expected.contains(o)) {
				numHits++;
			}
		}

		ErvinasMeasure m = new ErvinasMeasure();
		m.precision = saveDivision(numHits, numProposed);
		m.recall = saveDivision(numHits, numExpected);

		return m;
	}

	public static <T> ErvinasMeasure new2Measure(List<T> expected, List<T> proposed) {

		double numExpected = expected.size();
		double numProposed = proposed.size();

		double numHits = 0;
		for (Object o : proposed) {
			if (expected.contains(o)) {
				numHits++;
			}
		}

		ErvinasMeasure m = new ErvinasMeasure();
		m.precision = saveDivision(numHits, numProposed);
		m.recall = saveDivision(numHits, numExpected);

		return m;
	}

	private static double saveDivision(double numerator, double denominator) {
		if (denominator != 0.0) {
			return numerator / denominator;
		} else {
			return 1.0;
		}
	}

	public static <T> Set<T> dropAfterTotalRecall(Set<T> expected, Set<T> proposed) {
		Set<T> undropped = newLinkedHashSet();

		int numFound = 0;

		for (T m : proposed) {
			if (numFound < expected.size()) {
				undropped.add(m);

				if (expected.contains(m)) {
					numFound++;
				}
			}
		}

		return undropped;
	}
}