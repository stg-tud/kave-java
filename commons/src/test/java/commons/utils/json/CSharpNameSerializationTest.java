package commons.utils.json;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.Name;
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

public class CSharpNameSerializationTest {
	@Test
	public void serializesCsName() {
		assertSerialize(CsName.newName("identifier"), "\"CSharp.Name:identifier\"");
	}

	@Test
	public void serializesAliasName() {
		assertSerialize(CsAliasName.newAliasName("alias"), "\"CSharp.AliasName:alias\"");
	}

	@Test
	public void serializesAssemblyName() {
		assertSerialize(CsAssemblyName.newAssemblyName("A, 1.2.3.4"), "\"CSharp.AssemblyName:A, 1.2.3.4\"");
	}

	@Test
	public void serializesEventName() {
		assertSerialize(CsEventName.newEventName("[HT] [DT].Event"), "\"CSharp.EventName:[HT] [DT].Event\"");
	}

	@Test
	public void serializesFieldName() {
		assertSerialize(CsFieldName.newFieldName("[VT] [DT]._field"), "\"CSharp.FieldName:[VT] [DT]._field\"");
	}

	@Test
	public void serializesLambdaName() {
		assertSerialize(CsLambdaName.newLambdaName("[VT] ()"), "\"CSharp.LambdaName:[VT] ()\"");
	}

	@Test
	public void serializesLocalVariableName() {
		assertSerialize(CsLocalVariableName.newLocalVariableName("[VT] v"), "\"CSharp.LocalVariableName:[VT] v\"");
	}

	@Test
	public void serializesMethodName() {
		assertSerialize(CsMethodName.newMethodName("[RT] [DT].M()"), "\"CSharp.MethodName:[RT] [DT].M()\"");
	}

	@Test
	public void serializesNamespaceName() {
		assertSerialize(CsNamespaceName.newNamespaceName("A.B"), "\"CSharp.NamespaceName:A.B\"");
	}

	@Test
	public void serializesParameterName() {
		assertSerialize(CsParameterName.newParameterName("[VT] parameter"), "\"CSharp.ParameterName:[VT] parameter\"");
	}

	@Test
	public void serializesPropertyName() {
		assertSerialize(CsPropertyName.newPropertyName("[VT] [DT].Property"),
				"\"CSharp.PropertyName:[VT] [DT].Property\"");
	}

	/**
	 * We currently don't have specialized subtypes for type names,
	 * serialization might change if we introduce them. However, deserialization
	 * already works for both cases, hence, we are rebust against this
	 * extension.
	 * 
	 * @see CSharpNameDeserializationTest#DeserializesTypeName()
	 */
	@Test
	public void serializesTypeName() {
		assertSerialize(CsTypeName.newTypeName("T,A,1.2.3.4"), "\"CSharp.TypeName:T,A,1.2.3.4\"");
	}

	@Test
	public void serializesDelegateTypeName() {
		assertSerialize(CsDelegateTypeName.newDelegateTypeName("d:T,A,1.2.3.4"),
				"\"CSharp.DelegateTypeName:d:T,A,1.2.3.4\"");
	}

	private <T extends Name> void assertSerialize(T originalInstance, String expected) {
		String json = JsonUtils.toString(originalInstance);
		assertThat(json, is(expected));
	}
}
