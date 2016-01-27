/**
 * Copyright 2015 Waldemar Graf
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

package eclipse.commons.analysis.sstanalysistestsuite;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class SSTAnalysisFixture extends BaseSSTAnalysisTest{

	public static final TypeName INT = CsTypeName.newTypeName("%int, rt.jar, 1.8");

	public static final TypeName FLOAT = CsTypeName.newTypeName("%float, rt.jar, 1.8");

	public static final TypeName DOUBLE = CsTypeName.newTypeName("%double, rt.jar, 1.8");

	public static final TypeName LONG = CsTypeName.newTypeName("%long, rt.jar, 1.8");

	public static final TypeName BOOLEAN = CsTypeName.newTypeName("%boolean, rt.jar, 1.8");

	public static final TypeName SHORT = CsTypeName.newTypeName("%short, rt.jar, 1.8");

	public static final TypeName VOID = CsTypeName.newTypeName("%void, rt.jar, 1.8");

	public static final TypeName STRING = CsTypeName.newTypeName("java.lang.String, rt.jar, 1.8");

	public static final TypeName OBJECT = CsTypeName.newTypeName("java.lang.Object, rt.jar, 1.8");

	public static final MethodName OBJECT_CTOR = CsMethodName
			.newMethodName("[%void, rt.jar, 1.8] [java.lang.Object, rt.jar, 1.8]..ctor()");

	public static MethodName getHashCode(TypeName type) {
		return CsMethodName.newMethodName("[%int, rt.jar, 1.8] [java.lang.Object, rt.jar, 1.8].hashCode()");
	}

	public static final MethodName HASHCODE = CsMethodName.newMethodName("[%int, rt.jar, 1.8] [].hashCode()");

	// TODO: get name
	public static final MethodName LIST_OF_INT_INIT = CsMethodName.newMethodName("[java.util. ][java.util. ]..ctor()");
}
