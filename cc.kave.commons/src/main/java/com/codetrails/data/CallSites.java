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

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.CoReMethodName;

public class CallSites {
	public static CallSite createParameterCallSite(String methodName, int argIndex) {
		ICoReMethodName method = CoReMethodName.get(methodName);
		return createParameterCallSite(method, argIndex);
	}

	public static CallSite createParameterCallSite(ICoReMethodName method, int argIndex) {
		CallSite site = new CallSite();
		site.setKind(CallSiteKind.PARAM_CALL_SITE);
		site.setCall(method);
		site.setArgumentIndex(argIndex);
		return site;
	}

	public static CallSite createReceiverCallSite(String methodName) {
		ICoReMethodName method = CoReMethodName.get(methodName);
		return createReceiverCallSite(method);
	}

	public static CallSite createReceiverCallSite(ICoReMethodName method) {
		CallSite site = new CallSite();
		site.setKind(CallSiteKind.RECEIVER_CALL_SITE);
		site.setCall(method);
		return site;
	}
}