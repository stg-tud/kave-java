package commons.utils.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import flexjsonexample.FlexjsonUtil;
import gsonexample.GsonUtil;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.utils.json.JsonUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SSTSerializationTest {

	@Test
	public void deserializesFromSerializedSST() {
		ISST example = SSTTestfixture.getExample();
		String testable = toGsonString(example);
		ISST testableSST = fromGsonString(testable);
		assertThat(example, equalTo(testableSST));
	}

	@Test
	public void deserialzesWithStockMethod() {
		ISST example = SSTTestfixture.getExample();
		String testable = toGsonString(example);
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

	static public String toGsonString(ISST sst) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(sst);
	}

	static public ISST fromGsonString(String json) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.fromJson(json, SST.class);
	}

}
