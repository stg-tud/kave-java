package cc.kave.commons.utils.sstprinter;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.csharp.NamespaceName;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;
import cc.kave.commons.utils.sstprinter.SSTPrintingUtils;

public class SSTPrintingUtilsTest {

	@Test
	public void testUsingListFormattedCorrectly() {
		Set<INamespaceName> namespaces = new HashSet<>();
		namespaces.add(NamespaceName.newNamespaceName("Z"));
		namespaces.add(NamespaceName.newNamespaceName("System"));
		namespaces.add(NamespaceName.newNamespaceName("System"));
		namespaces.add(NamespaceName.newNamespaceName("System.Collections.Generic"));
		namespaces.add(NamespaceName.newNamespaceName("A"));
		namespaces.add(NamespaceName.getGlobalNamespace());

		SSTPrintingContext context = new SSTPrintingContext();
		SSTPrintingUtils.formatAsUsingList(namespaces, context);
		String expected = String.join("\n", "using A;", "using System;", "using System.Collections.Generic;",
				"using Z;");
		Assert.assertEquals(expected, context.toString());
	}

	@Test
	public void testUnknownNameIsNotAddedToList() {
		Set<INamespaceName> namespaces = new HashSet<>();
		namespaces.add(NamespaceName.UNKNOWN_NAME);
		namespaces.add(NamespaceName.getGlobalNamespace());

		SSTPrintingContext context = new SSTPrintingContext();
		SSTPrintingUtils.formatAsUsingList(namespaces, context);
		String expected = "";
		Assert.assertEquals(expected, context.toString());
	}

}
