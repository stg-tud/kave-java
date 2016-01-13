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
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import cc.kave.eclipse.namefactory.NodeFactory;

public class MethodInvocationVisitor extends ASTVisitor {
	private List<Expression> methods = new ArrayList<Expression>();

	@Override
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		NodeFactory.createNodeName(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		methods.add(node);
		NodeFactory.createNodeName(node);
		return super.visit(node);
	}

	public List<Expression> getMethods() {
		return methods;
	}

	public Expression getMethod(String signature) {
		for (Expression m : methods) {
			if (m instanceof MethodInvocation) {
				if (!signature.endsWith(")") && ((MethodInvocation) m).getName().getIdentifier().equals(signature)) {
					return m;
				} else if (getMethodSignature(m).equals(signature)) {
					return m;
				}
			} else if (m instanceof SuperMethodInvocation) {
				if (!signature.endsWith(")")
						&& ((SuperMethodInvocation) m).getName().getIdentifier().equals(signature)) {
					return m;
				} else if (getMethodSignature((SuperMethodInvocation) m).equals(signature)) {
					return m;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private String getMethodSignature(Expression invocation) {
		StringBuilder sb = new StringBuilder();

		List<Expression> arguments = new ArrayList<Expression>();

		if (invocation instanceof MethodInvocation) {
			sb.append(((MethodInvocation) invocation).getName().getIdentifier());
			arguments = ((MethodInvocation) invocation).arguments();
		} else if (invocation instanceof SuperMethodInvocation) {
			sb.append(((SuperMethodInvocation) invocation).getName().getIdentifier());
			arguments = ((SuperMethodInvocation) invocation).arguments();
		}
		sb.append("(");

		for (Object variable : arguments) {

			Expression v = (Expression) variable;
			v.resolveTypeBinding().getName();

			sb.append(v.resolveTypeBinding().getName());
			sb.append(" ?, ");
		}

		if (sb.toString().endsWith(", ")) {
			sb.replace(sb.length() - 2, sb.length(), ")");
		} else {
			sb.append(")");
		}

		return sb.toString();
	}
}
