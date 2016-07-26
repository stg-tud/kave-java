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

import com.google.common.base.Strings;

import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;

public class RaychevQueryPrinter extends JavaPrintingVisitor {

	@SuppressWarnings("serial")
	public class InvalidJavaCodeException extends RuntimeException {
	}

	@Override
	public Void visit(IDelegateDeclaration stmt, SSTPrintingContext context) {
		throw new InvalidJavaCodeException();
	}

	@Override
	public Void visit(IEventDeclaration stmt, SSTPrintingContext context) {
		throw new InvalidJavaCodeException();
	}

	@Override
	public Void visit(IComposedExpression expr, SSTPrintingContext context) {
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

}
