package cc.kave.commons.testutils.model.names;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.NamespaceName;
import cc.kave.commons.model.names.csharp.TypeName;

public class TestNameFactory {
	private static int _counter;

	private static int NextCounter() {
		return ++_counter;
	}

	public static void Reset() {
		_counter = 0;
	}

	public static IMethodName GetAnonymousMethodName() {
		return MethodName.newMethodName(
				"[" + GetAnonymousTypeName() + "] [" + GetAnonymousTypeName() + "].Method" + NextCounter() + "()");
	}

	public static ITypeName GetAnonymousTypeName() {
		return TypeName.newTypeName("SomeType" + NextCounter() + ", SomeAssembly" + NextCounter() + ", 9.8.7.6");
	}

	public static INamespaceName GetAnonymousNamespace() {
		return NamespaceName.newNamespaceName("A.N" + NextCounter());
	}
}
