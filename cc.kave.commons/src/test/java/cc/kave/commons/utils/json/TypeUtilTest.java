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
package cc.kave.commons.utils.json;

import org.junit.Assert;
import org.junit.Test;

public class TypeUtilTest {

	@Test
	public void SSTTypeToJaveTypeName() {
		String type = "[SST:Expressions.Simple.UnknownExpression]";
		String expected = "cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression";
		String actual = TypeUtil.toJavaTypeNames(type);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void contextToJavaTypeName() {
		String type = "\"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\"";
		String expected = "\"cc.kave.commons.model.events.completionevents.Context\"";
		String actual = TypeUtil.toJavaTypeNames(type);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void sstType() {
		String type = "[SST:Declarations.FieldDeclaration]";
		String expected = "cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration";
		String actual = TypeUtil.toJavaTypeNames(type);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void JavaTypeNameToCsharp() {
		String type = "\"cc.kave.commons.model.events.completionevents.Context\"";
		String expected = "\"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\"";
		String actual = TypeUtil.toCSharpTypeNames(type);
		Assert.assertEquals(expected, actual);
	}

}
