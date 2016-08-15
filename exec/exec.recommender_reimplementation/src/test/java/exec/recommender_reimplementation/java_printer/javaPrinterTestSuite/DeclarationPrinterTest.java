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
package exec.recommender_reimplementation.java_printer.javaPrinterTestSuite;

import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;

import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import exec.recommender_reimplementation.java_printer.JavaPrintingVisitor.InvalidJavaCodeException;

public class DeclarationPrinterTest extends JavaPrintingVisitorBaseTest {

	@Test
	public void addsPackageDeclaration() {
		SST sst = new SST();
		sst.setEnclosingType(TypeName.newTypeName("FirstPackage.SecondPackage.TestClass,P"));

		assertPrint(sst, "package FirstPackage.SecondPackage;",
				"class TestClass", "{", "}");
	}

	@Test
	public void appendsImportList() {
		SST sst = new SST();
		sst.setEnclosingType(type("TestClass"));
		sst.getMethods()
				.add(methodDecl(method(type("T1"), type("TestClass"), "m1"), declare("foo", type("T1"))));
		
		assertPrint(sst, "import T1;", "class TestClass", "{", "    T1 m1()", "    {", "        T1 foo;", "    }", "}");
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

		assertPrintWithCustomContext(sst, typeShape,
				"class TestClass extends SuperClass implements IDoesSomething, IDoesSomethingElse", "{", "}");
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

		assertPrintWithCustomContext(sst, typeShape, "class TestClass implements IDoesSomething", "{", "}");
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

		assertPrintWithCustomContext(sst, typeShape, "class TestClass extends SuperClass", "{", "}");
	}

	@Test
	public void SSTDeclaration_PublicModifier() {
		ITypeName thisType = TypeName.newTypeName("TestClass,P");

		SST sst = new SST();
		sst.setEnclosingType(thisType);

		assertPrintWithPublicModifier(sst, "public class TestClass", "{", "}");
	}

	@Test
	public void FieldDeclaration() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(FieldName.newFieldName("[FieldType,P] [DeclaringType,P].F"));

		assertPrint(sst, "FieldType F;");
	}

	@Test
	public void FieldDeclaration_Static() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(FieldName.newFieldName("static [FieldType,P] [DeclaringType,P].F"));

		assertPrint(sst, "static FieldType F;");
	}

	@Test
	public void FieldDeclaration_PublicModifier() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(FieldName.newFieldName("[FieldType,P] [DeclaringType,P].F"));

		assertPrintWithPublicModifier(sst, "public FieldType F;");
	}

	@Test
	public void FieldDeclaration_Array() {
		FieldDeclaration sst = new FieldDeclaration();
		sst.setName(FieldName.newFieldName("[d:[V, A] [N.TD, A].()[]] [DT, A]._delegatesField"));

		assertPrint(sst, "TD[] _delegatesField;");
	}

	@Test
	public void MethodDeclaration_EmptyMethod() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));

		assertPrint(sst, "ReturnType M(ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_Static() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("static [ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));

		assertPrint(sst, "static ReturnType M(ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_PublicModifier() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));

		assertPrintWithPublicModifier(sst, "public ReturnType M(ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_PassedByReference() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(
				MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M(ref [System.Int32, mscore, 4.0.0.0] p)"));

		assertPrint(sst, "ReturnType M(Int32 p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_Output() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M(out [ParameterType,P] p)"));

		assertPrint(sst, "ReturnType M(ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_Params() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M(params [ParameterType[],P] p)"));

		assertPrint(sst, "ReturnType M(ParameterType[]... p) { }");
	}

	@Test
	public void MethodDeclaration_ParameterModifiers_Optional() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M(opt [ParameterType,P] p)"));

		assertPrint(sst, "ReturnType M(ParameterType p) { }");
	}

	@Test
	public void MethodDeclaration_WithBody() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "ReturnType M(ParameterType p)", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void MethodDeclaration_Generic() {

		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[ReturnType, P] [DeclaringType, P].M`1[[T -> T]]([T] p)"));

		assertPrint(sst, "<?> ReturnType M(? p) { }");
	}

	@Test
	public void MethodDeclaration_Constructor() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[DeclaringType, P1] [DeclaringType, P1]..ctor()"));

		assertPrint(sst, "DeclaringType() { }");
	}

	@Test
	public void MethodDeclaration_ConstructorWithParameters() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName.newMethodName("[DeclaringType,P] [DeclaringType,P]..ctor([ParameterType,P] p)"));

		assertPrint(sst, "DeclaringType(ParameterType p) { }");
	}

	@Test
	public void addsOverrideTag() {
		ITypeName thisType = TypeName.newTypeName("TestClass,P");
		ITypeName superType = TypeName.newTypeName("SuperClass,P");

		IMethodDeclaration methodDecl = methodDecl(method(type("T1"), thisType, "m1"));
		TypeShape typeShape = new TypeShape();
		MethodHierarchy methodHierarchy = new MethodHierarchy();
		methodHierarchy.setElement(method(type("T1"), thisType, "m1"));
		methodHierarchy.setSuper(method(type("T1"), superType, "m1"));
		typeShape.setMethodHierarchies(Sets.newHashSet(methodHierarchy));

		assertPrintWithCustomContext(methodDecl, typeShape,
				"@Override", "T1 m1() { }");
	}

	@Test
	public void ignoresObjectOverrides() {
		ITypeName thisType = TypeName.newTypeName("TestClass,P");
		ITypeName superType = TypeName.newTypeName("System.Object,P");

		IMethodDeclaration methodDecl = methodDecl(method(type("T1"), thisType, "m1"));
		TypeShape typeShape = new TypeShape();
		MethodHierarchy methodHierarchy = new MethodHierarchy();
		methodHierarchy.setElement(method(type("T1"), thisType, "m1"));
		methodHierarchy.setSuper(method(type("T1"), superType, "m1"));
		typeShape.setMethodHierarchies(Sets.newHashSet(methodHierarchy));

		assertPrintWithCustomContext(methodDecl, typeShape, "T1 m1() { }");
	}

	@Test
	public void printEnum() {
		ITypeName thisType = TypeName.newTypeName("e:TestClass,P");
		
		SST sst = new SST();
		sst.setEnclosingType(thisType);
		sst.setFields(Sets.newHashSet(
				fieldDecl(field(thisType, thisType, "VAL1"))));

		assertPrint(sst, "enum TestClass", "{", "    VAL1", "}");
	}

	@Test
	public void ignoresTypeParameters() {
		ITypeName genericsType = type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]");

		assertPrint(declare("foo", genericsType), "Dictionary foo;");
	}

	@Test(expected = InvalidJavaCodeException.class)
	public void exceptionOnDelegateDeclaration() {
		assertPrint(new DelegateDeclaration(), "");
	}

	@Test(expected = InvalidJavaCodeException.class)
	public void exceptionOnEventDeclaration() {
		assertPrint(new EventDeclaration(), "");
	}

	@Test(expected = InvalidJavaCodeException.class)
	public void exceptionOnPropertyDeclaration() {
		assertPrint(new PropertyDeclaration(), "");
	}
}
