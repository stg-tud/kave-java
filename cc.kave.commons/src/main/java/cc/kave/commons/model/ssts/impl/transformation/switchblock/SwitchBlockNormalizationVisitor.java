/*
 * Copyright 2016 Carina Oberle
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
package cc.kave.commons.model.ssts.impl.transformation.switchblock;

import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.switchBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;

public class SwitchBlockNormalizationVisitor extends AbstractStatementNormalizationVisitor<IReferenceExpression> {

//	private Map<IReferenceExpression, IBinaryExpression> refLookup;
	private NodeFinderVisitor breakFinder;
	private int counter;

	public SwitchBlockNormalizationVisitor() {
		counter = 0;
		breakFinder = new NodeFinderVisitor(new BreakStatement());
//		refLookup = new HashMap<IReferenceExpression, IBinaryExpression>();
	}

	@Override
	public List<IStatement> visit(ISwitchBlock block, IReferenceExpression context) {
		// normalize inner switch blocks
		super.visit(block, context);

		IVariableReference ref = block.getReference();
		List<ICaseBlock> sections = block.getSections();
		List<IStatement> defaultSection = block.getDefaultSection();

		if (sections.isEmpty())
			return resolveBreakStatements(defaultSection);

		ICaseBlock section = sections.get(0);
		ISimpleExpression label = section.getLabel();
		
		boolean fallthrough = !containsBreak(section.getBody());
		boolean fallthroughToDefault = fallthrough && sections.size() == 1;

		List<IStatement> conditionStatements = getConditionStatements(ref, label, context);
		IReferenceExpression condition = getMainCondition(conditionStatements);
		IReferenceExpression updatedContext = fallthrough ? condition : null;

		List<ICaseBlock> elseSections = sections.subList(1, sections.size());
		List<IStatement> thenPart = getThenPart(section);
		List<IStatement> elsePart = getElsePart(elseSections, defaultSection, ref, updatedContext);

		IIfElseBlock ifElse = assembleIfElse(condition, thenPart, elsePart, fallthrough);
		boolean emptyIf = ifElse.getThen().isEmpty() && ifElse.getElse().isEmpty();
		
		List<IStatement> normalized = new ArrayList<IStatement>();

		if (!(emptyIf)) {
			normalized.addAll(conditionStatements);
			normalized.add(ifElse);
		} else if (fallthrough && !fallthroughToDefault)
			normalized.addAll(conditionStatements);
		if (fallthrough)
			normalized.addAll(elsePart);

		return normalized;
	}

	// -------------------------- condition -----------------------------------

	private String getConditionName() {
		int count = counter;
		counter++;
		return "cond_" + count;
	}

	private IVariableDeclaration conditionDeclaration() {
		// TODO: check TypeName
		return declare(getConditionName(), TypeName.newTypeName("System.Boolean"));
	}

	// private List<ISimpleExpression> collectConditionLabels(List<ICaseBlock>
	// sections) {
	// List<ISimpleExpression> labels = Lists.newArrayList();
	// if (sections.isEmpty())
	// return labels;
	//
	// ListIterator<ICaseBlock> it = sections.listIterator();
	// ICaseBlock current = it.next();
	// labels.add(current.getLabel());
	//
	// while (it.hasNext() && !containsBreak(current.getBody())) {
	// current = it.next();
	// labels.add(current.getLabel());
	// }
	//
	// return labels;
	// }

	private IReferenceExpression getMainCondition(List<IStatement> conditionStatements) {
		List<IVariableDeclaration> vars = conditionStatements.stream().filter(s -> s instanceof IVariableDeclaration)
				.map(v -> (IVariableDeclaration) v).collect(Collectors.toList());
		return refExpr(vars.get(vars.size() - 1).getReference());
	}

	private List<IStatement> getConditionStatements(IVariableReference ref, ISimpleExpression label,
			IReferenceExpression context) {
		IVariableDeclaration eqVar = conditionDeclaration();
		IVariableReference eqRef = eqVar.getReference();
		IBinaryExpression eqExpr = binExpr(BinaryOperator.Equal, refExpr(ref), label);
		IAssignment eqAssign = assign(eqRef, eqExpr);

		List<IStatement> statements = new ArrayList<IStatement>();
		statements.add(eqVar);
		statements.add(eqAssign);

		if (context != null) {
			IVariableDeclaration orVar = conditionDeclaration();
			IAssignment orAssign = assign(orVar.getReference(), binExpr(BinaryOperator.Or, context, refExpr(eqRef)));
			statements.add(orVar);
			statements.add(orAssign);
		}

		return statements;
	}

	// private List<IStatement> getConditionStatements(IVariableReference ref,
	// List<ISimpleExpression> labels) {
	// List<IStatement> statements = getEqualityStatements(ref, labels);
	// List<IReferenceExpression> vars = new ArrayList<IReferenceExpression>();
	//
	// for (IStatement s : statements)
	// if (s instanceof IVariableDeclaration)
	// vars.add(refExpr(((IVariableDeclaration) s).getReference()));
	//
	// vars.stream().reduce((v1, v2) -> {
	// IVariableDeclaration var = conditionDeclaration();
	// IAssignment assignment = assign(var.getReference(),
	// binExpr(BinaryOperator.Or, v1, v2));
	// statements.add(var);
	// statements.add(assignment);
	// return refExpr(var.getReference());
	// });
	//
	// return statements;
	// }

	// private List<IStatement> getEqualityStatements(IVariableReference ref,
	// List<ISimpleExpression> labels) {
	// List<IStatement> statements = new ArrayList<IStatement>();
	// labels.forEach(l -> {
	// IBinaryExpression eqExpr = binExpr(BinaryOperator.Equal, refExpr(ref),
	// l);
	// IVariableDeclaration var = conditionDeclaration();
	// IAssignment assignment = assign(var.getReference(), eqExpr);
	// statements.add(var);
	// statements.add(assignment);
	// });
	// return statements;
	// }

	// -------------------------- then part -----------------------------------

	private List<IStatement> getThenPart(ICaseBlock section) {
		return resolveBreakStatements(section.getBody());
	}

	// -------------------------- else part -----------------------------------

	private List<IStatement> getElsePart(List<ICaseBlock> remainingSections, List<IStatement> defaultSection,
			IVariableReference ref, IReferenceExpression context) {
		return visit(switchBlock(ref, remainingSections, defaultSection), context);
	}

	// ------------------------ handle breaks ---------------------------------

	private boolean containsBreak(List<IStatement> statements) {
		for (IStatement s : statements) {
			if (s.accept(breakFinder, null))
				return true;
		}
		return false;
	}

	// TODO
	private List<IStatement> resolveBreakStatements(List<IStatement> statements) {
		int size = statements.size();
		for (int i = 0; i < statements.size(); i++) {
			if (statements.get(i) instanceof IBreakStatement) {
				size = i;
				break;
			}
		}
		return statements.subList(0, size);
	}

	// --------------------------- helper -------------------------------------

	private IIfElseBlock assembleIfElse(IReferenceExpression condition, List<IStatement> thenPart,
			List<IStatement> elsePart, boolean fallthrough) {
		IfElseBlock ifElse = new IfElseBlock();
		ifElse.setCondition(condition);
		ifElse.setThen(thenPart);
		if (!fallthrough)
			ifElse.setElse(elsePart);
		return ifElse;
	}

	private IAssignment assign(IAssignableReference ref, IBinaryExpression expr) {
		IAssignment statement = SSTUtil.assign(ref, expr);
//		refLookup.put(refExpr(ref), expr);
		return statement;
	}

}