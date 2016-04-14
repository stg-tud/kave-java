/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.tests;

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public abstract class TestBuilder {

	public IVariableDeclaration declare(String id, ITypeName type) {
		VariableDeclaration varDecl = new VariableDeclaration();
		varDecl.setReference(variableReference(id));
		varDecl.setType(type);
		return varDecl;
	}

	public IFieldDeclaration declare(IFieldName field) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setName(field);
		return fieldDecl;
	}

	public IPropertyDeclaration declare(IPropertyName property) {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(property);
		propertyDecl.setGet(Collections.emptyList());
		propertyDecl.setSet(Collections.emptyList());
		return propertyDecl;
	}

	public ITypeName type(String id) {
		return TypeName.newTypeName(id + ",Test");
	}

	public ITypeName voidType() {
		return TypeName.newTypeName("System.Void, mscorlib");
	}

	public IMethodName method(ITypeName declType, String name, ITypeName... parameters) {
		return method(voidType(), declType, name, parameters);
	}

	public IMethodName method(ITypeName retType, ITypeName declType, String name, ITypeName... parameters) {
		String parameterIdentifiers = IntStream.range(0, parameters.length)
				.mapToObj(i -> "[" + parameters[i].getIdentifier() + "] p" + i).collect(Collectors.joining(", "));
		return MethodName.newMethodName("[" + retType.getIdentifier() + "] [" + declType.getIdentifier() + "]." + name
				+ "(" + parameterIdentifiers + ")");
	}

	public IFieldName field(ITypeName type, ITypeName declType, int id) {
		return FieldName.newFieldName("[" + type.getIdentifier() + "] [" + declType.getIdentifier() + "].f" + id);
	}

	public IPropertyName property(ITypeName type, ITypeName declType, int id) {
		return PropertyName.newPropertyName(
				"get set [" + type.getIdentifier() + "] [" + declType.getIdentifier() + "].p" + id + "()");
	}

	public IConstantValueExpression constantExpr() {
		return new ConstantValueExpression();
	}

	public IAssignment assign(String dest, IAssignableExpression srcExpr) {
		Assignment assignment = new Assignment();
		assignment.setReference(variableReference(dest));
		assignment.setExpression(srcExpr);
		return assignment;
	}

	public IAssignment assign(String dest, String src) {
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(variableReference(src));
		Assignment assignment = new Assignment();
		assignment.setReference(variableReference(dest));
		assignment.setExpression(refExpr);
		return assignment;
	}

	public IInvocationExpression invoke(String recv, IMethodName method) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setReference(variableReference(recv));
		invocation.setMethodName(method);
		invocation.setParameters(Collections.emptyList());
		return invocation;
	}

	public IReferenceExpression refExpr(IReference ref) {
		ReferenceExpression referenceExpression = new ReferenceExpression();
		referenceExpression.setReference(ref);
		return referenceExpression;
	}

	public IExpressionStatement exprStmt(IAssignableExpression expr) {
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(expr);
		return stmt;
	}

	public Context context(ITypeName type, Set<IMethodDeclaration> methods, Set<IFieldDeclaration> fields,
			Set<IPropertyDeclaration> properties) {
		SST sst = new SST();
		sst.setEnclosingType(type);
		sst.setDelegates(Collections.emptySet());
		sst.setEvents(Collections.emptySet());
		sst.setFields(fields);
		sst.setMethods(methods);
		sst.setProperties(properties);

		TypeShape typeShape = new TypeShape();
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(type);
		typeShape.setTypeHierarchy(typeHierarchy);
		Set<IMethodHierarchy> methodHierarchies = methods.stream().filter(IMethodDeclaration::isEntryPoint).map(md -> {
			MethodHierarchy methodHierarchy = new MethodHierarchy();
			methodHierarchy.setElement(md.getName());
			return methodHierarchy;
		}).collect(Collectors.toCollection(Sets::newLinkedHashSet));
		typeShape.setMethodHierarchies(methodHierarchies);

		Context context = new Context();
		context.setSST(sst);
		context.setTypeShape(typeShape);
		return context;
	}

}
