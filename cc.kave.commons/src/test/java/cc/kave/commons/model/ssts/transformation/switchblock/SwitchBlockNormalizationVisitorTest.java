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
package cc.kave.commons.model.ssts.transformation.switchblock;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.breakStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.caseBlock;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.unaryExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.impl.transformation.switchblock.SwitchBlockNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class SwitchBlockNormalizationVisitorTest {
	private SwitchBlockNormalizationVisitor sut;
	private SwitchBlock switchBlock;
	private MethodDeclaration expectedMethod;
	private IVariableReference var;
	private IStatement stmt0, stmt1, stmt2, stmt3;
	private SST sst, expectedSST;
	IConstantValueExpression label0, label1, label2;

	@Before
	public void setup() {
		sut = new SwitchBlockNormalizationVisitor();
		var = variableReference("x");
		switchBlock = new SwitchBlock();
		switchBlock.setReference(var);
		sst = new SST();
		sst.setMethods(Sets.newHashSet(declareMethod(switchBlock)));

		expectedMethod = new MethodDeclaration();
		expectedSST = new SST();
		expectedSST.setMethods(Sets.newHashSet(expectedMethod));

		label0 = constant("0");
		label1 = constant("1");
		label2 = constant("2");

		stmt0 = dummyStatement(0);
		stmt1 = dummyStatement(1);
		stmt2 = dummyStatement(2);
		stmt3 = dummyStatement(3);
	}

	// --------------------- No fall-through ----------------------------------

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾|
	// | switch(x) { .... |..=>..| stmt; |
	// | ..default: stmt; |......|_______|
	// | } .............. |
	// |__________________|
	@Test
	public void testOnlyDefaultSection() {
		setDefaultSection(stmt0);
		setExpected(stmt0);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |..=>..| if ( ! x == 0 ) |
	// | ..case 0: break;. |......| ..stmt; ....... |
	// | ..default: stmt;. |......|_________________|
	// | } ............... |
	// |___________________|
	@Test
	public void testEmptySectionNoFallthrough() {
		ICaseBlock case0 = caseBlock(label0, breakStatement());

		setSections(case0);
		setDefaultSection(stmt0);

		IVariableReference var0 = variableReference("cond_0");
		IVariableReference var1 = variableReference("cond_1");
		IAssignableExpression eq0 = binExpr(BinaryOperator.Equal, refExpr(var), label0);
		IAssignableExpression neg0 = unaryExpr(UnaryOperator.Not, refExpr(var0));
		IAssignment assign0 = assign(var0, eq0);
		IAssignment assign1 = assign(var1, neg0);

		IfElseBlock ifElseBlock = ifCond(var1);
		setThen(ifElseBlock, stmt0);

		setExpected(booleanDec(var0), assign0, booleanDec(var1), assign1, ifElseBlock);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if( x == 0 ) |
	// | ..case 0: stmt0;. |..=>..| ..stmt0; .... |
	// | .....break; ..... |......| else ........ |
	// | ..default: stmt1; |......| ..stmt1; .... |
	// | } ............... |......|_______________|
	// |___________________|
	@Test
	public void testOneSectionNoFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0, breakStatement());

		setSections(case0);
		setDefaultSection(stmt1);

		IVariableReference var0 = variableReference("cond_0");
		IAssignableExpression eq0 = binExpr(BinaryOperator.Equal, refExpr(var), label0);
		IAssignment assign0 = assign(var0, eq0);

		IfElseBlock ifElseBlock = ifCond(var0);
		setThen(ifElseBlock, stmt0);
		setElse(ifElseBlock, stmt1);

		setExpected(booleanDec(var0), assign0, ifElseBlock);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ) .... |
	// | ..case 0: stmt0;. |......| ..stmt0; ......... |
	// | .....break; ..... |..=>..| else if ( x == 1 ) |
	// | ..case 1: stmt1;. |......| ..stmt1; ......... |
	// | .....break; ..... |......| else ............. |
	// | ..default: stmt2; |......| ..stmt2; ......... |
	// | } ............... |......|____________________|
	// |___________________|
	@Test
	public void testTwoSectionsNoFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0, breakStatement());
		ICaseBlock case1 = caseBlock(label1, stmt1, breakStatement());

		setSections(case0, case1);
		setDefaultSection(stmt2);

		IVariableReference var0 = variableReference("cond_0");
		IVariableReference var1 = variableReference("cond_1");
		IAssignableExpression eq0 = binExpr(BinaryOperator.Equal, refExpr(var), label0);
		IAssignableExpression eq1 = binExpr(BinaryOperator.Equal, refExpr(var), label1);
		IAssignment assign0 = assign(var0, eq0);
		IAssignment assign1 = assign(var1, eq1);

		IfElseBlock ifElseInner = ifCond(var1);
		setThen(ifElseInner, stmt1);
		setElse(ifElseInner, stmt2);
		IfElseBlock ifElseOuter = ifCond(var0);
		setThen(ifElseOuter, stmt0);
		setElse(ifElseOuter, booleanDec(var1), assign1, ifElseInner);

		setExpected(booleanDec(var0), assign0, ifElseOuter);
		assertTransformedSST();
	}

	// ------------------------ fall-through ----------------------------------

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾|
	// | switch(x) { ..... |..=>..| stmt; |
	// | ..case 0: ....... |......|_______|
	// | ..default: stmt;. |
	// | } ............... |
	// |___________________|
	@Test
	public void testEmptySectionDefaultFallthrough() {
		setSections(new CaseBlock());
		setDefaultSection(stmt0);
		setExpected(stmt0);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ); |
	// | ..case 0: stmt0;. |..=>..| ..stmt0; ..... |
	// | ..default: stmt1; |......| stmt1; ....... |
	// | } ............... |......|________________|
	// |___________________|
	@Test
	public void testNonEmptySectionDefaultFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0);

		setSections(case0);
		setDefaultSection(stmt1);

		IVariableReference var0 = variableReference("cond_0");
		IAssignableExpression eq0 = binExpr(BinaryOperator.Equal, refExpr(var), label0);
		IAssignment assign0 = assign(var0, eq0);
		IfElseBlock ifBlock = ifCond(var0);
		setThen(ifBlock, stmt0);

		setExpected(booleanDec(var0), assign0, ifBlock, stmt1);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 || x == 1); |
	// | ..case 0: ....... |..=>..| ..stmt0; .............. |
	// | ..case 1: stmt0;. |......| else .................. |
	// | ....break; ...... |......| ..stmt1; .............. |
	// | ..default: stmt1; |......|_________________________|
	// | } ............... |
	// |___________________|
	@Test
	public void testEmptySectionFallthrough() {
		ICaseBlock case0 = caseBlock(label0);
		ICaseBlock case1 = caseBlock(label1, stmt0, breakStatement());

		setSections(case0, case1);
		setDefaultSection(stmt1);

		IVariableReference var0 = variableReference("cond_0");
		IVariableReference var1 = variableReference("cond_1");
		IVariableReference var2 = variableReference("cond_2");
		IAssignableExpression eq0 = binExpr(BinaryOperator.Equal, refExpr(var), label0);
		IAssignableExpression eq1 = binExpr(BinaryOperator.Equal, refExpr(var), label1);
		IAssignableExpression orExpr = binExpr(BinaryOperator.Or, refExpr(var0), refExpr(var1));
		IAssignment assign0 = assign(var0, eq0);
		IAssignment assign1 = assign(var1, eq1);
		IAssignment assign2 = assign(var2, orExpr);

		IfElseBlock ifElse = ifCond(var2);
		setThen(ifElse, stmt0);
		setElse(ifElse, stmt1);

		setExpected(booleanDec(var0), assign0, booleanDec(var1), assign1, booleanDec(var2), assign2, ifElse);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ) .......... |
	// | ..case 0: stmt0;. |..=>..| ..stmt0; ............... |
	// | ..case 1: stmt1;. |......| if ( x == 0 || x == 1 ) |
	// | ....break; ...... |......| ..stmt1; ............... |
	// | ..default: stmt2; |......| else ................... |
	// | } ............... |......| ..stmt2; ............... |
	// |___________________|......|__________________________|
	@Test
	public void testNonEmptySectionFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0);
		ICaseBlock case1 = caseBlock(label1, stmt1, breakStatement());

		setSections(case0, case1);
		setDefaultSection(stmt2);

		IVariableReference var0 = variableReference("cond_0");
		IVariableReference var1 = variableReference("cond_1");
		IVariableReference var2 = variableReference("cond_2");
		IAssignableExpression eq0 = binExpr(BinaryOperator.Equal, refExpr(var), label0);
		IAssignableExpression eq1 = binExpr(BinaryOperator.Equal, refExpr(var), label1);
		IAssignableExpression or0 = binExpr(BinaryOperator.Or, refExpr(var0), refExpr(var1));
		IAssignment assign0 = assign(var0, eq0);
		IAssignment assign1 = assign(var1, eq1);
		IAssignment assign2 = assign(var2, or0);

		IfElseBlock ifElse0 = ifCond(var0);
		IfElseBlock ifElse1 = ifCond(var2);
		setThen(ifElse0, stmt0);
		setThen(ifElse1, stmt1);
		setElse(ifElse1, stmt2);

		setExpected(booleanDec(var0), assign0, ifElse0, booleanDec(var1), assign1, booleanDec(var2), assign2, ifElse1);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ) ..................... |
	// | ..case 0: stmt0.. |......| ..stmt0; .......................... |
	// | ..case 1: stmt1;. |......| if ( x == 0 || x == 1 ) ........... |
	// | ..case 2: stmt2;. |..=>..| ..stmt1; .......................... |
	// | ....break; ...... |......| if ( x == 0 || x == 1 || x == 2 ).. |
	// | ..default: stmt3; |......| ..stmt2; .......................... |
	// | } ............... |......| else .............................. |
	// |___________________|......| ..stmt3; .......................... |
	// ...........................|_____________________________________|
	@Test
	public void testMultipleNonEmptySectionsFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0);
		ICaseBlock case1 = caseBlock(label1, stmt1);
		ICaseBlock case2 = caseBlock(label2, stmt2, breakStatement());

		setSections(case0, case1, case2);
		setDefaultSection(stmt3);

		IVariableReference var0 = variableReference("cond_0");
		IVariableReference var1 = variableReference("cond_1");
		IVariableReference var2 = variableReference("cond_2");
		IVariableReference var3 = variableReference("cond_3");
		IVariableReference var4 = variableReference("cond_4");

		IAssignableExpression eq0 = binExpr(BinaryOperator.Equal, refExpr(var), label0);
		IAssignableExpression eq1 = binExpr(BinaryOperator.Equal, refExpr(var), label1);
		IAssignableExpression eq2 = binExpr(BinaryOperator.Equal, refExpr(var), label2);
		IAssignableExpression or0 = binExpr(BinaryOperator.Or, refExpr(var0), refExpr(var1));
		IAssignableExpression or1 = binExpr(BinaryOperator.Or, refExpr(var2), refExpr(var3));
		IAssignment assign0 = assign(var0, eq0);
		IAssignment assign1 = assign(var1, eq1);
		IAssignment assign2 = assign(var2, or0);
		IAssignment assign3 = assign(var3, eq2);
		IAssignment assign4 = assign(var4, or1);

		IfElseBlock ifElse0 = ifCond(var0);
		IfElseBlock ifElse1 = ifCond(var2);
		IfElseBlock ifElse2 = ifCond(var4);
		setThen(ifElse0, stmt0);
		setThen(ifElse1, stmt1);
		setThen(ifElse2, stmt2);
		setElse(ifElse2, stmt3);

		setExpected(booleanDec(var0), assign0, ifElse0, booleanDec(var1), assign1, booleanDec(var2), assign2, ifElse1,
				booleanDec(var3), assign3, booleanDec(var4), assign4, ifElse2);
		assertTransformedSST();
	}

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { ..... |......| if ( x == 0 ) ................... |
	// | ..case 0: stmt0.. |......| ..stmt0; ........................ |
	// | ..case 1: stmt1;. |..=>..| if ( x == 0 || x == 1 ) ......... |
	// | ..case 2: stmt2;. |......| ..stmt1; ........................ |
	// | ..default: stmt3; |......| if ( x == 0 || x == 1 || x == 2 ) |
	// | } ............... |......| ..stmt2; ........................ |
	// |___________________|......| stmt3; .......................... |
	// ...........................|___________________________________|
	@Test
	public void testMultipleNonEmptySectionsDefaultFallthrough() {
		ICaseBlock case0 = caseBlock(label0, stmt0);
		ICaseBlock case1 = caseBlock(label1, stmt1);
		ICaseBlock case2 = caseBlock(label2, stmt2);

		setSections(case0, case1, case2);
		setDefaultSection(stmt3);

		IVariableReference var0 = variableReference("cond_0");
		IVariableReference var1 = variableReference("cond_1");
		IVariableReference var2 = variableReference("cond_2");
		IVariableReference var3 = variableReference("cond_3");
		IVariableReference var4 = variableReference("cond_4");

		IAssignableExpression eq0 = binExpr(BinaryOperator.Equal, refExpr(var), label0);
		IAssignableExpression eq1 = binExpr(BinaryOperator.Equal, refExpr(var), label1);
		IAssignableExpression eq2 = binExpr(BinaryOperator.Equal, refExpr(var), label2);
		IAssignableExpression or0 = binExpr(BinaryOperator.Or, refExpr(var0), refExpr(var1));
		IAssignableExpression or1 = binExpr(BinaryOperator.Or, refExpr(var2), refExpr(var3));

		IAssignment assign0 = assign(var0, eq0);
		IAssignment assign1 = assign(var1, eq1);
		IAssignment assign2 = assign(var2, or0);
		IAssignment assign3 = assign(var3, eq2);
		IAssignment assign4 = assign(var4, or1);

		IfElseBlock ifElse0 = ifCond(var0);
		IfElseBlock ifElse1 = ifCond(var2);
		IfElseBlock ifElse2 = ifCond(var4);
		setThen(ifElse0, stmt0);
		setThen(ifElse1, stmt1);
		setThen(ifElse2, stmt2);

		setExpected(booleanDec(var0), assign0, ifElse0, booleanDec(var1), assign1, booleanDec(var2), assign2, ifElse1,
				booleanDec(var3), assign3, booleanDec(var4), assign4, ifElse2, stmt3);
		assertTransformedSST();
	}

	// ------------------------ inner breaks ----------------------------------

	// |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|......|‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
	// | switch(x) { . |......| if( x == 0 ) |
	// | ..case 0: ... |..=>..| ..if(c) .... |
	// | ....if(c) ... |......| ....stmt0; . |
	// | ......stmt0;. |......| ..else ..... |
	// | ....else .... |......|______________|
	// | ......break;. |......|______________|
	// | ..default: .. |......|______________|
	// | ....stmt1; .. |
	// |_______________|
	// TODO
	@Test
	public void testInnerBreak() {
		assertTransformedSST();
	}

	// ---------------------------- asserts -----------------------------------

	private void assertSSTs(ISST expectedSST, ISST sst) {
		sst.accept(sut, null);
		Set<IMethodDeclaration> methods = sst.getMethods();
		Set<IMethodDeclaration> expectedMethods = expectedSST.getMethods();
		List<IStatement> body = methods.iterator().next().getBody();
		List<IStatement> expectedBody = expectedMethods.iterator().next().getBody();

		assertThat(methods.size(), equalTo(1));
		assertThat(methods.size(), equalTo(expectedMethods.size()));
		assertThat(body.size(), equalTo(expectedBody.size()));

		for (int i = 0; i < body.size(); i++) {
			assertThat(body.get(i), equalTo(expectedBody.get(i)));
		}
	}

	private void assertTransformedSST() {
		assertSSTs(expectedSST, sst);
	}

	// ---------------------------- helpers -----------------------------------

	private void setSections(ICaseBlock... caseBlocks) {
		switchBlock.setSections(Lists.newArrayList(caseBlocks));
	}

	private void setDefaultSection(IStatement... defaultStatements) {
		switchBlock.setDefaultSection(Lists.newArrayList(defaultStatements));
	}

	private void setExpected(IStatement... statements) {
		expectedMethod.setBody(Lists.newArrayList(statements));
	}

	private IVariableDeclaration booleanDec(IVariableReference ref) {
		VariableDeclaration var = new VariableDeclaration();
		var.setReference(ref);
		var.setType(TypeName.newTypeName("System.Boolean"));
		return var;
	}

	private IStatement dummyStatement(int i) {
		return SSTUtil.declareVar("dummy" + i);
	}

	private IfElseBlock ifCond(IVariableReference ref) {
		return ifCond(refExpr(ref));
	}

	private IfElseBlock ifCond(ISimpleExpression condition) {
		IfElseBlock ifElse = new IfElseBlock();
		ifElse.setCondition(condition);
		return ifElse;
	}

	private void setThen(IfElseBlock ifElse, IStatement... statements) {
		ifElse.setThen(Lists.newArrayList(statements));
	}

	private void setElse(IfElseBlock ifElse, IStatement... statements) {
		ifElse.setElse(Lists.newArrayList(statements));
	}
}