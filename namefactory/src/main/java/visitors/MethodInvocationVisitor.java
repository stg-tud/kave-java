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

package visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import namefactory.NodeFactory;

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
			if (!signature.endsWith(")") && ((MethodInvocation) m).getName().getIdentifier().equals(signature)) {
				return m;
			} else if (getMethodSignature((MethodInvocation) m).equals(signature)) {
				return m;
			}
		}
		return null;
	}

	private String getMethodSignature(MethodInvocation invocation) {
		StringBuilder sb = new StringBuilder();
		sb.append(invocation.getName().getIdentifier());
		sb.append("(");

		for (Object variable : invocation.arguments()) {
			
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
