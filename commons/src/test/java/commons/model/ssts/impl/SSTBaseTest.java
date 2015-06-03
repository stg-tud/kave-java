package commons.model.ssts.impl;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.declarations.VariableDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public abstract class SSTBaseTest {

	protected ISimpleExpression label(String label) {
		ConstantValueExpression expr = new ConstantValueExpression();
		expr.setValue(label);
		return expr;
	}

	protected IVariableDeclaration someDeclaration(String type) {
		VariableDeclaration decl = new VariableDeclaration();
		decl.setType(CsTypeName.newTypeName(type + ",P1"));
		return decl;
	}

	protected IVariableDeclaration someDeclaration() {
		return someDeclaration("T1");
	}

	protected IVariableReference someVarRef(String id) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(id);
		return ref;
	}

	protected IVariableReference someVarRef() {
		return someVarRef("v");
	}

	protected List<IVariableReference> refs(String[] strRefs) {
		List<IVariableReference> varRefs = new ArrayList<IVariableReference>();
		for (int i = 0; i < strRefs.length; i++)
			varRefs.add(someVarRef(strRefs[i]));
		return varRefs;
	}

	protected MethodName getMethod(String simpleName) {
		String methodName = "[T1, P1] [T2, P2]" + simpleName + "()";
		return CsMethodName.newMethodName(methodName);
	}

	protected List<ISimpleExpression> refExprs(String[] ids) {
		List<ISimpleExpression> exprs = new ArrayList<ISimpleExpression>();
		for (String id : ids) {
			ReferenceExpression refExpr = new ReferenceExpression();
			VariableReference varRef = new VariableReference();
			varRef.setIdentifier(id);
			refExpr.setReference(varRef);
			exprs.add(refExpr);
		}
		return exprs;
	}
}
