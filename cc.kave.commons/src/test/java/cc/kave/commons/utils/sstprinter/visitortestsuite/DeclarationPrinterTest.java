/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.utils.sstprinter.visitortestsuite;

import org.junit.Test;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.DelegateTypeName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.commons.utils.sstprinter.SSTPrintingContext;

public class DeclarationPrinterTest extends SSTPrintingVisitorBaseTest {
	@Test
	public void SSTDeclaration_EmptyClass() {
		SST sst = new SST();
		sst.setEnclosingType(TypeName.newTypeName("TestClass,TestProject"));

		AssertPrint(sst, "class TestClass", "{", "}");
	}

	@Test
	public void SSTDeclaration_WithSupertypes() {
		ITypeName thisType = TypeName.newTypeName("TestClass,P");
		ITypeName superType = TypeName.newTypeName("SuperClass,P");
		ITypeName interface1 = TypeName.newTypeName("i:IDoesSomething,P");
		ITypeName interface2 = TypeName.newTypeName("i:IDoesSomethingElse,P");

		SST sst = new SST();
		sst.setEnclosingType(thisType);
		TypeShape typeShape = new TypeShape();
		TypeHierarchy type = new TypeHierarchy();
		TypeHierarchy type2 = new TypeHierarchy();
		TypeHierarchy interface1Hierarchy = new TypeHierarchy();
		TypeHierarchy interface2Hierarchy = new TypeHierarchy();
		interface1Hierarchy.setElement(interface1);
		interface2Hierarchy.setElement(interface2);
		type2.setElement(superType);
		type.setElement(thisType);
		type.setExtends(type2);
		type.getImplements().add(interface1Hierarchy);
		type.getImplements().add(interface2Hierarchy);
		typeShape.setTypeHierarchy(type);

		SSTPrintingContext context = new SSTPrintingContext();
		context.setTypeShape(typeShape);
		AssertPrintWithCustomContext(sst, context, "class TestClass : SuperClass, IDoesSomething, IDoesSomethingElse",
				"{", "}");
	}

	@Test
	public void SSTDeclaration_WithSupertypes_OnlyInterface() {
		ITypeName thisType = TypeName.newTypeName("TestClass,P");
		ITypeName interface1 = TypeName.newTypeName("i:IDoesSomething,P");

		SST sst = new SST();
		sst.setEnclosingType(thisType);
		TypeShape typeShape = new TypeShape();
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		TypeHierarchy type2 = new TypeHierarchy();
		type2.setElement(interface1);
		typeHierarchy.setElement(thisType);
		typeHierarchy.getImplements().add(type2);
		typeShape.setTypeHierarchy(typeHierarchy);

		SSTPrintingContext context = new SSTPrintingContext();
		context.setTypeShape(typeShape);
		AssertPrintWithCustomContext(sst, context, "class TestClass : IDoesSomething", "{", "}");
	}

	@Test
	public void SSTDeclaration_WithSupertypes_OnlySuperclass() {
		ITypeName thisType = TypeName.newTypeName("TestClass,P");
		ITypeName superType = TypeName.newTypeName("SuperClass,P");

		SST sst = new SST();
		sst.setEnclosingType(thisType);
		TypeShape typeShape = new TypeShape();
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(thisType);

		TypeHierarchy type2 = new TypeHierarchy();
		type2.setElement(superType);
		typeHierarchy.setExtends(type2);
		typeShape.setTypeHierarchy(typeHierarchy);
		SSTPrintingContext context = new SSTPrintingContext();
		context.setTypeShape(typeShape);
		AssertPrintWithCustomContext(sst, context, "class TestClass : SuperClass", "{", "}");
	}

	@Test
	public void SSTDeclaration_FullClass() {
		SST sst = new SST();
		sst.setEnclosingType(TypeName.newTypeName("TestClass,P"));
		DelegateDeclaration delegate = new DelegateDeclaration();
		delegate.setName(DelegateTypeName.newDelegateTypeName("d:[T,P][TestDelegate,P].()"));
		sst.getDelegates().add(delegate);
		EventDeclaration event = new EventDeclaration();
		event.setName(EventName.newEventName("[EventType,P] [TestClass,P].SomethingHappened"));
		sst.getEvents().add(event);
		FieldDeclaration field1 = new FieldDeclaration();
		FieldDeclaration field2 = new FieldDeclaration();
		field1.setName(FieldName.newFieldName("[FieldType,P] [TestClass,P].SomeField"));
		field2.setName(FieldName.newFieldName("[FieldType,P] [TestClass,P].AnotherField"));
		sst.getFields().add(field1);
		sst.getFields().add(field2);
		PropertyDeclaration property = new PropertyDeclaration();
		property.setName(PropertyName.newPropertyName("get set [PropertyType,P] [TestClass,P].SomeProperty"));
		sst.getProperties().add(property);
		MethodDeclaration method1 = new MethodDeclaration();
		MethodDeclaration method2 = new MethodDeclaration();
		method1.setName(MethodName.newMethodName("[ReturnType,P] [TestClass,P].M([ParameterType,P] p)"));
		method2.setName(MethodName.newMethodName("[ReturnType,P] [TestClass,P].M2()"));
		method2.getBody().add(new BreakStatement());
		sst.getMethods().add(method1);
		sst.getMethods().add(method2);

		AssertPrint(sst, "class TestClass", "{", "    delegate TestDelegate();", "",
				"    event EventType SomethingHappened;", "", "    FieldType SomeField;", "    FieldType AnotherField;",
				"", "    PropertyType SomeProperty { get; set; }", "", "    ReturnType M(ParameterType p) { }", "",
				"    ReturnType M2()", "    {", "        break;", "    }", "}");
	}

	@Test
	public void SSTDeclaration_Interface() {
		SST sst = new SST();
		sst.setEnclosingType(TypeName.newTypeName("i:SomeInterface,P"));

		AssertPrint(sst, "interface SomeInterface", "{", "}");
	}

	@Test
	public void SSTDeclaration_Struct() {
		SST sst = new SST();
		sst.setEnclosingType(TypeName.newTypeName("s:SomeStruct,P"));

		AssertPrint(sst, "struct SomeStruct", "{", "}");
	}

	@Test
	public void SSTDeclaration_Enum() {
		SST sst = new SST();
		sst.setEnclosingType(TypeName.newTypeName("e:SomeEnum,P"));

		AssertPrint(sst, "enum SomeEnum", "{", "}");
	}

	@Test
	public void DelegateDeclaration_Parameterless() {
		DelegateDeclaration sst = new DelegateDeclaration();
		sst.setName(DelegateTypeName.newDelegateTypeName("d:[R, P] [Some.DelegateType, P].()"));

		AssertPrint(sst, "delegate DelegateType();");
	}

	@Test
	public void DelegateDeclaration_WithParameters() {
		DelegateDeclaration sst = new DelegateDeclaration();
		sst.setName(DelegateTypeName.newDelegateTypeName("d:[R, P] [Some.DelegateType, P].([C, P] p1, [D, P] p2)"));

		AssertPrint(sst, "delegate DelegateType(C p1, D p2);");
	}

	@Test
	public void DelegateDeclaration_Generic() {
		DelegateDeclaration sst = new DelegateDeclaration();
		sst.setName(DelegateTypeName.newDelegateTypeName("d:[R, P] [Some.DelegateType`1[[T -> T]], P].([T] p1)"));

		AssertPrint(sst, "delegate DelegateType<?>(? p1);");
	}

	@Test
	public void EventDeclaration() {
		EventDeclaration sst = new EventDeclaration();
		sst.setName(EventName.newEventName("[EventType,P] [DeclaringType,P].E"));

		AssertPrint(sst, "event EventType E;");
	}

	@Test
	public void EventDeclaration_GenericEventArgsType() {
		EventDeclaration sst = new EventDeclaration();
		sst.setName(EventName.newEventName("[EventType`1[[T -> EventArgsType,P]],P] [DeclaringType,P].E"));

		AssertPrint(sst, "event EventType<EventArgsType> E;");
	}

	@Test
	public void FieldDeclaration() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(FieldName.newFieldName("[FieldType,P] [DeclaringType,P].F"));

		AssertPrint(sst, "FieldType F;");
	}

	@Test
	public void FieldDeclaration_Static() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(FieldName.newFieldName("static [FieldType,P] [DeclaringType,P].F"));

		AssertPrint(sst, "static FieldType F;");
	}

	@Test
	public void FieldDeclaration_Array() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(FieldName.newFieldName("[d:[V, A] [N.TD, A].()[]] [DT, A]._delegatesField"));

		AssertPrint(sst, "TD[] _delegatesField;");
	}

	@Test
	public void PropertyDeclaration_GetterOnly() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get [PropertyType,P] [DeclaringType,P].P"));

		AssertPrint(sst, "PropertyType P { get; }");
	}

	@Test
	public void PropertyDeclaration_SetterOnly() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("set [PropertyType,P] [DeclaringType,P].P"));

		AssertPrint(sst, "PropertyType P { set; }");
	}

	@Test
	public void PropertyDeclaration() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));

		AssertPrint(sst, "PropertyType P { get; set; }");
	}

	@Test
	public void PropertyDeclaration_WithBodies() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));
		sst.getGet().add(new ContinueStatement());
		sst.getGet().add(new BreakStatement());
		sst.getSet().add(new BreakStatement());
		sst.getSet().add(new ContinueStatement());

		AssertPrint(sst, "PropertyType P", "{", "    get", "    {", "        continue;", "        break;", "    }",
				"    set", "    {", "        break;", "        continue;", "    }", "}");
	}

	@Test
	public void PropertyDeclaration_WithOnlyGetterBody() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));
		sst.getGet().add(new BreakStatement());

		AssertPrint(sst, "PropertyType P", "{", "    get", "    {", "        break;", "    }", "    set;", "}");
	}

	@Test
	public void PropertyDeclaration_WithOnlySetterBody() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));
		sst.getSet().add(new BreakStatement());

		AssertPrint(sst, "PropertyType P", "{", "    get;", "    set", "    {", "        break;", "    }", "}");
	}

	@Test
	public void MethodDeclaration_EmptyMethod() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));

		AssertPrint(sst, "ReturnType M(ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_Static() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("static [ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));

		AssertPrint(sst, "static ReturnType M(ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_PassedByReference() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName
				.newMethodName("[ReturnType,P] [DeclaringType,P].M(ref [System.Int32, mscore, 4.0.0.0] p)"));

		AssertPrint(sst, "ReturnType M(ref Int32 p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_Output() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M(out [ParameterType,P] p)"));

		AssertPrint(sst, "ReturnType M(out ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_Params() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M(params [ParameterType[],P] p)"));

		AssertPrint(sst, "ReturnType M(params ParameterType[] p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_Optional() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M(opt [ParameterType,P] p)"));

		AssertPrint(sst, "ReturnType M(opt ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_WithBody() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		AssertPrint(sst, "ReturnType M(ParameterType p)", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void MethodDeclaration_Generic() {

		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType, P] [DeclaringType, P].M`1[[T -> T]]([T] p)"));

		AssertPrint(sst, "ReturnType M<?>(? p) { }");
	}

	@Test
	public void VariableDeclaration() {
		IVariableDeclaration sst = SSTUtil.declare("var", TypeName.newTypeName("T,P"));

		AssertPrint(sst, "T var;");
	}
}
