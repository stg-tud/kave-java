package cc.kave.commons.utils.sstprinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.csharp.NamespaceName;
import cc.kave.commons.model.ssts.ISST;

public class SSTPrintingUtils {
	public static void formatAsUsingList(Set<INamespaceName> namespaces, SSTPrintingContext context) {
		List<String> filteredNamespaceStrings = new ArrayList();
		for (INamespaceName name : namespaces) {
			if (!name.equals(NamespaceName.UNKNOWN_NAME)) {
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

	public static String printSST(ISST sst) {
		SSTPrintingContext context = new SSTPrintingContext();
		sst.accept(new SSTPrintingVisitor(), context);
		return context.toString();
	}
}
