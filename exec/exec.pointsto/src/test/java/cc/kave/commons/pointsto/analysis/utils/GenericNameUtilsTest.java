/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.utils;

import org.junit.Test;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;

import static org.junit.Assert.assertEquals;

public class GenericNameUtilsTest {

	@Test
	public void testMethods() {

		IMethodName a = MethodName.newMethodName(
				"[System.Void, mscorlib, 4.0.0.0] [SharpCompress.Archive.AbstractWritableArchive`2[[TEntry -> TEntry],[TVolume -> TVolume]], SharpCompress].RemoveEntry([TEntry] entry)");
		IMethodName aErased = GenericNameUtils.eraseGenericInstantiations(a);
		assertEquals(
				MethodName.newMethodName(
						"[System.Void, mscorlib, 4.0.0.0] [SharpCompress.Archive.AbstractWritableArchive`2[[TEntry],[TVolume]], SharpCompress].RemoveEntry([TEntry] entry)"),
				aErased);

		IMethodName b = MethodName.newMethodName(
				"[System.Void, mscorlib, 4.0.0.0] [System.Collections.Generic.List`1[[T -> System.Collections.Generic.Dictionary`2[[TKey -> System.String, mscorlib, 4.0.0.0],[TValue -> System.String, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0]..ctor()");
		IMethodName bErased = GenericNameUtils.eraseGenericInstantiations(b);
		assertEquals(
				MethodName.newMethodName(
						"[System.Void, mscorlib, 4.0.0.0] [System.Collections.Generic.List`1[[T]], mscorlib, 4.0.0.0]..ctor()"),
				bErased);
	}
}
