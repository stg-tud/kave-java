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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.NamespaceName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;

public class JavaPrintingUtils {
	
	public static void formatAsImportList(Iterator<INamespaceName> namespaces, SSTPrintingContext context) {
		List<String> filteredNamespaceStrings = new ArrayList<>();
		while (namespaces.hasNext()) {
			INamespaceName name = namespaces.next();
			if (!name.equals(NamespaceName.UNKNOWN_NAME)) {
				String s = name.getIdentifier().trim();
				if (!s.isEmpty()) {
					filteredNamespaceStrings.add(s);
				}
			}
		}
		filteredNamespaceStrings.sort(null);
		for (String n : filteredNamespaceStrings) {
			context.keyword("import").space().text(n).text(";");

			if (!n.equals(filteredNamespaceStrings.get(filteredNamespaceStrings.size() - 1))) {
				context.newLine();
			}
		}
	}
	
	public static String printJava(ISST sst) {
		JavaPrintingContext context = new JavaPrintingContext();
		sst.accept(new JavaPrintingVisitor(sst), context);
		return context.toString();
	}
	
	public static String printRaychevJava(ISST sst) {
		JavaPrintingContext context = new JavaPrintingContext();
		sst.accept(new RaychevQueryPrinter(sst), context);
		JavaPrintingContext importContext = new JavaPrintingContext();
		formatAsImportList(context.getSeenNamespaces(), importContext);
		return importContext.toString() + "\n" + context.toString();
	}
	
	public static String getDefaultValueForType(ITypeName returnType) {
		if (returnType.isValueType()) {
			String type = returnType.getFullName();
			String translatedType = JavaNameUtils.getTypeAliasFromFullTypeName(type);
			switch (translatedType) {
			case "boolean":
				return "false";
			case "byte":
				return "0";
			case "short":
				return "0";
			case "int":
				return "0";
			case "long":
				return "0";
			case "double":
				return "0.0d";
			case "float":
				return "0.0f";
			case "char":
				return "'.'";
			default:
				break;
			}
		}
		return "null";
	}
}
