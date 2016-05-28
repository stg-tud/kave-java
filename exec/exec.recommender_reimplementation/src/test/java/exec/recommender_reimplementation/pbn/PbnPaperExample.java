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
package exec.recommender_reimplementation.pbn;

import static cc.kave.commons.pointsto.extraction.CoReNameConverter.convert;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

import com.google.common.collect.Sets;

public class PbnPaperExample {

	
	private List<Context> contexts;

	private UsageExtractor uut = new UsageExtractor();
	
	@Before
	public void LoadPaperExample() {
		TestSSTBuilder sstBuilder = new TestSSTBuilder();
		contexts = sstBuilder.createPaperTest();
	}
	
	@Test
	public void CheckPaperExample() {
		List<Usage> usageList = new ArrayList<>();
		for (Context context : contexts) {
			uut.extractUsageFromContext(context, usageList);
		}
		ITypeName sType = TypeName.newTypeName("Test.PaperTest.S, Test");
		ITypeName aType = TypeName.newTypeName("Test.PaperTest.A, Test");
		ITypeName bType = TypeName.newTypeName("Test.PaperTest.B, Test");
		ITypeName cType = TypeName.newTypeName("Test.PaperTest.C, Test");
		ITypeName dType = TypeName.newTypeName("Test.PaperTest.D, Test");
		
		ITypeName voidType = TypeName.newTypeName("System.Void, mscorlib");
		IMethodName sEntry1 = createMethodName(voidType, sType, "entry1", "");
		IMethodName cEntry2 = createMethodName(voidType, cType, "entry2","[" + bType.getIdentifier() + "]" + " b");
		IMethodName cEntry3 = createMethodName(voidType, cType, "entry3", "");
		
		IFieldName bField = FieldName.newFieldName(String.format(Locale.US, "[%s] [%s].b", bType.getIdentifier(), aType.getIdentifier()));
		
		Query bS = new Query();
		bS.setClassContext(convert(sType));
		bS.setMethodContext(convert(sEntry1));
		bS.setType(convert(bType));
		bS.setDefinition(DefinitionSites.createDefinitionByField(convert(bField)));
		bS.setAllCallsites(Sets.newHashSet(CallSites.createReceiverCallSite(convert(createMethodName(voidType, bType, "m1", ""))), 
				CallSites.createReceiverCallSite(convert(createMethodName(voidType, bType, "m2", ""))),
				CallSites.createParameterCallSite(convert(cEntry2), 0)));
		
		Query cS = new Query();
		cS.setClassContext(convert(sType));
		cS.setMethodContext(convert(sEntry1));
		cS.setType(convert(cType));
		cS.setDefinition(DefinitionSites.createDefinitionByReturn(convert(createMethodName(cType, sType, "fromS", ""))));
		cS.setAllCallsites(Sets.newHashSet(CallSites.createReceiverCallSite(convert(cEntry2))));
		
		Query sS = new Query();
		sS.setClassContext(convert(sType));
		sS.setMethodContext(convert(sEntry1));
		sS.setType(convert(sType));
		sS.setDefinition(DefinitionSites.createDefinitionByThis());
		sS.setAllCallsites(Sets.newHashSet(CallSites.createReceiverCallSite(convert(createMethodName(cType, sType, "fromS", "")))));
		
		Query bC = new Query();
		bC.setClassContext(convert(cType));
		bC.setMethodContext(convert(cEntry2));
		bC.setType(convert(bType));
		bC.setDefinition(DefinitionSites.createDefinitionByParam(convert(cEntry2), 0));
		bC.setAllCallsites(Sets.newHashSet(CallSites.createReceiverCallSite(convert(createMethodName(voidType, bType, "m3", "")))));
		
		Query dC = new Query();
		dC.setClassContext(convert(cType));
		dC.setMethodContext(convert(cEntry3));
		dC.setType(convert(dType));
		dC.setDefinition(DefinitionSites.createDefinitionByConstructor(convert(createMethodName(voidType, dType, ".ctor", ""))));
		dC.setAllCallsites(Sets.newHashSet(CallSites.createReceiverCallSite(convert(createMethodName(voidType, dType, "m4", "")))));
		
		assertThat(usageList, Matchers.containsInAnyOrder(bS,cS,sS,bC,dC));
	}

	public static IMethodName createMethodName(ITypeName returnType, ITypeName className, String name, String parameter) {
		return MethodName.newMethodName(
				String.format("[%s] [%s].%s(%s)", returnType.getIdentifier(), className.getIdentifier(), name, parameter));
	}

}
