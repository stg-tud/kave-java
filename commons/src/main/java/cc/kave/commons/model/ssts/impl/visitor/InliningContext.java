package cc.kave.commons.model.ssts.impl.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class InliningContext {

	private List<IStatement> before;
	private List<IStatement> currentBlock;
	private List<IStatement> newMethodBody;
	private Set<IVariableReference> names;
	private Map<IVariableReference, IVariableReference> changedNames;
	private Set<IMethodDeclaration> nonEntryPoints;
	private boolean inline = false;

	public InliningContext() {
		this.before = new ArrayList<>();
		this.currentBlock = new ArrayList<>();
		this.newMethodBody = new ArrayList<>();
		this.names = new HashSet<>();
		this.nonEntryPoints = new HashSet<>();
		this.changedNames = new HashMap<>();
		this.inline = false;
	}

	public List<IStatement> getBefore() {
		return before;
	}

	public void setBefore(List<IStatement> before) {
		this.before = before;
	}

	public List<IStatement> getCurrentBlock() {
		return currentBlock;
	}

	public void setCurrentBlock(List<IStatement> currentBlock) {
		this.currentBlock = currentBlock;
	}

	public Set<IVariableReference> getNames() {
		return names;
	}

	public void setNames(Set<IVariableReference> names) {
		this.names = names;
	}

	public List<IStatement> getNewMethodBody() {
		return newMethodBody;
	}

	public void setNewMethodBody(List<IStatement> newMethodBody) {
		this.newMethodBody = newMethodBody;
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

	public Map<IVariableReference, IVariableReference> getChangedNames() {
		return changedNames;
	}

	public void setChangedNames(Map<IVariableReference, IVariableReference> changedNames) {
		this.changedNames = changedNames;
	}

	public boolean isInline() {
		return inline;
	}

	public void setInline(boolean inline) {
		this.inline = inline;
	}

}
