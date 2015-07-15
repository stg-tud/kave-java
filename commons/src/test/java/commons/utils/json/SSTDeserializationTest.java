package commons.utils.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.utils.json.JsonUtils;

public class SSTDeserializationTest {

	@Test
	public void deserializedToSSTWithEnclosingType() {
		ISST a = SSTTestfixture.getExample();
		ISST b = JsonUtils.parseJson(SSTTestfixture.getExampleJson_Current(), SST.class);
		assertThat(a, equalTo(b));
	}

	@Test
	public void deserializedFromJavaJson() {
		ISST a = SSTTestfixture.getExample();
		ISST b = JsonUtils.parseJson(JsonUtils.toString(a), SST.class);
		assertThat(a, equalTo(b));
	}

}
