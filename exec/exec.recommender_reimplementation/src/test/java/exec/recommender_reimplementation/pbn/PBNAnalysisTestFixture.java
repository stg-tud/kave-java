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
package exec.recommender_reimplementation.pbn;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;

public class PBNAnalysisTestFixture {
	public static ITypeName voidType = Names.newType("p:void");
	
	public static ITypeName intType = Names.newType("p:int");
	
	public static ITypeName stringType = Names.newType("p:string");

	public static ITypeName booleanType = Names.newType("p:bool");

	public static ITypeName objectType = Names.newType("p:object");

}
