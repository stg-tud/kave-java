/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.java_printer;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;

public class PhantomClassGenerator {

	public Set<ISST> convert(Set<ISST> ssts) {
		Map<ITypeName, SST> phantomClasses = Maps.newHashMap();
		
		for (ISST sst : ssts) {
			sst.accept(new PhantomClassVisitor(), phantomClasses);
		}

		return Sets.newHashSet(phantomClasses.values());
	}

}
