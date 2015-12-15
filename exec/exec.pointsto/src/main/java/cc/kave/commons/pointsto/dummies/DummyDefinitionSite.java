/**
 * Copyright 2015 Simon Reuß
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
package cc.kave.commons.pointsto.dummies;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MethodName;
import cc.recommenders.usages.DefinitionSiteKind;

public class DummyDefinitionSite {

	private DefinitionSiteKind kind;
	private MethodName method;
	private FieldName field;
	private int argIndex = -1;

	private DummyDefinitionSite() {
	}

	public static DummyDefinitionSite byConstructor(MethodName method) {
		DummyDefinitionSite defSite = new DummyDefinitionSite();
		defSite.setKind(DefinitionSiteKind.NEW);
		defSite.setMethod(method);
		return defSite;
	}

	public static DummyDefinitionSite byReturn(MethodName method) {
		DummyDefinitionSite defSite = new DummyDefinitionSite();
		defSite.setKind(DefinitionSiteKind.RETURN);
		defSite.setMethod(method);
		return defSite;
	}

	public static DummyDefinitionSite byField(FieldName field) {
		DummyDefinitionSite defSite = new DummyDefinitionSite();
		defSite.setKind(DefinitionSiteKind.FIELD);
		defSite.setField(field);
		return defSite;
	}

	public static DummyDefinitionSite byParam(MethodName method, int argIndex) {
		DummyDefinitionSite defSite = new DummyDefinitionSite();
		defSite.setKind(DefinitionSiteKind.PARAM);
		defSite.setMethod(method);
		defSite.setArgIndex(argIndex);
		return defSite;
	}

	public static DummyDefinitionSite byThis() {
		DummyDefinitionSite defSite = new DummyDefinitionSite();
		defSite.setKind(DefinitionSiteKind.THIS);
		return defSite;
	}

	public static DummyDefinitionSite byConstant() {
		DummyDefinitionSite defSite = new DummyDefinitionSite();
		defSite.setKind(DefinitionSiteKind.CONSTANT);
		return defSite;
	}

	public static DummyDefinitionSite unknown() {
		DummyDefinitionSite defSite = new DummyDefinitionSite();
		defSite.setKind(DefinitionSiteKind.UNKNOWN);
		return defSite;
	}

	public DefinitionSiteKind getKind() {
		return kind;
	}

	public void setKind(DefinitionSiteKind kind) {
		this.kind = kind;
	}

	public MethodName getMethod() {
		return method;
	}

	public void setMethod(MethodName method) {
		this.method = method;
	}

	public FieldName getField() {
		return field;
	}

	public void setField(FieldName field) {
		this.field = field;
	}

	public int getArgIndex() {
		return argIndex;
	}

	public void setArgIndex(int argIndex) {
		this.argIndex = argIndex;
	}

}
