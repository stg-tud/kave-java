package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;

public class CatchBlock implements ICatchBlock {

	private ParameterName parameter;
	private List<IStatement> body;
	private boolean isGeneral;
	private boolean isUnnamed;

	public CatchBlock() {
		this.parameter = CsParameterName.UNKNOWN_NAME;
		this.body = new ArrayList<>();
		this.isGeneral = false;
		this.isUnnamed = false;
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

	@Override
	public boolean isUnnamed() {
		return this.isUnnamed;
	}

	@Override
	public boolean isGeneral() {
		return this.isGeneral;
	}

	public void setGeneral(boolean isGeneral) {
		this.isGeneral = isGeneral;
	}

	public void setUnnamed(boolean isUnnamed) {
		this.isUnnamed = isUnnamed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + (isGeneral ? 1231 : 1237);
		result = prime * result + (isUnnamed ? 1231 : 1237);
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CatchBlock))
			return false;
		CatchBlock other = (CatchBlock) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (isGeneral != other.isGeneral)
			return false;
		if (isUnnamed != other.isUnnamed)
			return false;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		return true;
	}

}
