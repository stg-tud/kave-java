package cc.kave.commons.model.ssts.impl.declarations;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class PropertyDeclaration implements IPropertyDeclaration {

	private PropertyName name;
	private List<IStatement> get;
	private List<IStatement> set;

	public PropertyDeclaration() {
		this.name = CsPropertyName.UNKNOWN_NAME;
		this.get = new ArrayList<IStatement>();
		this.set = new ArrayList<IStatement>();
	}

	@Override
	public PropertyName getName() {
		return name;
	}

	@Override
	public List<IStatement> getGet() {
		return get;
	}

	@Override
	public List<IStatement> getSet() {
		return set;
	}

	public void setName(PropertyName name) {
		this.name = name;
	}

	public void setGet(List<IStatement> get) {
		this.get = get;
	}

	public void setSet(List<IStatement> set) {
		this.set = set;
	}

	@Override
	public int hashCode() {
		int hashCode = this.get.hashCode();
		hashCode = (hashCode * 397) ^ this.set.hashCode();
		hashCode = (hashCode * 397) ^ this.name.hashCode();
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
		PropertyDeclaration other = (PropertyDeclaration) obj;
		if (get == null) {
			if (other.get != null)
				return false;
		} else if (!get.equals(other.get))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (set == null) {
			if (other.set != null)
				return false;
		} else if (!set.equals(other.set))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
