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
package cc.kave.commons.model.ssts.impl.visitor;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class AbstractThrowingNodeVisitorTest extends AbstractNodeVisitorBaseTest<Void, Void> {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Override
	protected void assertVisitor(ISSTNode node, ISSTNode... ns) {
		exception.expect(RuntimeException.class);
		node.accept(sut, null);
	}

	@Override
	protected ISSTNodeVisitor<Void, Void> getTestVisitor() {
		return new TestVisitor();
	}
	
	public static class TestVisitor extends AbstractThrowingNodeVisitor<Void, Void> {
	}
}
