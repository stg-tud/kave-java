package cc.kave.commons.model.ssts.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Sets;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class SST implements ISST {

	private TypeName enclosingType;
	private Set<IFieldDeclaration> fields;
	private Set<IPropertyDeclaration> properties;
	private Set<IMethodDeclaration> methods;
	private Set<IEventDeclaration> events;
	private Set<IDelegateDeclaration> delegates;

	public SST() {
		this.enclosingType = CsTypeName.UNKNOWN_NAME;
		this.fields = new HashSet<IFieldDeclaration>();
		this.properties = new HashSet<IPropertyDeclaration>();
		this.methods = new HashSet<IMethodDeclaration>();
		this.events = new HashSet<IEventDeclaration>();
		this.delegates = new HashSet<IDelegateDeclaration>();
	}

	@Override
	public TypeName getEnclosingType() {
		return this.enclosingType;
	}

	@Override
	public Set<IFieldDeclaration> getFields() {
		return this.fields;
	}

	@Override
	public Set<IPropertyDeclaration> getProperties() {
		return this.properties;
	}

	@Override
	public Set<IMethodDeclaration> getMethods() {
		return this.methods;
	}

	@Override
	public Set<IEventDeclaration> getEvents() {
		return this.events;
	}

	@Override
	public Set<IDelegateDeclaration> getDelegates() {
		return this.delegates;
	}

	public void setEnclosingType(TypeName enclosingType) {
		this.enclosingType = enclosingType;
	}

	public void setFields(Set<IFieldDeclaration> fields) {
		this.fields = fields;
	}

	public void setProperties(Set<IPropertyDeclaration> properties) {
		this.properties = properties;
	}

	public void setMethods(Set<IMethodDeclaration> methods) {
		this.methods = methods;
	}

	public void setEvents(Set<IEventDeclaration> events) {
		this.events = events;
	}

	public void setDelegates(Set<IDelegateDeclaration> delegates) {
		this.delegates = delegates;
	}

	@Override
	public Set<IMethodDeclaration> getEntryPoints() {
		return Sets.newHashSet(methods.stream().filter(m -> m.isEntryPoint()).collect(Collectors.toSet()));
	}

	@Override
	public Set<IMethodDeclaration> getNonEntryPoints() {
		return Sets.newHashSet(methods.stream().filter(m -> !m.isEntryPoint()).collect(Collectors.toSet()));
	}

	public int hashCode() {
		int hashCode = this.enclosingType.hashCode();
		hashCode = (hashCode * 397) ^ this.fields.hashCode();
		hashCode = (hashCode * 397) ^ this.methods.hashCode();
		hashCode = (hashCode * 397) ^ this.properties.hashCode();
		hashCode = (hashCode * 397) ^ this.events.hashCode();
		hashCode = (hashCode * 397) ^ this.delegates.hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SST))
			return false;
		SST other = (SST) obj;
		if (delegates == null) {
			if (other.delegates != null)
				return false;
		} else if (!delegates.equals(other.delegates))
			return false;
		if (enclosingType == null) {
			if (other.enclosingType != null)
				return false;
		} else if (!enclosingType.equals(other.enclosingType))
			return false;
		if (events == null) {
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		if (methods == null) {
			if (other.methods != null)
				return false;
		} else if (!methods.equals(other.methods))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
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
