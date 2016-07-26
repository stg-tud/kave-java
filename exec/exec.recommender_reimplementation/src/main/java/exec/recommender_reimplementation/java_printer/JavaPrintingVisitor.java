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
import java.util.stream.Collectors;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;
import cc.kave.commons.utils.sstprinter.SSTPrintingVisitor;

public class JavaPrintingVisitor extends SSTPrintingVisitor {
	@Override
	public Void visit(ISST sst, SSTPrintingContext context) {
		context.indentation();

		if (sst.getEnclosingType().isInterfaceType()) {
			context.keyword("interface");
		} else if (sst.getEnclosingType().isEnumType()) {
			context.keyword("enum");
		} else {
			context.keyword("class");
		}

		context.space().type(sst.getEnclosingType());
		if (context.typeShape != null
				&& context.typeShape.getTypeHierarchy().hasSupertypes()) {

			ITypeHierarchy extends1 = context.typeShape.getTypeHierarchy()
					.getExtends();
			if (context.typeShape.getTypeHierarchy().hasSuperclass()
					&& extends1 != null) {
				context.text(" extends ");
				context.type(extends1.getElement());
			}

			if (context.typeShape.getTypeHierarchy().isImplementingInterfaces()) {
				context.text(" implements ");
			}

			int index = 0;
			for (ITypeHierarchy i : context.typeShape.getTypeHierarchy()
					.getImplements()) {
				context.type(i.getElement());
				index++;
				if (index != context.typeShape.getTypeHierarchy()
						.getImplements().size()) {
					context.text(", ");
				}
			}
		}

		context.newLine().indentation().text("{").newLine();

		context.indentationLevel++;

		appendMemberDeclarationGroup(context, sst.getDelegates().stream()
				.collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(context,
				sst.getEvents().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(context,
				sst.getFields().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(context, sst.getProperties().stream()
				.collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(context, sst.getMethods().stream()
				.collect(Collectors.toSet()), 2, 1);

		context.indentationLevel--;

		context.indentation().text("}");
		return null;
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
	public Void visit(IPropertyDeclaration stmt, SSTPrintingContext context) {
		boolean hasBody = !stmt.getGet().isEmpty() || !stmt.getSet().isEmpty();
		String propertyName = stmt.getName().getName();
		if(propertyName.endsWith("()")) {
			propertyName = propertyName.replace("()", "");
		}
		
		if (hasBody) { // Long version: add methods for getter and setter; no
						// backing field

			if (stmt.getName().hasGetter()) {
				context.indentation().type(stmt.getName().getValueType())
						.space().text("get" + propertyName)
						.text("()");

				appendPropertyAccessor(context, stmt.getGet());

			}

			if (stmt.getName().hasSetter()) {
				context.indentation().text("void").space()
						.text("set" + propertyName).text("(")
						.type(stmt.getName().getValueType()).space()
						.text("value").text(")");

				appendPropertyAccessor(context, stmt.getSet());

			}
		} else // Short Version: add methods for getter and setter + backing
				// field
		{

			String backingFieldName = "$property_" + propertyName;
			context.indentation().type(stmt.getName().getValueType()).space()
					.text(backingFieldName).text(";");

			context.newLine();

			if (stmt.getName().hasGetter()) {
				context.indentation().type(stmt.getName().getValueType())
						.space().text("get" + propertyName)
						.text("()");

				context.newLine().indentation();

				context.text("{").newLine();
				context.indentationLevel++;

				context.indentation().text("return").space()
						.text(backingFieldName).text(";").newLine();

				context.indentationLevel--;
				context.indentation().text("}").newLine();

			}

			if (stmt.getName().hasSetter()) {
				context.indentation().text("void").space()
						.text("set" + propertyName).text("(")
						.type(stmt.getName().getValueType()).space()
						.text("value").text(")");

				context.newLine().indentation();

				context.text("{").newLine();
				context.indentationLevel++;

				context.indentation().text(backingFieldName).text(" = ")
						.text("value").text(";").newLine();

				context.indentationLevel--;
				context.indentation().text("}").newLine();

			}
		}
		return null;
	}

	@Override
	public Void visit(IAssignment assignment, SSTPrintingContext context) {
		// Handle Property Get
		IPropertyReference propertyReferenceGet = expressionContainsPropertyReference(assignment
				.getExpression());
		if (propertyReferenceGet != null) {
			String propertyName = propertyReferenceGet.getPropertyName().getName();
			if(propertyName.endsWith("()")) {
				propertyName = propertyName.replace("()", "");
			}
			context.indentation();
			assignment.getReference().accept(this, context);
			context.text(" = ");
			context.text("get")
					.text(propertyName)
					.text("(").text(")").text(";");
		} else {
			// Handle Property Set
			IAssignableReference reference = assignment.getReference();
			if (reference instanceof IPropertyReference) {
				IPropertyReference propertyReferenceSet = (IPropertyReference) reference;
				String propertyName = propertyReferenceSet.getPropertyName().getName();
				if(propertyName.endsWith("()")) {
					propertyName = propertyName.replace("()", "");
				}
				context.indentation().text("set")
						.text(propertyName)
						.text("(");
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
		context.indentation().statementBlock(block.getBody(), this, true);

		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, SSTPrintingContext context) {
		// unsafe not implemented in java
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, SSTPrintingContext context) {
		// ignore completion expressions
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, SSTPrintingContext context) {
		// ignored
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, SSTPrintingContext context) {
		context.parameterList(expr.getName().getParameters()).space()
				.text("->");
		context.statementBlock(expr.getBody(), this, true);
		return null;
	}

	@Override
	public Void visit(IPropertyReference propertyRef, SSTPrintingContext context) {
		context.text(propertyRef.getReference().getIdentifier());
		context.text(".");
		// converts property reference to reference to created backing field
		String propertyName = propertyRef.getPropertyName().getName();
		if(propertyName.endsWith("()")) {
			propertyName = propertyName.replace("()", "");
		}
		context.text("$property_" + propertyName);
		return null;
	}

	@Override
	public Void visit(ICastExpression expr, SSTPrintingContext context) {
		if (expr.getOperator() == CastOperator.SafeCast) {
			// handles safe cast by using ?-operator
			context.text(expr.getReference().getIdentifier())
					.text(" instanceof ").type(expr.getTargetType()).space()
					.text("?").space().text("(").type(expr.getTargetType())
					.text(") ").text(expr.getReference().getIdentifier())
					.text(" : ").text("null");
		} else {
			context.text("(" + expr.getTargetType().getName() + ") ");
			context.text(expr.getReference().getIdentifier());
		}
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, SSTPrintingContext context) {
		// ignores UnknownStatement
		return null;
	}

	private Void appendPropertyAccessor(SSTPrintingContext context,
			List<IStatement> body) {
		context.statementBlock(body, this, true);
	
		context.newLine();
		return null;
	}

	private IPropertyReference expressionContainsPropertyReference(
			IAssignableExpression expression) {
		if (expression instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) expression;
			if (refExpr.getReference() instanceof IPropertyReference) {
				return (IPropertyReference) refExpr.getReference();
			}
		}
		return null;
	}

	// TODO: IndexAccessExpression no type information on variable reference
}
