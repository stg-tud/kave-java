package cc.kave.commons.utils.sstprinter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;

public class SSTPrintingVisitor extends AbstractNodeVisitor<SSTPrintingContext, Void> {

	public Void visit(ISST sst, SSTPrintingContext c) {
		c.indentation();

		if (sst.getEnclosingType().isInterfaceType()) {
			c.keyword("interface");
		} else if (sst.getEnclosingType().isEnumType()) {
			c.keyword("enum");
		} else if (sst.getEnclosingType().isStructType()) {
			c.keyword("struct");
		} else {
			c.keyword("class");
		}

		c.space().type(sst.getEnclosingType());
		if (c.typeShape != null && c.typeShape.getTypeHierarchy().hasSupertypes()) {

			c.text(" : ");

			if (c.typeShape.getTypeHierarchy().hasSuperclass() && c.typeShape.getTypeHierarchy().getExtends() != null) {
				c.type(c.typeShape.getTypeHierarchy().getExtends().getElement());

				if (c.typeShape.getTypeHierarchy().isImplementingInterfaces()) {
					c.text(", ");
				}
			}
			int index = 0;
			for (ITypeHierarchy i : c.typeShape.getTypeHierarchy().getImplements()) {
				c.type(i.getElement());
				index++;
				if (index != c.typeShape.getTypeHierarchy().getImplements().size()) {
					c.text(", ");
				}
			}
		}

		c.newLine().indentation().text("{").newLine();

		c.indentationLevel++;

		appendMemberDeclarationGroup(c, sst.getDelegates().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(c, sst.getEvents().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(c, sst.getFields().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(c, sst.getProperties().stream().collect(Collectors.toSet()), 1, 2);
		appendMemberDeclarationGroup(c, sst.getMethods().stream().collect(Collectors.toSet()), 2, 1);

		c.indentationLevel--;

		c.indentation().text("}");
		return null;
	}

	private Void appendMemberDeclarationGroup(SSTPrintingContext c, Set<IMemberDeclaration> nodeGroup,
			int inBetweenNewLineCount, int trailingNewLineCount) {

		List<IMemberDeclaration> nodeList = nodeGroup.stream().collect(Collectors.toList());
		for (int i = 0; i < nodeList.size(); i++) {
			IMemberDeclaration node = nodeList.get(i);
			node.accept(this, c);

			int newLinesNeeded = (i < (nodeList.size() - 1) ? inBetweenNewLineCount : trailingNewLineCount);

			for (int j = 0; j < newLinesNeeded; j++) {
				c.newLine();
			}
		}
		return null;
	}

	public Void visit(IDelegateDeclaration stmt, SSTPrintingContext c) {
		c.indentation().keyword("delegate").space().type(stmt.getName())
				.parameterList(((DelegateTypeName) stmt.getName()).getParameters()).text(";");
		return null;
	}

	public Void visit(IEventDeclaration stmt, SSTPrintingContext c) {
		c.indentation().keyword("event").space().type(stmt.getName().getHandlerType()).space()
				.text(stmt.getName().getName()).text(";");
		return null;
	}

	public Void visit(IFieldDeclaration stmt, SSTPrintingContext c) {
		c.indentation();

		if (stmt.getName().isStatic()) {
			c.keyword("static").space();
		}

		c.type(stmt.getName().getValueType()).space().text(stmt.getName().getName()).text(";");
		return null;
	}

	public Void visit(IMethodDeclaration stmt, SSTPrintingContext c) {
		c.indentation();

		if (stmt.getName().isStatic()) {
			c.keyword("static").space();
		}

		c.type(stmt.getName().getReturnType()).space().text(stmt.getName().getName());
		if (stmt.getName().hasTypeParameters()) {
			c.typeParameters(stmt.getName().getTypeParameters());
		}

		c.parameterList(stmt.getName().getParameters());

		c.statementBlock(stmt.getBody(), this, true);
		return null;
	}

	public Void visit(IPropertyDeclaration stmt, SSTPrintingContext c) {
		c.indentation().type(stmt.getName().getValueType()).space().text(stmt.getName().getName());

		boolean hasBody = !stmt.getGet().isEmpty() || !stmt.getSet().isEmpty();

		if (hasBody) // Long version: At least one body exists --> line breaks +
						// indentation
		{
			c.newLine().indentation();

			c.indentationLevel++;

			c.text("{").newLine();

			if (stmt.getName().hasGetter()) {
				appendPropertyAccessor(c, stmt.getGet(), "get");
			}

			if (stmt.getName().hasSetter()) {
				appendPropertyAccessor(c, stmt.getSet(), "set");
			}

			c.indentationLevel--;

			c.indentation().text("}");
		} else // Short Version: No bodies --> getter/setter declaration : same
				// line
		{
			c.text(" { ");
			if (stmt.getName().hasGetter()) {
				c.keyword("get").text(";").space();
			}
			if (stmt.getName().hasSetter()) {
				c.keyword("set").text(";").space();
			}
			c.text("}");
		}
		return null;
	}

	private Void appendPropertyAccessor(SSTPrintingContext c, List<IStatement> body, String keyword) {
		if (!body.isEmpty()) {
			c.indentation().text(keyword);
			c.statementBlock(body, this, true);
		} else {
			c.indentation().text(keyword).text(";");
		}

		c.newLine();
		return null;
	}

	public Void visit(IVariableDeclaration stmt, SSTPrintingContext c) {
		c.indentation().type(stmt.getType()).space();
		stmt.getReference().accept(this, c);
		c.text(";");
		return null;
	}

	public Void visit(IAssignment stmt, SSTPrintingContext c) {
		c.indentation();
		stmt.getReference().accept(this, c);
		c.text(" = ");
		stmt.getExpression().accept(this, c);
		c.text(";");
		return null;
	}

	public Void visit(IBreakStatement stmt, SSTPrintingContext c) {
		c.indentation().keyword("break").text(";");
		return null;
	}

	public Void visit(IContinueStatement stmt, SSTPrintingContext c) {
		c.indentation().keyword("continue").text(";");
		return null;
	}

	public Void visit(IExpressionStatement stmt, SSTPrintingContext c) {
		c.indentation();
		stmt.getExpression().accept(this, c);
		c.text(";");
		return null;
	}

	public Void visit(IGotoStatement stmt, SSTPrintingContext c) {
		c.indentation().keyword("goto").space().text(stmt.getLabel()).text(";");
		return null;
	}

	public Void visit(ILabelledStatement stmt, SSTPrintingContext c) {
		c.indentation().keyword(stmt.getLabel()).text(":").newLine();
		stmt.getStatement().accept(this, c);
		return null;
	}

	public Void visit(IReturnStatement stmt, SSTPrintingContext c) {
		c.indentation().keyword("return");

		if (!stmt.isVoid()) {
			c.space();
			stmt.getExpression().accept(this, c);
		}

		c.text(";");
		return null;
	}

	public Void visit(IThrowStatement stmt, SSTPrintingContext c) {
		c.indentation().keyword("throw").space().keyword("new").space().text(stmt.getException().getName()).text("();");
		return null;
	}

	public Void visit(IDoLoop block, SSTPrintingContext c) {
		c.indentation().keyword("do");

		c.statementBlock(block.getBody(), this, true);

		c.newLine().indentation().keyword("while").space().text("(");
		c.indentationLevel++;
		block.getCondition().accept(this, c);
		c.indentationLevel--;
		c.newLine().indentation().text(")");
		return null;
	}

	public Void visit(IForEachLoop block, SSTPrintingContext c) {
		c.indentation().keyword("foreach").space().text("(").type(block.getDeclaration().getType()).space();
		block.getDeclaration().getReference().accept(this, c);
		c.space().keyword("in").space();
		block.getLoopedReference().accept(this, c);
		c.text(")");

		c.statementBlock(block.getBody(), this, true);
		return null;
	}

	public Void visit(IForLoop block, SSTPrintingContext c) {
		c.indentation().keyword("for").space().text("(");

		c.indentationLevel++;

		c.statementBlock(block.getInit(), this, true);
		c.text(";");
		block.getCondition().accept(this, c);
		c.text(";");
		c.statementBlock(block.getStep(), this, true);

		c.indentationLevel--;

		c.newLine().indentation().text(")");

		c.statementBlock(block.getBody(), this, true);
		return null;
	}

	public Void visit(IIfElseBlock block, SSTPrintingContext c) {
		c.indentation().keyword("if").space().text("(");
		block.getCondition().accept(this, c);
		c.text(")");

		c.statementBlock(block.getThen(), this, true);

		if (!block.getElse().isEmpty()) {
			c.newLine().indentation().keyword("else");

			c.statementBlock(block.getElse(), this, true);
		}
		return null;
	}

	public Void visit(ILockBlock stmt, SSTPrintingContext c) {
		c.indentation().keyword("lock").space().text("(");
		stmt.getReference().accept(this, c);
		c.text(")");

		c.statementBlock(stmt.getBody(), this, true);
		return null;
	}

	public Void visit(ISwitchBlock block, SSTPrintingContext c) {
		c.indentation().keyword("switch").space().text("(");
		block.getReference().accept(this, c);
		c.text(")").newLine().indentation();
		c.indentationLevel++;
		c.text("{");

		for (ICaseBlock section : block.getSections()) {
			c.newLine().indentation().keyword("case").space();
			section.getLabel().accept(this, c);
			c.text(":").statementBlock(section.getBody(), this, false);
		}

		if (!block.getDefaultSection().isEmpty()) {
			c.newLine().indentation().keyword("default").text(":").statementBlock(block.getDefaultSection(), this,
					false);
		}

		c.newLine();
		c.indentationLevel--;
		c.indentation().text("}");
		return null;
	}

	public Void visit(ITryBlock block, SSTPrintingContext c) {
		c.indentation().keyword("try").statementBlock(block.getBody(), this, true);

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			c.newLine().indentation().keyword("catch");

			if (catchBlock.getKind() != CatchBlockKind.General) {
				c.space().text("(").type(catchBlock.getParameter().getValueType());

				if (catchBlock.getKind() != CatchBlockKind.Unnamed) {
					c.space().text(catchBlock.getParameter().getName());
				}

				c.text(")");
			}

			c.statementBlock(catchBlock.getBody(), this, true);
		}

		if (!block.getFinally().isEmpty()) {
			c.newLine().indentation().keyword("finally").statementBlock(block.getFinally(), this, true);
		}
		return null;
	}

	public Void visit(IUncheckedBlock block, SSTPrintingContext c) {
		c.indentation().keyword("unchecked").statementBlock(block.getBody(), this, true);
		return null;
	}

	public Void visit(IUnsafeBlock block, SSTPrintingContext c) {
		c.indentation().keyword("unsafe").text(" { ").comment("/* content ignored */").text(" }");
		return null;
	}

	public Void visit(IUsingBlock block, SSTPrintingContext c) {
		c.indentation().keyword("using").space().text("(");
		block.getReference().accept(this, c);
		c.text(")").statementBlock(block.getBody(), this, true);
		return null;
	}

	public Void visit(IWhileLoop block, SSTPrintingContext c) {
		c.indentation().keyword("while").space().text("(");
		c.indentationLevel++;
		block.getCondition().accept(this, c);
		c.indentationLevel--;
		c.newLine().indentation().text(")");

		c.statementBlock(block.getBody(), this, true);
		return null;
	}

	public Void visit(ICompletionExpression entity, SSTPrintingContext c) {
		if (entity.getObjectReference() != null) {
			c.text(entity.getObjectReference().getIdentifier()).text(".");
		} else if (entity.getTypeReference() != null) {
			c.type(entity.getTypeReference()).text(".");
		}

		c.text(entity.getToken()).cursorPosition();
		return null;
	}

	public Void visit(IComposedExpression expr, SSTPrintingContext c) {
		c.keyword("composed").text("(");

		for (IReference reference : expr.getReferences()) {
			reference.accept(this, c);

			if (!reference.equals(expr.getReferences().get(expr.getReferences().size() - 1))) {
				c.text(", ");
			}
		}

		c.text(")");
		return null;
	}

	public Void visit(IIfElseExpression expr, SSTPrintingContext c) {
		c.text("(");
		expr.getCondition().accept(this, c);
		c.text(")").space().text("?").space();
		expr.getThenExpression().accept(this, c);
		c.space().text(":").space();
		expr.getElseExpression().accept(this, c);
		return null;
	}

	public Void visit(IInvocationExpression expr, SSTPrintingContext c) {
		MethodName methodName = expr.getMethodName();

		if (methodName.isConstructor()) {
			c.keyword("new");
			c.space();
			c.text(methodName.getDeclaringType().getName());
		} else {
			if (methodName.isStatic()) {
				c.text(methodName.getDeclaringType().getName());
			} else {
				expr.getReference().accept(this, c);
			}
			c.text(".").text(methodName.getName());
		}

		c.text("(");
		boolean isFirst = true;
		for (ISimpleExpression parameter : expr.getParameters()) {
			if (!isFirst) {
				c.text(", ");
				isFirst = false;
			}
			parameter.accept(this, c);
		}
		c.text(")");

		return null;
	}

	public Void visit(ILambdaExpression expr, SSTPrintingContext c) {
		c.parameterList(expr.getName().getParameters()).space().text("=>");
		c.statementBlock(expr.getBody(), this, true);
		return null;
	}

	public Void visit(ILoopHeaderBlockExpression expr, SSTPrintingContext c) {
		c.statementBlock(expr.getBody(), this, true);
		return null;
	}

	public Void visit(IConstantValueExpression expr, SSTPrintingContext c) {
		if (expr.getValue() != null) {
			String value = !expr.getValue().isEmpty() ? expr.getValue() : "...";

			// Double.TryParse(expr.Value, out parsed
			if (value.equals("false") || value.equals("true") || value.matches("[0-9]+")
					|| value.matches("[0-9]+\\.[0-9]+")) {
				c.keyword(value);
			} else {
				c.stringLiteral(value);
			}
		}
		return null;
	}

	public Void visit(INullExpression expr, SSTPrintingContext c) {
		c.keyword("null");
		return null;
	}

	public Void visit(IReferenceExpression expr, SSTPrintingContext c) {
		expr.getReference().accept(this, c);
		return null;
	}

	public Void visit(IEventReference eventRef, SSTPrintingContext c) {
		c.text(eventRef.getReference().getIdentifier());
		c.text(".");
		c.text(eventRef.getEventName().getName());
		return null;
	}

	public Void visit(IFieldReference fieldRef, SSTPrintingContext c) {
		c.text(fieldRef.getReference().getIdentifier());
		c.text(".");
		c.text(fieldRef.getFieldName().getName());
		return null;
	}

	public Void visit(IMethodReference methodRef, SSTPrintingContext c) {
		c.text(methodRef.getReference().getIdentifier());
		c.text(".");
		c.text(methodRef.getMethodName().getName());
		return null;
	}

	public Void visit(IPropertyReference propertyRef, SSTPrintingContext c) {
		c.text(propertyRef.getReference().getIdentifier());
		c.text(".");
		c.text(propertyRef.getPropertyName().getName());
		return null;
	}

	public Void visit(IVariableReference varRef, SSTPrintingContext c) {
		c.text(varRef.getIdentifier());
		return null;
	}

	public Void visit(IUnknownReference unknownRef, SSTPrintingContext c) {
		c.unknownMarker();
		return null;
	}

	public Void visit(IUnknownExpression unknownExpr, SSTPrintingContext c) {
		c.unknownMarker();
		return null;
	}

	public Void visit(IUnknownStatement unknownStmt, SSTPrintingContext c) {
		c.indentation().unknownMarker().text(";");
		return null;
	}
}
