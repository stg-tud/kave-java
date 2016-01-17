package cc.kave.commons.model.ssts.impl.transformation.switchblock;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.ifElseBlock;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.switchBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;

public class SwitchBlockNormalizationVisitor extends AbstractStatementNormalizationVisitor<Void> {

	private int counter = 0;

	@Override
	public List<IStatement> visit(ISwitchBlock block, Void context) {
		// normalize inner switch blocks
		super.visit(block, context);

		IVariableReference ref = block.getReference();
		List<ICaseBlock> sections = block.getSections();
		List<IStatement> defaultSection = block.getDefaultSection();

		List<ISimpleExpression> conditionLabels = collectConditionLabels(sections);

		if (conditionLabels.isEmpty())
			return resolveBreakStatements(defaultSection);

		List<IStatement> conditionStatements = getConditionStatements(ref, conditionLabels);
		IReferenceExpression condition = getCondition(conditionStatements);

		ICaseBlock head = sections.get(0);
		List<ICaseBlock> tail = tail(sections);

		// TODO: allow fallthrough, handle breaks
		List<IStatement> thenPart = resolveBreakStatements(head.getBody());
		List<IStatement> elsePart = visit(switchBlock(ref, tail, defaultSection), context);
		IfElseBlock ifElseBlock = ifElseBlock(condition, thenPart, elsePart);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.addAll(conditionStatements);
		normalized.add(ifElseBlock);
		return normalized;
	}

	private IReferenceExpression getCondition(List<IStatement> conditionStatements) {
		List<IVariableDeclaration> vars = conditionStatements.stream().filter(s -> s instanceof IVariableDeclaration)
				.map(v -> (IVariableDeclaration) v).collect(Collectors.toList());
		return refExpr(vars.get(vars.size() - 1).getReference());
	}

	private List<IStatement> getConditionStatements(IVariableReference ref, List<ISimpleExpression> labels) {
		List<IStatement> statements = getEqualityStatements(ref, labels);
		List<IReferenceExpression> vars = statements.stream().filter(s -> s instanceof IVariableDeclaration)
				.map(s -> (IVariableDeclaration) s).map(v -> refExpr(v.getReference())).collect(Collectors.toList());

		vars.stream().reduce((v1, v2) -> {
			IVariableDeclaration var = conditionDeclaration();
			IAssignment assignment = assign(var.getReference(), binExpr(BinaryOperator.Or, v1, v2));
			statements.add(var);
			statements.add(assignment);
			return refExpr(var.getReference());
		});

		return statements;
	}

	private List<IStatement> getEqualityStatements(IVariableReference ref, List<ISimpleExpression> labels) {
		List<IStatement> statements = new ArrayList<IStatement>();
		labels.forEach(l -> {
			IBinaryExpression eqExpr = binExpr(BinaryOperator.Equal, refExpr(ref), l);
			IVariableDeclaration var = conditionDeclaration();
			IAssignment assignment = assign(var.getReference(), eqExpr);
			statements.add(var);
			statements.add(assignment);
		});
		return statements;
	}

	private IVariableDeclaration conditionDeclaration() {
		// TODO: check TypeName
		return SSTUtil.declare(getConditionName(), TypeName.newTypeName("System.Boolean"));
	}

	private List<ISimpleExpression> collectConditionLabels(List<ICaseBlock> sections) {
		List<ISimpleExpression> labels = Lists.newArrayList();
		if (sections.isEmpty())
			return labels;

		labels.add(sections.get(0).getLabel());

		while (!sections.isEmpty() && sections.get(0).getBody().isEmpty()) {
			sections.remove(0);
			labels.add(sections.get(0).getLabel());
		}
		// fall-through to default case, no condition necessary
		if (sections.isEmpty())
			labels.clear();

		return labels;
	}

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

	private String getConditionName() {
		int count = counter;
		counter++;
		return "cond_" + count;
	}

	private <T> List<T> tail(List<T> lst) {
		return lst.subList(1, lst.size());
	}

}