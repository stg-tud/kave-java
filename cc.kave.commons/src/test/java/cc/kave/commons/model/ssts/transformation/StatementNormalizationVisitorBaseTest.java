/*
 * Copyright 2016 Carina Oberle
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
package cc.kave.commons.model.ssts.transformation;

import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Set;

import org.junit.Before;

import com.google.common.collect.Sets;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;

public abstract class StatementNormalizationVisitorBaseTest<TReturn> {
	protected AbstractStatementNormalizationVisitor<TReturn> sut;
	protected SST sst, expectedSST;
	protected IStatement stmt0, stmt1, stmt2, stmt3;
	
	@Before
	public void setup() {
		sst = new SST();
		expectedSST = new SST();
		
		stmt0 = dummyStatement(0);
		stmt1 = dummyStatement(1);
		stmt2 = dummyStatement(2);
		stmt3 = dummyStatement(3);
	}
	
	// ---------------------------- helpers -----------------------------------
	
	protected void setNormalizing(IStatement... statements) {
		sst.setMethods(Sets.newHashSet(declareMethod(statements)));
	}

	protected void setExpected(IStatement... statements) {
		expectedSST.setMethods(Sets.newHashSet(declareMethod(statements)));
	}
	
	protected IStatement dummyStatement(int i) {
		return SSTUtil.declareVar("dummy" + i);
	}
	
	protected IVariableReference dummyVar(int i) {
		return SSTUtil.variableReference("var" + i);
	}
	
	// ---------------------------- asserts -----------------------------------

	protected void assertSSTs(ISST expectedSST, ISST sst) {
		Set<IMethodDeclaration> methods = sst.getMethods();
		Set<IMethodDeclaration> expectedMethods = expectedSST.getMethods();
		List<IStatement> body = methods.iterator().next().getBody();
		List<IStatement> expectedBody = expectedMethods.iterator().next().getBody();

		assertThat(methods.size(), equalTo(1));
		assertThat(methods.size(), equalTo(expectedMethods.size()));
		assertThat(body.size(), equalTo(expectedBody.size()));

		for (int i = 0; i < body.size(); i++) {
			assertThat(body.get(i), equalTo(expectedBody.get(i)));
		}
	}

	protected void assertTransformedSST() {
		sst.accept(sut, null);
		assertSSTs(expectedSST, sst);
	}
}
