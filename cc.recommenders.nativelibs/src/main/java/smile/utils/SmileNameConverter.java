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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmileNameConverter {
	
	private static String pattern = "[^A-Za-z0-9]";
	private static String replaceChar = "_";
	
	public static String convertToLegalSmileName(String name) {
		
		if(name.matches("^[0-9]")){
			name = "x" + name;
		}
			
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(name); 
		
		return m.replaceAll(replaceChar);
	}
}
