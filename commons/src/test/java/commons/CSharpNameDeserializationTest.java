package commons;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.AliasName;
import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.Name;
import cc.kave.commons.model.names.csharp.CsAliasName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.names.csharp.CsName;
import cc.kave.commons.utils.json.GsonNameDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	private void assertDeserialize(String json, Name expectedInstance, Class<? extends Name> mostSpecificInterface) {
		assertDeserialize(json, Name.class, expectedInstance);
		assertDeserialize(json, expectedInstance.getClass(), expectedInstance);
		assertDeserialize(json, mostSpecificInterface, expectedInstance);
	}

	private void assertDeserialize(String json, Class<? extends Name> requestedType, Name expectedInstance) {
		Name name = parseJson(json, requestedType);
		assertThat(name, sameInstance(expectedInstance));
	}

	private <T> T parseJson(String json, Class<T> targetType) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(AliasName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(BundleName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(EventName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(Name.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsAliasName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsAssemblyName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsEventName.class, new GsonNameDeserializer());
		gsonBuilder.registerTypeAdapter(CsName.class, new GsonNameDeserializer());
		Gson gson = gsonBuilder.create();

		return gson.fromJson(json, targetType);
	}
}
