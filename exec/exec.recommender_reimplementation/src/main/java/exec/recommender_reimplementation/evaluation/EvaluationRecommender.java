/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.evaluation;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import exec.recommender_reimplementation.java_transformation.JavaTransformationVisitor;

public abstract class EvaluationRecommender {

	protected List<MeasureCalculator> measures;

	protected boolean loggingActive;

	public abstract String getName();

	public abstract void analysis(List<Context> contextList);

	public abstract void initalizeRecommender();
	
	public void initalizeMeasures(List<MeasureCalculator> measures) {
		this.measures = measures;
	}

	public void calculateMeasures(Set<Tuple<ICoReMethodName, Double>> proposals, ICoReMethodName expectedMethod) {
		for (MeasureCalculator measure : measures) {
			measure.addValue(expectedMethod, proposals);
		}
	}

	public abstract Set<Tuple<ICoReMethodName, Double>> handleQuery(QueryContext query);

	public abstract boolean supportsAnalysis();

	public String getEvaluationResults() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName()).append(":").append(System.lineSeparator());
		for (MeasureCalculator measureCalculator : measures) {
			sb.append(measureCalculator.getName()).append("		").append(measureCalculator.getMean());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	protected void transformContext(Context context) {
		JavaTransformationVisitor transformationVisitor = new JavaTransformationVisitor(context.getSST());
		context.setSST(transformationVisitor.transform(context.getSST(), ISST.class));
	}

	public void setLogging(boolean value) {
		loggingActive = value;
	}

	public abstract String returnLog();

}