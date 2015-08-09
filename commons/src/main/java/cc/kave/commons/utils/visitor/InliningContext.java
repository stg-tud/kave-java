package cc.kave.commons.utils.visitor;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.visitor.NameScopeVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class InliningContext {
	private int counter;
	private SST sst;
	private NameScopeVisitor visitor;
	private Scope scope;
	private Set<IMethodDeclaration> nonEntryPoints;
	private boolean inline = false;
	private boolean guardNeeded = false;

	public InliningContext() {
		this.nonEntryPoints = new HashSet<>();
		this.sst = new SST();
		this.inline = false;
		this.scope = null;
		this.visitor = new NameScopeVisitor();
		this.counter = 0;
	}

	public Set<IMethodDeclaration> getNonEntryPoints() {
		return nonEntryPoints;
	}

	public void setNonEntryPoints(Set<IMethodDeclaration> nonEntryPoints) {
		this.nonEntryPoints = nonEntryPoints;
	}

	public IMethodDeclaration getNonEntryPoint(MethodName methodName) {
		for (IMethodDeclaration method : this.nonEntryPoints)
			if (method.getName().equals(methodName))
				return method;
		return null;
	}

	public boolean isInline() {
		return inline;
	}

	public void setInline(boolean inline) {
		this.inline = inline;
	}

	public void enterScope(List<IStatement> body) {
		Set<IVariableReference> newNames = new LinkedHashSet<>();
		for (IStatement statement : body) {
			statement.accept(visitor, newNames);
		}
		Scope newScope = new Scope();
		if (scope != null) {
			newScope.existingIds.addAll(scope.existingIds);
			newScope.parent = scope;
			for (IVariableReference ref : newNames) {
				if (newScope.existingIds.contains(ref)) {
					IVariableReference newRef = generateNewRef(ref);
					newScope.changedNames.put(ref, newRef);
					newScope.existingIds.add(newRef);
				} else
					newScope.existingIds.add(ref);
			}
		} else
			newScope.existingIds.addAll(newNames);
		scope = newScope;
	}

	public void leaveScope() {
		if (scope.parent != null) {
			scope.parent.existingIds.addAll(scope.existingIds);
			scope.parent.body.addAll(scope.body);
			scope = scope.parent;
		}
	}

	public void enterBlock() {
		Scope newScope = new Scope();
		newScope.parent = scope;
		newScope.existingIds.addAll(scope.existingIds);
		newScope.changedNames = scope.changedNames;
		newScope.resultName = scope.resultName;
		scope = newScope;
	}

	public void leaveBlock(List<IStatement> body) {
		body.addAll(scope.body);
		scope = scope.parent;
	}

	private IVariableReference generateNewRef(IVariableReference reference) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier("$" + counter + "_" + reference.getIdentifier());
		counter++;
		return ref;
	}

	public IVariableReference resolveRef(IVariableReference ref) {
		return scope.resolve(ref);
	}

	public void addStatement(IStatement stmt) {
		scope.body.add(stmt);
	}

	public List<IStatement> getBody() {
		return scope.body;
	}

	public void addMethod(MethodDeclaration method) {
		sst.getMethods().add(method);
	}

	public void createSST(ISST oldSST) {
		sst.getDelegates().addAll(oldSST.getDelegates());
		sst.setEnclosingType(oldSST.getEnclosingType());
		sst.getEvents().addAll(oldSST.getEvents());
		sst.getFields().addAll(oldSST.getFields());
		sst.getProperties().addAll(oldSST.getProperties());
	}

	public ISST getSST() {
		return sst;
	}

	public String getResultName() {
		return scope.resultName;
	}

	public void setResultName(String name) {
		scope.resultName = name;
	}

	public void setGuardNeeded(boolean b) {
		this.guardNeeded = b;
	}

	public boolean isGuardNeeded() {
		return this.guardNeeded;
	}
}
