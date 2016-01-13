package commons.utils.json;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.AliasName;
import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.LambdaName;
import cc.kave.commons.model.names.LocalVariableName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsAliasName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsLambdaName;
import cc.kave.commons.model.names.csharp.CsLocalVariableName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.utils.json.JsonUtils;

public class CSharpNameDeserializationTest {
	@Test
	public void DeserializesToCsName() {
		assertDeserialize("\"CSharp.Name:identifier\"", CsName.newName("identifier"), Name.class);
	}

	@Test
	public void DeserializesToAliasName() {
		assertDeserialize("\"CSharp.AliasName:alias\"", CsAliasName.newAliasName("alias"), AliasName.class);
	}

	@Test
	public void DeserializesToAssemblyName() {
		assertDeserialize("\"CSharp.AssemblyName:A, 1.2.3.4\"", CsAssemblyName.newAssemblyName("A, 1.2.3.4"),
				BundleName.class);
	}

	@Test
	public void DeserializesToEventName() {
		assertDeserialize("\"CSharp.EventName:[HT] [DT].Event\"", CsEventName.newEventName("[HT] [DT].Event"),
				EventName.class);
	}

	@Test
	public void DeserializesToFieldName() {
		assertDeserialize("\"CSharp.FieldName:[VT] [DT]._field\"", CsFieldName.newFieldName("[VT] [DT]._field"),
				FieldName.class);
	}

	@Test
	public void DeserializesToLambdaName() {
		assertDeserialize("\"CSharp.LambdaName:[VT] ()\"", CsLambdaName.newLambdaName("[VT] ()"), LambdaName.class);
	}

	@Test
	public void DeserializesToLocalVariableName() {
		assertDeserialize("\"CSharp.LocalVariableName:[VT] v\"", CsLocalVariableName.newLocalVariableName("[VT] v"),
				LocalVariableName.class);
	}

	@Test
	public void DeserializesToMethodName() {
		assertDeserialize("\"CSharp.MethodName:[RT] [DT].M()\"", CsMethodName.newMethodName("[RT] [DT].M()"),
				MethodName.class);
	}

	@Test
	public void DeserializesToNamespaceName() {
		assertDeserialize("\"CSharp.NamespaceName:A.B\"", CsNamespaceName.newNamespaceName("A.B"), NamespaceName.class);
	}

	@Test
	public void DeserializesToParameterName() {
		assertDeserialize("\"CSharp.ParameterName:[VT] parameter\"", CsParameterName.newParameterName("[VT] parameter"),
				ParameterName.class);
	}

	@Test
	public void DeserializesToPropertyName() {
		assertDeserialize("\"CSharp.PropertyName:[VT] [DT].Property\"",
				CsPropertyName.newPropertyName("[VT] [DT].Property"), PropertyName.class);
	}

	@Test
	public void DeserializesToTypeName() {
		assertDeserialize("\"CSharp.TypeName:T,A,1.2.3.4\"", CsTypeName.newTypeName("T,A,1.2.3.4"), TypeName.class);
		// assertDeserialize("\"CSharp.ArrayTypeName:T[],A,5.4.3.2\"",
		// CsTypeName.newTypeName("T[],A,5.4.3.2"),
		// TypeName.class);
	}

	@Test
	public void DeserializesToDelegateTypeName() {
		assertDeserialize("\"CSharp.DelegateTypeName:d:T,A,1.2.3.4\"",
				CsDelegateTypeName.newDelegateTypeName("d:T,A,1.2.3.4"), DelegateTypeName.class);
	}

	private <T extends Name> void assertDeserialize(String json, T expectedInstance, Class<T> mostSpecificInterface) {
		assertDeserialize(json, Name.class, expectedInstance);
		assertDeserialize(json, expectedInstance.getClass(), expectedInstance);
		assertDeserialize(json, mostSpecificInterface, expectedInstance);
	}

	private <T extends Name> void assertDeserialize(String json, Class<T> requestedType, Name expectedInstance) {
		Name name = JsonUtils.fromJson(json, requestedType);
		assertThat(name, sameInstance(expectedInstance));
	}
}