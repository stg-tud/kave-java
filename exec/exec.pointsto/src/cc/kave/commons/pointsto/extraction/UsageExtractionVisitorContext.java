/**
 * Copyright 2015 Simon Reuß
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
package cc.kave.commons.pointsto.extraction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.Callpath;
import cc.kave.commons.pointsto.analysis.PointerAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.QueryContextKey;
import cc.kave.commons.pointsto.dummies.DummyUsage;

public class UsageExtractionVisitorContext {

	private PointerAnalysis pointerAnalysis;
	private TypeName enclosingClass;

	private Map<AbstractLocation, DummyUsage> locationUsages = new HashMap<>();

	private IStatement currentStatement;
	private Callpath currentCallpath;

	public UsageExtractionVisitorContext(PointsToContext context) {
		this.pointerAnalysis = context.getPointerAnalysis();
		this.enclosingClass = context.getTypeShape().getTypeHierarchy().getElement();
	}
	
	public void setEntryPoint(MethodName method) {
		currentCallpath = new Callpath(method);
	}
	
	public void setCurrentStatement(IStatement stmt) {
		this.currentStatement = stmt;
	}

	public void enterNonEntryPoint(MethodName method) {
		currentCallpath.enterMethod(method);
	}
	
	public void leaveNonEntryPoint() {
		currentCallpath.leaveMethod();
	}

	private DummyUsage initializeUsage() {
		DummyUsage usage = new DummyUsage();

		usage.setClassContext(enclosingClass);
		usage.setMethodContext(currentCallpath.getFirst());

		return usage;
	}

	public void useReference(IReference reference) {
		QueryContextKey queryKey = new QueryContextKey(reference, currentStatement, null, currentCallpath);
		Set<AbstractLocation> locations = pointerAnalysis.query(queryKey);

		// ensure that there is a usage for each location
		for (AbstractLocation location : locations) {
			if (!locationUsages.containsKey(location)) {
				locationUsages.put(location, initializeUsage());
			}
		}

	}

}
