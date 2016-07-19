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

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
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

			context.text(" extends ");

			ITypeHierarchy extends1 = context.typeShape.getTypeHierarchy()
					.getExtends();
			if (context.typeShape.getTypeHierarchy().hasSuperclass()
					&& extends1 != null) {
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
		// TODO could implement delegates as interfaces with one method
		return null;
	}

	@Override
	public Void visit(IEventDeclaration stmt, SSTPrintingContext context) {
		// TODO how to handle events -> construct does not exist in java; hard
		// to implement in general case
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, SSTPrintingContext context) {
		boolean hasBody = !stmt.getGet().isEmpty() || !stmt.getSet().isEmpty();

		if (hasBody) { // Long version: add methods for getter and setter; no
						// backing field

			if (stmt.getName().hasGetter()) {
				context.indentation().type(stmt.getName().getValueType())
						.space().text("get" + stmt.getName().getName())
						.text("()");

				context.newLine().indentation();

				context.text("{").newLine();
				context.indentationLevel++;
				appendPropertyAccessor(context, stmt.getGet(), "");
				context.indentationLevel--;
				context.text("}").newLine();

			}

			if (stmt.getName().hasSetter()) {
				context.indentation().type(stmt.getName().getValueType())
						.space().text("set" + stmt.getName().getName())
						.text("(").type(stmt.getName().getValueType()).space()
						.text("value").text(")");

				context.newLine().indentation();

				context.text("{").newLine();
				context.indentationLevel++;
				appendPropertyAccessor(context, stmt.getSet(), "");
				context.indentationLevel--;
				context.text("}").newLine();

			}
		} else // Short Version: add methods for getter and setter + backing
				// field
		{
			String backingFieldName = "$property_" + stmt.getName().getName();
			context.indentation().type(stmt.getName().getValueType()).space()
					.text(backingFieldName);

			context.newLine();

			if (stmt.getName().hasGetter()) {
				context.indentation().type(stmt.getName().getValueType())
						.space().text("get" + stmt.getName().getName());

				context.newLine().indentation();

				context.text("{").newLine();
				context.indentationLevel++;

				context.indentation().text("return").space()
						.text(backingFieldName);

				context.indentationLevel--;
				context.text("}").newLine();

			}

			if (stmt.getName().hasSetter()) {
				context.indentation().type(stmt.getName().getValueType())
						.space().text("set" + stmt.getName().getName());

				context.newLine().indentation();

				context.text("{").newLine();
				context.indentationLevel++;

				context.indentation().text(backingFieldName).text(" = ")
						.text("value");

				context.indentationLevel--;
				context.text("}").newLine();

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
			context.indentation();
			assignment.getReference().accept(this, context);
			context.text(" = ");
			context.indentation().text("get")
					.text(propertyReferenceGet.getPropertyName().getName())
					.text("(").text(")").text(";");
		} else {
			// Handle Property Set
			IAssignableReference reference = assignment.getReference();
			if (reference instanceof IPropertyReference) {
				IPropertyReference propertyReferenceSet = (IPropertyReference) reference;
				context.indentation().text("set")
						.text(propertyReferenceSet.getPropertyName().getName())
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

	@Override
	public Void visit(IGotoStatement stmt, SSTPrintingContext context) {
		// unused in java
		return null;
	}

	@Override
	public Void visit(IDoLoop block, SSTPrintingContext context) {
		ISimpleExpression condition;
		if (block.getCondition() instanceof ILoopHeaderBlockExpression) {
			condition = appendLoopHeaderBlock(
					(ILoopHeaderBlockExpression) block.getCondition(), context);
		} else {
			condition = (ISimpleExpression) block.getCondition();
		}

		context.indentation().keyword("do");

		List<IStatement> statementListWithLoopHeader = Lists.newArrayList(block
				.getBody());
		if (block.getCondition() instanceof ILoopHeaderBlockExpression) {
			statementListWithLoopHeader
					.addAll(getLoopHeaderBlockWithoutDeclaration((ILoopHeaderBlockExpression) block
							.getCondition()));
		}

		context.statementBlock(statementListWithLoopHeader, this, true);

		context.newLine().indentation().keyword("while").space().text("(");
		context.indentationLevel++;
		condition.accept(this, context);
		context.indentationLevel--;
		context.newLine().indentation().text(")");
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, SSTPrintingContext context) {
		ISimpleExpression condition;
		if (block.getCondition() instanceof ILoopHeaderBlockExpression) {
			condition = appendLoopHeaderBlock(
					(ILoopHeaderBlockExpression) block.getCondition(), context);
		} else {
			condition = (ISimpleExpression) block.getCondition();
		}

		context.indentation().keyword("while").space().text("(");
		context.indentationLevel++;
		condition.accept(this, context);
		context.indentationLevel--;
		context.newLine().indentation().text(")");

		List<IStatement> statementListWithLoopHeader = Lists.newArrayList(block
				.getBody());
		if (block.getCondition() instanceof ILoopHeaderBlockExpression) {
			statementListWithLoopHeader
					.addAll(getLoopHeaderBlockWithoutDeclaration((ILoopHeaderBlockExpression) block
							.getCondition()));
		}

		context.statementBlock(statementListWithLoopHeader, this, true);

		return null;
	}

	private List<IStatement> getLoopHeaderBlockWithoutDeclaration(
			ILoopHeaderBlockExpression loopHeaderBlock) {
		List<IStatement> blockList = Lists.newArrayList();
		for (IStatement statement : loopHeaderBlock.getBody()) {
			if (statement instanceof IVariableDeclaration
					|| statement instanceof IReturnStatement)
				continue;
			blockList.add(statement);
		}
		return blockList;
	}

	private ISimpleExpression appendLoopHeaderBlock(
			ILoopHeaderBlockExpression loopHeaderBlock,
			SSTPrintingContext context) {
		for (IStatement statement : loopHeaderBlock.getBody()) {
			context.indentationLevel++;

			if (statement instanceof IReturnStatement) {
				IReturnStatement returnStatement = (IReturnStatement) statement;
				context.newLine();
				return returnStatement.getExpression();
			}

			context.newLine();
			statement.accept(this, context);

			context.indentationLevel--;
		}
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
		// TODO: how to handle composed expressions?
		context.text("(");

		for (IReference reference : expr.getReferences()) {
			reference.accept(this, context);

			if (!reference.equals(expr.getReferences().get(
					expr.getReferences().size() - 1))) {
				context.text(", ");
			}
		}

		context.text(")");
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
		// TODO: temporary property solution by using field
		context.text("$Property_" + propertyRef.getPropertyName().getName());
		return null;
	}

	@Override
	public Void visit(ICastExpression expr, SSTPrintingContext context) {
		if (expr.getOperator() == CastOperator.SafeCast) {
			// handles safe cast by using ?-operator
			context.text(expr.getReference().getIdentifier())
					.text(" instanceof ").text(expr.getTargetType().getName())
					.space().text("?").space()
					.text("(" + expr.getTargetType().getName() + ") ")
					.text(expr.getReference().getIdentifier()).text(" : ")
					.text("null");
		} else {
			context.text("(" + expr.getTargetType().getName() + ") ");
			context.text(expr.getReference().getIdentifier());
		}
		return null;
	}

	// TODO how to handle IIndexAccessExpression and IIndexAccessReference
}
