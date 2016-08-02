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

import org.junit.Test;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public class DeclarationPrinterTest extends JavaPrintingVisitorBaseTest {

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
	public void PropertyDeclaration_GetterOnly() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get [PropertyType,P] [DeclaringType,P].P"));
		
		assertPrint(sst, "PropertyType $property_P;", "PropertyType getP()", "{", "    return $property_P;", "}", "");
	}

	@Test
	public void PropertyDeclaration_SetterOnly() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("set [PropertyType,P] [DeclaringType,P].P"));

		assertPrint(sst, "PropertyType $property_P;", "void setP(PropertyType value)", "{", "    $property_P = value;",
				"}", "");
	}

	@Test
	public void PropertyDeclaration() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));
		
		assertPrint(sst, "PropertyType $property_P;", "PropertyType getP()", "{", "    return $property_P;", "}",
				"void setP(PropertyType value)", "{", "    $property_P = value;", "}", "");
	}
	
	@Test
	public void PropertyDeclaration_static() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("static get set [PropertyType,P] [DeclaringType,P].P"));
		
		assertPrint(sst, "static PropertyType $property_P;", "static PropertyType getP()", "{", "    return $property_P;", "}",
				"static void setP(PropertyType value)", "{", "    $property_P = value;", "}", "");
	}

	@Test
	public void PropertyDeclaration_WithBodies() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));
		sst.getGet().add(new ContinueStatement());
		sst.getGet().add(new BreakStatement());
		sst.getSet().add(new BreakStatement());
		sst.getSet().add(new ContinueStatement());

		assertPrint(sst, "PropertyType getP()", "{", "    continue;", "    break;", "}",
				"void setP(PropertyType value)", "{", "    break;", "    continue;", "}", "");
	}
	
	@Test
	public void PropertyDeclaration_WithBodies_static() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("static get set [PropertyType,P] [DeclaringType,P].P"));
		sst.getGet().add(new ContinueStatement());
		sst.getGet().add(new BreakStatement());
		sst.getSet().add(new BreakStatement());
		sst.getSet().add(new ContinueStatement());

		assertPrint(sst, "static PropertyType getP()", "{", "    continue;", "    break;", "}",
				"static void setP(PropertyType value)", "{", "    break;", "    continue;", "}", "");
	}

	@Test
	public void PropertyDeclaration_WithOnlyGetterBody() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));
		sst.getGet().add(new BreakStatement());

		assertPrint(sst, "PropertyType getP()", "{", "    break;", "}",
				"void setP(PropertyType value) { }", "");	
	}

	@Test
	public void PropertyDeclaration_WithOnlySetterBody() {
		PropertyDeclaration sst = new PropertyDeclaration();
		sst.setName(PropertyName.newPropertyName("get set [PropertyType,P] [DeclaringType,P].P"));
		sst.getSet().add(new BreakStatement());

		assertPrint(sst, "PropertyType getP() { }",
				"void setP(PropertyType value)", "{", "    break;",  "}", "");	
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
	public void MethodDeclaration_ParameterModifiers_PassedByReference() {
		MethodDeclaration sst = new MethodDeclaration();
		sst.setName(MethodName
				.newMethodName("[ReturnType,P] [DeclaringType,P].M(ref [System.Int32, mscore, 4.0.0.0] p)"));

		assertPrint(sst, "ReturnType M(int p) { }");
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

}
