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

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.TypeName;

public class TypeErasure {
	public static ITypeName of(ITypeName type) {
		return TypeName.newTypeName(of(type.getIdentifier()));
	}

	public static IMethodName of(IMethodName method) {
		return MethodName.newMethodName(of(method.getIdentifier()));
	}

	public static String of(String id) {
		int startIdx = id.indexOf('`');
		if (startIdx == -1) {
			return id;
		}

		Map<String, String> replacements = Maps.newLinkedHashMap();
		int tick = FindNext(id, 0, '`');

		while (tick != -1) {
			int open = FindNext(id, tick, '[');
			int length = open - tick - 1;
			String numStr = length > 0 ? id.substring(tick + 1, open).trim() : "0";

			if (length < 1) {
				// TODO fix name creation, this should not happen!
				System.out.printf("\nEE: cannot remove generic (no tick number): %s", id);
			}

			int numGenerics = 0;
			try {
				numGenerics = Integer.parseInt(numStr);
			} catch (NumberFormatException e) {
				// TODO fix name creation, this should not happen!
				System.out.printf("\nEE: cannot remove generic (invalid tick number between %d and %d): %s", tick, open,
						id);
			}

			for (int i = 0; i < numGenerics; i++) {
				open = FindNext(id, open + 1, '[');
				int close = FindCorrespondingCloseBracket(id, open);

				int arrowStart = FindNext(id, open, '-');
				if (arrowStart != -1 && arrowStart < close) {
					String param = id.substring(open, arrowStart).trim();
					String complete = id.substring(open, close);
					replacements.put(complete, param);
				}
			}
			tick = FindNext(id, tick + 1, '`');
		}
		String res = id;
		for (String k : replacements.keySet()) {
			String with = replacements.get(k);
			res = res.replace(k, with);
		}
		return res;
	}

	public static boolean HasParameters(String identifierWithparameters) {
		int startOfParameters = identifierWithparameters.indexOf('(');
		return (startOfParameters > 0 && identifierWithparameters.charAt(startOfParameters + 1) != ')');
	}

	public static List<IParameterName> GetParameterNames(String identifierWithParameters) {
		List<IParameterName> parameters = Lists.newLinkedList();
		int endOfParameters = identifierWithParameters.lastIndexOf(')');
		boolean hasNoBrackets = endOfParameters == -1;
		if (hasNoBrackets) {
			return parameters;
		}

		int startOfParameters = FindCorrespondingOpenBracket(identifierWithParameters, endOfParameters);
		int current = startOfParameters;

		boolean hasNoParams = startOfParameters == (endOfParameters - 1);
		if (hasNoParams) {
			return parameters;
		}

		while (current != endOfParameters) {
			int startOfParam = ++current;

			if (identifierWithParameters.charAt(current) != '[') {
				current = FindNext(identifierWithParameters, current, '[');
			}
			current = FindCorrespondingCloseBracket(identifierWithParameters, current);
			current = FindNext(identifierWithParameters, current, ',', ')');
			int endOfParam = current;

			int lengthOfSubstring = endOfParam - startOfParam;
			String paramSubstring = identifierWithParameters.substring(startOfParam, lengthOfSubstring);
			parameters.add(ParameterName.newParameterName(paramSubstring.trim()));
		}

		return parameters;
	}

	public static int FindNext(String str, int currentIndex, char... characters) {
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

	public static int FindPrevious(String str, int currentIndex, char character) {
		for (int i = currentIndex; i >= 0; i--) {
			if (str.charAt(i) == character) {
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

	private static int UpdateDepth(int depth, char open, char close, char current) {
		if (current == open) {
			return depth + 1;
		}
		if (current == close) {
			return depth - 1;
		}
		return depth;
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
			throw new RuntimeException(String.format("no supported bracket type: {0}", c));
		}
	}

	/// <summary>
	/// Returns the index after the ']' that corresponds to the first '[' in the
	/// identifier, starting at the given offset.
	/// </summary>
	public static int EndOfNextTypeIdentifier(String identifier, int offset) {
		int index = StartOfNextTypeIdentifier(identifier, offset);
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
	public static int StartOfNextTypeIdentifier(String identifier, int offset) {
		return identifier.indexOf('[', offset);
	}
}