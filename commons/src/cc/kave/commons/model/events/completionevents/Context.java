package cc.kave.commons.model.events.completionevents;

import com.google.gson.annotations.SerializedName;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.TypeShape;

public class Context {
	private ITypeShape typeShape;
	@SerializedName("SST")
	private ISST sst;

	public Context() {
		this.typeShape = new TypeShape();
		this.sst = new SST();
	}

	public ITypeShape getTypeShape() {
		return typeShape;
	}

	public void setTypeShape(ITypeShape typeShape) {
		this.typeShape = typeShape;
	}

	public ISST getSST() {
		return sst;
	}

	public void setSST(ISST sst) {
		this.sst = sst;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sst == null) ? 0 : sst.hashCode());
		result = prime * result + ((typeShape == null) ? 0 : typeShape.hashCode());
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
		Context other = (Context) obj;
		if (sst == null) {
			if (other.sst != null)
				return false;
		} else if (!sst.equals(other.sst))
			return false;
		if (typeShape == null) {
			if (other.typeShape != null)
				return false;
		} else if (!typeShape.equals(other.typeShape))
			return false;
		return true;
	}

}
