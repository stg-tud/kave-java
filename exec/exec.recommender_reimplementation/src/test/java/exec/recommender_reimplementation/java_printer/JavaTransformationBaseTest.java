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
package exec.recommender_reimplementation.java_printer;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class JavaTransformationBaseTest {

	protected JavaTransformationVisitor sut;

	public void assertNode(ISSTNode node, ISSTNode expected) {
		sut = new JavaTransformationVisitor(node);
		ISSTNode actual = sut.transform(node);
		assertEquals(expected, actual);
	}

	public void assertAroundMethodDeclaration(List<IStatement> testStatements, IStatement... expectedStatements) {
		IMethodDeclaration methodDecl = methodDeclaration(testStatements);
		sut = new JavaTransformationVisitor(methodDecl);
		IMethodDeclaration actual = sut.transform(methodDecl, IMethodDeclaration.class);
		IMethodDeclaration expected = SSTUtil.declareMethod(expectedStatements);
		assertEquals(expected, actual);
	}

	public void assertPropertyDeclaration(PropertyDeclaration testDecl, ISST expected) {
		SST sst = new SST();
		sst.getProperties().add(testDecl);
		sut = new JavaTransformationVisitor(sst);
		ISST actual = sut.transform(sst, ISST.class);
		assertEquals(expected, actual);
	}

	public IMethodDeclaration methodDeclaration(List<IStatement> statements) {
		MethodDeclaration methodDecl = new MethodDeclaration();
		methodDecl.setBody(statements);
		return methodDecl;
	}

	protected IFieldName field(ITypeName valType, ITypeName declType, String fieldName) {
		String field = String.format("[%1$s] [%2$s].%3$s", valType, declType, fieldName);
		return FieldName.newFieldName(field);
	}

	protected static IMethodName method(ITypeName returnType, ITypeName declType, String simpleName,
			IParameterName... parameters) {
		String parameterStr = Joiner.on(", ")
				.join(Arrays.asList(parameters).stream().map(p -> p.getIdentifier()).toArray());
		String methodIdentifier = String.format("[%1$s] [%2$s].%3$s(%4$s)", returnType, declType, simpleName,
				parameterStr);
		return MethodName.newMethodName(methodIdentifier);
	}

	protected IParameterName parameter(ITypeName valType, String paramName) {
		String param = String.format("[%1$s] %2$s", valType, paramName);
		return ParameterName.newParameterName(param);
	}

	protected ITypeName type(String simpleName) {
		return TypeName.newTypeName(simpleName + ",P1");
	}

	protected ConstantValueExpression constant(String value) {
		ConstantValueExpression expr = new ConstantValueExpression();
		expr.setValue(value);
		return expr;
	}

	protected IInvocationExpression invocation(String identifier, IMethodName methodName,
			ISimpleExpression... parameters) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setReference(varRef(identifier));
		invocation.setMethodName(methodName);
		invocation.setParameters(Lists.newArrayList(parameters));
		return invocation;
	}

	protected IFieldReference fieldRef(String identifier, IFieldName fieldName) {
		FieldReference fieldReference = new FieldReference();
		fieldReference.setFieldName(fieldName);
		fieldReference.setReference(varRef(identifier));
		return fieldReference;
	}

	protected IFieldDeclaration fieldDecl(IFieldName fieldName) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setName(fieldName);
		return fieldDecl;
	}

	protected IMethodDeclaration methodDecl(IMethodName methodName, IStatement... statements) {
		return methodDecl(methodName, Lists.newArrayList(statements));
	}

	protected IMethodDeclaration methodDecl(IMethodName methodName, List<IStatement> statements) {
		MethodDeclaration methodDecl = new MethodDeclaration();
		methodDecl.setName(methodName);
		methodDecl.setBody(statements);
		methodDecl.setEntryPoint(true);
		return methodDecl;
	}

	protected VariableReference varRef(String identifier) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(identifier);
		return ref;
	}

	protected ISST defaultSSTWithBackingField(IFieldDeclaration fieldDecl, IMethodDeclaration... methodDecls) {
		SST sst = new SST();
		sst.getFields().add(fieldDecl);
		sst.setMethods(Sets.newHashSet(methodDecls));
		return sst;
	}

	protected ISST defaultSST(IMethodDeclaration... methodDecls) {
		SST sst = new SST();
		sst.setMethods(Sets.newHashSet(methodDecls));
		return sst;
	}
}
