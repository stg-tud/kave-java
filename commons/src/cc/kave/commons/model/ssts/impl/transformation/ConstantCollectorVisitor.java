package cc.kave.commons.model.ssts.impl.transformation;

import java.util.Set;

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
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;

public class ConstantCollectorVisitor extends AbstractNodeVisitor<Set<IFieldDeclaration>, Set<IFieldDeclaration>> {
	@Override
	public Set<IFieldDeclaration> visit(ISST sst, Set<IFieldDeclaration> constants) {

		for (IFieldDeclaration field : sst.getFields()) {
			if (field.getName().getDeclaringType().isSimpleType())
				constants.add(field);
		}
		for (IPropertyDeclaration property : sst.getProperties()) {
			property.accept(this, constants);
		}
		for (IMethodDeclaration method : sst.getMethods()) {
			method.accept(this, constants);
		}
		return constants;
	}

	@Override
	public Set<IFieldDeclaration> visit(IMethodDeclaration stmt, Set<IFieldDeclaration> constants) {
		for (IStatement s : stmt.getBody()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IPropertyDeclaration stmt, Set<IFieldDeclaration> constants) {
		for (IStatement s : stmt.getGet()) {
			s.accept(this, constants);
		}
		for (IStatement s : stmt.getSet()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IAssignment stmt, Set<IFieldDeclaration> constants) {
		stmt.getReference().accept(this, constants);
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IVariableDeclaration stmt, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IBreakStatement stmt, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IContinueStatement stmt, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IExpressionStatement stmt, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IGotoStatement stmt, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(ILabelledStatement stmt, Set<IFieldDeclaration> constants) {
		stmt.getStatement().accept(this, constants);
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IReturnStatement stmt, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IThrowStatement stmt, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IDoLoop block, Set<IFieldDeclaration> constants) {
		for (IStatement s : block.getBody()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IForEachLoop block, Set<IFieldDeclaration> constants) {
		for (IStatement s : block.getBody()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IForLoop block, Set<IFieldDeclaration> constants) {
		for (IStatement s : block.getInit()) {
			s.accept(this, constants);
		}
		for (IStatement s : block.getBody()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IIfElseBlock block, Set<IFieldDeclaration> constants) {
		for (IStatement s : block.getThen()) {
			s.accept(this, constants);
		}
		for (IStatement s : block.getElse()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(ILockBlock stmt, Set<IFieldDeclaration> constants) {
		for (IStatement s : stmt.getBody()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(ISwitchBlock block, Set<IFieldDeclaration> constants) {
		for (IStatement s : block.getDefaultSection()) {
			s.accept(this, constants);
		}
		for (ICaseBlock caseBlock : block.getSections()) {
			for (IStatement stm : caseBlock.getBody()) {
				stm.accept(this, constants);
			}
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(ITryBlock block, Set<IFieldDeclaration> constants) {
		for (IStatement s : block.getBody()) {
			s.accept(this, constants);
		}
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			for (IStatement s : catchBlock.getBody()) {
				s.accept(this, constants);
			}
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IUncheckedBlock block, Set<IFieldDeclaration> constants) {
		for (IStatement s : block.getBody()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IUnsafeBlock block, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IUsingBlock block, Set<IFieldDeclaration> constants) {
		for (IStatement s : block.getBody()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IWhileLoop block, Set<IFieldDeclaration> constants) {
		for (IStatement s : block.getBody()) {
			s.accept(this, constants);
		}
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IEventReference eventRef, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IFieldReference fieldRef, Set<IFieldDeclaration> constants) {
		constants.removeIf(field -> field.getName().equals(fieldRef.getFieldName()));
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IMethodReference methodRef, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IPropertyReference propertyRef, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IVariableReference varRef, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IUnknownReference unknownRef, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IUnknownStatement unknownStmt, Set<IFieldDeclaration> constants) {
		return null;
	}

	@Override
	public Set<IFieldDeclaration> visit(IEventSubscriptionStatement stmt, Set<IFieldDeclaration> constants) {
		stmt.getReference().accept(this, constants);
		return null;
	}

}
