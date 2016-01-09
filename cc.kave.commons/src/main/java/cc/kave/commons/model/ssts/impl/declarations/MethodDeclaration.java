package cc.kave.commons.model.ssts.impl.declarations;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class MethodDeclaration implements IMethodDeclaration {

	private IMethodName name;
	private boolean isEntryPoint;
	private List<IStatement> body;

	public MethodDeclaration() {
		name = MethodName.UNKNOWN_NAME;
		body = new ArrayList<>();
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return Lists.newArrayList();
	}

	@Override
	public IMethodName getName() {
		return this.name;
	}

	@Override
	public boolean isEntryPoint() {
		return this.isEntryPoint;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setName(IMethodName name) {
		this.name = name;
	}

	public void setEntryPoint(boolean isEntryPoint) {
		this.isEntryPoint = isEntryPoint;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		int hashCode = this.body.hashCode();
		hashCode = (hashCode * 397) ^ this.name.hashCode();
		hashCode = (hashCode * 397) ^ (this.isEntryPoint ? 1231 : 1237);
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodDeclaration other = (MethodDeclaration) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (isEntryPoint != other.isEntryPoint)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
