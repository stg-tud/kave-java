/**
 * Copyright 2015 Waldemar Graf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.kave.commons.model.names;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.csharp.CsNameUtils;
import cc.kave.commons.model.names.csharp.ParameterName;

public class CsNameUtilsTest {

	@Test
	public void HasParameters() {
		assertTrue(CsNameUtils.hasParameters("M([C,P] p)"));
		assertTrue(CsNameUtils.hasParameters("M([[DR, P] [D, P].()] p)"));
	}

	@Test
	public void HasNoParameters() {
		assertFalse(CsNameUtils.hasParameters("M()"));
	}

	@Test
	public void ParsesParametersWithParameterizedType() {
		List<IParameterName> parameterNames = CsNameUtils.getParameterNames("M([A`1[[B, P]], P] p)");
		assertEquals(ParameterName.newParameterName("[A`1[[B, P]], P] p"), parameterNames.get(0));
	}

	@Test
	public void ParsesParametersWithDelegateType() {
		List<IParameterName> parameterNames = CsNameUtils.getParameterNames("M([[DR, P] [D, P].()] p)");
		assertEquals(ParameterName.newParameterName("[[DR, P] [D, P].()] p"), parameterNames.get(0));
	}

	@Test
	public void ParsesMultipleParameters() {
		List<IParameterName> parameterNames = CsNameUtils.getParameterNames("M([T1,P] a, [T2,P] b)");
		assertEquals(2, parameterNames.size());
		assertEquals(ParameterName.newParameterName("[T1,P] a"), parameterNames.get(0));
		assertEquals(ParameterName.newParameterName("[T2,P] b"), parameterNames.get(1));
	}

	@Test
	public void ParsesUnknownNames() {
		List<IParameterName> parameterNames = CsNameUtils.getParameterNames("???");
		assertEquals(0, parameterNames.size());
	}

	@Test

	public void ParsesEmptyParameters() {
		List<IParameterName> parameterNames = CsNameUtils.getParameterNames("M()");
		assertEquals(0, parameterNames.size());
	}

	@Test

	public void ParsesParametersWithModifiers() {
		List<IParameterName> parameterNames = CsNameUtils.getParameterNames("M(out [T,P] p, out [T,P] q)");
		assertEquals(2, parameterNames.size());
		assertEquals(ParameterName.newParameterName("out [T,P] p"), parameterNames.get(0));
		assertEquals(ParameterName.newParameterName("out [T,P] q"), parameterNames.get(1));
	}

	@Test

	public void ParsesParametersWithAdditionalWhitespace() {
		List<IParameterName> parameterNames = CsNameUtils.getParameterNames("M(  out [T,P] p   )");
		assertEquals(1, parameterNames.size());
		assertEquals(ParameterName.newParameterName("out [T,P] p"), parameterNames.get(0));
	}

	// TODO more tests with delegates (in method name test)
	@Test

	public void FindNext() {
		int actual = CsNameUtils.findNext("abcabcabc", 1, 'a');
		assertEquals(3, actual);
	}

	@Test

	public void FindNext_array() {
		int actual = CsNameUtils.findNext("ccccab", 1, 'a', 'b');
		assertEquals(4, actual);
	}

	@Test

	public void FindNext_array2() {
		int actual = CsNameUtils.findNext("ccccba", 1, 'a', 'b');
		assertEquals(4, actual);
	}

	@Test

	public void FindNext_NotFound() {
		int actual = CsNameUtils.findNext("abbb", 1, 'a');
		assertEquals(-1, actual);
	}

	@Test

	public void FindPrevious() {
		int actual = CsNameUtils.findPrevious("abcabcabc", 5, 'a');
		assertEquals(3, actual);
	}

	@Test

	public void GetCorresponding_Round_Open() {
		char actual = CsNameUtils.getCorresponding('(');
		assertEquals(')', actual);
	}

	@Test

	public void GetCorresponding_Round_Close() {
		char actual = CsNameUtils.getCorresponding(')');
		assertEquals('(', actual);
	}

	@Test

	public void GetCorresponding_Curly_Open() {
		char actual = CsNameUtils.getCorresponding('{');
		assertEquals('}', actual);
	}

	@Test

	public void GetCorresponding_Curly_Close() {
		char actual = CsNameUtils.getCorresponding('}');
		assertEquals('{', actual);
	}

	@Test

	public void GetCorresponding_Array_Open() {
		char actual = CsNameUtils.getCorresponding('[');
		assertEquals(']', actual);
	}

	@Test

	public void GetCorresponding_Array_Close() {
		char actual = CsNameUtils.getCorresponding(']');
		assertEquals('[', actual);
	}

	@Test

	public void GetCorresponding_Pointy_Open() {
		char actual = CsNameUtils.getCorresponding('<');
		assertEquals('>', actual);
	}

	@Test

	public void GetCorresponding_Pointy_Close() {
		char actual = CsNameUtils.getCorresponding('>');
		assertEquals('<', actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void GetCorresponding_EverythingElse() {
		CsNameUtils.getCorresponding('x');
	}

	@Test

	public void FindPrevious_NotFound() {
		int actual = CsNameUtils.findPrevious("bbb", 1, 'a');
		assertEquals(-1, actual);
	}

	@Test

	public void FindCorrespondingCloseBracket_Round() {
		int actual = CsNameUtils.findCorrespondingCloseBracket("((()))", 1);
		assertEquals(4, actual);
	}

	@Test

	public void FindCorrespondingCloseBracket_Courly() {
		int actual = CsNameUtils.findCorrespondingCloseBracket("{{{}}}", 1);
		assertEquals(4, actual);
	}

	@Test

	public void FindCorrespondingCloseBracket_Array() {
		int actual = CsNameUtils.findCorrespondingCloseBracket("[[[]]]", 1);
		assertEquals(4, actual);
	}

	@Test

	public void FindCorrespondingCloseBracket_Pointy() {
		int actual = CsNameUtils.findCorrespondingCloseBracket("<<<>>>", 1);
		assertEquals(4, actual);
	}

	@Test

	public void FindCorrespondingOpenBracket_Round() {
		int actual = CsNameUtils.findCorrespondingOpenBracket("((()))", 4);
		assertEquals(1, actual);
	}

	@Test

	public void FindCorrespondingOpenBracket_Courly() {
		int actual = CsNameUtils.findCorrespondingOpenBracket("{{{}}}", 4);
		assertEquals(1, actual);
	}

	@Test

	public void FindCorrespondingOpenBracket_Array() {
		int actual = CsNameUtils.findCorrespondingOpenBracket("[[[]]]", 4);
		assertEquals(1, actual);
	}

	@Test

	public void FindCorrespondingOpenBracket_Pointy() {
		int actual = CsNameUtils.findCorrespondingOpenBracket("<<<>>>", 4);
		assertEquals(1, actual);
	}

}
