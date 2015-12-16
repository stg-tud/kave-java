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

package cc.kave.commons.model.names.csharp;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.IParameterName;

public class CsNameUtils {

	/**
	 * @param identifier
	 *            complete CsName identifier
	 * @param start
	 *            index of an opening bracket in the identifier
	 * @return partial identifier starting at an opening bracket and ending at a
	 *         closing bracket, empty if the identifier contains no closing
	 *         bracket or start points not to an opening bracket
	 */
	public static String getClosingBracket(String identifier, int start) {
		if (!identifier.contains("]") || identifier.charAt(start) != '[') {
			return "";
		}

		return identifier.substring(start, getClosingBracketIndex(identifier, start));
	}

	/**
	 * @param identifier
	 *            complete CsName identifier
	 * @param start
	 *            index of an opening bracket in the identifier
	 * @return index of the corresponding closing bracket, -1 if the identifier
	 *         contains no closing bracket or start points not to an opening
	 *         bracket
	 */
	public static int getClosingBracketIndex(String identifier, int start) {
		if (!identifier.contains("]") || identifier.charAt(start) != '[') {
			return -1;
		}

		int bracketCounter = 1;
		int charIndex = start + 1;

		while (bracketCounter > 0) {
			if (identifier.charAt(charIndex) == '[') {
				bracketCounter++;
			} else if (identifier.charAt(charIndex) == ']') {
				bracketCounter--;
			}

			charIndex++;

			if (bracketCounter == 0) {
				return charIndex;
			}
		}

		return -1;
	}

	public static boolean hasParameters(String identifier) {
		int startOfParameters = identifier.indexOf('(');
		return (startOfParameters > 0 && identifier.charAt(startOfParameters + 1) != ')');
	}

	public static List<IParameterName> getParameterNames(String identifier) {
		ArrayList<IParameterName> parameters = new ArrayList<IParameterName>();
		int endOfParameters = identifier.lastIndexOf(')');
		boolean hasNoBrackets = endOfParameters == -1;

		if (hasNoBrackets) {
			return parameters;
		}

		int startOfParameters = findCorrespondingOpenBracket(identifier, endOfParameters);
		int current = startOfParameters;

		boolean hasNoParams = startOfParameters == (endOfParameters - 1);
		if (hasNoParams) {
			return parameters;
		}

		while (current != endOfParameters) {
			int startOfParam = ++current;

			if (identifier.charAt(current) != '[') {
				current = findNext(identifier, current, '[');
			}
			current = findCorrespondingCloseBracket(identifier, current);
			current = findNext(identifier, current, ',', ')');
			int endOfParam = current;

			int lengthOfSubstring = endOfParam - startOfParam;
			String paramSubstring = identifier.substring(startOfParam, endOfParam);
			parameters.add(ParameterName.newParameterName(paramSubstring.trim()));
		}

		return parameters;
	}

	public static int findNext(String str, int currentIndex, char... characters) {
		for (int i = currentIndex; i < str.length(); i++) {
			for (int j = 0; j < characters.length; j++) {
				if (characters[j] == (str.charAt(i))) {
					return i;
				}
			}
		}
		return -1;
	}

	public static int findPrevious(String str, int currentIndex, char character) {
		for (int i = currentIndex; i >= 0; i--) {
			if (str.charAt(i) == character) {
				return i;
			}
		}
		return -1;
	}

	public static int findCorrespondingOpenBracket(String str, int currentIndex) {
		char open = str.charAt(currentIndex);
		char close = getCorresponding(open);

		int depth = 0;
		for (int i = currentIndex; i > 0; i--) {
			depth = updateDepth(depth, open, close, str.charAt(i));
			if (depth == 0) {
				return i;
			}
		}
		return -1;
	}

	private static int updateDepth(int depth, char open, char close, char current) {
		if (current == open) {
			return depth + 1;
		}
		if (current == close) {
			return depth - 1;
		}
		return depth;
	}

	public static int findCorrespondingCloseBracket(String str, int currentIndex) {
		char open = str.charAt(currentIndex);
		char close = getCorresponding(open);

		int depth = 0;
		for (int i = currentIndex; i < str.length(); i++) {
			depth = updateDepth(depth, open, close, str.charAt(i));
			if (depth == 0) {
				return i;
			}
		}
		return -1;
	}

	public static char getCorresponding(char c) {
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
			throw new IllegalArgumentException(String.format("no supported bracket type: %c", c));
		}
	}

	/// <summary>
	/// Returns the index after the ']' that corresponds to the first '[' in
	/// the identifier, starting at the given offset.
	/// </summary>
	public static int endOfNextTypeIdentifier(String identifier, int offset) {
		int index = startOfNextTypeIdentifier(identifier, offset);
		int brackets = 0;
		do {
			if (identifier.charAt(index) == '[') {
				brackets++;
			} else if (identifier.charAt(index) == ']') {
				brackets--;
			}
			index++;
		} while (index < identifier.length() && brackets > 0);
		return index;
	}

	/// <summary>
	/// Returns the index of the next '[' in the identifier, starting at the
	/// given offset.
	/// </summary>
	public static int startOfNextTypeIdentifier(String identifier, int offset) {
		return identifier.indexOf('[', offset);
	}

}
