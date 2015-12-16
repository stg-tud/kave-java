package cc.kave.commons.model.ssts.impl.transformation.constants;

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;

public class InlineConstantContext {
	private ConstantCollectorVisitor collector;
	private SimpleExpressionVisitor exprVisitor;
	private Set<IFieldDeclaration> constants;

	public InlineConstantContext() {
		this.collector = new ConstantCollectorVisitor();
		this.exprVisitor = new SimpleExpressionVisitor();
		this.constants = new HashSet<IFieldDeclaration>();
	}

	public void collectConstants(ISST sst) {
		constants.addAll(sst.accept(collector, new HashSet<IFieldDeclaration>()));
	}

	public Set<IFieldDeclaration> getConstants() {
		return constants;
	}

	public boolean isConstant(IFieldName field) {
		for (IFieldDeclaration constant : constants) {
			if (constant.getName().equals(field))
				return true;
		}
		return false;
	}

	public SimpleExpressionVisitor getExprVisitor() {
		return exprVisitor;
	}

}
