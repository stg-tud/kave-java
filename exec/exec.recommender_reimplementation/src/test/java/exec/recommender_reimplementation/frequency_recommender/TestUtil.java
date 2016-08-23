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
package exec.recommender_reimplementation.frequency_recommender;

import java.util.LinkedList;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;

public class TestUtil {
	public static IMethodName method1 = Names
			.newMethod("[p:void] [SSTDiff.Util.StringSimilarity, SSTDiff].ToString()");
	public static IMethodName method2 = Names
			.newMethod("[p:void] [SSTDiff.Util.StringSimilarity, SSTDiff].CompareStrings()");
	public static IMethodName method3 = Names
			.newMethod("[p:void] [SSTDiff.Util.StringSimilarity, SSTDiff].Get()");

	private static int counter;
	
	public static IMethodDeclaration CreateTestMethodDeclaration(boolean isEntryPoint) {
		LinkedList<IStatement> statementList = Lists.newLinkedList();
	
		statementList.add(CreateTestInvocation(method1));
		statementList.add(CreateTestInvocation(method2));
		statementList.add(CreateTestInvocation(method3));
		statementList.add(CreateTestInvocation(method2));
		statementList.add(CreateTestInvocation(method3));
		statementList.add(CreateTestInvocation(method2));
		statementList.add(CreateTestInvocation(method1));
		
		MethodDeclaration res = new MethodDeclaration();
		res.setBody(statementList);
		res.setEntryPoint(isEntryPoint);
		res.setName(Names.newMethod(
				"[p:void, mscorlib, 4.0.0.0], [SomeType, SomeAssembly, 1.2.3.4].SomeMethodName" + NextCounter()));
				
		return res;
	}
	
	private static int NextCounter() {
		return ++counter;
	}

	private static IExpressionStatement CreateTestInvocation(IMethodName methodName) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName(methodName);
		ExpressionStatement res = new ExpressionStatement();
		res.setExpression(invocation);
		return res;
	}
}
