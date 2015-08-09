package cc.kave.commons.utils.sstprinter;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;

public class SSTPrintingUtils {
	public static void formatAsUsingList(List<NamespaceName> namespaces, SSTPrintingContext context) {
		List<String> filteredNamespaceStrings = new ArrayList();
		for (NamespaceName name : namespaces) {
			if (!name.equals(CsNamespaceName.UNKNOWN_NAME)) {
				String s = name.getIdentifier().trim();
				if (!s.isEmpty()) {
					filteredNamespaceStrings.add(s);
				}
			}
		}
		filteredNamespaceStrings.sort(null);
		for (String n : filteredNamespaceStrings) {
			context.keyword("using").space().text(n).text(";");

			if (!n.equals(filteredNamespaceStrings.get(filteredNamespaceStrings.size() - 1))) {
				context.newLine();
			}
		}
	}
}
