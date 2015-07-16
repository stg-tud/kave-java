package commons.utils.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.utils.json.JsonUtils;

public class SSTSerializationTest {

	@Test
	public void serializedEmptySST() {
		ISST sst = new SST();
		String json = JsonUtils.toString(sst);
		assertEquals(
				"{\"$type\":\"[SST:SST]\",\"EnclosingType\":\"CSharp.UnknownTypeName:?\",\"Fields\":[],\"Properties\":[],\"Methods\":[],\"Events\":[],\"Delegates\":[]}",
				json);
	}

	@Test
	public void serializedSimpleSST() {
		ISST sst = new SST();
		DelegateDeclaration delegate = new DelegateDeclaration();
		// delegate.setName(CsDelegateTypeName.newDelegateTypeName("CSharp.DelegateTypeName:d:[R,P] [T2,P].()"));
		delegate.setName(CsDelegateTypeName.newDelegateTypeName("d:[R,P] [T2,P].()"));
		sst.getDelegates().add(delegate);
		String json = JsonUtils.toString(sst);
		assertEquals(
				"{\"$type\":\"[SST:SST]\",\"EnclosingType\":\"CSharp.UnknownTypeName:?\",\"Fields\":[],\"Properties\":[],\"Methods\":[],\"Events\":[],\"Delegates\":[{\"$type\":\"[SST:Declarations.DelegateDeclaration]\",\"Name\":\"CSharp.DelegateTypeName:d:[R,P] [T2,P].()\"}]}",
				json);
	}

	@Test
	public void serializedExampleSST() {
		String json = JsonUtils.toString(SSTTestfixture.getExample());
		assertEquals(SSTTestfixture.getExampleJson_Current(), json);
	}

	@Test
	public void serialzesCompletionExpression() {
		CompletionExpression ce = new CompletionExpression();
		JsonUtils.toString(ce);
	}

	// @Test
	public void serializesSST() {
		ISST sst = SSTTestfixture.getExample();
		String json = JsonUtils.toString(sst);
		assertEquals(SSTTestfixture.getExampleJson_Current(), json);
	}

	// @Test
	public void deserializesFromSerializedSST() {
		ISST example = SSTTestfixture.getExample();
		String testable = JsonUtils.toString(example);
		ISST testableSST = JsonUtils.fromString(testable, ISST.class);
		assertThat(example, equalTo(testableSST));
	}

	// @Test
	public void deserialzesWithStockMethod() {
		ISST example = SSTTestfixture.getExample();
		String testable = JsonUtils.toString(example);
		ISST testableSST = JsonUtils.parseJson(testable, SST.class);
		assertThat(example, equalTo(testableSST));
	}

	// @Test
	public void serializesToFile() {
		ISST sst = SSTTestfixture.getExample();
		try {
			JsonUtils.toFile(new File("C:/sst.gson"), sst);
			assertTrue(new File("C:/sst.gson").exists());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
