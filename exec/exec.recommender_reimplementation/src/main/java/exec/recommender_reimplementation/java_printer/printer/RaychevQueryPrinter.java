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
package exec.recommender_reimplementation.java_printer.printer;

import java.text.MessageFormat;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import exec.recommender_reimplementation.java_printer.JavaPrintingContext;
import exec.recommender_reimplementation.java_printer.RaychevQueryPrintingVisitor;

public class RaychevQueryPrinter extends JavaPrinter {

	@Override
	public String print(Context context) {
		ISST sst = transform(context);
		changeEnclosingType((SST) sst);
		JavaPrintingContext printingContext = getPrintingContext(context);

		StringBuilder sb = new StringBuilder();
		sst.accept(new RaychevQueryPrintingVisitor(context.getSST(), false), printingContext);

		if (!printingContext.toString().isEmpty()) {
			sb.append(printingContext.toString());
			return sb.toString();
		}
		return "";
	}

	private void changeEnclosingType(SST sst) {
		ITypeName typeName = sst.getEnclosingType();
		ITypeName newTypeName = TypeName.newTypeName(
				MessageFormat.format("{0}.{1},{2}", "com.example.fill", typeName.getName(), typeName.getAssembly()));
		sst.setEnclosingType(newTypeName);
	}

}
