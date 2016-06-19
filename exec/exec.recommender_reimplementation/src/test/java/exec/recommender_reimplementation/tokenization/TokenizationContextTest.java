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

import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.TypeName;

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
		uut.pushType(TypeName.newTypeName("System.String, mscore, 4.0.0.0"));
		
		assertListContainsToken("String");
	}
	
	@Test
	public void pushesTypeWithOneTypeParameters() {
		uut.pushType(TypeName.newTypeName("SomeType`1[[T -> System.Int32, mscore, 4.0.0.0]], A, 4.0.0.0"));
		
		assertListContainsTokens("SomeType","<", "Int32", ">");
	}
	
	@Test
	public void pushesTypeMultipleTypeParameters() {
		uut.pushType(TypeName.newTypeName("SomeType`3[[T1 -> System.Int32, mscore, 4.0.0.0], [T2 -> System.Int32, mscore, 4.0.0.0], [T3 -> System.Int32, mscore, 4.0.0.0]], A, 4.0.0.0"));
		
		assertListContainsTokens("SomeType","<", "Int32",  "," , "Int32",  ",", "Int32",  ">");
	}
	
	@Test
	public void pushesParamater() {
		uut.pushParameter(ParameterName.newParameterName("[System.String, mscore, 4.0.0.0] someString"));
		
		assertListContainsTokens("String", "someString");
	}
	
	@Test
	public void pushesParameterWithType() {
		uut.pushParameter(ParameterName.newParameterName("[SomeType`1[[T -> System.Int32, mscore, 4.0.0.0]], A, 4.0.0.0] foo"));
		
		assertListContainsTokens("SomeType", "<", "Int32", ">", "foo");
	}
	
	@Test
	public void pushesOutParamater() {
		uut.pushParameter(ParameterName.newParameterName("out [System.String, mscore, 4.0.0.0] someString"));
		
		assertListContainsTokens("out", "String", "someString");
	}
	@Test
	public void pushesParamsParamater() {
		uut.pushParameter(ParameterName.newParameterName("params [System.String, mscore, 4.0.0.0] someString"));
		
		assertListContainsTokens("params", "String", "someString");
	}
	@Test
	public void pushesRefParamater() {
		uut.pushParameter(ParameterName.newParameterName("ref [e:someEnum, A, 4.0.0.0] refParameter"));
		
		assertListContainsTokens("ref", "someEnum", "refParameter");
	}
	@Test
	public void pushesOptionalParamater() {
		uut.pushParameter(ParameterName.newParameterName("opt [System.String, mscore, 4.0.0.0] someString"));
		
		assertListContainsTokens("opt", "String", "someString");
	}
	
	@Test
	public void pushesParameterList() {
		IParameterName someStringParameter = ParameterName.newParameterName("[System.String, mscore, 4.0.0.0] someString");
		IParameterName someIntParameter = ParameterName.newParameterName("[System.Int32, mscore, 4.0.0.0] someInt");
		IParameterName someObjectParameter = ParameterName.newParameterName("[System.Object, mscore, 4.0.0.0] someObject");
	
		uut.pushParameters(Lists.newArrayList(someStringParameter,someIntParameter, someObjectParameter));
		
		assertListContainsTokens("(", "String", "someString", ",", "Int32", "someInt", ",", "Object", "someObject", ")");
	
	}
	
	protected void assertListContainsToken(String token) {
		assertThat(uut.getTokenStream(),Matchers.contains(token));
	}
	
	protected void assertListContainsTokens(String... tokens) {
		assertThat(uut.getTokenStream(), Matchers.contains(tokens));
	}
}
