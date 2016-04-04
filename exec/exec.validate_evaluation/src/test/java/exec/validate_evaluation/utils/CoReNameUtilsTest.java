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
package exec.validate_evaluation.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.recommenders.names.CoReFieldName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.names.ICoReFieldName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import exec.validate_evaluation.utils.CoReNameUtils;

public class CoReNameUtilsTest {

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
		kave = "a.b.c.T, P";
		core = "La/b/c/T";
		assertTypeConversion();
	}

	@Test
	public void t3() {
		kave = "n.T, A, 1.2.3.4";
		core = "Ln/T";
		assertTypeConversion();
	}

	@Test
	public void t4() {
		kave = "T+NT,P";
		core = "LT$NT";
		assertTypeConversion();
	}

	@Test
	public void t5() {
		kave = "T[],P";
		core = "[LT";
		assertTypeConversion();
	}

	@Test
	public void t6() {
		kave = "T`1[[T]],P";
		core = "LT";
		assertTypeConversion();
	}

	@Test
	public void t7() {
		kave = "T`1[[T -> T2,P]],P";
		core = "LT";
		assertTypeConversion();
	}

	@Test
	public void m1() {
		kave = f("[%s] [%s].M()", "RT,P", "DT,P");
		core = "LDT.M()LRT;";
		assertMethodConversion();
	}

	@Test
	public void m2() {
		kave = f("[%s] [%s].M([%s] p1)", "RT,P", "DT,P", "P1,P");
		core = "LDT.M(LP1;)LRT;";
		assertMethodConversion();
	}

	@Test
	public void m3() {
		kave = f("[%s] [%s].M([%s] p1, [%s] p2)", "RT,P", "DT,P", "P1,P", "P2,P");
		core = "LDT.M(LP1;,LP2;)LRT;";
		assertMethodConversion();
	}

	@Test
	public void m4() {
		kave = f("[%s] [%s].M`1[[T]]([%s] p1, [%s] p2)", "RT,P", "DT,P", "P1,P", "P2,P");
		core = "LDT.M(LP1;,LP2;)LRT;";
		assertMethodConversion();
	}

	@Test
	public void m5() {
		kave = f("[%s] [%s].M`1[[T -> T2]]([%s] p1, [%s] p2)", "RT,P", "DT,P", "P1,P", "P2,P");
		core = "LDT.M(LP1;,LP2;)LRT;";
		assertMethodConversion();
	}

	@Test
	public void f1() {
		kave = f("[%s] [%s].f", "RT,P", "DT,P", "P1,P", "P2,P");
		core = "LDT.f;LRT";
		assertFieldConversion();
	}

	private void assertTypeConversion() {
		ICoReTypeName expected = CoReTypeName.get(core);
		ICoReTypeName actual = CoReNameUtils.toCoReName(TypeName.newTypeName(kave));
		assertEquals(expected, actual);
	}

	private void assertMethodConversion() {
		ICoReMethodName expected = CoReMethodName.get(core);
		ICoReMethodName actual = CoReNameUtils.toCoReName(MethodName.newMethodName(kave));
		assertEquals(expected, actual);
	}

	private void assertFieldConversion() {
		ICoReFieldName expected = CoReFieldName.get(core);
		ICoReFieldName actual = CoReNameUtils.toCoReName(FieldName.newFieldName(kave));
		assertEquals(expected, actual);
	}

	private static String f(String s, Object... args) {
		return String.format(s, args);
	}
}