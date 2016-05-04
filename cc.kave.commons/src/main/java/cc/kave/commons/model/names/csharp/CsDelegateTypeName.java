package cc.kave.commons.model.names.csharp;

import java.util.List;

import cc.kave.commons.model.names.IDelegateTypeName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.TypeContext;

public class CsDelegateTypeName extends CsTypeName implements IDelegateTypeName {

	public CsDelegateTypeName(String s) {
		super(s);
	}

	public CsDelegateTypeName(TypeContext ctx) {
		super(ctx);
	}

	@Override
	public String getSignature() {
		return new CsMethodName(ctx.delegateType().method()).getSignature();
	}

	@Override
	public List<IParameterName> getParameters() {
		return new CsMethodName(ctx.delegateType().method()).getParameters();
	}

	@Override
	public boolean hasParameters() {
		return new CsMethodName(ctx.delegateType().method()).hasParameters();
	}

	@Override
	public ITypeName getReturnType() {
		return new CsMethodName(ctx.delegateType().method()).getReturnType();
	}

}
