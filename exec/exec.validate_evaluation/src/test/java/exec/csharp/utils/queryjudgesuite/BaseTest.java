/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package exec.csharp.utils.queryjudgesuite;

import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.csharp.utils.QueryJudge;

public class BaseTest {

	private Usage a;
	private Usage b;

	protected void setA(char def, int... callNums) {
		a = usage(def, callNums);
	}

	protected void setB(char def, int... callNums) {
		b = usage(def, callNums);
	}

	protected void nnA() {
		a = new NoUsage();
	}

	protected void nnB() {
		b = new NoUsage();
	}

	private Usage usage(char def, int... calls) {
		Query q = new Query();
		ICoReMethodName m = CoReMethodName.get(String.format("LT.m%c()V", def));
		q.setDefinition(DefinitionSites.createDefinitionByReturn(m));
		for (int call : calls) {
			q.addCallSite(CallSites.createReceiverCallSite("LT.m" + call + "()V"));
		}
		return q;
	}

	protected QueryJudge judge() {
		return new QueryJudge(a, b);
	}
}