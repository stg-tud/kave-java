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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.utils.SSTNodeHierarchy;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;
import cc.kave.commons.utils.sstprinter.SSTPrintingVisitor;
import static exec.recommender_reimplementation.java_printer.JavaPrintingUtils.getDefaultValueForType;

public class JavaPrintingVisitor extends SSTPrintingVisitor {

	protected SSTNodeHierarchy sstNodeHierarchy;
	protected Map<IVariableReference, ITypeName> variableTypeMap;
	private boolean setPublicModifier;

	public JavaPrintingVisitor(ISSTNode sst, boolean setPublicModifier) {
		this.setPublicModifier = setPublicModifier;
		sstNodeHierarchy = new SSTNodeHierarchy(sst);
		variableTypeMap = Maps.newHashMap();
	}

	@Override
	public Void visit(ISST sst, SSTPrintingContext context) {
		context.indentation();

		addPublicModifier(context);
		
		if (sst.getEnclosingType().isInterfaceType()) {
			context.keyword("interface");
		} 
//		else if (sst.getEnclosingType().isEnumType()) {
//			context.keyword("enum");
//		} 
		else {
			context.keyword("class");
		}

		context.space().type(sst.getEnclosingType());
		if (context.typeShape != null && context.typeShape.getTypeHierarchy().hasSupertypes()) {

			ITypeHierarchy extends1 = context.typeShape.getTypeHierarchy().getExtends();
			if (context.typeShape.getTypeHierarchy().hasSuperclass() && extends1 != null) {
				context.text(" extends ");
				context.type(extends1.getElement());
			}

			if (context.typeShape.getTypeHierarchy().isImplementingInterfaces()) {
				context.text(" implements ");
			}

			int index = 0;
			for (ITypeHierarchy i : context.typeShape.getTypeHierarchy().getImplements()) {
				context.type(i.getElement());
				index++;
				if (index != context.typeShape.getTypeHierarchy().getImplements().size()) {
					context.text(", ");
				}
			}
		}

		context.newLine().indentation().text("{").newLine();

		context.indentationLevel++;

		appendMemberDeclarationGroup(context, sst.getDelegates().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(context, sst.getEvents().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(context, sst.getFields().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(context, sst.getProperties().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(context, sst.getMethods().stream().collect(Collectors.toSet()), 2, 1);

		context.indentationLevel--;

		context.indentation().text("}");
		return null;
	}

	private void addPublicModifier(SSTPrintingContext context) {
		if(setPublicModifier) {
			context.text("public").space();
		}
		
	}

	@Override
	public Void visit(IDelegateDeclaration stmt, SSTPrintingContext context) {
		// could implement delegates as interfaces with one method
		// but for now ignored
		return null;
	}

	@Override
	public Void visit(IEventDeclaration stmt, SSTPrintingContext context) {
		// construct does not exist in java; hard
		// to implement in general case
		// ignored
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, SSTPrintingContext context) {
		context.indentation();

		addPublicModifier(context);

		if (stmt.getName().isStatic()) {
			context.keyword("static").space();
		}

		if (stmt.getName().isConstructor()) {
			context.text(stmt.getName().getDeclaringType().getName());
		} else {
			if (stmt.getName().hasTypeParameters()) {
				context.typeParameters(stmt.getName().getTypeParameters()).space();
			}
			context.type(stmt.getName().getReturnType()).space().text(stmt.getName().getName());
		}

		context.parameterList(stmt.getName().getParameters());

		context.statementBlock(stmt.getBody(), this, true);
		return null;
	}
	
	@Override
	public Void visit(IFieldDeclaration stmt, SSTPrintingContext context) {
		context.indentation();

		addPublicModifier(context);
		if (stmt.getName().isStatic()) {
			context.keyword("static").space();
		}

		context.type(stmt.getName().getValueType()).space().text(stmt.getName().getName()).text(";");
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, SSTPrintingContext context) {
		boolean hasBody = !stmt.getGet().isEmpty() || !stmt.getSet().isEmpty();
		String propertyName = stmt.getName().getName();
		if (propertyName.endsWith("()")) {
			propertyName = propertyName.replace("()", "");
		}

		if (hasBody) { // Long version: add methods for getter and setter; no
						// backing field

			if (stmt.getName().hasGetter()) {
				context.indentation();

				addPublicModifier(context);
				addStaticModifierForProperty(stmt, context);

				context.type(stmt.getName().getValueType()).space().text("get" + propertyName).text("()");

				appendPropertyAccessor(context, stmt.getGet());

			}

			if (stmt.getName().hasSetter()) {
				context.indentation();

				addPublicModifier(context);
				addStaticModifierForProperty(stmt, context);

				context.text("void").space().text("set" + propertyName).text("(").type(stmt.getName().getValueType())
						.space().text("value").text(")");

				appendPropertyAccessor(context, stmt.getSet());

			}
		} else // Short Version: add methods for getter and setter + backing
				// field
		{
			String backingFieldName = "$property_" + propertyName;
			context.indentation();

			addPublicModifier(context);
			addStaticModifierForProperty(stmt, context);

			context.type(stmt.getName().getValueType()).space().text(backingFieldName).text(";");

			context.newLine();

			if (stmt.getName().hasGetter()) {
				context.indentation();

				addPublicModifier(context);
				addStaticModifierForProperty(stmt, context);

				context.type(stmt.getName().getValueType()).space().text("get" + propertyName).text("()");

				context.newLine().indentation();

				context.text("{").newLine();
				context.indentationLevel++;

				context.indentation().text("return").space().text(backingFieldName).text(";").newLine();

				context.indentationLevel--;
				context.indentation().text("}").newLine();

			}

			if (stmt.getName().hasSetter()) {
				context.indentation();

				addPublicModifier(context);
				addStaticModifierForProperty(stmt, context);

				context.text("void").space().text("set" + propertyName).text("(").type(stmt.getName().getValueType())
						.space().text("value").text(")");

				context.newLine().indentation();

				context.text("{").newLine();
				context.indentationLevel++;

				context.indentation().text(backingFieldName).text(" = ").text("value").text(";").newLine();

				context.indentationLevel--;
				context.indentation().text("}").newLine();

			}
		}
		return null;
	}

	private void addStaticModifierForProperty(IPropertyDeclaration stmt, SSTPrintingContext context) {
		if (stmt.getName().isStatic()) {
			context.keyword("static").space();
		}
	}

	@Override
	public Void visit(IVariableDeclaration variableDecl, SSTPrintingContext context) {
		addVariableReferenceToTypeMapping(variableDecl);
		return super.visit(variableDecl, context);
	}

	@Override
	public Void visit(IAssignment assignment, SSTPrintingContext context) {
		// Handle Property Get
		IPropertyReference propertyReferenceGet = tryGetPropertyReferenceFromExpression(assignment.getExpression());
		if (propertyReferenceGet != null) {
			String propertyName = propertyReferenceGet.getPropertyName().getName();
			if (propertyName.endsWith("()")) {
				propertyName = propertyName.replace("()", "");
			}
			context.indentation();
			assignment.getReference().accept(this, context);
			context.text(" = ");
			addPropertyReferenceOrStaticType(propertyReferenceGet, context);
			context.text(".");
			context.text("get").text(propertyName).text("(").text(")").text(";");
		} else {
			// Handle Property Set
			IAssignableReference reference = assignment.getReference();
			if (reference instanceof IPropertyReference) {
				IPropertyReference propertyReferenceSet = (IPropertyReference) reference;
				String propertyName = propertyReferenceSet.getPropertyName().getName();
				if (propertyName.endsWith("()")) {
					propertyName = propertyName.replace("()", "");
				}
				context.indentation();
				addPropertyReferenceOrStaticType(propertyReferenceSet, context);
				context.text(".").text("set").text(propertyName).text("(");
				assignment.getExpression().accept(this, context);
				context.text(")").text(";");
			} else {
				context.indentation();
				assignment.getReference().accept(this, context);
				context.text(" = ");
				assignment.getExpression().accept(this, context);
				context.text(";");
			}
		}

		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, SSTPrintingContext context) {
		// unused in java
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, SSTPrintingContext context) {
		// ignores unchecked keyword
		statementBlockWithoutIndent(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, SSTPrintingContext context) {
		// unsafe not implemented in java
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, SSTPrintingContext context) {
		String value2 = expr.getValue();
		if (value2 != null) {
			String value = !value2.isEmpty() ? value2 : "...";

			// Double.TryParse(expr.Value, out parsed
			if (value.equals("false") || value.equals("true") || value.matches("[0-9]+")
					|| value.matches("[0-9]+\\.[0-9]+") || value.equals("null")) {
				context.keyword(value);
			} else {
				context.stringLiteral(value);
			}
		} else {
			context.text(getDefaultValueForNode(sstNodeHierarchy.getParent(expr)));
		}
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, SSTPrintingContext context) {
		// ignore completion expressions
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, SSTPrintingContext context) {
		context.text(getDefaultValueForNode(sstNodeHierarchy.getParent(expr)));
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, SSTPrintingContext context) {
		context.parameterList(expr.getName().getParameters()).space().text("->");
		context.statementBlock(expr.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(IPropertyReference propertyRef, SSTPrintingContext context) {
		addPropertyReferenceOrStaticType(propertyRef, context);
		context.text(".");
		// converts property reference to reference to created backing field
		String propertyName = propertyRef.getPropertyName().getName();
		if (propertyName.endsWith("()")) {
			propertyName = propertyName.replace("()", "");
		}
		context.text("$property_" + propertyName);
		return null;
	}

	private void addPropertyReferenceOrStaticType(IPropertyReference propertyRef, SSTPrintingContext context) {
		if (propertyRef.getPropertyName().isStatic()) {
			context.type(propertyRef.getPropertyName().getDeclaringType());
		} else {
			context.text(propertyRef.getReference().getIdentifier());
		}
	}

	@Override
	public Void visit(ICastExpression expr, SSTPrintingContext context) {
		if (expr.getOperator() == CastOperator.SafeCast) {
			// handles safe cast by using ?-operator
			context.text(expr.getReference().getIdentifier()).text(" instanceof ").type(expr.getTargetType()).space()
					.text("?").space().text("(").type(expr.getTargetType()).text(") ")
					.text(expr.getReference().getIdentifier()).text(" : ").text("null");
		} else {
			context.text("(" + expr.getTargetType().getName() + ") ");
			context.text(expr.getReference().getIdentifier());
		}
		return null;
	}

	@Override
	public Void visit(IIndexAccessExpression expr, SSTPrintingContext context) {
		if (variableTypeMap.containsKey(expr.getReference())) {
			ITypeName containerType = variableTypeMap.get(expr.getReference());
			context.text(expr.getReference().getIdentifier());
			if (containerType.isArrayType()) {
				context.text("[");
				printIndices(expr, context);
				context.text("]");
			} else {
				context.text(".").text("get").text("(");
				printIndices(expr, context);
				context.text(")");
			}
		}
		// if type not found expression is ignored
		return null;
	}

	private void printIndices(IIndexAccessExpression expr, SSTPrintingContext context) {
		for (int i = 0; i < expr.getIndices().size(); i++) {
			expr.getIndices().get(i).accept(this, context);
			if (i < expr.getIndices().size() - 1)
				context.text(",");
		}
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, SSTPrintingContext context) {
		context.text(getDefaultValueForNode(sstNodeHierarchy.getParent(unknownExpr)));
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, SSTPrintingContext context) {
		// ignores UnknownStatement
		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, SSTPrintingContext context) {
		// ignores EventSubscription Statement
		return null;
	}

	private void addVariableReferenceToTypeMapping(IVariableDeclaration varDecl) {
		variableTypeMap.put(varDecl.getReference(), varDecl.getType());
	}

	private Void appendPropertyAccessor(SSTPrintingContext context, List<IStatement> body) {
		context.statementBlock(body, this, true);

		context.newLine();
		return null;
	}

	private IMemberDeclaration findMemberDeclaration(ISSTNode sstNode) {
		ISSTNode parent = sstNodeHierarchy.getParent(sstNode);
		while (!(parent instanceof IMemberDeclaration)) {
			parent = sstNodeHierarchy.getParent(parent);
		}

		return (IMemberDeclaration) parent;
	}

	protected String getDefaultValueForNode(ISSTNode node) {
		if (node instanceof IAssignableExpression) {
			return getDefaultValueForAssignableExpression((IAssignableExpression) node);
		}

		if (node instanceof IIfElseBlock) {
			return "false";
		}

		if (node instanceof IReturnStatement) {
			return getDefaultValueForReturnStatement((IReturnStatement) node);
		}

		if (node instanceof IAssignment) {
			return getDefaultValueForAssignment((IAssignment) node);
		}
		return "null";
	}

	protected String getDefaultValueForAssignableExpression(IAssignableExpression assignableExpression) {
		ISSTNode parentStatement = sstNodeHierarchy.getParent(assignableExpression);
		if (parentStatement instanceof IAssignment) {
			return getDefaultValueForAssignment((IAssignment) parentStatement);
		}
		return "null";
	}

	protected String getDefaultValueForAssignment(IAssignment assignment) {
		IAssignableReference assignableReference = assignment.getReference();
		if (assignableReference instanceof IVariableReference) {
			return getDefaultValueForVariableReference((IVariableReference) assignment.getReference());
		}

		return "null";
	}

	protected String getDefaultValueForReturnStatement(IReturnStatement returnStatement) {
		IMemberDeclaration memberDeclaration = findMemberDeclaration(returnStatement);

		if (memberDeclaration instanceof IMethodDeclaration) {
			IMethodDeclaration methodDeclaration = (IMethodDeclaration) memberDeclaration;
			return getDefaultValueForType(methodDeclaration.getName().getReturnType());
		}
		if (memberDeclaration instanceof IPropertyDeclaration) {
			IPropertyDeclaration propertyDeclaration = (IPropertyDeclaration) memberDeclaration;
			return getDefaultValueForType(propertyDeclaration.getName().getValueType());
		}

		return "null";
	}

	protected String getDefaultValueForVariableReference(IVariableReference reference) {
		if (variableTypeMap.containsKey(reference)) {
			return getDefaultValueForType(variableTypeMap.get(reference));
		}
		return "null";
	}

	protected IPropertyReference tryGetPropertyReferenceFromExpression(IAssignableExpression expression) {
		if (expression instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) expression;
			if (refExpr.getReference() instanceof IPropertyReference) {
				return (IPropertyReference) refExpr.getReference();
			}
		}
		return null;
	}

}
