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
package exec.validate_evaluation.utils;

import java.util.List;

import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.recommenders.names.CoReFieldName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.names.ICoReFieldName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;

public class CoReNameUtils {

	public static ICoReMethodName toCoReName(IMethodName m) {
		ICoReTypeName dt = toCoReName(m.getDeclaringType());
		ICoReTypeName rt = toCoReName(m.getReturnType());
		String simpleName = m.getName();
		String parameterList = createParameterList(m.getParameters());
		return CoReMethodName.get(String.format("%s.%s(%s)%s;", dt, simpleName, parameterList, rt));
	}

	private static String createParameterList(List<IParameterName> parameters) {
		boolean isFirst = true;
		StringBuilder sb = new StringBuilder();
		for (IParameterName p : parameters) {
			if (!isFirst) {
				sb.append(',');
			}
			isFirst = false;
			sb.append(toCoReName(p.getValueType()));
			sb.append(';');
		}
		return sb.toString();
	}

	public static ICoReTypeName toCoReName(ITypeName t) {

		if (t.isUnknown()) {
			return CoReTypeName.get("LUnknown");
		}

		if (t.isArray()) {
			return CoReTypeName.get("[" + toCoReName(t.asArrayTypeName().getArrayBaseType()));
		}

		if (t.isNestedType()) {
			return CoReTypeName.get(toCoReName(t.getDeclaringType()) + "$" + t.getName());
		}

		String ns = t.getNamespace().toString().replace('.', '/');
		if (!ns.isEmpty()) {
			ns += '/';
		}
		return CoReTypeName.get(String.format("L%s%s", ns, t.getName()));
	}

	public static ICoReFieldName toCoReName(IFieldName f) {
		StringBuilder sb = new StringBuilder();
		sb.append(toCoReName(f.getDeclaringType()));
		sb.append('.');
		sb.append(f.getName());
		sb.append(";");
		sb.append(toCoReName(f.getValueType()));
		return CoReFieldName.get(sb.toString());
	}
}