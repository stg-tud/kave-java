package cc.kave.commons.model.names.csharp.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.ArrayPartContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.AssemblyContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.AssemblyVersionContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.DelegateTypeContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.FormalParamContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.GenericParamContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.GenericTypePartContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.IdContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.MethodContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.MethodEOLContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.NamespaceContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.NumContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.RegularTypeContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.ResolvedTypeContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.SimpleTypeNameContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.TypeContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.TypeEOLContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.TypeNameContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.TypeParameterContext;

public class TypeVisitor implements TypeNamingListener {

	public String namespace = "";
	public String type = "";
	
	@Override
	public void enterEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitTerminal(TerminalNode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeEOL(TypeEOLContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeEOL(TypeEOLContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterType(TypeContext ctx) {
		type = ctx.getText();
		System.out.println(ctx.getText());
	}

	@Override
	public void exitType(TypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeParameter(TypeParameterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTypeParameter(TypeParameterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterRegularType(RegularTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitRegularType(RegularTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterDelegateType(DelegateTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitDelegateType(DelegateTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterArrayPart(ArrayPartContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitArrayPart(ArrayPartContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterResolvedType(ResolvedTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitResolvedType(ResolvedTypeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterNamespace(NamespaceContext ctx) {
		namespace = ctx.getText();
		
	}

	@Override
	public void exitNamespace(NamespaceContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTypeName(TypeNameContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitTypeName(TypeNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSimpleTypeName(SimpleTypeNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSimpleTypeName(SimpleTypeNameContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterGenericTypePart(GenericTypePartContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGenericTypePart(GenericTypePartContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterGenericParam(GenericParamContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGenericParam(GenericParamContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAssembly(AssemblyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAssembly(AssemblyContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterAssemblyVersion(AssemblyVersionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitAssemblyVersion(AssemblyVersionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterMethod(MethodContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitMethod(MethodContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFormalParam(FormalParamContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFormalParam(FormalParamContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterId(IdContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitId(IdContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterNum(NumContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitNum(NumContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterMethodEOL(MethodEOLContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitMethodEOL(MethodEOLContext ctx) {
		// TODO Auto-generated method stub
		
	}

}
