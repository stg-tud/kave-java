package commons.utils.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.utils.json.JsonUtils;

public class ContextSerializationTest {

	@Test
	public void testSerializeToJson() {
		String sut = JsonUtils.parseObject(getExampleContext(), Context.class);
		assertThat(sut, equalTo(getExampleJson_Current()));
	}

	@Test
	public void testDeserializeToContext() {
		Context sut = JsonUtils.parseJson(getExampleJson_Current(), Context.class);
		assertThat(sut, equalTo(getExampleContext()));
	}

	private static String getExampleJson_Current() {
		return "{\"$type\":\"KaVE.Commons.Model.Events.CompletionEvents.Context, KaVE.Commons\",\"TypeShape\":{\"$type\":\"KaVE.Commons.Model.TypeShapes.TypeShape, KaVE.Commons\",\"TypeHierarchy\":{\"$type\":\"KaVE.Commons.Model.TypeShapes.TypeHierarchy, KaVE.Commons\",\"Element\":\"CSharp.UnknownTypeName:?\",\"Implements\":[]},\"MethodHierarchies\":[]},\"SST\":{\"$type\":\"[SST:SST]\",\"EnclosingType\":\"CSharp.TypeName:T,P\",\"Fields\":[],\"Properties\":[],\"Methods\":[],\"Events\":[],\"Delegates\":[]}}";
	}

	private static Context getExampleContext() {
		Context context = new Context();
		SST sst = new SST();
		sst.setEnclosingType(CsTypeName.newTypeName("T,P"));
		context.setSST(sst);
		return context;
	}

}
