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

import static exec.recommender_reimplementation.java_printer.JavaPrintingUtils.appendImportListToString;
import static exec.recommender_reimplementation.java_printer.JavaPrintingUtils.getUsedTypes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;
import cc.kave.commons.utils.sstprinter.SSTPrintingVisitor;

public class JavaPrintingVisitor extends SSTPrintingVisitor {

	private static final String C_SHARP_CONVERTER_TO_BOOL_METHOD_NAME = "CSharpConverter.toBool";
	private boolean setPublicModifier;

	public JavaPrintingVisitor(ISSTNode sst, boolean setPublicModifier) {
		this.setPublicModifier = setPublicModifier;
	}

	@SuppressWarnings("serial")
	public class InvalidJavaCodeException extends RuntimeException {
	}

	@Override
	public Void visit(IDelegateDeclaration stmt, SSTPrintingContext context) {
		throw new InvalidJavaCodeException();
	}

	@Override
	public Void visit(IEventDeclaration stmt, SSTPrintingContext context) {
		throw new InvalidJavaCodeException();
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, SSTPrintingContext context) {
		throw new InvalidJavaCodeException();
	}

	@Override
	public Void visit(ISST sst, SSTPrintingContext context) {
		addPackageName(sst.getEnclosingType(), context);

		appendImportListToString(getUsedTypes(sst), context);

		context.indentation();
		addPublicModifier(context);

		if (sst.getEnclosingType().isInterfaceType()) {
			context.keyword("interface");
		} 
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
			else {
				context.text(" extends ");
				context.text("Object");
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
		else if (!sst.getEnclosingType().getName().equals("Object")) {
			context.text(" extends ");
			context.text("Object");
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

	private void addPackageName(ITypeName enclosingType, SSTPrintingContext context) {
		String packageName = getPackageName(enclosingType);
		if (!packageName.isEmpty()) {
			context.indentation().text("package").space().text(packageName).newLine();
		}
	}

	private String getPackageName(ITypeName type) {
		String[] packages = type.getFullName().split("\\.");
		String fullPackageName = "";
		for (int i = 0; i < packages.length - 1; i++) {
			String packageName = packages[i];
			if (i < packages.length - 2) {
				fullPackageName += packageName + ".";
			} else {
				fullPackageName += packageName + ";";
			}
		}
		return fullPackageName;
	}

	private void addPublicModifier(SSTPrintingContext context) {
		if(setPublicModifier) {
			context.text("public").space();
		}
		
	}

	@Override
	public Void visit(IMethodDeclaration stmt, SSTPrintingContext context) {
		context.indentation();

		if (isOverride(stmt, context)) {
			context.text("@Override").newLine().indentation();
		}

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
	
	private boolean isOverride(IMethodDeclaration stmt, SSTPrintingContext context) {
		if (context.typeShape != null) {
			Set<IMethodHierarchy> methodHierarchies = context.typeShape.getMethodHierarchies();
			for (IMethodHierarchy methodHierarchy : methodHierarchies) {
				if (methodHierarchy.getElement().equals(stmt.getName())) {
					if (methodHierarchy.getFirst() != null
							|| methodHierarchy.getSuper() != null) {
						return true;
					}
				}
			}
		}
		return false;
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
		throw new InvalidJavaCodeException();
	}

	@Override
	public Void visit(IVariableDeclaration stmt, SSTPrintingContext context) {
		context.indentation().type(stmt.getType()).space();
		stmt.getReference().accept(this, context);
		context.text(" = ").text("null").text(";");
		return null;
	}

	@Override
	public Void visit(IDoLoop block, SSTPrintingContext context) {
		ISimpleExpression condition = getSimpleConditionOrAppendLoopBlock(block.getCondition(), context);

		context.indentation().keyword("do");

		List<IStatement> statementListWithLoopHeader = Lists.newArrayList(
				Iterables.concat(block.getBody(), getLoopHeaderBlockWithoutDeclaration(block.getCondition())));

		context.statementBlock(statementListWithLoopHeader, this, true);

		context.newLine().indentation().keyword("while").space().text("(").text(C_SHARP_CONVERTER_TO_BOOL_METHOD_NAME)
				.text("(");
		condition.accept(this, context);
		context.text("));");
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, SSTPrintingContext context) {
		context.indentation().keyword("if").space().text("(").text(C_SHARP_CONVERTER_TO_BOOL_METHOD_NAME).text("(");
		block.getCondition().accept(this, context);
		context.text("))");

		context.statementBlock(block.getThen(), this, true);

		if (!block.getElse().isEmpty()) {
			context.newLine().indentation().keyword("else");

			context.statementBlock(block.getElse(), this, true);
		}
		return null;
	}

	@Override
	public Void visit(IForLoop block, SSTPrintingContext context) {
		statementBlockWithoutIndent(block.getInit(), context);

		ISimpleExpression condition = getSimpleConditionOrAppendLoopBlock(block.getCondition(), context);

		context.indentation().keyword("for").space().text("(").text(";").text(C_SHARP_CONVERTER_TO_BOOL_METHOD_NAME)
				.text("(");
		condition.accept(this, context);
		context.text(")").text(";").text(")");

		List<IStatement> statementListWithLoopHeader = Lists.newArrayList(Iterables.concat(block.getBody(),
				block.getStep(), getLoopHeaderBlockWithoutDeclaration(block.getCondition())));

		context.statementBlock(statementListWithLoopHeader, this, true);

		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, SSTPrintingContext context) {
		// ignores unchecked keyword
		statementBlockWithoutIndent(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, SSTPrintingContext context) {
		ISimpleExpression condition = getSimpleConditionOrAppendLoopBlock(block.getCondition(), context);

		context.indentation().keyword("while").space().text("(").text(C_SHARP_CONVERTER_TO_BOOL_METHOD_NAME).text("(");
		condition.accept(this, context);
		context.text("))");

		List<IStatement> statementListWithLoopHeader = Lists.newArrayList(
				Iterables.concat(block.getBody(), getLoopHeaderBlockWithoutDeclaration(block.getCondition())));

		context.statementBlock(statementListWithLoopHeader, this, true);

		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, SSTPrintingContext context) {
		String value2 = expr.getValue();
		if (value2 != null) {
			String value = !value2.isEmpty() ? value2 : "...";

			// Double.TryParse(expr.Value, out parsed
			if (value.equals("false")) {
				context.text("CSharpConstants.FALSE");
			} else if (value.equals("true")) {
				context.text("CSharpConstants.TRUE");
			} else if (value.matches("[0-9]+") || value.matches("[0-9]+\\.[0-9]+(f|d|m)?") || value.matches("'.'")
					|| value.equals("null")) {
				context.keyword(value);
			} else {
				context.stringLiteral(value);
			}
		}
		else {
			throw new InvalidJavaCodeException();
		}
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
		throw new InvalidJavaCodeException();
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

}
