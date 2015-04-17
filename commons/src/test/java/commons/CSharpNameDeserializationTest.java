package commons;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsName;
import cc.kave.commons.utils.json.GsonNameDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CSharpNameDeserializationTest {
	@Test
	public void DeserializesToCsName() {
		CsName name = parseJson("\"CSharp.Name:Identifier\"", CsName.class);

		assertThat(name, sameInstance(CsName.newName("Identifier")));
	}
	
	@Test
	public void DeserializesToName() {
		Name name = parseJson("\"CSharp.Name:Identifier\"", Name.class);
		
		assertThat(name, sameInstance(CsName.newName("Identifier")));
	}

	private <T> T parseJson(String json, Class<T> targetType) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CsName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(Name.class, new GsonNameDeserializer());
		Gson gson = gsonBuilder.create();
		
		return gson.fromJson(json, targetType);
	}
}
