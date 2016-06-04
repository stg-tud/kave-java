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

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;

public class PBNAnalysisTestFixture {
	public static ITypeName voidType = TypeName.newTypeName("System.Void, mscorlib, 4.0.0.0");
	
	public static ITypeName intType = TypeName.newTypeName("System.Int32, mscore, 4.0.0.0");
	
	public static ITypeName stringType = TypeName.newTypeName("System.String, mscore, 4.0.0.0");

	public static ITypeName booleanType = TypeName.newTypeName("System.Boolean, mscorlib, 4.0.0.0");

	public static ITypeName objectType = TypeName.newTypeName("System.Object, mscorlib, 4.0.0.0");

}
