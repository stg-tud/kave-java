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
package cc.kave.commons.model.ssts.impl.transformation.constants;

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;

public class InlineConstantContext {
	private ConstantCollectorVisitor collector;
	private SimpleExpressionVisitor exprVisitor;
	private Set<IFieldDeclaration> constants;

	public InlineConstantContext() {
		this.collector = new ConstantCollectorVisitor();
		this.exprVisitor = new SimpleExpressionVisitor();
		this.constants = new HashSet<IFieldDeclaration>();
	}

	public void collectConstants(ISST sst) {
		constants.addAll(sst.accept(collector, new HashSet<IFieldDeclaration>()));
	}

	public Set<IFieldDeclaration> getConstants() {
		return constants;
	}

	public boolean isConstant(IFieldName field) {
		for (IFieldDeclaration constant : constants) {
			if (constant.getName().equals(field))
				return true;
		}
		return false;
	}

	public SimpleExpressionVisitor getExprVisitor() {
		return exprVisitor;
	}

}
