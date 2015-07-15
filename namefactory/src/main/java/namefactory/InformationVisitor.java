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

package namefactory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class InformationVisitor extends ASTVisitor {
	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	private PackageDeclaration packages;
	private List<ImportDeclaration> imports = new ArrayList<ImportDeclaration>();

	@Override
	public boolean visit(FieldDeclaration node) {
		fields.add(node);
		NodeFactory.getNodeName(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		NodeFactory.getNodeName(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		packages = node;
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		NodeFactory.getNodeName(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		imports.add(node);
		NodeFactory.getNodeName(node);
		return super.visit(node);
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	public List<FieldDeclaration> getFields() {
		return fields;
	}

	public PackageDeclaration getPackages() {
		return packages;
	}

	public List<ImportDeclaration> getImports() {
		return imports;
	}
}