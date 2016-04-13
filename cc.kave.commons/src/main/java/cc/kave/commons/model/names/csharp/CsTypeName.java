package cc.kave.commons.model.names.csharp;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import cc.kave.commons.model.names.csharp.parser.TypeNameParseUtil;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.TypeContext;
import cc.kave.commons.model.names.csharp.parser.TypeVisitor;
import cc.recommenders.exceptions.AssertionException;

public class CsTypeName {
	
	private TypeContext ctx;
	
	public CsTypeName(String type){
		try{
			TypeContext ctx = TypeNameParseUtil.validateTypeName(type);
			this.ctx = ctx;
		}catch(AssertionException e){
			System.err.println("Invalid Type: " + e.getMessage());
		}
	}
	
	public String getNamespace(){
		ParseTreeWalker walker = new ParseTreeWalker();
		TypeVisitor v = new TypeVisitor();
        walker.walk(v, ctx);
		return v.namespace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ctx == null) ? 0 : ctx.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CsTypeName other = (CsTypeName) obj;
		if (ctx == null) {
			if (other.ctx != null)
				return false;
		} else if (!ctx.equals(other.ctx))
			return false;
		return true;
	}
}
