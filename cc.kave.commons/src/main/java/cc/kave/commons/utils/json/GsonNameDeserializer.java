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
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.impl.v0.others.ReSharperLiveTemplateName;

public class GsonNameDeserializer implements JsonDeserializer<IName>, JsonSerializer<IName> {

	@Override
	public IName deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String[] nameInfo = json.getAsString().split(":", 2);
		String discriminator = nameInfo[0];
		final String identifier = nameInfo[1];
		switch (discriminator) {
		case "CSharp.AliasName":
			return Names.newAlias(identifier);
		case "CSharp.AssemblyName":
			return Names.newAssembly(identifier);
		case "CSharp.EventName":
			return Names.newEvent(identifier);
		case "CSharp.FieldName":
			return Names.newField(identifier);
		case "CSharp.LambdaName":
			return Names.newLambda(identifier);
		case "CSharp.LocalVariableName":
			return Names.newLocalVariable(identifier);
		case "CSharp.MethodName":
			return Names.newMethod(identifier);
		case "CSharp.Name":
			return Names.newGeneral(identifier);
		case "CSharp.NamespaceName":
			return Names.newNamespace(identifier);
		case "CSharp.ParameterName":
			return Names.newParameter(identifier);
		case "CSharp.PropertyName":
			return Names.newProperty(identifier);
		case "CSharp.TypeName":
			return Names.newType(identifier);
		case "ReSharper.LiveTemplateName":
			return Names.newLiveTemplateName(identifier);
		default:
			if ((discriminator.startsWith("CSharp.") && discriminator.endsWith("TypeName"))
					|| discriminator.equals("CSharp.TypeParameterName")) {
				return Names.newType(identifier);
			}
			throw new JsonParseException("Not a valid serialized name: '" + json + "'");
		}
	}

	@Override
	public JsonElement serialize(IName src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive((src.getClass() != ReSharperLiveTemplateName.class ? "CSharp." : "ReSharper.")
				+ src.getClass().getSimpleName().replaceFirst("Cs", "") + ":" + src.getIdentifier());
	}

}
