package cc.kave.commons.utils.json;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.IAliasName;
import cc.kave.commons.model.names.IBundleName;
import cc.kave.commons.model.names.IDelegateTypeName;
import cc.kave.commons.model.names.IEventName;
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.ILambdaName;
import cc.kave.commons.model.names.ILocalVariableName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IName;
import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.AliasName;
import cc.kave.commons.model.names.csharp.AssemblyName;
import cc.kave.commons.model.names.csharp.DelegateTypeName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.LambdaName;
import cc.kave.commons.model.names.csharp.LocalVariableName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.Name;
import cc.kave.commons.model.names.csharp.NamespaceName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.utils.json.JsonUtils;

public class CSharpNameDeserializationTest {
	@Test
	public void DeserializesToCsName() {
		assertDeserialize("\"CSharp.Name:identifier\"", Name.newName("identifier"), IName.class);
	}

	@Test
	public void DeserializesToAliasName() {
		assertDeserialize("\"CSharp.AliasName:alias\"", AliasName.newAliasName("alias"), IAliasName.class);
	}

	@Test
	public void DeserializesToAssemblyName() {
		assertDeserialize("\"CSharp.AssemblyName:A, 1.2.3.4\"", AssemblyName.newAssemblyName("A, 1.2.3.4"),
				IBundleName.class);
	}

	@Test
	public void DeserializesToEventName() {
		assertDeserialize("\"CSharp.EventName:[HT] [DT].Event\"", EventName.newEventName("[HT] [DT].Event"),
				IEventName.class);
	}

	@Test
	public void DeserializesToFieldName() {
		assertDeserialize("\"CSharp.FieldName:[VT] [DT]._field\"", FieldName.newFieldName("[VT] [DT]._field"),
				IFieldName.class);
	}

	@Test
	public void DeserializesToLambdaName() {
		assertDeserialize("\"CSharp.LambdaName:[VT] ()\"", LambdaName.newLambdaName("[VT] ()"), ILambdaName.class);
	}

	@Test
	public void DeserializesToLocalVariableName() {
		assertDeserialize("\"CSharp.LocalVariableName:[VT] v\"", LocalVariableName.newLocalVariableName("[VT] v"),
				ILocalVariableName.class);
	}

	@Test
	public void DeserializesToMethodName() {
		assertDeserialize("\"CSharp.MethodName:[RT] [DT].M()\"", MethodName.newMethodName("[RT] [DT].M()"),
				IMethodName.class);
	}

	@Test
	public void DeserializesToNamespaceName() {
		assertDeserialize("\"CSharp.NamespaceName:A.B\"", NamespaceName.newNamespaceName("A.B"), INamespaceName.class);
	}

	@Test
	public void DeserializesToParameterName() {
		assertDeserialize("\"CSharp.ParameterName:[VT] parameter\"", ParameterName.newParameterName("[VT] parameter"),
				IParameterName.class);
	}

	@Test
	public void DeserializesToPropertyName() {
		assertDeserialize("\"CSharp.PropertyName:[VT] [DT].Property\"",
				PropertyName.newPropertyName("[VT] [DT].Property"), IPropertyName.class);
	}

	@Test
	public void DeserializesToTypeName() {
		assertDeserialize("\"CSharp.TypeName:T,A,1.2.3.4\"", TypeName.newTypeName("T,A,1.2.3.4"), ITypeName.class);
		// assertDeserialize("\"CSharp.ArrayTypeName:T[],A,5.4.3.2\"",
		// CsTypeName.newTypeName("T[],A,5.4.3.2"),
		// TypeName.class);
	}

	@Test
	public void DeserializesToDelegateTypeName() {
		assertDeserialize("\"CSharp.DelegateTypeName:d:T,A,1.2.3.4\"",
				DelegateTypeName.newDelegateTypeName("d:T,A,1.2.3.4"), IDelegateTypeName.class);
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