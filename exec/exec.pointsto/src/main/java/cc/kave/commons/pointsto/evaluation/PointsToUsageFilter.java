/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.evaluation;

import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Sets;

import cc.recommenders.names.ITypeName;
import cc.recommenders.names.Names;
import cc.recommenders.usages.Usage;

public class PointsToUsageFilter implements Predicate<Usage> {

	private static final Set<String> PRIMITIVE_TYPE_NAMES = Sets.newHashSet("System.Byte", "System.SByte",
			"System.Int32", "System.UInt32", "System.UInt64", "System.Int64", "System.Single", "System.Double",
			"System.Decimal", "System.Char", "System.Boolean");

	@Override
	public boolean test(Usage usage) {

		ITypeName type = usage.getType();

		if (type.isArrayType()) {
			return false;
		}

		if (usage.getReceiverCallsites().isEmpty()) {
			return false;
		}

		if (PRIMITIVE_TYPE_NAMES.contains(Names.vm2srcQualifiedType(type))) {
			return false;
		}

		return true;
	}

}
