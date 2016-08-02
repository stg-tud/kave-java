/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.java_printer.javaPrinterTestSuite;

import org.junit.Test;

import com.google.common.collect.Lists;

import static cc.kave.commons.model.ssts.impl.SSTUtil.*;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class StatementPrinterTest extends JavaPrintingVisitorBaseTest {
	@Test
	public void testAssignment() {
		IAssignment sst = assignmentToLocal("var", constant("true"));
		assertPrint(sst, "var = true;");
	}

	@Test
	public void testPropertySet() {
		assertPrint(
				assign(propertyReference(varRef("this"),
						"get set [PropertyType,P] [DeclaringType,P].P"),
						referenceExprToVariable("var")), //
				"this.setP(var);");
	}

	@Test
	public void testPropertyGet() {
		assertPrint(
				assign(varRef("var"),
						refExpr(propertyReference(varRef("this"),
								"get set [PropertyType,P] [DeclaringType,P].P"))), //
				"var = this.getP();");
	}
	
	@Test
	public void testPropertySet_Static() {
		assertPrint(
				assign(propertyReference(varRef("this"),
						"static get set [PropertyType,P] [DeclaringType,P].P"),
						referenceExprToVariable("var")), //
				"DeclaringType.setP(var);");
	}

	@Test
	public void testPropertyGet_Static() {
		assertPrint(
				assign(varRef("var"),
						refExpr(propertyReference(varRef("this"),
								"static get set [PropertyType,P] [DeclaringType,P].P"))), //
				"var = DeclaringType.getP();");
	}

	@Test
	public void testGotoStatement() {
		GotoStatement sst = new GotoStatement();
		sst.setLabel("L");

		assertPrint(sst, "");
	}
	
	@Test
	public void testEmptyReturnStatementInMethodDeclaration() {
		IMethodDeclaration methodDecl = declareMethod(method(type("s:System.Boolean"), type("Class1, P1"), "m1"), true, new ReturnStatement());
		assertPrint(methodDecl, "boolean m1()", "{", "    return false;", "}");
	}
	
	@Test
	public void testEmptyReturnStatementInPropertyDeclaration() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(PropertyName.newPropertyName("get [s:System.Int32,P] [DeclaringType,P].P"));
		propertyDecl.setGet(Lists.newArrayList(new ReturnStatement()));
		assertPrint(propertyDecl, "int getP()", "{", "    return 0;", "}", "");
	}
}
