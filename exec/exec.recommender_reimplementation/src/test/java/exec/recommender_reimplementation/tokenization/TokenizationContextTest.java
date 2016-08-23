/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.tokenization;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;


public class TokenizationContextTest {

	protected TokenizationContext uut;
	
	@Before
	public void setup() {
		uut = new TokenizationContext(new TokenizationSettings(true));
	}
	
	@Test
	public void pushesToken() {
		uut.pushToken("someToken");
		
		assertListContainsToken("someToken");
	}
	
	@Test
	public void pushesKeyword() {
		uut.pushKeyword("if");
		
		assertListContainsToken("if");
	}
	
	@Test
	public void pushesOperator() {
		uut.pushOperator("=");
		
		assertListContainsToken("=");
	}
	
	@Test
	public void pushesTypeWithoutTypeParameters() {
		uut.pushType(Names.newType("p:string"));
		
		assertListContainsToken("string");
	}
	
	@Test
	public void pushesTypeWithOneTypeParameters() {
		uut.pushType(Names.newType("SomeType`1[[T -> p:int]], A, 4.0.0.0"));
		
		assertListContainsTokens("SomeType", "<", "int", ">");
	}
	
	@Test
	public void pushesTypeMultipleTypeParameters() {
		uut.pushType(Names.newType(
				"SomeType`3[[T1 -> p:int], [T2 -> p:int], [T3 -> p:int]], A, 4.0.0.0"));
		
		assertListContainsTokens("SomeType", "<", "int", ",", "int", ",", "int", ">");
	}
	
	@Test
	public void pushesParamater() {
		uut.pushParameter(Names.newParameter("[p:string] someString"));
		
		assertListContainsTokens("string", "someString");
	}
	
	@Test
	public void pushesParameterWithType() {
		uut.pushParameter(Names.newParameter("[SomeType`1[[T -> p:int]], A, 4.0.0.0] foo"));
		
		assertListContainsTokens("SomeType", "<", "int", ">", "foo");
	}
	
	@Test
	public void pushesOutParamater() {
		uut.pushParameter(Names.newParameter("out [p:string] someString"));
		
		assertListContainsTokens("out", "string", "someString");
	}
	@Test
	public void pushesParamsParamater() {
		uut.pushParameter(Names.newParameter(
				"params [" + Names.newArrayType(1, (TypeName) Names.newType("T1,P1")).getIdentifier()
						+ "] someString"));
		
		assertListContainsTokens("params", "T1[]", "someString");
	}

	@Test
	public void pushesRefParamater() {
		uut.pushParameter(Names.newParameter("ref [e:someEnum, A, 4.0.0.0] refParameter"));
		
		assertListContainsTokens("ref", "someEnum", "refParameter");
	}
	@Test
	public void pushesOptionalParamater() {
		uut.pushParameter(Names.newParameter("opt [p:string] someString"));
		
		assertListContainsTokens("opt", "string", "someString");
	}
	
	@Test
	public void pushesParameterList() {
		IParameterName someStringParameter = Names.newParameter("[p:string] someString");
		IParameterName someIntParameter = Names.newParameter("[p:int] someInt");
		IParameterName someObjectParameter = Names.newParameter("[p:object] someObject");
	
		uut.pushParameters(Lists.newArrayList(someStringParameter,someIntParameter, someObjectParameter));
		
		assertListContainsTokens("(", "string", "someString", ",", "int", "someInt", ",", "object", "someObject", ")");
	
	}
	
	protected void assertListContainsToken(String token) {
		assertThat(uut.getTokenStream(),Matchers.contains(token));
	}
	
	protected void assertListContainsTokens(String... tokens) {
		assertThat(uut.getTokenStream(), Matchers.contains(tokens));
	}
}
