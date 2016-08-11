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

package cc.kave.commons.model.naming.impl.csharp;

import static cc.kave.commons.utils.StringUtils.FindCorrespondingCloseBracket;
import static cc.kave.commons.utils.StringUtils.FindCorrespondingOpenBracket;
import static cc.kave.commons.utils.StringUtils.FindNext;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IParameterName;

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

		int startOfParameters = FindCorrespondingOpenBracket(identifier, endOfParameters);
		int current = startOfParameters;

		boolean hasNoParams = startOfParameters == (endOfParameters - 1);
		if (hasNoParams) {
			return parameters;
		}

		while (current != endOfParameters) {
			int startOfParam = ++current;

			if (identifier.charAt(current) != '[') {
				current = FindNext(identifier, current, '[');
			}
			current = FindCorrespondingCloseBracket(identifier, current);
			current = FindNext(identifier, current, ',', ')');
			int endOfParam = current;

			String paramSubstring = identifier.substring(startOfParam, endOfParam);
			parameters.add(Names.newParameter(paramSubstring.trim()));
		}

		return parameters;
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
