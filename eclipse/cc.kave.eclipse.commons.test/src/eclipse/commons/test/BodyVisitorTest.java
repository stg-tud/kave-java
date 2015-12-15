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

import org.junit.BeforeClass;
import org.junit.Test;

import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;

public class BodyVisitorTest extends BaseDeclarationTest {

	@BeforeClass
	public static void initialize() {
		PluginAstParser parser = new PluginAstParser("test.project.a", "example.classes;StatementTestClass.java");
		context = parser.getContext();
	}

	@Test
	public void returnStmtTest(){
		ReturnStatement expected = new ReturnStatement();
		
		context.getMethods();
	}
}
