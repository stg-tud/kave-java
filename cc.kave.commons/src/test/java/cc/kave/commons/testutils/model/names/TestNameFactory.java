/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.testutils.model.names;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class TestNameFactory {
	private static int _counter;

	private static int NextCounter() {
		return ++_counter;
	}

	public static void Reset() {
		_counter = 0;
	}

	public static IMethodName GetAnonymousMethodName() {
		return Names.newMethod(
				"[" + GetAnonymousTypeName() + "] [" + GetAnonymousTypeName() + "].Method" + NextCounter() + "()");
	}

	public static ITypeName GetAnonymousTypeName() {
		return Names.newType("SomeType" + NextCounter() + ", SomeAssembly" + NextCounter() + ", 9.8.7.6");
	}

	public static INamespaceName GetAnonymousNamespace() {
		return Names.newNamespace("A.N" + NextCounter());
	}
}