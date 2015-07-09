package commons.utils.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import flexjsonexample.FlexjsonUtil;
import gsonexample.GsonUtil;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.utils.json.JsonUtils;

public class SSTSerializationTest {

	@Test
	public void serializedEmptySST() {
		ISST sst = new SST();
		String json = GsonUtil.toString(sst);
		assertEquals(
				"{\"$type\":\"[SST:SST]\",\"enclosingType\":{\"identifier\":\"?\"},\"fields\":[],\"properties\":[],\"methods\":[],\"events\":[],\"delegates\":[]}",
				json);
	}

	@Test
	public void serializedSimpleSST() {
		ISST sst = new SST();
		DelegateDeclaration delegate = new DelegateDeclaration();
		delegate.setName(CsDelegateTypeName.newDelegateTypeName("CSharp.DelegateTypeName:d:[R,P] [T2,P].()"));
		sst.getDelegates().add(delegate);
		String json = GsonUtil.toString(sst);
		assertEquals(
				"{\"$type\":\"[SST:SST]\",\"enclosingType\":{\"identifier\":\"?\"},\"fields\":[],\"properties\":[],\"methods\":[],\"events\":[],\"delegates\":[]}",
				json);
	}

	@Test
	public void serializesSST() {
		ISST sst = SSTTestfixture.getExample();
		String json = GsonUtil.toString(sst);
		assertEquals(SSTTestfixture.getExampleJson_Current(), json);
	}

	@Test
	public void deserializesFromSerializedSST() {
		ISST example = SSTTestfixture.getExample();
		String testable = GsonUtil.toString(example);
		ISST testableSST = GsonUtil.fromString(testable, ISST.class);
		assertThat(example, equalTo(testableSST));
	}

	@Test
	public void deserialzesWithStockMethod() {
		ISST example = SSTTestfixture.getExample();
		String testable = GsonUtil.toString(example);
		ISST testableSST = JsonUtils.parseJson(testable, SST.class);
		assertThat(example, equalTo(testableSST));
	}

	@Test
	public void serializesToFile() {
		ISST sst = SSTTestfixture.getExample();
		try {
			GsonUtil.toFile(new File("C:/sst.gson"), sst);
			assertTrue(new File("C:/sst.gson").exists());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void serializesToFileFlexjson() {
		ISST sst = SSTTestfixture.getExample();
		try {
			FlexjsonUtil.toFile(new File("C:/sst.flexjson"), sst);
			assertTrue(new File("C:/sst.flexjson").exists());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
