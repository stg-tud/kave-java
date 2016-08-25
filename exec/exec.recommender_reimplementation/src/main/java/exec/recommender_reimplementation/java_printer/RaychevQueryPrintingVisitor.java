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
package exec.recommender_reimplementation.java_printer;

import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;

public class RaychevQueryPrintingVisitor extends JavaPrintingVisitor {

	public RaychevQueryPrintingVisitor(ISSTNode sst, boolean setPublicModifier) {
		super(sst,setPublicModifier);
	}

	@Override
	public Void visit(IUnknownReference unknownRef, SSTPrintingContext context) {
		throw new InvalidJavaCodeException();
	}

	@Override
	public Void visit(ICompletionExpression entity, SSTPrintingContext context) {
		IVariableReference varRef = entity.getVariableReference();
		if(varRef == null || varRef.getIdentifier().isEmpty()) {
			throw new InvalidJavaCodeException();
		}
		context.text("UNK.Must1(").text(varRef.getIdentifier()).text(")");
		return null;
	}

	@Override
	public Void visit(IInvocationExpression invocation, SSTPrintingContext context) {
		if (invocation.getMethodName().isUnknown()) {
			throw new InvalidJavaCodeException();
		}
		return super.visit(invocation, context);
	}
}
