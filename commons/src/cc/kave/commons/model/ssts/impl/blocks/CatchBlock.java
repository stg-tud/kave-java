package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;

public class CatchBlock implements ICatchBlock {
	private CatchBlockKind kind;
	private ParameterName parameter;
	private List<IStatement> body;

	public CatchBlock() {
		this.parameter = CsParameterName.UNKNOWN_NAME;
		this.body = new ArrayList<>();
		this.kind = CatchBlockKind.Default;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	@Override
	public ParameterName getParameter() {
		return this.parameter;
	}

	public void setParameter(ParameterName parameter) {
		this.parameter = parameter;
	}

	public CatchBlockKind getKind() {
		return kind;
	}

	public void setKind(CatchBlockKind kind) {
		this.kind = kind;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
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
		CatchBlock other = (CatchBlock) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (kind != other.kind)
			return false;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}

}
