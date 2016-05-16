/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.frequency_recommender;

import java.util.Map;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;

public class MethodFrequencyVisitor extends AbstractTraversingNodeVisitor<Map<IMethodName,MethodFrequency>, Object>{
	
	@Override
	public Object visit(IInvocationExpression expr, Map<IMethodName,MethodFrequency> context) {
		IMethodName methodName = expr.getMethodName();
		
		if(!context.containsKey(methodName)) {
			context.put(methodName, new MethodFrequency(methodName, 1));
		}
		else {
			context.get(methodName).frequency++;
		}
		
		return super.visit(expr, context);
	}
	
}
