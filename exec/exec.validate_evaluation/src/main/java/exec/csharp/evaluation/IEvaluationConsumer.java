/*
 * Copyright 2014 Technische Universität Darmstadt
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
package exec.csharp.evaluation;

import java.util.List;

import cc.recommenders.names.ITypeName;
import cc.recommenders.usages.DefinitionSiteKind;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.csharp.queries.QueryMode;
import exec.csharp.utils.MicroCommit;

public interface IEvaluationConsumer {

	void run();

	void startingType(ITypeName type, List<Usage> usages, List<MicroCommit> histories);

	void skippingType(ITypeName type, List<Usage> us, List<MicroCommit> histories);

	void startingQueryMode(QueryMode mode);

	void skipCommit_NoChange(QueryMode mode);

	void skipCommit_NoAddition(QueryMode mode);

	void registerQuery(DefinitionSiteKind def, int before, int add, int after);

	void addResult(Query start, Query end, QueryMode queryMode, double f1);

	void finish();

}