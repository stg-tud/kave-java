package commons.model.ssts.impl.visitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;

public class InliningBaseTest {

	protected IStatement invocationStatement(String name, IVariableReference reference,
			ISimpleExpression... parameters) {
		return expr(invocationExpr(name, reference, parameters));
	}

	protected IInvocationExpression invocationExpr(String name, IVariableReference reference,
			ISimpleExpression... parameters) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setParameters(Lists.newArrayList(parameters));
		invocation.setReference(reference);
		invocation.setMethodName(CsMethodName.newMethodName(name));
		return invocation;
	}

	protected IStatement returnStatement(ISimpleExpression expr, boolean isVoid) {
		ReturnStatement statement = new ReturnStatement();
		statement.setIsVoid(isVoid);
		statement.setExpression(expr);
		return statement;
	}

	protected IExpressionStatement expr(IAssignableExpression expr) {
		ExpressionStatement statement = new ExpressionStatement();
		statement.setExpression(expr);
		return statement;
	}

	protected IStatement invocationStatement(String name, ISimpleExpression... parameters) {
		return invocationStatement(name, null, parameters);
	}

	protected IMethodDeclaration declareEntryPoint(String name, IStatement... statements) {
		return declareMethod(name, true, statements);
	}

	protected IMethodDeclaration declareNonEntryPoint(String name, IStatement... statements) {
		return declareMethod(name, false, statements);
	}

	protected IMethodDeclaration declareMethod(String name, boolean entryPoint, IStatement... statements) {
		MethodDeclaration method = new MethodDeclaration();
		method.setName(CsMethodName.newMethodName(name));
		method.setEntryPoint(entryPoint);
		for (IStatement s : statements)
			method.getBody().add(s);
		return method;
	}

	protected IComposedExpression compose(IVariableReference... refs) {
		ComposedExpression composedExpr = new ComposedExpression();
		composedExpr.setReferences(Lists.newArrayList(refs));
		return composedExpr;
	}

	protected IForLoop forLoop(String var, ILoopHeaderBlockExpression condition, IStatement... body) {
		ForLoop forLoop = new ForLoop();
		forLoop.setInit(Lists.newArrayList(declareVar(var), assign(ref(var), constant("0"))));
		forLoop.setStep(Lists.newArrayList(assign(ref(var), constant("2"))));
		forLoop.setBody(Lists.newArrayList(body));
		forLoop.setCondition(condition);
		return forLoop;
	}

	protected IIfElseBlock simpleIf(IStatement... body) {
		IfElseBlock ifElse = new IfElseBlock();
		ifElse.setCondition(constant("true"));
		ifElse.setThen(Lists.newArrayList(body));
		return ifElse;
	}

	protected IDoLoop doLoop(ILoopHeaderBlockExpression condition, IStatement... body) {
		DoLoop doLoop = new DoLoop();
		doLoop.setBody(Lists.newArrayList(body));
		doLoop.setCondition(condition);
		return doLoop;
	}

	protected IForEachLoop forEachLoop(String variable, String ref, IStatement... body) {
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.setBody(Lists.newArrayList(body));
		forEachLoop.setDeclaration(declareVar(variable));
		forEachLoop.setLoopedReference(ref(ref));
		return forEachLoop;
	}

	protected ILockBlock lockBlock(String identifier, IStatement... body) {
		LockBlock lockBlock = new LockBlock();
		lockBlock.setBody(Lists.newArrayList(body));
		lockBlock.setReference(ref(identifier));
		return lockBlock;
	}

	protected ISwitchBlock switchBlock(String identifier, ICaseBlock... caseBlocks) {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock.setReference(ref(identifier));
		switchBlock.setSections(Lists.newArrayList(caseBlocks));
		return switchBlock;
	}

	protected ICaseBlock caseBlock(String label, IStatement... body) {
		CaseBlock caseBlock = new CaseBlock();
		caseBlock.setLabel(constant(label));
		caseBlock.setBody(Lists.newArrayList(body));
		return caseBlock;
	}

	protected ITryBlock simpleTryBlock(IStatement... body) {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(Lists.newArrayList(body));
		return tryBlock;
	}

	protected ITryBlock simpleTryBlock(List<IStatement> body, ICatchBlock... catchBlocks) {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(body);
		tryBlock.setCatchBlocks(Lists.newArrayList(catchBlocks));
		return tryBlock;
	}

	protected ICatchBlock catchBlock(IStatement... body) {
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setBody(Lists.newArrayList(body));
		return catchBlock;
	}

	protected IUncheckedBlock uncheckedBlock(IStatement... body) {
		UncheckedBlock uncheckedBlock = new UncheckedBlock();
		uncheckedBlock.setBody(Lists.newArrayList(body));
		return uncheckedBlock;
	}

	protected IWhileLoop whileLoop(ILoopHeaderBlockExpression condition, IStatement... body) {
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(Lists.newArrayList(body));
		whileLoop.setCondition(condition);
		return whileLoop;
	}

	protected ILoopHeaderBlockExpression loopHeader(IStatement... condition) {
		LoopHeaderBlockExpression loopheader = new LoopHeaderBlockExpression();
		loopheader.setBody(Lists.newArrayList(condition));
		return loopheader;
	}

	protected IAssignment assign(IAssignableReference ref, IAssignableExpression expr) {
		Assignment statement = new Assignment();
		statement.setReference(ref);
		statement.setExpression(expr);
		return statement;
	}

	protected IConstantValueExpression constant(String value) {
		ConstantValueExpression constant = new ConstantValueExpression();
		constant.setValue(value);
		return constant;
	}

	protected IReferenceExpression refExpr(String identifier) {
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(ref(identifier));
		return refExpr;
	}

	protected IVariableReference ref(String identifier) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(identifier);
		return ref;
	}

	protected IFieldReference refField(String identifier) {
		FieldReference ref = new FieldReference();
		ref.setFieldName(CsFieldName.newFieldName(identifier));
		ref.setReference(ref(identifier));
		return ref;
	}

	protected IVariableDeclaration declareVar(String identifier) {
		// TODO: int, bool, unknown Type methods
		VariableDeclaration variable = new VariableDeclaration();
		variable.setReference(ref(identifier));
		return variable;
	}

	protected Set<IFieldDeclaration> declareFields(String... fieldNames) {
		Set<IFieldDeclaration> fields = new HashSet<>();
		for (String name : fieldNames) {
			FieldDeclaration fieldDeclaration = new FieldDeclaration();
			fieldDeclaration.setName(CsFieldName.newFieldName(name));
			fields.add(fieldDeclaration);
		}
		return fields;
	}

	protected ISST buildSST(IMethodDeclaration... declarations) {
		return buildSST(null, declarations);
	}

	protected ISST buildSST(Set<IFieldDeclaration> fields, IMethodDeclaration... declarations) {
		SST sst = new SST();
		if (fields == null) {
			FieldDeclaration fieldDeclaration = new FieldDeclaration();
			fieldDeclaration.setName(CsFieldName.newFieldName("[T4,P] [T5,P].F"));
			sst.setFields(Sets.newHashSet(fieldDeclaration));
		} else {
			sst.setFields(fields);
		}

		PropertyDeclaration propertyDeclaration = new PropertyDeclaration();
		propertyDeclaration.setName(CsPropertyName.newPropertyName("[T10,P] [T11,P].P"));
		propertyDeclaration.setGet(Lists.newArrayList(new ReturnStatement()));
		propertyDeclaration.setSet(Lists.newArrayList(new Assignment()));

		EventDeclaration eventDeclaration = new EventDeclaration();
		eventDeclaration.setName(CsEventName.newEventName("[T2,P] [T3,P].E"));

		DelegateDeclaration delegateDeclaration = new DelegateDeclaration();
		delegateDeclaration.setName(CsDelegateTypeName.newDelegateTypeName("d:[R,P] [T2,P].()"));
		sst.setEnclosingType(CsTypeName.newTypeName("T,P"));
		sst.setDelegates(Sets.newHashSet(delegateDeclaration));
		sst.setEvents(Sets.newHashSet(eventDeclaration));
		sst.setProperties(Sets.newHashSet(propertyDeclaration));

		for (IMethodDeclaration method : declarations) {
			sst.getMethods().add(method);
		}
		return sst;
	}
}
