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

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IAliasName;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.ILocalVariableName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class CSharpNameDeserializationTest {
	@Test
	public void DeserializesToCsName() {
		assertDeserialize("\"CSharp.Name:identifier\"", Names.newGeneral("identifier"), IName.class);
	}

	@Test
	public void DeserializesToAliasName() {
		assertDeserialize("\"CSharp.AliasName:alias\"", Names.newAlias("alias"), IAliasName.class);
	}

	@Test
	public void DeserializesToAssemblyName() {
		assertDeserialize("\"CSharp.AssemblyName:A, 1.2.3.4\"", Names.newAssembly("A, 1.2.3.4"), IAssemblyName.class);
	}

	@Test
	public void DeserializesToEventName() {
		assertDeserialize("\"CSharp.EventName:[HT] [DT].Event\"", Names.newEvent("[HT] [DT].Event"), IEventName.class);
	}

	@Test
	public void DeserializesToFieldName() {
		assertDeserialize("\"CSharp.FieldName:[VT] [DT]._field\"", Names.newField("[VT] [DT]._field"),
				IFieldName.class);
	}

	@Test
	public void DeserializesToLambdaName() {
		assertDeserialize("\"CSharp.LambdaName:[VT] ()\"", Names.newLambda("[VT] ()"), ILambdaName.class);
	}

	@Test
	public void DeserializesToLocalVariableName() {
		assertDeserialize("\"CSharp.LocalVariableName:[VT] v\"", Names.newLocalVariable("[VT] v"),
				ILocalVariableName.class);
	}

	@Test
	public void DeserializesToMethodName() {
		assertDeserialize("\"CSharp.MethodName:[RT] [DT].M()\"", Names.newMethod("[RT] [DT].M()"), IMethodName.class);
	}

	@Test
	public void DeserializesToNamespaceName() {
		assertDeserialize("\"CSharp.NamespaceName:A.B\"", Names.newNamespace("A.B"), INamespaceName.class);
	}

	@Test
	public void DeserializesToParameterName() {
		assertDeserialize("\"CSharp.ParameterName:[VT] parameter\"", Names.newParameter("[VT] parameter"),
				IParameterName.class);
	}

	@Test
	public void DeserializesToPropertyName() {
		assertDeserialize("\"CSharp.PropertyName:[VT] [DT].Property\"", Names.newProperty("[VT] [DT].Property"),
				IPropertyName.class);
	}

	@Test
	public void DeserializesToTypeName() {
		assertDeserialize("\"CSharp.TypeName:T,A,1.2.3.4\"", Names.newType("T,A,1.2.3.4"), ITypeName.class);
		// assertDeserialize("\"CSharp.ArrayTypeName:T[],A,5.4.3.2\"",
		// CsTypeName.newTypeName("T[],A,5.4.3.2"),
		// TypeName.class);
	}

	@Test
	public void DeserializesToDelegateTypeName() {
		assertDeserialize("\"CSharp.DelegateTypeName:d:T,A,1.2.3.4\"",
				Names.newType("d:T,A,1.2.3.4").asDelegateTypeName(), IDelegateTypeName.class);
	}

	private <T extends IName> void assertDeserialize(String json, T expectedInstance, Class<T> mostSpecificInterface) {
		assertDeserialize(json, IName.class, expectedInstance);
		assertDeserialize(json, expectedInstance.getClass(), expectedInstance);
		assertDeserialize(json, mostSpecificInterface, expectedInstance);
	}

	private <T extends IName> void assertDeserialize(String json, Class<T> requestedType, IName expectedInstance) {
		IName name = JsonUtils.fromJson(json, requestedType);
		assertThat(name, sameInstance(expectedInstance));
	}
}