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

package cc.kave.rs.commons.analysis.transformer;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MemberName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.eclipse.namefactory.NodeFactory;

public class DeclarationVisitor extends ASTVisitor {

	private SST context;

	public DeclarationVisitor(SST context) {
		this.context = context;
	}

	@Override
	public boolean visit(FieldDeclaration node) {

		if (node != null) {
			List<VariableDeclarationFragment> fragments = node.fragments();

			for (VariableDeclarationFragment fragment : fragments) {

				FieldName name = (FieldName) NodeFactory.createNodeName(fragment);

				if (!isNestedDeclaration(name, context)) {
					// TODO: return super.visit(node);
					cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration fieldDeclaration = new cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration();
					fieldDeclaration.setName(name);

					context.getFields().add(fieldDeclaration);
				}

			}
		}

		return super.visit(node);
	}

	private static boolean isNestedDeclaration(DelegateTypeName name, SST context) {
		return !name.getDeclaringType().equals(context.getEnclosingType());
	}

	private static boolean isNestedDeclaration(MemberName name, SST context) {
		return !name.getDeclaringType().equals(context.getEnclosingType());
	}

}
