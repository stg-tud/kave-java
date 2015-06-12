package namefactory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

public class InformationVisitor extends ASTVisitor {
	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	private List<PackageDeclaration> packages = new ArrayList<PackageDeclaration>();
	private List<ImportDeclaration> imports = new ArrayList<ImportDeclaration>();

	@Override
	public boolean visit(FieldDeclaration node) {
		fields.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		packages.add(node);
		return super.visit(node);
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		imports.add(node);
		return super.visit(node);
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	public List<FieldDeclaration> getFields() {
		return fields;
	}

	public List<PackageDeclaration> getPackages() {
		return packages;
	}

	public List<ImportDeclaration> getImports() {
		return imports;
	}
}