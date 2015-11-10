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

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import cc.kave.eclipse.namefactory.NodeFactory;

public class MethodDeclarationVisitor extends ASTVisitor {
	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

	@Override
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		NodeFactory.createNodeName(node);
		return super.visit(node);
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	public MethodDeclaration getMethod(String signature) {
		for (MethodDeclaration m : methods) {
			if (!signature.endsWith(")") && m.getName().getIdentifier().equals(signature)) {
				return m;
			} else if (getMethodSignature(m).equals(signature)) {
				return m;
			}
		}
		return null;
	}

	private String getMethodSignature(MethodDeclaration declaration) {
		StringBuilder sb = new StringBuilder();
		sb.append(declaration.getName().getIdentifier());
		sb.append("(");

		for (Object variable : declaration.parameters()) {
			SingleVariableDeclaration v = (SingleVariableDeclaration) variable;
			sb.append(v.resolveBinding().getType().getName());
			sb.append(" ");
			sb.append(v.getName().getIdentifier());
			sb.append(", ");
		}

		if (sb.toString().endsWith(", ")) {
			sb.replace(sb.length() - 2, sb.length(), ")");
		} else {
			sb.append(")");
		}

		return sb.toString();
	}
}
