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
package cc.kave.commons.utils;

import static cc.kave.commons.utils.StringUtils.AsBytes;
import static cc.kave.commons.utils.StringUtils.AsString;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingCloseBracket;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingOpenBracket;
import static cc.kave.commons.utils.StringUtils.FindNext;
import static cc.kave.commons.utils.StringUtils.FindPrevious;
import static cc.kave.commons.utils.StringUtils.GetCorresponding;
import static cc.kave.commons.utils.StringUtils.TakeUntil;
import static cc.kave.commons.utils.StringUtils.containsAny;
import static cc.kave.commons.utils.StringUtils.containsIgnoreCase;
import static cc.kave.commons.utils.StringUtils.f;
import static org.junit.Assert.assertEquals;

import java.security.InvalidParameterException;

import org.junit.Test;
import org.junit.runner.RunWith;

import cc.kave.commons.testutils.ParameterData;
import cc.recommenders.exceptions.AssertionException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class StringUtilsTest {

	@Test
	public void SimpleRoundTrip() {
		String expected = "asd";
		String actual = AsString(AsBytes(expected));
		assertEquals(expected, actual);
	}

	@Test
	@Parameters({ "myfoobar,true", "myFoobar,true", "myFOObar,true", "myfOObar,true", "myf00bar,false",
			"myfobar,false" })
	public void ContainsIgnoreCase(String value, boolean expected) {
		assertEquals(expected, containsIgnoreCase(value, "foo"));
	}

	public static Object[][] provideContainsAnyCases() {
		ParameterData pd = new ParameterData();
		pd.add(new String[] { "foo" }, true);
		pd.add(new String[] { "wut" }, false);
		pd.add(new String[] { "some", "other", "foo" }, true);
		pd.add(new String[] { "but", "not", "this", "time" }, false);
		return pd.toArray();
	}

	@Test
	@Parameters(method = "provideContainsAnyCases")
	public void ContainsAny(String[] needles, boolean expected) {
		assertEquals(expected, containsAny("myfoobar", needles));
	}

	@Test
	public void Format() {
		String actual = f("a%sc", "b");
		String expected = "abc";
		assertEquals(expected, actual);
	}

	@Test
	public void FindNext_happy() {
		int actual = FindNext("abcabcabc", 1, 'a');
		assertEquals(3, actual);
	}

	@Test
	public void FindNext_array() {
		int actual = FindNext("ccccab", 1, 'a', 'b');
		assertEquals(4, actual);
	}

	@Test
	public void FindNext_array2() {
		int actual = FindNext("ccccba", 1, 'b', 'a');
		assertEquals(4, actual);
	}

	@Test
	public void FindNext_NotFound() {
		int actual = FindNext("abbb", 1, 'a');
		assertEquals(-1, actual);
	}

	@Test(expected = AssertionException.class)
	public void FindNext_IndexTooLow() {
		FindNext("abc", -1, 'a');
	}

	@Test(expected = AssertionException.class)
	public void FindNext_IndexTooHigh() {
		FindNext("abc", 3, 'a');
	}

	@Test
	public void FindPrevious_happy() {
		int actual = FindPrevious("abcabcabc", 5, 'a');
		assertEquals(3, actual);
	}

	@Test
	public void FindPrevious_arr() {
		int actual = FindPrevious("abcabcabc", 5, 'a', 'b');
		assertEquals(4, actual);
	}

	@Test
	public void FindPrevious_arr2() {
		int actual = FindPrevious("abcabcabc", 5, 'b', 'a');
		assertEquals(4, actual);
	}

	@Test
	public void FindPrevious_NotFound() {
		int actual = FindPrevious("abcabcabc", 5, 'x');
		assertEquals(-1, actual);
	}

	@Test(expected = AssertionException.class)
	public void FindPrevious_IndexTooLow() {
		FindPrevious("abc", -1, 'a');
	}

	@Test(expected = AssertionException.class)
	public void FindPrevious_IndexTooHigh() {
		FindPrevious("abc", 3, 'a');
	}

	@Test
	public void GetCorresponding_Round_Open() {
		char actual = GetCorresponding('(');
		assertEquals(')', actual);
	}

	@Test
	public void GetCorresponding_Round_Close() {
		char actual = GetCorresponding(')');
		assertEquals('(', actual);
	}

	@Test
	public void GetCorresponding_Curly_Open() {
		char actual = GetCorresponding('{');
		assertEquals('}', actual);
	}

	@Test
	public void GetCorresponding_Curly_Close() {
		char actual = GetCorresponding('}');
		assertEquals('{', actual);
	}

	@Test
	public void GetCorresponding_Array_Open() {
		char actual = GetCorresponding('[');
		assertEquals(']', actual);
	}

	@Test
	public void GetCorresponding_Array_Close() {
		char actual = GetCorresponding(']');
		assertEquals('[', actual);
	}

	@Test
	public void GetCorresponding_Pointy_Open() {
		char actual = GetCorresponding('<');
		assertEquals('>', actual);
	}

	@Test
	public void GetCorresponding_Pointy_Close() {
		char actual = GetCorresponding('>');
		assertEquals('<', actual);
	}

	@Test(expected = InvalidParameterException.class)
	public void GetCorresponding_EverythingElse() {
		GetCorresponding('x');
	}

	@Test
	public void FindCorrespondingCloseBracket_Round() {
		int actual = FindCorrespondingCloseBracket("((()))", 1);
		assertEquals(4, actual);
	}

	@Test
	public void FindCorrespondingCloseBracket_Courly() {
		int actual = FindCorrespondingCloseBracket("{{{}}}", 1);
		assertEquals(4, actual);
	}

	@Test
	public void FindCorrespondingCloseBracket_Array() {
		int actual = FindCorrespondingCloseBracket("[[[]]]", 1);
		assertEquals(4, actual);
	}

	@Test
	public void FindCorrespondingCloseBracket_Pointy() {
		int actual = FindCorrespondingCloseBracket("<<<>>>", 1);
		assertEquals(4, actual);
	}

	@Test
	public void FindCorrespondingOpenBracket_Round() {
		int actual = FindCorrespondingOpenBracket("((()))", 4);
		assertEquals(1, actual);
	}

	@Test
	public void FindCorrespondingOpenBracket_Courly() {
		int actual = FindCorrespondingOpenBracket("{{{}}}", 4);
		assertEquals(1, actual);
	}

	@Test
	public void FindCorrespondingOpenBracket_Array() {
		int actual = FindCorrespondingOpenBracket("[[[]]]", 4);
		assertEquals(1, actual);
	}

	@Test
	public void FindCorrespondingOpenBracket_Pointy() {
		int actual = FindCorrespondingOpenBracket("<<<>>>", 4);
		assertEquals(1, actual);
	}

	@Test
	public void TakeUntil_noChar() {
		String strIn = "abcd";
		String actual = TakeUntil(strIn);
		String expected = "abcd";
		assertEquals(expected, actual);
	}

	@Test
	public void TakeUntil_singleChar() {
		String strIn = "abcd";
		String actual = TakeUntil(strIn, 'b');
		String expected = "a";
		assertEquals(expected, actual);
	}

	@Test
	public void TakeUntil_multiChar() {
		String strIn = "abcd";
		String actual = TakeUntil(strIn, 'b', 'c');
		String expected = "a";
		assertEquals(expected, actual);
	}

	@Test
	public void TakeUntil_notFound() {
		String strIn = "abcd";
		String actual = TakeUntil(strIn, 'x');
		String expected = "abcd";
		assertEquals(expected, actual);
	}
}