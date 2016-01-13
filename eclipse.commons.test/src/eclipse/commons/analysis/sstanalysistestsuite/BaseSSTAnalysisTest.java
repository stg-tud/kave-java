/**
 * Copyright 2015 Waldemar Graf
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

package eclipse.commons.analysis.sstanalysistestsuite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import eclipse.commons.test.PluginAstParser;

public class BaseSSTAnalysisTest {

	protected SST context;

	protected MethodDeclaration newMethodDeclaration(String identifier) {
		MethodDeclaration decl = new MethodDeclaration();
		decl.setName(CsMethodName.newMethodName(identifier));

		return decl;
	}

	protected FieldDeclaration newFieldDeclaration(String identifier) {
		FieldDeclaration decl = new FieldDeclaration();
		decl.setName(CsFieldName.newFieldName(identifier));

		return decl;
	}

	protected IConstantValueExpression newConstantValue(String v) {
		ConstantValueExpression constExpr = new ConstantValueExpression();
		constExpr.setValue(v);
		return constExpr;
	}

	protected ExpressionStatement newEmptyCompletionExpression() {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(new CompletionExpression());
		return expressionStatement;
	}

	protected IVariableDeclaration newVariableDeclaration(String varName,
			TypeName type) {
		VariableDeclaration var = new VariableDeclaration();
		var.setType(type);
		var.setReference(newVariableReference(varName));

		return var;
	}

	protected static IVariableReference newVariableReference(String id) {
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier(id);

		return varRef;
	}

	protected static IAssignment newAssignment(String id,
			IAssignableExpression expr) {
		Assignment assignment = new Assignment();
		assignment.setReference(newVariableReference(id));
		assignment.setExpression(expr);
		return assignment;
	}

	protected static InvocationExpression newInvokeConstructor(
			MethodName methodName, ISimpleExpression... parameters) {
		assertThat("methodName is not a constructor",
				methodName.isConstructor());
		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName(methodName);
		invocation.setParameters(Arrays.asList(parameters));
		return invocation;
	}

	// protected void assertAllMethods(IMethodDeclaration... expectedDecls) {
	// Set<IMethodDeclaration> actualDecls = context.getMethods();
	//
	// if (expectedDecls.length != actualDecls.size()) {
	// System.out.format("\nexpected %d declarations, but was:\n",
	// expectedDecls.length);
	//
	// for (IMethodDeclaration m : actualDecls) {
	// System.out.println("-----");
	// System.out.println(m);
	// }
	//
	// assertEquals("incorrect number of method declarations",
	// expectedDecls.length, actualDecls.size());
	// }
	//
	// for (IMethodDeclaration expectedDecl : expectedDecls) {
	// if (!actualDecls.contains(expectedDecl)) {
	// System.out.println("expected:");
	// System.out.println(expectedDecl);
	// System.out.println("but was:");
	//
	// for (IMethodDeclaration m : actualDecls) {
	// System.out.println("-----");
	// System.out.println(m);
	// }
	//
	// assertTrue("expected method not found in actual list of method
	// declarations",
	// actualDecls.contains(expectedDecl));
	// }
	// }
	// }

	protected MethodName defaultMethodName(String qualifiedName) {
		String identifier = "[%void, rt.jar, 1.8] [" + qualifiedName
				+ ", ?].method()";
		return CsMethodName.newMethodName(identifier);
	}

	protected IMethodDeclaration getFirstMethod() {
		return context.getMethods().iterator().next();
	}

	protected IStatement getFirstStatement() {
		return getFirstMethod().getBody().get(0);
	}

	protected <Decl> List<Decl> newList(Decl... item) {
		List<Decl> list = new ArrayList<Decl>();

		for (Decl decl : item) {
			list.add(decl);
		}

		return list;
	}

	protected <Decl> Set<Decl> newSet(Decl... item) {
		Set<Decl> set = new HashSet<Decl>();

		for (Decl decl : item) {
			set.add(decl);
		}

		return set;
	}

	/*
	 * Has to be called for every new test class.
	 */
	protected void updateContext(String projectName, String packageName) {
		PluginAstParser parser = new PluginAstParser(projectName, packageName);
		context = parser.getContext();
	}
}
