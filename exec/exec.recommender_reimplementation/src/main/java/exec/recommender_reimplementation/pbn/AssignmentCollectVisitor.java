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
package exec.recommender_reimplementation.pbn;

import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class AssignmentCollectVisitor extends AbstractTraversingNodeVisitor<List<IAssignment>, Void> {

	@Override
	protected void visit(List<IStatement> body, List<IAssignment> context) {
		for (IStatement stmt : body) {
			if (stmt instanceof IAssignment) {
				context.add((IAssignment) stmt);
			}
			stmt.accept(this, context);
		}
	}
}
