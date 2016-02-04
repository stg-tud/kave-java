/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package smile.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SmileNameConverterTest {
	
	@Test
	public void charactersAndNumbersAreNotConverted() {
		String expected = "aA1_";
		String actual = SmileNameConverter.convertToLegalSmileName(expected);
		
		assertEquals(expected, actual);
	}

	@Test
	public void preceedingNumbersAreHandled() {
		String actual = SmileNameConverter.convertToLegalSmileName("1");
		String expected = "x1";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void nonAlphanumericCharactersAreConvertedToUnderscore() {
		String in = "aA1!\"§$%&/()=?{[]}-+*#;,:.><|öäüÖÄÜ^€@";
		String actual = SmileNameConverter.convertToLegalSmileName(in);
		String expected = "aA1___________________________________";
		
		assertEquals(expected, actual);
	}
}