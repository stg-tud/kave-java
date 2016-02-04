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
package cc.kave.commons.model.names.csharp;

import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.ILambdaName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;

public class LambdaName extends Name implements ILambdaName {
	private static final Map<String, LambdaName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final LambdaName UNKNOWN_NAME = newLambdaName(UNKNOWN_NAME_IDENTIFIER);

	public static LambdaName newLambdaName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new LambdaName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private LambdaName(String identifier) {
		super(identifier);
	}

	@Override
	public String getSignature() {
		int startOfSignature = identifier.indexOf('(');
		return identifier.substring(startOfSignature);
	}

	@Override
	public List<IParameterName> getParameters() {
		return CsNameUtils.getParameterNames(identifier);
	}

	@Override
	public boolean hasParameters() {
		return CsNameUtils.hasParameters(identifier);
	}

	@Override
	public ITypeName getReturnType() {
		int startIndexOfValueTypeIdentifier = identifier.indexOf('[') + 1;
		int lastIndexOfValueTypeIdentifer = identifier.indexOf("]") + 1;
		int lengthOfValueTypeIdentifier = lastIndexOfValueTypeIdentifer - startIndexOfValueTypeIdentifier;
		return TypeName
				.newTypeName(identifier.substring(startIndexOfValueTypeIdentifier, lengthOfValueTypeIdentifier));
	}

}
