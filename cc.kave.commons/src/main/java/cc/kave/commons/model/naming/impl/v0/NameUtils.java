/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.model.naming.impl.v0;

import static cc.kave.commons.utils.StringUtils.f;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.impl.v0.types.TypeParameterName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.utils.StringUtils;
import cc.recommenders.assertions.Asserts;

public class NameUtils {

	public static List<ITypeParameterName> ParseTypeParameterList(String id, int open, int close) {
		if (StringUtils.isNullOrEmpty(id) || open < 0 || close >= id.length() || close < open || id.charAt(open) != '['
				|| id.charAt(close) != ']') {
			Asserts.fail(f("error parsing parameters from '%s' (%d, %d)", id, open, close));
		}

		List<ITypeParameterName> parameters = Lists.newLinkedList();
		for (int cur = open; cur < close;) {
			cur++; // skip open bracket or comma

			cur = StringUtils.FindNext(id, cur, '[');
			int closeParam = StringUtils.FindCorrespondingCloseBracket(id, cur);

			cur++; // skip bracket

			String tpId = id.substring(cur, closeParam);
			parameters.add(new TypeParameterName(tpId));

			closeParam++; // skip bracket

			cur = StringUtils.FindNext(id, closeParam, ',', ']');
		}
		return parameters;
	}
}