/**
 * Copyright 2015 Waldemar Graf
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

package cc.kave.eclipse.namefactory.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import cc.kave.eclipse.namefactory.NodeFactory;

public class VariableDeclarationVisitor extends ASTVisitor {

	private List<ASTNode> variables = new ArrayList<ASTNode>();

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		variables.add(node);
		NodeFactory.createNodeName(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		variables.add(node);
		NodeFactory.createNodeName(node);
		return super.visit(node);
	}

	/**
	 * 
	 * @return A list of VariableDeclarationStatement,
	 *         VariableDeclarationExpression and SingleVariableDeclaration
	 *         nodes.
	 */
	public List<ASTNode> getVariables() {
		return variables;
	}

	public ASTNode getVariable(String name) {
		for (ASTNode variable : variables) {
			if (variable instanceof VariableDeclarationFragment && !(variable.getParent() instanceof FieldDeclaration)) {
				if (name.equals(((VariableDeclarationFragment) variable).getName().getIdentifier())
						|| name.endsWith(((VariableDeclarationFragment) variable).getName().getIdentifier())) {
					return variable;
				}
			} else if (variable instanceof SingleVariableDeclaration) {
				if (((SingleVariableDeclaration) variable).getName().getIdentifier().equals(name))
					return variable;
			}
		}
		return null;
	}

	public ASTNode getField(String name) {
		for (ASTNode variable : variables) {
			if (variable instanceof VariableDeclarationFragment && variable.getParent() instanceof FieldDeclaration) {
				if (name.equals(((VariableDeclarationFragment) variable).getName().getIdentifier())
						|| name.endsWith(((VariableDeclarationFragment) variable).getName().getIdentifier())) {
					return variable;
				}
			}
		}
		return null;
	}
}
