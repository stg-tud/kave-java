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
package exec.validate_evaluation.streaks;

import java.util.List;

import cc.kave.commons.model.names.IParameterName;
import cc.recommenders.names.IMethodName;
import cc.recommenders.names.ITypeName;
import cc.recommenders.names.VmMethodName;
import cc.recommenders.names.VmTypeName;

public class NameUtils {

	public static IMethodName toCoReName(cc.kave.commons.model.names.IMethodName m) {
		ITypeName dt = toCoReName(m.getDeclaringType());
		ITypeName rt = toCoReName(m.getReturnType());
		String simpleName = m.getName();
		String parameterList = createParameterList(m.getParameters());
		return VmMethodName.get(String.format("%s.%s(%s)%s", dt, simpleName, parameterList, rt));
	}

	private static String createParameterList(List<IParameterName> parameters) {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	private static String createParameter(IParameterName parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ITypeName toCoReName(cc.kave.commons.model.names.ITypeName t) {
		return VmTypeName.get(String.format("%s.%s", t.getNamespace(), t.getName()));
	}
}