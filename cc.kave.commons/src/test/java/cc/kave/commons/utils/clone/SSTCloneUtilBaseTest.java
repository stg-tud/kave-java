package cc.kave.commons.utils.clone;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.utils.ParentChildrenUtil;
import cc.kave.commons.utils.SSTCloneUtil;

public class SSTCloneUtilBaseTest {

	protected void assertClone(ISSTNode original) {
		assertClone(original, SSTCloneUtil.clone(original, ISSTNode.class));
	}

	private void assertClone(Iterable<ISSTNode> o, Iterable<ISSTNode> c) {
		List<ISSTNode> original = Lists.newArrayList(o);
		List<ISSTNode> clone = Lists.newArrayList(c);
		assertThat(clone, equalTo(original));
		assertThat(clone, not(sameInstance(original)));
		for (int i = 0; i < original.size(); i++) {
			assertClone(original.get(i), clone.get(i));
		}
	}

	private void assertClone(ISSTNode original, ISSTNode clone) {
		assertThat(clone, equalTo(original));
		assertThat(clone, not(sameInstance(original)));
		ParentChildrenUtil originalUtil = new ParentChildrenUtil(original);
		ParentChildrenUtil cloneUtil = new ParentChildrenUtil(clone);
		assertClone(originalUtil.getChildren(original), cloneUtil.getChildren(clone));
	}

	protected ISST defaultSST() {
		SST sst = new SST();
		sst.setEnclosingType(TypeName.newTypeName("SST"));
		sst.setPartialClassIdentifier("SST");
		return sst;
	}

	protected IVariableReference someVarRef() {
		VariableReference ref = new VariableReference();
		ref.setIdentifier("a");
		return ref;
	}

	protected ConstantValueExpression constant() {
		ConstantValueExpression expr = new ConstantValueExpression();
		expr.setValue("a");
		return expr;
	}

	protected ITypeName someType() {
		return TypeName.newTypeName("t");
	}

	protected IAssignableExpression someExpr() {
		UnaryExpression expr = new UnaryExpression();
		expr.setOperand(constant());
		return expr;
	}

	protected IVariableDeclaration someVarDec() {
		VariableDeclaration var = new VariableDeclaration();
		var.setReference(someVarRef());
		var.setType(someType());
		return var;
	}
}
