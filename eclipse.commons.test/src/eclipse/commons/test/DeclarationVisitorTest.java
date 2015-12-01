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

package eclipse.commons.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;

public class FieldTest {

	@Test
	public void name() {
		PluginAstParser parser = new PluginAstParser("test.project.a", "example.classes;TestClass.java");
		
		FieldDeclaration expected = new FieldDeclaration();
		expected.setName(CsFieldName.newFieldName("[%int, rt.jar, 1.8] [example.classes.TestClass, ?].intTest"));
		
		FieldDeclaration actual;
		assertEquals(expected, null);
	}
}
