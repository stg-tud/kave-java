package cc.kave.commons.model.names.csharp;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import cc.kave.commons.model.names.csharp.parser.TypeNameParseUtil;
import cc.kave.commons.model.names.csharp.parser.TypeVisitor;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.MethodContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.TypeContext;
import cc.recommenders.exceptions.AssertionException;

public class CsMethodName {
	
	private MethodContext ctx;

	public CsMethodName(String type){
		try{
			MethodContext ctx = TypeNameParseUtil.validateMethodName(type);
			this.ctx = ctx;
		}catch(AssertionException e){
			System.err.println("Invalid Type: " + e.getMessage());
		}
	}
	
	public CsTypeName getType(){
		ParseTreeWalker walker = new ParseTreeWalker();
		TypeVisitor v = new TypeVisitor();
        walker.walk(v, ctx);
		return new CsTypeName(v.type);
	}

	public Object getReturnType() {
		// TODO Auto-generated method stub
		return null;
	}
}
