/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package com.codetrails.data;

import java.util.Set;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.usages.AbstractUsage;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.DefinitionSite;

public class DecoratedObjectUsage extends AbstractUsage {

	private com.codetrails.data.ObjectUsage usage;
	private UsageConverter converter = new UsageConverter();

	public DecoratedObjectUsage(com.codetrails.data.ObjectUsage usage) {
		this.usage = usage;
	}

	public ICoReTypeName getType() {
		return usage.getType();
	}

	public ICoReTypeName getClassContext() {
		ICoReTypeName superclass = usage.getContext().getSuperclass();
		if (superclass == null) {
			// Logger.log("rewriting class context");
			return usage.getContext().getName().getDeclaringType();
		} else {
			return superclass;
		}
	}

	public ICoReMethodName getMethodContext() {
		EnclosingMethodContext context = usage.getContext();
		ICoReMethodName method = context.getName();
		ICoReTypeName firstType = context.getIntroducedBy();
		if (firstType == null) {
			// Logger.log("rewriting method context");
			return method;
		} else {
			ICoReMethodName firstDeclaration = CoReMethodName.rebase(firstType, method);
			return firstDeclaration;
		}
	}

	public DefinitionSite getDefinitionSite() {
		return converter.toRecommenderDefinition(usage.getDef());
	}

	/**
	 * @return concatenation of paths of the underlying usage, which contains
	 *         each callsite exactly once
	 */
	public Set<CallSite> getAllCallsites() {
		// TODO write tests for rebasing
		// TODO get rid of rebasing?
		// site.targetMethod = VmMethodName.rebase(usage.type,
		// site.targetMethod);
		return converter.toRecommenderCalls(usage.getPaths());
	}

	public ObjectUsage getOriginal() {
		return usage;
	}
}