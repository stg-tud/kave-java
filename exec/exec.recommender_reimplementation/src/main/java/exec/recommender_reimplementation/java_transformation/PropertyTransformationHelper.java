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
package exec.recommender_reimplementation.java_transformation;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.expr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationExpression;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class PropertyTransformationHelper {
	public static final String GET_PROPERTY_METHOD_NAME_FORMAT = "[%1$s] [%2$s].%3$s$%4$s()";
	public static final String SET_PROPERTY_METHOD_NAME_FORMAT = "[%1$s] [%2$s].%3$s$%4$s([%5$s] value)";
	public static final String PROPERTY_FIELD_NAME_FORMAT = "[%1$s] [%2$s].Property$%3$s";
	public static final ITypeName VOID_TYPE = TypeName.newTypeName("System.Void, mscorlib, 4.0.0.0");

	public void transformPropertyDeclaration(IPropertyDeclaration propertyDeclaration, ISST sst) {
		boolean hasGetter = !propertyDeclaration.getGet().isEmpty();
		boolean hasSetter = !propertyDeclaration.getSet().isEmpty();
		boolean hasBody = hasGetter || hasSetter;

		if (hasBody) { // add methods for getter and setter; no backing field
			if (hasGetter) {
				sst.getMethods().add(generateGetterMethodDeclarationWithBody(propertyDeclaration));
			}
			if (hasSetter) {
				sst.getMethods().add(generateSetterMethodDeclarationWithBody(propertyDeclaration));
			}
		} else { // add methods for getter and setter + backing field
			sst.getFields().add(generatePropertyBackingField(propertyDeclaration));
			IPropertyName propertyName = propertyDeclaration.getName();
			if (propertyName.hasGetter()) {
				sst.getMethods().add(generateGetterMethodDeclarationWithoutBody(propertyDeclaration));
			}
			if (propertyName.hasSetter()) {
				sst.getMethods().add(generateSetterMethodDeclarationWithoutBody(propertyDeclaration));
			}
		}
	}

	public void transformLeftHandPropertyAssignment(Assignment assignment) {
		IPropertyReference propertyReferenceGet = tryGetPropertyReferenceFromExpression(assignment.getExpression());
		if (propertyReferenceGet != null) {
			IPropertyName propertyName = propertyReferenceGet.getPropertyName();
			IInvocationExpression invocation = invocationExpression(propertyReferenceGet.getReference().getIdentifier(),
					createGetterPropertyMethodName(propertyName));
			assignment.setExpression(invocation);
		}
	}

	public IStatement transformRightHandPropertyAssignment(Assignment assignment, JavaTransformationVisitor visitor) {
		IAssignableReference reference = assignment.getReference();
		if (reference instanceof IPropertyReference) {
			IPropertyReference propertyReferenceSet = (IPropertyReference) reference;
			IPropertyName propertyName = propertyReferenceSet.getPropertyName();
			IAssignableExpression assignableExpression = (IAssignableExpression) assignment.getExpression()
					.accept(visitor,
					null);

			if (assignableExpression instanceof ISimpleExpression) {
				// transform to setter call
				IInvocationExpression invocation = generateSetterInvocation(propertyReferenceSet.getReference(),
						createSetterPropertyMethodName(propertyName), (ISimpleExpression) assignableExpression);
				return expr(invocation);
			} else {
				IFieldReference fieldReference = fieldReferenceToLocalField(
						createPropertyFieldName(propertyReferenceSet.getPropertyName()),
						propertyReferenceSet.getReference());
				assignment.setReference(fieldReference);
			}

		}
		return assignment;
	}

	public void transformPropertyReference(IReferenceExpression expr) {
		IPropertyReference propertyReference = (IPropertyReference) expr.getReference();
		IFieldReference fieldReference = fieldReferenceToLocalField(
				createPropertyFieldName(propertyReference.getPropertyName()), propertyReference.getReference());
		ReferenceExpression refExpr = (ReferenceExpression) expr;
		refExpr.setReference(fieldReference);
	}

	private IMethodDeclaration generateMethodDeclaration(IMethodName methodName, List<IStatement> statementList) {
		MethodDeclaration methodDecl = new MethodDeclaration();
		methodDecl.setName(methodName);
		methodDecl.setBody(statementList);
		methodDecl.setEntryPoint(true);
		return methodDecl;
	}

	private IFieldDeclaration generatePropertyBackingField(IPropertyDeclaration propertyDeclaration) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setName(createPropertyFieldName(propertyDeclaration.getName()));
		return fieldDecl;
	}

	private IMethodDeclaration generateSetterMethodDeclarationWithBody(IPropertyDeclaration propertyDeclaration) {
		return generateMethodDeclaration(createSetterPropertyMethodName(propertyDeclaration.getName()),
				propertyDeclaration.getSet());
	}

	private IMethodDeclaration generateSetterMethodDeclarationWithoutBody(IPropertyDeclaration propertyDeclaration) {
		return generateMethodDeclaration(createSetterPropertyMethodName(propertyDeclaration.getName()),
				Lists.newArrayList( //
						assign( //
								fieldReferenceToLocalField(createPropertyFieldName(propertyDeclaration.getName()),
										variableReference("this")),
								refExpr(variableReference("value")))));
	}

	private IMethodDeclaration generateGetterMethodDeclarationWithBody(IPropertyDeclaration propertyDeclaration) {
		return generateMethodDeclaration(createGetterPropertyMethodName(propertyDeclaration.getName()),
				propertyDeclaration.getGet());
	}

	private IMethodDeclaration generateGetterMethodDeclarationWithoutBody(IPropertyDeclaration propertyDeclaration) {
		return generateMethodDeclaration(createGetterPropertyMethodName(propertyDeclaration.getName()),
				Lists.newArrayList( //
						returnStatement( //
								refExpr( //
										fieldReferenceToLocalField(
												createPropertyFieldName(propertyDeclaration.getName()),
												variableReference("this"))))));
	}

	private IInvocationExpression generateSetterInvocation(IVariableReference reference,
			IMethodName createSetterPropertyMethodName, ISimpleExpression assignableExpression) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName(createSetterPropertyMethodName);
		invocation.setReference(reference);
		invocation.setParameters(Lists.newArrayList(assignableExpression));
		return invocation;
	}

	private IMethodName createGetterPropertyMethodName(IPropertyName propertyName) {
		String methodName = String.format(GET_PROPERTY_METHOD_NAME_FORMAT, propertyName.getValueType(),
				propertyName.getDeclaringType(), "get", getNameForProperty(propertyName));
		methodName = insertStaticModifier(propertyName, methodName);
		return MethodName.newMethodName(methodName);
	}

	private IMethodName createSetterPropertyMethodName(IPropertyName propertyName) {
		String methodName = String.format(SET_PROPERTY_METHOD_NAME_FORMAT, VOID_TYPE, propertyName.getDeclaringType(),
				"set", getNameForProperty(propertyName), propertyName.getValueType());
		methodName = insertStaticModifier(propertyName, methodName);
		return MethodName.newMethodName(methodName);
	}

	private IFieldName createPropertyFieldName(IPropertyName propertyName) {
		String fieldName = String.format(PROPERTY_FIELD_NAME_FORMAT, propertyName.getValueType(),
				propertyName.getDeclaringType(), getNameForProperty(propertyName));
		fieldName = insertStaticModifier(propertyName, fieldName);
		return FieldName.newFieldName(fieldName);
	}

	private String insertStaticModifier(IPropertyName propertyName, String memberName) {
		if (propertyName.isStatic())
			memberName = "static " + memberName;
		return memberName;
	}

	private String getNameForProperty(IPropertyName propertyName) {
		String name = propertyName.getName();
		if (name.endsWith("()")) {
			return name.substring(0, name.length() - 2);
		}
		return name;
	}

	private IFieldReference fieldReferenceToLocalField(IFieldName fieldName, IVariableReference varRef) {
		FieldReference fieldRef = new FieldReference();
		fieldRef.setReference(varRef);
		fieldRef.setFieldName(fieldName);
		return fieldRef;
	}

	private IPropertyReference tryGetPropertyReferenceFromExpression(IAssignableExpression expression) {
		if (expression instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) expression;
			if (refExpr.getReference() instanceof IPropertyReference) {
				return (IPropertyReference) refExpr.getReference();
			}
		}
		return null;
	}
}
