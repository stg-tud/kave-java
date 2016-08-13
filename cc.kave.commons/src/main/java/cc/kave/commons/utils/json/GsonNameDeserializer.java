/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.utils.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.utils.naming.NameSerialization;

public class GsonNameDeserializer implements JsonDeserializer<IName>, JsonSerializer<IName> {

	@Override
	public IName deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String str = json.getAsString();
		return NameSerialization.deserialize(str);
	}

	@Override
	public JsonElement serialize(IName src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(NameSerialization.serialize(src));
	}
}