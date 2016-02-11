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
package cc.kave.commons.pointsto.analysis;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.pointsto.analysis.methods.EnclosingMethodCollector;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;

/**
 * A convenience class for building {@link PointsToQuery} objects for a {@link PointsToAnalysis}.
 * 
 * Uses the provided arguments and its internal information about the associated {@link Context} to create a complete
 * {@link PointsToQuery}.
 */
public class PointsToQueryBuilder {

	private TypeCollector typeCollector;
	private EnclosingMethodCollector methodCollector;

	/**
	 * Constructs a new {@link PointsToQueryBuilder} using existing collector instances.
	 */
	public PointsToQueryBuilder(TypeCollector typeCollector, EnclosingMethodCollector methodCollector) {
		this.typeCollector = typeCollector;
		this.methodCollector = methodCollector;
	}

	public PointsToQueryBuilder(Context context) {
		typeCollector = new TypeCollector(context);
		methodCollector = new EnclosingMethodCollector(context.getSST());
	}

	/**
	 * Creates a new {@link PointsToQuery} for a {@link PointsToAnalysis} using the supplied {@link IReference} and
	 * {@link IStatement}. The {@link ITypeName} and the potentially surrounding method is inferred using stored
	 * information about the {@link ISST}. If the statement has an enclosing method, a {@link Callpath} is created from
	 * it.
	 */
	public PointsToQuery newQuery(IReference reference, IStatement stmt) {
		ITypeName type = typeCollector.getType(reference);

		IMethodName method = methodCollector.getMethod(stmt);
		Callpath callpath = null;
		if (method != null) {
			callpath = new Callpath(method);
		}

		return new PointsToQuery(reference, stmt, type, callpath);
	}

	/**
	 * Creates a new {@link PointsToQuery} for a {@link PointsToAnalysis} using the supplied {@link IReference},
	 * {@link IStatement} and {@link Callpath}. The {@link ITypeName} is inferred using stored information about the
	 * {@link ISST}.
	 */
	public PointsToQuery newQuery(IReference reference, IStatement stmt, Callpath callpath) {
		ITypeName type = typeCollector.getType(reference);
		return new PointsToQuery(reference, stmt, type, callpath);
	}

}
