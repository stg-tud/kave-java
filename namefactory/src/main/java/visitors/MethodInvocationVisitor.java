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

public class MethodInvocationVisitor extends ASTVisitor {
	private List<MethodInvocation> methods = new ArrayList<MethodInvocation>();

	@Override
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return super.visit(node);
	}

	public List<MethodInvocation> getMethods() {
		return methods;
	}

	public MethodInvocation getMethod(String signature) {
		for (MethodInvocation m : methods) {
			if (!signature.endsWith(")") && m.getName().getIdentifier().equals(signature)) {
				return m;
			} else if (getMethodSignature(m).equals(signature)) {
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
