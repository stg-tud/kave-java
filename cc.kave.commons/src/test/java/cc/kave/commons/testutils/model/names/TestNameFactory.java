package cc.kave.commons.testutils.model.names;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class TestNameFactory {
	private static int _counter;

	private static int NextCounter() {
		return ++_counter;
	}

	public static void Reset() {
		_counter = 0;
	}

	public static MethodName GetAnonymousMethodName() {
		return CsMethodName.newMethodName(
				"[" + GetAnonymousTypeName() + "] [" + GetAnonymousTypeName() + "].Method" + NextCounter() + "()");
	}

	public static TypeName GetAnonymousTypeName() {
		return CsTypeName.newTypeName("SomeType" + NextCounter() + ", SomeAssembly" + NextCounter() + ", 9.8.7.6");
	}

	public static NamespaceName GetAnonymousNamespace() {
		return CsNamespaceName.newNamespaceName("A.N" + NextCounter());
	}
}
