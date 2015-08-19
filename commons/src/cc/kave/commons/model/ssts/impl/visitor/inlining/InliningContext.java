package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.NameScopeVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.RecursiveCallVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class InliningContext {

	private static final TypeName GOT_RESULT_TYPE = CsTypeName.newTypeName("Boolean");
	private static final String RESULT_NAME = "$result_";
	private static final String RESULT_FLAG = "$gotNoResult_";

	private int counter;
	private SST sst;
	private NameScopeVisitor visitor;
	private Scope scope;
	private Set<IMethodDeclaration> nonEntryPoints;
	private boolean inline = false;
	private boolean guardNeeded = false;
	private boolean globalGuardNeed = false;
	private final InliningIStatementVisitor statementVisitor;
	private final InliningIReferenceVisitor referenceVisitor;
	private final InliningIExpressionVisitor expressionVisitor;

	private Set<IMethodDeclaration> removeList;

	public InliningContext() {
		this.nonEntryPoints = new HashSet<>();
		this.sst = new SST();
		this.inline = false;
		this.scope = null;
		this.visitor = new NameScopeVisitor();
		this.counter = 0;
		this.statementVisitor = new InliningIStatementVisitor();
		this.referenceVisitor = new InliningIReferenceVisitor();
		this.expressionVisitor = new InliningIExpressionVisitor();
		this.removeList = new HashSet<>();
	}

	public InliningIStatementVisitor getStatementVisitor() {
		return statementVisitor;
	}

	public InliningIReferenceVisitor getReferenceVisitor() {
		return referenceVisitor;
	}

	public InliningIExpressionVisitor getExpressionVisitor() {
		return expressionVisitor;
	}

	public Set<IMethodDeclaration> getNonEntryPoints() {
		return nonEntryPoints;
	}

	public void setNonEntryPoints(Set<IMethodDeclaration> nonEntryPoints) {
		this.nonEntryPoints = testForRecursiveCalls(nonEntryPoints);
	}

	private Set<IMethodDeclaration> testForRecursiveCalls(Set<IMethodDeclaration> nonEntryPoints) {
		Map<MethodName, Set<MethodName>> calls = new HashMap<>();
		Set<IMethodDeclaration> outputNonEntryPoints = new HashSet<>();
		Set<MethodName> removeMethods = new HashSet<>();
		for (IMethodDeclaration method : nonEntryPoints) {
			Set<MethodName> context = new HashSet<>();
			method.accept(new RecursiveCallVisitor(), context);
			if (calls.containsKey(method.getName()))
				removeMethods.add(method.getName());
			calls.put(method.getName(), context);
		}
		for (MethodName name : removeMethods) {
			calls.remove(name);
		}
		for (IMethodDeclaration method : nonEntryPoints) {
			if (calls.containsKey(method.getName())) {
				Set<MethodName> invocations = calls.get(method.getName());
				boolean hasRecursiveCall = invocations.contains(method.getName());
				if (!hasRecursiveCall && !invocations.isEmpty() && invocations != null) {
					hasRecursiveCall = checkCallTree(invocations, calls, Sets.newHashSet(), method);
				}
				if (!hasRecursiveCall)
					outputNonEntryPoints.add(method);
				else
					addMethod(method);
			}
			if (removeMethods.contains(method.getName()))
				addMethod(method);
		}
		return outputNonEntryPoints;
	}

	private boolean checkCallTree(Set<MethodName> invocations, Map<MethodName, Set<MethodName>> calls,
			HashSet<MethodName> met, IMethodDeclaration method) {
		boolean recursive = false;
		if (invocations != null) {
			for (MethodName call : invocations) {
				if (met.contains(call))
					continue;
				else {
					if (call.equals(method.getName()))
						recursive = true;
					met.add(call);
					recursive = checkCallTree(calls.get(call), calls, met, method) || recursive;
				}
			}
		}
		return recursive;
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

	public void enterScope(List<IStatement> body, Map<IVariableReference, IVariableReference> preChangedNames) {
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
		if (preChangedNames != null)
			newScope.changedNames.putAll(preChangedNames);
		scope = newScope;
	}

	public void leaveScope() {
		if (scope.parent != null) {
			scope.parent.existingIds.addAll(scope.existingIds);
			scope.parent.body.addAll(scope.body);
			scope = scope.parent;
		}
		this.setGlobalGuardNeeded(false);
		this.setGuardNeeded(false);
	}

	public void enterBlock() {
		Scope newScope = new Scope();
		newScope.parent = scope;
		if (scope != null) {
			newScope.existingIds.addAll(scope.existingIds);
			newScope.changedNames = scope.changedNames;
			newScope.resultName = scope.resultName;
			newScope.gotResultName = scope.gotResultName;
		}
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

	public void addMethod(IMethodDeclaration method) {
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

	public String getGotResultName() {
		return scope.gotResultName;
	}

	public void setResultName(String name) {
		scope.resultName = name;
	}

	public void setGotResultName(String name) {
		scope.gotResultName = name;
	}

	public void setGuardVariableNames(String name) {
		scope.resultName = RESULT_NAME + name;
		scope.gotResultName = RESULT_FLAG + name;
	}

	public void setGuardNeeded(boolean b) {
		this.guardNeeded = b;
	}

	public void setGlobalGuardNeeded(boolean b) {
		this.globalGuardNeed = b;
	}

	public boolean isGuardNeeded() {
		return this.guardNeeded;
	}

	public boolean isGlobalGuardNeeded() {
		return this.globalGuardNeed;
	}

	public void addMethodToRemove(IMethodDeclaration method) {
		this.removeList.add(method);
	}

	public Set<IMethodDeclaration> getRemoveList() {
		return removeList;
	}

	public void resetScope() {
		scope = null;
		counter = 0;
	}
}
