/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package exec.validate_evaluation.streaks;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;

public class NameUtilsTest {

	private String kave;
	private String core;

	@Test
	public void t1() {
		kave = "T,P";
		core = "LT";
		assertTypeConversion();
	}

	@Test
	public void t2() {
		kave = "n.T, P";
		core = "Ln/T";
		assertTypeConversion();
	}

	@Test
	public void t3() {
		kave = "n.T, A, 1.2.3.4";
		core = "Ln/T";
		assertTypeConversion();
	}

	@Test
	public void m1() {
		kave = f("[%s] [%s].M()", "T,P", "T2,P");
		core = "LT.M()LT2";
		assertMethodConversion();
	}

	private void assertTypeConversion() {
		ICoReTypeName expected = CoReTypeName.get(core);
		ICoReTypeName actual = NameUtils.toCoReName(TypeName.newTypeName(kave));
		assertEquals(expected, actual);
	}

	private void assertMethodConversion() {
		ICoReMethodName expected = CoReMethodName.get(core);
		ICoReMethodName actual = NameUtils.toCoReName(MethodName.newMethodName(kave));
		assertEquals(expected, actual);
	}

	private static String f(String s, Object... args) {
		return String.format(s, args);
	}
}