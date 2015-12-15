package cc.kave.commons.utils.sstprinter;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;
import cc.kave.commons.utils.sstprinter.SSTPrintingUtils;

public class SSTPrintingUtilsTest {

	@Test
	public void testUsingListFormattedCorrectly() {
		Set<NamespaceName> namespaces = new HashSet<>();
		namespaces.add(CsNamespaceName.newNamespaceName("Z"));
		namespaces.add(CsNamespaceName.newNamespaceName("System"));
		namespaces.add(CsNamespaceName.newNamespaceName("System"));
		namespaces.add(CsNamespaceName.newNamespaceName("System.Collections.Generic"));
		namespaces.add(CsNamespaceName.newNamespaceName("A"));
		namespaces.add(CsNamespaceName.getGlobalNamespace());

		SSTPrintingContext context = new SSTPrintingContext();
		SSTPrintingUtils.formatAsUsingList(namespaces, context);
		String expected = String.join("\n", "using A;", "using System;", "using System.Collections.Generic;",
				"using Z;");
		Assert.assertEquals(expected, context.toString());
	}

	@Test
	public void testUnknownNameIsNotAddedToList() {
		Set<NamespaceName> namespaces = new HashSet<>();
		namespaces.add(CsNamespaceName.UNKNOWN_NAME);
		namespaces.add(CsNamespaceName.getGlobalNamespace());

		SSTPrintingContext context = new SSTPrintingContext();
		SSTPrintingUtils.formatAsUsingList(namespaces, context);
		String expected = "";
		Assert.assertEquals(expected, context.toString());
	}

}
