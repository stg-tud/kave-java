package cc.kave.commons.model.events.completionevents;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}