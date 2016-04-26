/**
 * Copyright 2016 Sebastian Proksch
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
package cc.kave.commons.model.names.csharp.parser;

import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.recommenders.exceptions.AssertionException;

public class TypeValidationTest {

	private int numPassed = 0;
	private Set<String> errors;
	private TypeNameParseUtil sut;

	@Before
	public void setup() {
		numPassed = 0;
		errors = new HashSet<String>();
		sut = new TypeNameParseUtil();
	}

	@After
	public void teardown() {
		if (errors.size() > 0) {
			int numErrors = errors.size();
			int numTotal = numErrors + numPassed;
			StringBuilder sb = new StringBuilder(String.format("%d/%d validations failed:\n", numErrors, numTotal));
			for (String error : errors) {
				sb.append(error);
			}

			fail(sb.toString());
		}
	}

	@Test
	public void positiveCases() {
		for (String t : new String[] {
				// type parameters
				"T", "T2", "t",
				// regular types
				"T,P", "n.T,P", "T,A,0.0.0.0", "?",
				// nested types
				"n:T+NT,P", "n:n:T+NT+T,P", "n:a.T+NT,P",
				// type qualifier
				"i:T,P", "e:T,P", "s:T,P", "n.i:T,P",
				// delegates
				"d:[T1,P] [T2,P].M()", "d:[?] [?].M( )", "d:[?] [?].M([?] p)", "d:[?] [?].M([?] p1,[?] p2)",
				// unnecessary whitespaces
				"T, A, 0.1.2.3", "T'1[[T1 -> ?]], P", "d:[?][?].M([?]p1,[?] p2, [?] p3 , [?] p4 )",
				// generics
				"T'1[[T1]],P", "T'2[[T1],[T2->?]],P",
				// arrays
				"arr(1):T,P", "arr(2):T,P",
				// combination
				/* arr+X */ "arr(1):d:[T,P][T,P].M()", "arr(1):T'1[[T2]],P", "arr(1):T", //
				/* nested+X */ "n:T'1[[T]]+NT,P", //
				"n:T+i:N,P",
				"n:T+NT'1[[T]],P", }) {
			assertValid(t);
		}
	}

	@Test
	public void generateValidCases() {
		Set<String> types = getTypes(2);
		System.out.printf("generated %d types...\n", types.size());
		for (String t : types) {
			System.out.println(t);
			assertValid(t);
		}
	}

	private Set<String> getTypes(int maxDepth) {
		Set<String> types = new LinkedHashSet<String>();
		types.add("T"); // generic type parameter

		for (String simpleTypeName : createSimpleTypeNames()) {
			// simple cases
			add(types, "n.%s,P", simpleTypeName);
			add(types, "n.%s,A,0.0.0.1", simpleTypeName);
			for (String nestedSimpleTypeName : createSimpleTypeNames()) {
				// single nesting
				add(types, "n:n.%s+%s,P", simpleTypeName, nestedSimpleTypeName);
				for (String nested2SimpleTypeName : createSimpleTypeNames()) {
					// double nesting
					add(types, "n:n:n.%s+%s+%s,P", simpleTypeName, nestedSimpleTypeName, nested2SimpleTypeName);
				}
			}
		}

		if (maxDepth > 0) {
			maxDepth = maxDepth - 1;
			for (String gt : createGenericTypes(maxDepth)) {
				types.add(gt);
			}
			for (String dt : createDelegates(maxDepth)) {
				types.add(dt);
			}
		}

		Set<String> typesOut = new LinkedHashSet<String>();
		typesOut.add("?"); // unknown
		typesOut.addAll(types);
		for (int arrSize : new int[] { 0, 1, 2 }) {
			String arrPart = createArrPart(arrSize);
			for (String t : types) {
				typesOut.add(arrPart+t);
			}
		}
		return typesOut;
	}

	private Set<String> createSimpleTypeNames() {
		Set<String> types = new LinkedHashSet<String>();
		types.add("T");
		types.add("T'1[[T]]");
		types.add("T'1[[T->T,P]]");
		return types;
	}

	private void add(Set<String> types, String string, Object... args) {
		types.add(String.format(string, args));
	}

	private String createArrPart(int arrSize) {
		if (arrSize <= 0)
			return "";
		return "arr(" + arrSize + "):";
	}

	private Set<String> createGenericTypes(int maxDepth) {
		Set<String> types = new LinkedHashSet<String>();
		for (String t : getTypes(maxDepth)) {
			types.add("n.T1'1[[T2->" + t + "]],P");
		}
		return types;
	}

	public Set<String> createDelegates(int maxDepth) {
		Set<String> types = new LinkedHashSet<String>();
		for (String t : getTypes(maxDepth)) {
			for (int numParam : new int[] { 0, 1, 2 }) {
				types.add(String.format("d:[%s] [%s].M(%s)", t, t, createParameterList(numParam, t)));
			}
		}
		return types;
	}

	private String createParameterList(int numParam, String pt) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numParam; i++) {
			if (i > 0) {
				sb.append(',');
			}
			sb.append('[');
			sb.append(pt);
			sb.append("] ");
			sb.append('p').append(i);
		}
		return sb.toString();
	}

	@Test
	public void negativeCases() {
		for (String t : new String[] {
				// basic cases (names)
				"",
				// type param with spaces
				"A B",
				// invalid ids
				"T[,A", "T],A", "T(,A", "T),A", "T{,A", "T},A", "T,U,A,1.0.0.0", "T,A,0.0.0.01",
				// generics
				"T'1,P", "T[[T1]]", "T'-1[[T]]", "T'1[[]]", "T'1[[T ->]]", "T'1[[T -> T]]", "T'0[[T]]", "T'1[]",

		}) {
			assertInvalid(t);
		}
	}

	@Test
	public void manual() {
		TypeNameParseUtil.validateTypeName("T'2[[T1],[T2->?]],P");
	}

	private void assertValid(String type) {
		assertResult(type, true);
	}

	private void assertInvalid(String type) {
		assertResult(type, false);
	}

	private void assertResult(String t, boolean expected) {
		try {
			TypeNameParseUtil.validateTypeName(t);
			if (!expected) {
				String error = String.format("validation of '%s' failed: expected '%s'\n", t, "invalid");
				errors.add(error);
			}
		} catch (AssertionException e) {
			if (expected) {
				String error = String.format("validation of '%s' failed: expected '%s'\n", t, "invalid");
				errors.add(error);
			}
		}
	}
}