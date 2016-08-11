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

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;

import cc.recommenders.assertions.Asserts;

public class StringUtils {

	public static byte[] AsBytes(String string) {
		return string.getBytes(StandardCharsets.UTF_8);
	}

	public static String AsString(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static String f(String string, Object... args) {
		return String.format(string, args);
	}

	public static boolean containsIgnoreCase(String strIn, String needle) {
		return strIn.toLowerCase().contains(needle.toLowerCase());
	}

	public static boolean containsAny(String string, String... needles) {
		for (String needle : needles) {
			if (string.contains(needle)) {
				return true;
			}
		}
		return false;
	}

	public static int FindNext(String str, int currentIndex, char... characters) {
		AssertIndexBoundaries(str, currentIndex);
		for (int i = currentIndex; i < str.length(); i++) {
			char c = str.charAt(i);
			if (contains(characters, c)) {
				return i;
			}
		}
		return -1;
	}

	private static boolean contains(char[] characters, char c) {
		for (char c2 : characters) {
			if (c2 == c) {
				return true;
			}
		}
		return false;
	}

	private static void AssertIndexBoundaries(String str, int currentIndex) {
		if (currentIndex < 0 || currentIndex >= str.length()) {
			Asserts.fail(f("index '%d' is out of bounds for string '%s'", currentIndex, str));
		}
	}

	public static int FindPrevious(String str, int currentIndex, char... characters) {
		AssertIndexBoundaries(str, currentIndex);
		for (int i = currentIndex; i >= 0; i--) {
			char c = str.charAt(i);
			if (contains(characters, c)) {
				return i;
			}
		}
		return -1;
	}

	public static int FindCorrespondingOpenBracket(String str, int currentIndex) {
		char open = str.charAt(currentIndex);
		char close = GetCorresponding(open);

		int depth = 0;
		for (int i = currentIndex; i > 0; i--) {
			depth = UpdateDepth(depth, open, close, str.charAt(i));
			if (depth == 0) {
				return i;
			}
		}
		return -1;
	}

	public static int FindCorrespondingCloseBracket(String str, int currentIndex) {
		char open = str.charAt(currentIndex);
		char close = GetCorresponding(open);

		int depth = 0;
		for (int i = currentIndex; i < str.length(); i++) {
			depth = UpdateDepth(depth, open, close, str.charAt(i));
			if (depth == 0) {
				return i;
			}
		}
		return -1;
	}

	private static int UpdateDepth(int depth, char open, char close, char current) {
		if (current == open) {
			return depth + 1;
		}
		if (current == close) {
			return depth - 1;
		}
		return depth;
	}

	public static char GetCorresponding(char c) {
		switch (c) {
		case '(':
			return ')';
		case ')':
			return '(';
		case '{':
			return '}';
		case '}':
			return '{';
		case '[':
			return ']';
		case ']':
			return '[';
		case '<':
			return '>';
		case '>':
			return '<';
		default:
			throw new InvalidParameterException(String.format("no supported bracket type: {0}", c));
		}
	}

	public static String TakeUntil(String strIn, char... needles) {
		StringBuilder sb = new StringBuilder();
		int cur = 0;
		char c;
		while (cur < strIn.length() && !contains(needles, (c = strIn.charAt(cur++)))) {
			sb.append(c);
		}
		return sb.toString();
	}
}