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

import org.eclipse.jdt.core.dom.ThisExpression;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class SSTAnalysisFixture extends BaseSSTAnalysisTest {

	public static final TypeName INT = CsTypeName.newTypeName("%int, rt.jar, 1.8");

	public static final TypeName INT_OBJECT = CsTypeName.newTypeName("java.lang.Integer, rt.jar, 1.8");

	public static final TypeName FLOAT = CsTypeName.newTypeName("%float, rt.jar, 1.8");

	public static final TypeName DOUBLE = CsTypeName.newTypeName("%double, rt.jar, 1.8");

	public static final TypeName LONG = CsTypeName.newTypeName("%long, rt.jar, 1.8");

	public static final TypeName BOOLEAN = CsTypeName.newTypeName("%boolean, rt.jar, 1.8");

	public static final TypeName SHORT = CsTypeName.newTypeName("%short, rt.jar, 1.8");

	public static final TypeName VOID = CsTypeName.newTypeName("%void, rt.jar, 1.8");

	public static final TypeName STRING = CsTypeName.newTypeName("java.lang.String, rt.jar, 1.8");

	public static final TypeName OBJECT = CsTypeName.newTypeName("java.lang.Object, rt.jar, 1.8");

	public static final TypeName LIST = CsTypeName.newTypeName("i: java.util.List`1[[T -> T]], ?");

	public static final TypeName ARRAY_LIST = CsTypeName.newTypeName("java.util.ArrayList`1[[T -> T]], ?");

	public static final TypeName EXCEPTION = CsTypeName.newTypeName("java.lang.Exception, rt.jar, 1.8");

	public static final TypeName FILE_INPUT_STREAM = CsTypeName.newTypeName("java.io.FileInputStream, rt.jar, 1.8");

	public static final TypeName RUNNABLE = CsTypeName.newTypeName("i: java.lang.Runnable, rt.jar, 1.8");

	public static final TypeName INT_ARRAY = CsTypeName.newTypeName("%int[], rt.jar, 1.8");

	public static final TypeName FUNCTION = CsTypeName
			.newTypeName("i: java.util.function.Function`2[[T -> T],[T -> T]], ?");
	
			public static final TypeName BIFUNCTION = CsTypeName
			.newTypeName("i: java.util.function.BiFunction`3[[T -> T],[T -> T],[T -> T]], ?");

	public static final MethodName OBJECT_CTOR = CsMethodName
			.newMethodName("[java.lang.Object, rt.jar, 1.8] [java.lang.Object, rt.jar, 1.8]..ctor()");

	public static final MethodName ARRAYLIST_CTOR = CsMethodName
			.newMethodName("[java.util.ArrayList`1[[T -> T]], ?] [java.util.ArrayList`1[[T -> T]], ?]..ctor()");

	public static final MethodName STRING_CTOR = CsMethodName
			.newMethodName("[java.lang.String, rt.jar, 1.8] [java.lang.String, rt.jar, 1.8]..ctor()");

	public static final MethodName EXCEPTION_CTOR = CsMethodName
			.newMethodName("[java.lang.Exception, rt.jar, 1.8] [java.lang.Exception, rt.jar, 1.8]..ctor()");

	public static final MethodName FILE_INPUT_STREAM_CTOR = CsMethodName.newMethodName(
			"[java.io.FileInputStream, rt.jar, 1.8] [java.io.FileInputStream, rt.jar, 1.8]..ctor([java.lang.String, rt.jar, 1.8] ?)");

	public static MethodName getHashCode(TypeName type) {
		return CsMethodName.newMethodName("[%int, rt.jar, 1.8] [" + type.getIdentifier() + "].hashCode()");
	}

	public static final MethodName HASHCODE = CsMethodName
			.newMethodName("[%int, rt.jar, 1.8] [java.lang.Object, rt.jar, 1.8].hashCode()");

	public static final MethodName LENGTH = CsMethodName
			.newMethodName("[%int, rt.jar, 1.8] [java.lang.String, rt.jar, 1.8].length()");

	public static final MethodName TO_STRING = CsMethodName
			.newMethodName("[java.lang.String, rt.jar, 1.8] [java.lang.Object, rt.jar, 1.8].toString()");

	public static final MethodName OBJECT_EQUALS = CsMethodName.newMethodName(
			"[%boolean, rt.jar, 1.8] [java.lang.Object, rt.jar, 1.8].equals([java.lang.Object, rt.jar, 1.8] ?)");

	public static MethodName constructCtor(TypeName name, TypeName... args) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(name.getIdentifier()).append("] [").append(name.getIdentifier()).append("]..ctor(");

		for (int i = 0; i < args.length; i++) {
			sb.append("[").append(args[i].getIdentifier()).append("] ?");

			if (i < args.length - 1) {
				sb.append(",");
			}
		}
		sb.append(")");

		return CsMethodName.newMethodName(sb.toString());
	}

}
