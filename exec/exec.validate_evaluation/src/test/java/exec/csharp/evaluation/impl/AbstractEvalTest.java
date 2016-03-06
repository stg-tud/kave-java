/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.csharp.evaluation.impl;

import cc.recommenders.names.IMethodName;
import cc.recommenders.names.VmMethodName;
import cc.recommenders.names.VmTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;

public class AbstractEvalTest {

	protected Query createQuery(int... callNums) {
		Query q = new Query();
		q.setType(VmTypeName.get("LT"));
		q.setClassContext(VmTypeName.get("LCtx"));
		q.setMethodContext(method("Ctx", "m"));
		q.setDefinition(DefinitionSites.createDefinitionByThis());
		for (int callNum : callNums) {
			String methodName = "LT.m" + callNum + "()V";
			CallSite cs = CallSites.createReceiverCallSite(methodName);
			q.addCallSite(cs);
		}
		return q;
	}

	private IMethodName method(String cName, String mName) {
		return VmMethodName.get(String.format("L%s.%s()V", cName, mName));
	}
}