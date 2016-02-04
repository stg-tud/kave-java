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
package cc.kave.commons.model.groum;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;

public class GroumFactory {
	public List<Groum> extractGroums(ISST sst) {
		Set<IMethodDeclaration> methods = sst.getMethods();
		List<Groum> groums = new LinkedList<>();

		for (IMethodDeclaration method : methods) {
			extractGroum(method);
		}
		return groums;
	}

	private void extractGroum(IMethodDeclaration method) {
		// TODO

	}

}
