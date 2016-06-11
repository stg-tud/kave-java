package exec.recommender_reimplementation.tokenization;

import java.util.Iterator;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class TokenizationVisitor extends TraversingVisitor<TokenizationContext, Object> {

	@Override
	public Object visit(ISST sst, TokenizationContext c) {
		if(sst.isPartialClass()) {
			c.pushToken("partial");
		}
		
		// class classifier
		ITypeName enclosingType = sst.getEnclosingType();
		if(enclosingType.isInterfaceType()) {
			c.pushToken("interface");
		}
		else if(enclosingType.isEnumType()) {
			c.pushToken("enum");
		}
		else if(enclosingType.isStructType()) {
			c.pushToken("struct");
		}
		else {
			c.pushToken("class");
		}
		
		c.pushType(enclosingType);
		
		// handle class extensions
		if(c.typeShape != null && c.typeShape.getTypeHierarchy().hasSupertypes()) {
			c.pushToken(":");
			
			ITypeHierarchy typeHierarchy = c.typeShape.getTypeHierarchy();
			
			if(typeHierarchy.hasSuperclass() && typeHierarchy.getExtends() != null) {
				c.pushType(typeHierarchy.getExtends().getElement());
				
				if(typeHierarchy.isImplementingInterfaces()) {
					c.pushToken(",");
				}
			}
			
			for (Iterator<ITypeHierarchy> iterator = typeHierarchy.getImplements().iterator(); iterator.hasNext();) {
				ITypeHierarchy interfaceTypeHierarchy = (ITypeHierarchy) iterator.next();
				
				c.pushType(interfaceTypeHierarchy.getElement());
				
				if(iterator.hasNext()) {
					c.pushToken(",");
				}
							
			}
			
		}
	
		c.pushOpeningCurlyBracket();
		
		super.visit(sst, c);
		
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	/* Declarations */
	
	@Override
	public Object visit(IDelegateDeclaration decl, TokenizationContext c) {
		c.pushToken("delegate")
		.pushType(decl.getName())
		.pushParameters(decl.getName().getParameters())
		.pushSemicolon();
		
		return null;
	}
	
	@Override
	public Object visit(IEventDeclaration decl, TokenizationContext c) {
		c.pushToken("event")
		.pushType(decl.getName().getHandlerType())
		.pushToken(decl.getName().getName())
		.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IFieldDeclaration decl, TokenizationContext c) {
		if(decl.getName().isStatic()) {
			c.pushToken("static");
		}
		
		c.pushType(decl.getName().getValueType())
		.pushToken(decl.getName().getName())
		.pushSemicolon();
				
		return null;
	}
	
	@Override
	public Object visit(IMethodDeclaration decl, TokenizationContext c) {
		if(decl.getName().isStatic()) {
			c.pushToken("static");
		}
		
		c.pushType(decl.getName().getReturnType())
		.pushToken(decl.getName().getName());
		
		if(decl.getName().hasTypeParameters()) {
			c.pushTypeParameters(decl.getName().getTypeParameters());
		}
		
		c.pushParameters(decl.getName().getParameters())
		.pushOpeningCurlyBracket();
		
		visitStatements(decl.getBody(), c);
		
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	@Override
	public Object visit(IPropertyDeclaration decl, TokenizationContext c) {
		if(decl.getName().isStatic()) {
			c.pushToken("static");
		}
		
		c.pushType(decl.getName().getValueType())
		.pushToken(decl.getName().getName());
		
		boolean hasBody = !decl.getGet().isEmpty() || !decl.getSet().isEmpty();
		
		c.pushOpeningCurlyBracket();
		
		if(hasBody) {
			
			if(decl.getName().hasGetter()) {
				c.pushOpeningCurlyBracket()
				.pushToken("get");
				
				visitStatements(decl.getGet(), c);
				
				c.pushClosingCurlyBracket();
			}
			
			if(decl.getName().hasSetter()) {
				c.pushOpeningCurlyBracket()
				.pushToken("set");
				
				visitStatements(decl.getSet(), c);
				
				c.pushClosingCurlyBracket();
			}
		}
		else {
			c.pushToken("get")
			.pushSemicolon()
			.pushToken("set")
			.pushSemicolon();
		}
		
		c.pushClosingCurlyBracket();
		
		return null;
	}
	
	
	/* Statements */
	
	@Override
	public Object visit(IAssignment stmt, TokenizationContext c) {
		stmt.getReference().accept(this, c);
		
		c.pushToken("=");
		
		stmt.getExpression().accept(this, c);
		
		c.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IBreakStatement stmt, TokenizationContext c) {
		c.pushToken("break").pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IContinueStatement stmt, TokenizationContext c) {
		c.pushToken("continue").pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IEventSubscriptionStatement stmt, TokenizationContext c) {
		stmt.getReference().accept(this, c);
		
		switch (stmt.getOperation()) {
		case Add:
			c.pushToken("+=");
			break;
		case Remove:
			c.pushToken("-=");
			break;
		}
		
		stmt.getExpression().accept(this, c);
		
		c.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IExpressionStatement stmt, TokenizationContext c) {
		stmt.getExpression().accept(this, c);
		c.pushSemicolon();
		return null;
	}
	
	
	@Override
	public Object visit(IGotoStatement stmt, TokenizationContext c) {
		c.pushToken("goto").pushToken(stmt.getLabel()).pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(ILabelledStatement stmt, TokenizationContext c) {
		c.pushToken(stmt.getLabel()).pushToken(":");
		stmt.getStatement().accept(this, c);
		return null;
	}

	
	@Override
	public Object visit(IReturnStatement stmt, TokenizationContext c) {
		c.pushToken("return");
		if(!stmt.isVoid()) stmt.getExpression().accept(this, c);
		c.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IThrowStatement stmt, TokenizationContext c) {
		c.pushToken("throw");
		if(!stmt.isReThrow()) stmt.getReference().accept(this, c);
		c.pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IUnknownStatement unknownStmt, TokenizationContext c) {
		c.pushUnknown().pushSemicolon();
		return null;
	}
	
	@Override
	public Object visit(IVariableDeclaration stmt, TokenizationContext c) {
		c.pushType(stmt.getType());
		stmt.getReference().accept(this, c);
		c.pushSemicolon();
		return null;
	}
	
	/* Blocks */
	
	@Override
	public Object visit(IDoLoop block, TokenizationContext c) {
		c.pushToken("do").pushOpeningCurlyBracket();
		visitStatements(block.getBody(), c);
		c.pushClosingCurlyBracket().pushOpeningBracket().pushToken("while");
		block.getCondition().accept(this, c);
		c.pushClosingBracket();
		
		return null;
	}
}
