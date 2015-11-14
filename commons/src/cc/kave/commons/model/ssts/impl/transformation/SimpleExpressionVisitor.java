package cc.kave.commons.model.ssts.impl.transformation;

import java.util.Set;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.references.IFieldReference;

public class SimpleExpressionVisitor extends AbstractNodeVisitor<Set<IFieldDeclaration>, Boolean> {

	@Override
	public Boolean visit(IConstantValueExpression expr, Set<IFieldDeclaration> constants) {
		return false;
	}

	@Override
	public Boolean visit(INullExpression expr, Set<IFieldDeclaration> constants) {
		return false;
	}

	@Override
	public Boolean visit(IReferenceExpression expr, Set<IFieldDeclaration> constants) {
		IReference reference = expr.getReference();
		if (reference instanceof IFieldReference) {
			for (IFieldDeclaration constant : constants) {
				if (constant.getName().equals(((IFieldReference) reference).getFieldName()))
					return true;
			}
		}
		return false;
	}

	@Override
	public Boolean visit(IUnknownExpression unknownExpr, Set<IFieldDeclaration> constants) {
		return false;
	}

}
